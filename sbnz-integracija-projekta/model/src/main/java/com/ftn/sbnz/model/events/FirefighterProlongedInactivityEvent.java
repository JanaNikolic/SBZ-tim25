package com.ftn.sbnz.model.events;

import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class FirefighterProlongedInactivityEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    private Date timestamp;
    private Long firefighterId;
    private Long fireId;

    public FirefighterProlongedInactivityEvent() {
    }

    public FirefighterProlongedInactivityEvent(Long firefighterId, Long fireId) {
        this.firefighterId = firefighterId;
        this.fireId = fireId;
        this.timestamp = new Date();
    }

    public Long getFirefighterId() {
        return firefighterId;
    }

    public void setFirefighterId(Long firefighterId) {
        this.firefighterId = firefighterId;
    }

    public Long getFireId() {
        return fireId;
    }

    public void setFireId(Long fireId) {
        this.fireId = fireId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirefighterProlongedInactivityEvent that = (FirefighterProlongedInactivityEvent) o;
        return timestamp.equals(that.timestamp) && firefighterId.equals(that.firefighterId) && fireId.equals(that.fireId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, firefighterId, fireId);
    }
}
