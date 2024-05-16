package com.ftn.sbnz.model.events;

import java.io.Serializable;

import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;

@Role(Role.Type.EVENT)
@Expires("30m")
public class FirefighterInactivityEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long firefighterId;
    private Long fireId;
    private FirefighterActivityEvent.Status status;

    public FirefighterInactivityEvent() {
        super();
    }

    public FirefighterInactivityEvent(Long firefighterId, Long fireId) {
        this.firefighterId = firefighterId;
        this.fireId = fireId;
        this.status = FirefighterActivityEvent.Status.STATIONARY;
    }

    public FirefighterInactivityEvent(FirefighterActivityEvent event) {

    }
}
