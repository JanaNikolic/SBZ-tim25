package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.models.ActiveFire;
import com.ftn.sbnz.model.models.FireIncident;
import com.ftn.sbnz.model.models.FireSteps;
import com.ftn.sbnz.service.services.FireIncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class FireIncidentController {
    private static Logger log = LoggerFactory.getLogger(FireIncidentController.class);
    private final FireIncidentService fireIncidentService;

    @Autowired
    public FireIncidentController(FireIncidentService fireIncidentService) {
        this.fireIncidentService = fireIncidentService;
    }

    @RequestMapping(value = "/fire", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public ActiveFire postFireIncident(@RequestBody FireIncident fire) {
        return fireIncidentService.createFireIncident(fire);
    }
}
