package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.events.FirefighterActivityEvent;
import com.ftn.sbnz.model.events.FirefighterHealthChangeEvent;
import com.ftn.sbnz.model.events.FirefighterInactivityEvent;
import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.model.models.enums.Status;
import com.ftn.sbnz.model.models.exceptions.CustomException;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.dto.ActionDTO;
import com.ftn.sbnz.service.dto.FireIncidentDTO;
import com.ftn.sbnz.service.dto.FirefighterObservationDTO;
import com.ftn.sbnz.service.repositories.FireIncidentRepository;
import com.ftn.sbnz.service.security.jwtUtils.TokenUtils;
import org.drools.core.ClassObjectFilter;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.internal.utils.KieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.awt.desktop.SystemEventListener;
import java.io.InputStream;
import java.util.*;

@Service
public class FireIncidentService {

    private static Logger log = LoggerFactory.getLogger(FireIncidentService.class);

    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private FireIncidentRepository fireIncidentRepository;
    @Autowired
    private UserService userService;
    private final KieContainer kieContainer;
    private final KieSession kieSession;
    private final KieSession cepKieSession;

    @Autowired
    public FireIncidentService(KieContainer kieContainer) {
        log.info("Initializing a new example session.");
        this.kieContainer = kieContainer;
        this.kieSession = kieContainer.newKieSession("fwKsession");
        this.cepKieSession = kieContainer.newKieSession("cepKsession");
        new Thread(this.cepKieSession::fireUntilHalt).start();
    }

    public ActiveFire createFireIncident(FireIncident fire, String token) {
        String email = tokenUtils.getUsernameFromToken(token.substring(7));
        User user = this.userService.getByEmail(email);
        FireCompany fireCompany = userService.getFireCompanyByCaptain(user);

        if (fireIncidentRepository.existsByFireCompanyAndFinishedIsFalse(fireCompany)) {
            throw new CustomException("The fire company already has an active fire incident!");
        }
        fire.setFireCompany(fireCompany);

        FireIncident fireIncident = fireIncidentRepository.save(fire);
        ActiveFire activeFire = new ActiveFire(fireIncident);
        insertFirefightersInCepSession(fireCompany.getFirefighters(), fireIncident.getId());

        InputStream templateStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/classify-fire.drt");
        InputStream dataStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/fire-classes.xls");

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(dataStream, templateStream, 3, 2);

        KieSession tempKieSession = createKieSessionFromDRL(drl);

        tempKieSession.insert(fireIncident);
        tempKieSession.insert(activeFire);
        int ruleCount = tempKieSession.fireAllRules();
        log.info("Number of fired rules TMP: " + ruleCount);

        tempKieSession.dispose();

        kieSession.getAgenda().getAgendaGroup("update-fire").setFocus();

        kieSession.insert(fireIncident);
        kieSession.insert(activeFire);
        ruleCount = kieSession.fireAllRules();
        log.info("Number of fired rules BW: " + ruleCount);

        cepKieSession.insert(fireIncident);

        return activeFire;
    }

    private void insertFirefightersInCepSession(Set<User> firefighters, Long id) {
        for (User firefighter : firefighters) {
            cepKieSession.insert(new Firefighter(firefighter.getId(), 100.0, 100, id));
        }
    }

    public ActiveFire insertObservations(FirefighterObservationDTO observationDTO, String token, String id) {
        String email = tokenUtils.getUsernameFromToken(token.substring(7));
        User captain = userService.getByEmail(email);

        FireIncident fireIncident = fireIncidentRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new CustomException("Fire Incident not found"));
        if (fireIncident.isFinished()) throw new CustomException("Fire Incident has already finished!");
        if (!Objects.equals(fireIncident.getFireCompany().getCaptain().getId(), captain.getId())) throw new CustomException("You are not assigned to this fire!");

        for (FirefighterObservation fo : observationDTO.getObservations()) {
            kieSession.insert(fo);
        }

        kieSession.getAgenda().getAgendaGroup("update-fire").setFocus();
        int ruleCount = kieSession.fireAllRules();
        log.info("Number of fired rules on observation: " + ruleCount);

        Map<String, Integer> params = new HashMap<>();
        params.put("$fireId", Math.toIntExact(fireIncident.getId()));

