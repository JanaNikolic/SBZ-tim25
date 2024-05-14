package com.ftn.sbnz.service.services;

import com.ftn.sbnz.model.models.ActiveFire;
import com.ftn.sbnz.model.models.FireIncident;
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
import java.util.List;

@Service
public class FireIncidentService {

    private static Logger log = LoggerFactory.getLogger(FireIncidentService.class);

    private final KieContainer kieContainer;

    @Autowired
    public FireIncidentService(KieContainer kieContainer) {
        log.info("Initialising a new example session.");
        this.kieContainer = kieContainer;
    }

//    public ActiveFire createFireIncident1(FireIncident fire) {
//        ActiveFire activeFire = new ActiveFire(fire);
//        KieSession kieSession = kieContainer.newKieSession("bwKsession");
//        kieSession.insert(fire);
//        kieSession.insert(activeFire);
//        kieSession.fireAllRules();
//
//        // Retrieve modified ActiveFire object from the session
//        QueryResults results = kieSession.getQueryResults("GetActiveFire");
//        for (QueryResultsRow row : results) {
//            activeFire = (ActiveFire) row.get("$activeFire");
//            break; // Assuming there's only one result
//        }
//
//        kieSession.dispose();
//        return activeFire;
//    }

    public ActiveFire createFireIncident(FireIncident fire) {
        ActiveFire activeFire = new ActiveFire(fire);

        InputStream templateStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/classify-fire.drt");
        InputStream dataStream = FireIncidentService.class.getResourceAsStream("/rules/templates/classification/fire-classes.xls");

        ExternalSpreadsheetCompiler converter = new ExternalSpreadsheetCompiler();
        String drl = converter.compile(dataStream, templateStream, 3, 2);
//        System.out.println(drl);
        KieSession kieSession = createKieSessionFromDRL(drl);

        kieSession.insert(fire);
        kieSession.insert(activeFire);
        kieSession.fireAllRules();

        kieSession.dispose();

        KieSession kieSession2 = kieContainer.newKieSession("bwKsession");
        kieSession2.insert(fire);
        kieSession2.insert(activeFire);
        kieSession2.fireAllRules();

//        QueryResults results2 = kieSession2.getQueryResults("GetActiveFire");
//        for (QueryResultsRow row : results2) {
//            activeFire = (ActiveFire) row.get("$activeFire");
//            break;
//        }

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
