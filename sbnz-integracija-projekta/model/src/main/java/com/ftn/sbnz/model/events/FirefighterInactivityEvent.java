package com.ftn.sbnz.model.events;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import com.ftn.sbnz.model.models.enums.Status;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

@Role(Role.Type.EVENT)
@Expires("2m")
@Timestamp("timestamp")
public class FirefighterInactivityEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long firefighterId;
    private Long fireId;
    private Status status;
    private Date timestamp;

    public FirefighterInactivityEvent() {
        super();
    }

    public FirefighterInactivityEvent(Long firefighterId, Long fireId) {
        this.firefighterId = firefighterId;
        this.fireId = fireId;
        this.status = Status.STATIONARY;
        this.timestamp = new Date();
    }

    public FirefighterInactivityEvent(FirefighterActivityEvent event) {

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
        FirefighterInactivityEvent that = (FirefighterInactivityEvent) o;
        return firefighterId.equals(that.firefighterId) && fireId.equals(that.fireId) && status == that.status && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firefighterId, fireId, status, timestamp);
    }

    @Override
    public String toString() {
        return "FirefighterInactivityEvent{" +
                "firefighterId=" + firefighterId +
                ", fireId=" + fireId +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
