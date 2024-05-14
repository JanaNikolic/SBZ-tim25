package com.ftn.sbnz.service.tests;

import com.ftn.sbnz.model.models.ActiveFire;
import com.ftn.sbnz.model.models.FireIncident;
import com.ftn.sbnz.model.models.FirefighterObservation;
import com.ftn.sbnz.model.models.enums.*;
import com.ftn.sbnz.service.services.FireIncidentService;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.utils.KieHelper;

import java.io.InputStream;
import java.util.List;


public class CEPConfigTest {

    @Test
    public void test() {
         KieServices ks = KieServices.Factory.get();
         KieContainer kContainer = ks.getKieClasspathContainer();
         KieSession kieSession = kContainer.newKieSession("foKsession");

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


        kieSession.insert(fire);
        kieSession.insert(activeFire);
        kieSession.fireAllRules();
        kieSession.insert(o1);
        kieSession.insert(o2);
        kieSession.insert(o3);

        System.out.println(activeFire);

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

        KieSession kieSession2 = kieContainer.newKieSession("bwKsession");
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
}
