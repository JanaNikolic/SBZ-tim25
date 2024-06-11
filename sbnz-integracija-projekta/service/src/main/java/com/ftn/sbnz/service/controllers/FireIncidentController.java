package com.ftn.sbnz.service.controllers;

import com.ftn.sbnz.model.models.*;
import com.ftn.sbnz.service.dto.ActionDTO;
import com.ftn.sbnz.service.dto.FireIncidentDTO;
import com.ftn.sbnz.service.dto.FirefighterObservationDTO;
import com.ftn.sbnz.service.services.FireIncidentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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

    @RequestMapping(value = "{id}/observation", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @PreAuthorize("hasRole('CAPTAIN')")
    public ActiveFire postFirefighterObservation(@RequestBody FirefighterObservationDTO observationDTO, @RequestHeader("Authorization") String token, @PathVariable String id) {
        return fireIncidentService.insertObservations(observationDTO, token, id);
    }

    @RequestMapping (value = "/{id}/finish", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('CAPTAIN')")
    public ResponseEntity<Void> finishFireIncident(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        fireIncidentService.finishFireIncident(id, token);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'FIREFIGHTER')")
    public ResponseEntity<FireIncidentDTO> getActiveFireIncident(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(fireIncidentService.getActiveFireIncident(token));
    }

    @RequestMapping(value = "/active-decision", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'FIREFIGHTER')")
    public ResponseEntity<ActiveFire> getAllActiveFireIncidents(@RequestHeader("Authorization") String token) {
        ActiveFire activeFireIncidents = fireIncidentService.getActiveFire(token);
        return ResponseEntity.ok(activeFireIncidents);
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

//    @RequestMapping(value = "/{fireId}/{firefighterId}", method = RequestMethod.POST, produces = "application/json")
//    @PreAuthorize("hasAnyRole('CAPTAIN')")
//    public ResponseEntity<?> firefighterActivity(@PathVariable Long fireId, @PathVariable Long firefighterId) {
//        return ResponseEntity.ok(fireIncidentService.firefighterActivity(fireId, firefighterId));
//    }

    @RequestMapping(value = "/firefighter-stats", method = RequestMethod.POST, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN')")
    public ResponseEntity<?> firefighterStats(@RequestBody Firefighter firefighter) {
        return ResponseEntity.ok(fireIncidentService.firefighterStats(firefighter));
    }

    @RequestMapping(value = "/{fireId}/actions", method = RequestMethod.GET, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN')")
    public ResponseEntity<List<ActionDTO>> getFireActions(@PathVariable Long fireId) {
        List<ActionDTO> actions = fireIncidentService.getFireActions(fireId);
        return ResponseEntity.ok(actions);
    }

    @RequestMapping(value = "/validate-report", method = RequestMethod.POST, produces = "application/json")
    @PreAuthorize("hasAnyRole('CAPTAIN', 'CHIEF')")
    public ResponseEntity<MessageResponse> validateReport(@RequestBody IncidentReport report) {
        String result = fireIncidentService.validateIncidentReport(report);
        return ResponseEntity.ok(new MessageResponse(result));
    }

}