        QueryResults results = kieSession.getQueryResults("getActiveFireByIncidentId", params);
        if (results.size() == 0) {
            throw new CustomException("Active Fire not found for the given Fire Incident");
        }

        ActiveFire foundActiveFire = (ActiveFire) results.iterator().next().get("$activeFire");
        log.info("Found ActiveFire: " + foundActiveFire);

        return (ActiveFire) results.iterator().next().get("$activeFire");
    }


    private KieSession createKieSessionFromDRL(String drl){
        KieHelper kieHelper = new KieHelper();
        kieHelper.addContent(drl, ResourceType.DRL);

        Results results = kieHelper.verify();

        if (results.hasMessages(Message.Level.WARNING, Message.Level.ERROR)){
            List<Message> messages = results.getMessages(Message.Level.WARNING, Message.Level.ERROR);
            for (Message message : messages) {
                System.out.println("Error: "+message.getText());
            }

            throw new IllegalStateException("Compilation errors were found. Check the logs.");
        }

        return kieHelper.build().newKieSession();
    }

    public void finishFireIncident(Long id, String token) {
        String email = tokenUtils.getUsernameFromToken(token.substring(7));
        User captain = userService.getByEmail(email);
        if (captain.getRole() != User.UserRole.CAPTAIN) {
            throw new CustomException("User must be a captain!");
        }

        FireIncident fireIncident = fireIncidentRepository.findById(id)
                .orElseThrow(() -> new CustomException("Fire incident not found!"));

        FireCompany fireCompany = fireIncident.getFireCompany();
        if (!fireCompany.getCaptain().getId().equals(captain.getId())) {
            throw new CustomException("You are not authorized to finish this fire incident!");
        }

        fireIncident.setFinished(true);
        fireIncident = fireIncidentRepository.save(fireIncident);

        kieSession.insert(fireIncident);
        kieSession.getAgenda().getAgendaGroup("cleanup").setFocus();
        int ruleCount = kieSession.fireAllRules();
        System.out.println("Number of fired rules on fw cleanup: " + ruleCount);

        cepKieSession.insert(fireIncident);
        cepKieSession.getAgenda().getAgendaGroup("cleanup").setFocus();
        ruleCount = cepKieSession.fireAllRules();
        System.out.println("Number of fired rules on cep cleanup: " + ruleCount);
    }

    public FireIncidentDTO getActiveFireIncident(String token) {
        String email = tokenUtils.getUsernameFromToken(token.substring(7));
        User user = userService.getByEmail(email);
        return new FireIncidentDTO(findActiveFireIncidentForUser(user));
    }

    private FireIncident findActiveFireIncidentForUser(User user) {
        FireCompany fireCompany = userService.getFireCompany(user);
        return fireIncidentRepository.findByFinishedFalseAndFireCompany(fireCompany).orElseThrow(() -> new CustomException("No active fire incidents!"));
    }

    public List<FireIncidentDTO> getActiveFireIncidents() {
        return null;
    }
    public List<FireIncidentDTO> getAllFireIncident() {
        List<FireIncident> fireIncidents =  fireIncidentRepository.findAll();
        List<FireIncidentDTO> dtos = new ArrayList<>();
        for (FireIncident fi :
                fireIncidents) {
            dtos.add(new FireIncidentDTO(fi));
        }
        return dtos;
    }

    public List<FireIncidentDTO> getAllFireIncidents(String token) {
        String email = tokenUtils.getUsernameFromToken(token.substring(7));
        User user = userService.getByEmail(email);

        FireCompany fireCompany = userService.getFireCompany(user);
        List<FireIncident> fireIncidents = fireIncidentRepository.findAllByFireCompanyAndFinishedIsTrue(fireCompany);
        List<FireIncidentDTO> dtos = new ArrayList<>();
        for (FireIncident fi :
                fireIncidents) {
            dtos.add(new FireIncidentDTO(fi));
        }
        return dtos;
    }

    @PreDestroy
    public void cleanUp() {
        if (kieSession != null) {
            kieSession.dispose();
        }
        if (cepKieSession != null) {
            cepKieSession.dispose();
        }
    }

    public Object firefighterActivity(Long fireId, Long firefighterId) {
        FirefighterActivityEvent activityEvent = new FirefighterActivityEvent(firefighterId, fireId, Status.MOVING);
        cepKieSession.insert(activityEvent);

        Collection<?> newEvents = cepKieSession.getObjects(new ClassObjectFilter(FirefighterInactivityEvent.class));
        FirefighterInactivityEvent inactivityEvent = (FirefighterInactivityEvent) newEvents.iterator().next();
        System.out.println(inactivityEvent);
        if (inactivityEvent == null) return new MessageResponse("Activity registered.");

        return inactivityEvent;
    }

    public Object firefighterStats(Firefighter firefighter) {
        FirefighterActivityEvent activityEvent = new FirefighterActivityEvent(firefighter.getId(), firefighter.getFireId(), Status.MOVING);
        cepKieSession.insert(activityEvent);
        cepKieSession.insert(new FirefighterHealthChangeEvent(firefighter.getId(), firefighter.getOxygenLevel(), firefighter.getHeartRate()));

        List<Action> actions = new ArrayList<>();
        Collection<?> newEvents = cepKieSession.getObjects(new ClassObjectFilter(Action.class));
        for (Object event : newEvents) {
            if (event instanceof Action) {
                Action action = (Action) event;
                if (action.getFireId().equals(firefighter.getFireId()) && action.getFirefighterId().equals(firefighter.getId())) {
                    actions.add(action);
                }
            }
        }
        if (actions.size() == 0) return new MessageResponse("Stats registered.");

        return actions;
    }
    public List<ActionDTO> getFireActions(Long fireId) {
        List<ActionDTO> actions = new ArrayList<>();
        Collection<?> newEvents = cepKieSession.getObjects(new ClassObjectFilter(Action.class));
        for (Object event : newEvents) {
            if (event instanceof Action) {
                Action action = (Action) event;
                if (action.getFireId().equals(fireId)) {
                    User user = userService.getById(action.getFirefighterId());
                    if (user.getRole() == User.UserRole.FIREFIGHTER) {
                        actions.add(new ActionDTO(user.getId(), action.getAction(), user.getName(), user.getSurname()));
                    }
                }
            }
        }
        return actions;
    }

    public String validateIncidentReport(IncidentReport report) {
        KieSession backwardKieSession = kieContainer.newKieSession("backwardKsession");

        backwardKieSession.insert(new ValidStep("Shutting Off Gas/Electricity", "Evacuation"));
        backwardKieSession.insert(new ValidStep("Shutting Off Gas/Electricity", "Ventilation"));

        backwardKieSession.insert(new ValidStep("Evacuation", "Ventilation"));
        backwardKieSession.insert(new ValidStep("Evacuation", "Fire Localization"));

        backwardKieSession.insert(new ValidStep("Ventilation", "Fire Localization"));
        backwardKieSession.insert(new ValidStep("Ventilation", "Evacuation"));

        backwardKieSession.insert(new ValidStep("Fire Localization", "Evacuation"));
        backwardKieSession.insert(new ValidStep("Fire Localization", "Ventilation"));
        backwardKieSession.insert(new ValidStep("Fire Localization", "Extinguishing Fire"));

        backwardKieSession.insert(report);
        for (Step step :
                report.getSteps()) {
            backwardKieSession.insert(step);
        }
        int ruleCount = backwardKieSession.fireAllRules();
        System.out.println("Fired " + ruleCount + " rules.");

        Collection<?> newEvents = backwardKieSession.getObjects(new ClassObjectFilter(String.class));
        String result = (String) newEvents.iterator().next();
        backwardKieSession.dispose();

        return result;
    }

    public ActiveFire getActiveFire(String token) {
        String email = tokenUtils.getUsernameFromToken(token.substring(7));
        User user = this.userService.getByEmail(email);

        FireIncident fireIncident = findActiveFireIncidentForUser(user);
        Collection<?> newEvents = kieSession.getObjects(new ClassObjectFilter(ActiveFire.class));
        for (Object event : newEvents) {
            if (event instanceof ActiveFire) {
                ActiveFire action = (ActiveFire) event;
                if (action.getFireIncidentId().equals(fireIncident.getId())) {
                    return action;
                }
            }
        }
        return null;
    }
}
