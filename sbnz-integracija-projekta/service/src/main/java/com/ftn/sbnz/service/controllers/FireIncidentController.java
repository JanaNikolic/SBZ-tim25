package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.models.ActiveFire;
import com.ftn.sbnz.model.models.FireIncident;
import com.ftn.sbnz.service.dto.FireIncidentDTO;
import com.ftn.sbnz.service.services.FireIncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("api/fire-incident")
public class FireIncidentController {
    private static Logger log = LoggerFactory.getLogger(FireIncidentController.class);
    private final FireIncidentService fireIncidentService;

    @Autowired
    public FireIncidentController(FireIncidentService fireIncidentService) {
        this.fireIncidentService = fireIncidentService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasRole('CAPTAIN')")
    public ActiveFire postFireIncident(@RequestBody FireIncident fire, @RequestHeader("Authorization") String token) {
        return fireIncidentService.createFireIncident(fire, token);
    }

    @RequestMapping (value = "/{id}/finish", produces = "application/json")
    @PreAuthorize("hasRole('CAPTAIN')")
    public ResponseEntity<Void> finishFireIncident(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        fireIncidentService.finishFireIncident(id, token);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'FIREFIGHTER')")
    public ResponseEntity<FireIncidentDTO> getActiveFireIncidents() {
        FireIncident activeFireIncident = fireIncidentService.getActiveFireIncident();
        return ResponseEntity.ok(new FireIncidentDTO(activeFireIncident));
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasRole('CHIEF')")
    public ResponseEntity<List<FireIncidentDTO>> getAllFireIncidents() {
        List<FireIncidentDTO> activeFireIncidents = fireIncidentService.getAllFireIncident();
        return ResponseEntity.ok(activeFireIncidents);
    }

    @RequestMapping(value = "/all-company", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'FIREFIGHTER')")
    public ResponseEntity<List<FireIncidentDTO>> getAllFireIncidentsForCaptain(@RequestHeader("Authorization") String token) {
        List<FireIncidentDTO> activeFireIncidents = fireIncidentService.getAllFireIncidents(token);
        return ResponseEntity.ok(activeFireIncidents);
    }
}
