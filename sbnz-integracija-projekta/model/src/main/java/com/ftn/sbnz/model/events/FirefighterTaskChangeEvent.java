package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.models.Firefighter;
import com.ftn.sbnz.model.models.enums.FirefighterTask;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class FirefighterTaskChangeEvent {
    private Firefighter firefighter;
    private FirefighterTask task;
    private Date timestamp;

    public FirefighterTaskChangeEvent(Firefighter firefighter, FirefighterTask task) {
        this.firefighter = firefighter;
        this.task = task;
        this.timestamp = new Date();
    }

    public FirefighterTaskChangeEvent() {
    }

    public Firefighter getFirefighter() {
        return firefighter;
    }

    public void setFirefighter(Firefighter firefighter) {
        this.firefighter = firefighter;
    }

    public FirefighterTask getTask() {
        return task;
    }

    public void setTask(FirefighterTask task) {
        this.task = task;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
