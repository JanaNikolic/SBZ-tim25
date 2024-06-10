package com.ftn.sbnz.service.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import com.ftn.sbnz.model.events.FirefighterActivityEvent;
import com.ftn.sbnz.model.events.FirefighterHealthChangeEvent;
import com.ftn.sbnz.model.events.FirefighterInactivityEvent;
import com.ftn.sbnz.model.events.FirefighterProlongedInactivityEvent;
import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.model.models.enums.*;
import com.ftn.sbnz.model.models.exceptions.CustomException;
import com.ftn.sbnz.service.services.FireIncidentService;
import org.drools.core.ClassObjectFilter;
import org.drools.core.time.SessionPseudoClock;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.KieSessionConfiguration;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CEPConfigTest {
    @Test
    public void testDetectOxygenLevelChange() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("cepKsession");

        FireIncident fire = new FireIncident(1L, BurningMatter.WOOD, StructureType.RESIDENTIAL_BUILDING, FlamesType.LOW, 3.5, SmokeType.THICK, 10.2, WindDirection.NORTH, 50.0, RoomPlacement.BASEMENT, null, true, 15.0);
        Firefighter firefighter = new Firefighter(1L, 100.0, 100, 1L);
        FirefighterHealthChangeEvent initialEvent = new FirefighterHealthChangeEvent(1L, 80, 80);
        kieSession.insert(fire);
        kieSession.insert(firefighter);
        kieSession.insert(initialEvent);

        int ruleCount = kieSession.fireAllRules();
        assertThat(ruleCount, equalTo(0));
        System.out.println(firefighter);

        FirefighterHealthChangeEvent decreasedOxygenEvent = new FirefighterHealthChangeEvent(1L, 60, 40);
        kieSession.insert(decreasedOxygenEvent);

        SessionPseudoClock clock = kieSession.getSessionClock();
        clock.advanceTime(2, TimeUnit.MINUTES);

        ruleCount = kieSession.fireAllRules();
//        assertThat(ruleCount, equalTo(1));
        Collection<?> newEvents = kieSession.getObjects(new ClassObjectFilter(Action.class));
        Action action = (Action) newEvents.iterator().next();
        System.out.println(action);
        System.out.println(firefighter);
    }

    @Test
    public void testRescueInactiveFirefighters() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("cepKsession");

        SessionPseudoClock clock = kieSession.getSessionClock();

        FireIncident fire = new FireIncident(1L, BurningMatter.WOOD, StructureType.RESIDENTIAL_BUILDING, FlamesType.LOW, 3.5, SmokeType.THICK, 10.2, WindDirection.NORTH, 50.0, RoomPlacement.BASEMENT, null, true, 15.0);
        kieSession.insert(fire);

        FirefighterProlongedInactivityEvent inactivityEvent1 = new FirefighterProlongedInactivityEvent(1L, 1L);
        kieSession.insert(inactivityEvent1);

        int ruleCount = kieSession.fireAllRules();
        assertThat(ruleCount, equalTo(0));

        clock.advanceTime(45, TimeUnit.SECONDS);
        FirefighterProlongedInactivityEvent inactivityEvent2 = new FirefighterProlongedInactivityEvent(1L, 1L);
        kieSession.insert(inactivityEvent2);

        ruleCount = kieSession.fireAllRules();
        assertThat(ruleCount, equalTo(0));

        clock.advanceTime(60, TimeUnit.SECONDS);
        FirefighterProlongedInactivityEvent inactivityEvent3 = new FirefighterProlongedInactivityEvent(1L, 1L);
        kieSession.insert(inactivityEvent3);

        ruleCount = kieSession.fireAllRules();
        assertThat(ruleCount, equalTo(1));

        Collection<?> newEvents = kieSession.getObjects(new ClassObjectFilter(Action.class));
        assertThat(newEvents.size(), equalTo(1));
        Action action = (Action) newEvents.iterator().next();
        assertThat(action.getAction(), equalTo(WarningType.RESCUE_INACTIVE_FIREFIGHTER));

        System.out.println(action);

        kieSession.dispose();
    }

    @Test
    public void testDetectLackOfMovementAndProlongedInactivity() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("cepKsession");

        SessionPseudoClock clock = kieSession.getSessionClock();

        FireIncident fire = new FireIncident(1L, BurningMatter.WOOD, StructureType.RESIDENTIAL_BUILDING, FlamesType.LOW, 3.5, SmokeType.THICK, 10.2, WindDirection.NORTH, 50.0, RoomPlacement.BASEMENT, null, true, 15.0);
        Firefighter firefighter = new Firefighter(1L, 100.0, 100, 1L);
        kieSession.insert(fire);
        kieSession.insert(firefighter);

        for (int index = 0; index < 6; index++) {
            FirefighterActivityEvent movingEvent = new FirefighterActivityEvent(1L, 1L, Status.MOVING);
            kieSession.insert(movingEvent);
            clock.advanceTime(2, TimeUnit.SECONDS);
            int ruleCount = kieSession.fireAllRules();
            assertThat(ruleCount, equalTo(0));
        }

        clock.advanceTime(11, TimeUnit.SECONDS);
        int ruleCount = kieSession.fireAllRules();
        assertThat(ruleCount, equalTo(7)); // Lack of movement should be detected

        Collection<?> inactivityEvents = kieSession.getObjects(new ClassObjectFilter(FirefighterInactivityEvent.class));
        assertThat(inactivityEvents.size(), equalTo(6));

        Collection<?> prolongedInactivityEvents = kieSession.getObjects(new ClassObjectFilter(FirefighterProlongedInactivityEvent.class));
        assertThat(prolongedInactivityEvents.size(), equalTo(1));

        kieSession.dispose();
    }



    @Test
    public void cepTest1() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();

        KieSessionConfiguration config = KieServices.Factory.get().newKieSessionConfiguration();
        config.setOption(ClockTypeOption.get("pseudo"));

        KieSession kieSession = kContainer.newKieSession("cepKsession", config);
        SessionPseudoClock clock = kieSession.getSessionClock();

        FireIncident fire = new FireIncident(1L, BurningMatter.WOOD, StructureType.RESIDENTIAL_BUILDING, FlamesType.LOW, 3.5, SmokeType.THICK, 10.2, WindDirection.NORTH, 50.0, RoomPlacement.BASEMENT, null, true, 15.0);
        kieSession.insert(fire);

        for (int index = 0; index < 10; index++) {
            FirefighterActivityEvent beep = new FirefighterActivityEvent(1L, 1L, Status.MOVING);
//            kieSession.getEntryPoint("firefighter-activity-stream").insert(beep);
            kieSession.insert(beep);
            clock.advanceTime(4, TimeUnit.SECONDS);
            int ruleCount = kieSession.fireAllRules();
            assertThat(ruleCount, equalTo(0));
            System.out.println("Inserted FirefighterActivityEvent " + (index + 1));
            System.out.println(beep);
        }

        // Print the session state before advancing the clock
        Collection<?> allEventsBefore = kieSession.getObjects();
        System.out.println("Session state before advancing time: " + allEventsBefore.size());

        // Advance time to trigger the inactivity detection
        System.out.println("Advancing time by 10 seconds...");
        clock.advanceTime(20, TimeUnit.MINUTES);
        int ruleCount = kieSession.fireAllRules();
        System.out.println("Rule count after advancing time: " + ruleCount);
        assertThat(ruleCount, equalTo(1));  // Check if the rule fires once inactivity is detected

        Collection<?> newEvents = kieSession.getObjects(new ClassObjectFilter(FirefighterInactivityEvent.class));
        System.out.println("FirefighterInactivityEvent count: " + newEvents.size());
        assertThat(newEvents.size(), equalTo(1));  // Ensure exactly one inactivity event is created
        System.out.println(newEvents);

        kieSession.dispose();
    }





    @Test
    public void test() {
         KieServices ks = KieServices.Factory.get();
         KieContainer kContainer = ks.getKieClasspathContainer();
         KieSession kieSession = kContainer.newKieSession("fwKsession");

        FireIncident fire = new FireIncident(1L, BurningMatter.WOOD, StructureType.RESIDENTIAL_BUILDING, FlamesType.LOW, 3.5, SmokeType.THICK, 10.2, WindDirection.NORTH, 50.0, RoomPlacement.BASEMENT, null, true, 15.0);
        ActiveFire activeFire = activeFireStatus(fire, kContainer);
        FirefighterObservation o1 = new FirefighterObservation(
                15.3,
                25.5,
                10.2,
                1L,
                4
        );
        FirefighterObservation o2 = new FirefighterObservation(
                6.0,
                30.7,
                5.9,
                1L,
                2
        );
        FirefighterObservation o3 = new FirefighterObservation(
                18.5,
                28.2,
                12.6,
                1L,
                1
        );

        kieSession.getAgenda().getAgendaGroup("update-fire").setFocus();
        kieSession.insert(fire);
        kieSession.insert(activeFire);
        int ruleCount = kieSession.fireAllRules();
        System.out.println(activeFire);
        System.out.println("Rules " + ruleCount);
        kieSession.insert(o1);
        kieSession.insert(o2);
        kieSession.insert(o3);
        kieSession.getAgenda().getAgendaGroup("update-fire").setFocus();
        ruleCount = kieSession.fireAllRules();
        System.out.println(activeFire);
        System.out.println("Rules " + ruleCount);

        // Define the parameter to be used in the query
        Map<String, Integer> params = new HashMap<>();
        params.put("$fireId", Math.toIntExact(fire.getId()));

        // Execute the query to find the ActiveFire
//        QueryResults results = kieSession.getQueryResults("getActiveFireByIncidentId", params);
        QueryResults results = kieSession.getQueryResults("getActiveFire");
        if (results.size() == 0) {
            throw new CustomException("ActiveFire not found for the given Fire Incident");
        }

        System.out.println((ActiveFire) results.iterator().next().get("$activeFire"));

        kieSession.dispose();
      
    }

    private ActiveFire activeFireStatus(FireIncident fire, KieContainer kieContainer) {
        ActiveFire activeFire = new ActiveFire(fire);
        InputStream templateStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/classify-fire.drt");
        InputStream dataStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/fire-classes.xls");

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(dataStream, templateStream, 3, 2);

        KieSession kieSession = createKieSessionFromDRL(drl);

        kieSession.insert(fire);
        kieSession.insert(activeFire);
        kieSession.fireAllRules();

        kieSession.dispose();

        KieSession kieSession2 = kieContainer.newKieSession("fwKsession");
        kieSession2.insert(fire);
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

    @Test
    public void testIncidentReportValidation() {
        KieServices ks = KieServices.Factory.get();
        KieContainer kContainer = ks.getKieClasspathContainer();
        KieSession kieSession = kContainer.newKieSession("backwardKsession");

        kieSession.insert(new ValidStep("Shutting Off Gas/Electricity", "Evacuation"));
        kieSession.insert(new ValidStep("Shutting Off Gas/Electricity", "Ventilation"));

        kieSession.insert(new ValidStep("Evacuation", "Ventilation"));
        kieSession.insert(new ValidStep("Evacuation", "Fire Localization"));

        kieSession.insert(new ValidStep("Ventilation", "Fire Localization"));
        kieSession.insert(new ValidStep("Ventilation", "Evacuation"));

        kieSession.insert(new ValidStep("Fire Localization", "Evacuation"));
        kieSession.insert(new ValidStep("Fire Localization", "Ventilation"));
        kieSession.insert(new ValidStep("Fire Localization", "Extinguishing Fire"));

        Step step1 = new Step("Shutting Off Gas/Electricity", 1, 1);
        Step step2 = new Step("Evacuation", 2, 1);
        Step step3 = new Step("Ventilation", 5, 1);
        Step step4 = new Step("Fire Localization", 4, 1);
        Step step5 = new Step("Extinguishing Fire", 3, 1);

        IncidentReport report = new IncidentReport(Arrays.asList(step1, step2, step3, step4, step5), 1);

        kieSession.insert(report);
        kieSession.insert(step1);
        kieSession.insert(step2);
        kieSession.insert(step3);
        kieSession.insert(step4);
        kieSession.insert(step5);

        int ruleCount = kieSession.fireAllRules();
        System.out.println("Fired " + ruleCount + " rules.");

        kieSession.dispose();
    }

}
