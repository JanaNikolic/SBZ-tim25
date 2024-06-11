package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.models.enums.Status;
import org.kie.api.definition.type.Expires;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.io.Serializable;
import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
@Expires("2m")
public class FirefighterActivityEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long firefighterId;
    private Long fireId;
    private Date timestamp;
    private Status status;

    public FirefighterActivityEvent() {
    }

    public FirefighterActivityEvent(Long firefighterId, Long fireId, Status status) {
        this.firefighterId = firefighterId;
        this.fireId = fireId;
        this.timestamp = new Date();
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Override toString() for debugging and logging
    @Override
    public String toString() {
        return "FirefighterActivityEvent{" +
                "firefighterId='" + firefighterId + '\'' +
                ", timestamp=" + timestamp +
                ", status=" + status +
                '}';
    }
}
