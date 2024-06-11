package com.ftn.sbnz.service.dto;

import com.ftn.sbnz.model.models.FirefighterObservation;

import java.util.List;

public class FirefighterObservationDTO {
    List<FirefighterObservation> observations;

    public FirefighterObservationDTO(List<FirefighterObservation> observations) {
        this.observations = observations;
    }

    public FirefighterObservationDTO() {
    }

    public List<FirefighterObservation> getObservations() {
        return observations;
    }

    public void setObservations(List<FirefighterObservation> observations) {
        this.observations = observations;
    }
}
