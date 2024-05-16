package com.ftn.sbnz.model.events;

import com.ftn.sbnz.model.models.Firefighter;
import org.kie.api.definition.type.Role;
import org.kie.api.definition.type.Timestamp;

import java.util.Date;

@Role(Role.Type.EVENT)
@Timestamp("timestamp")
public class FirefighterHealthChangeEvent {
    private Firefighter firefighter;
    private double oxygenLevel;
    private int heartRate;
    private Date timestamp;

    public FirefighterHealthChangeEvent() {
    }

    public FirefighterHealthChangeEvent(Firefighter firefighter, double oxygenLevel, int heartRate) {
        this.firefighter = firefighter;
        this.oxygenLevel = oxygenLevel;
        this.heartRate = heartRate;
        this.timestamp = new Date();
    }

    public Firefighter getFirefighter() {
        return firefighter;
    }

    public void setFirefighter(Firefighter firefighter) {
        this.firefighter = firefighter;
    }

    public double getOxygenLevel() {
        return oxygenLevel;
    }

    public void setOxygenLevel(double oxygenLevel) {
        this.oxygenLevel = oxygenLevel;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

