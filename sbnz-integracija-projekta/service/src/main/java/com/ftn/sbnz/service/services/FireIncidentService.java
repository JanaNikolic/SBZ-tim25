package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.ActiveFire;
import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.FireIncident;
import com.ftn.sbnz.model.models.exceptions.CustomException;
import com.ftn.sbnz.model.models.users.User;
import com.ftn.sbnz.service.dto.FireIncidentDTO;
import com.ftn.sbnz.service.repositories.FireIncidentRepository;
import com.ftn.sbnz.service.security.jwtUtils.TokenUtils;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    public FireIncidentService(KieContainer kieContainer) {
        log.info("Initialising a new example session.");
        this.kieContainer = kieContainer;
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

        InputStream templateStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/classify-fire.drt");
        InputStream dataStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/fire-classes.xls");

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(dataStream, templateStream, 3, 2);

        KieSession kieSession = createKieSessionFromDRL(drl);

        kieSession.insert(fireIncident);
        kieSession.insert(activeFire);
        kieSession.fireAllRules();

        kieSession.dispose();

        KieSession kieSession2 = kieContainer.newKieSession("bwKsession");
        kieSession2.insert(fireIncident);
        kieSession2.insert(activeFire);
        kieSession2.fireAllRules();

        kieSession2.dispose();


        return activeFire;
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
        fireIncidentRepository.save(fireIncident);
    }

    public FireIncident getActiveFireIncident() {
        return fireIncidentRepository.findByFinishedFalse().orElseThrow(() -> new CustomException("No active fire incidents!"));
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

}
