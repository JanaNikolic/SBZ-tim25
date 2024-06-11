package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.enums.WarningType;

import java.io.Serializable;

public class Action implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long fireId;
    private Long firefighterId;
    private WarningType action;

    public Action() {
    }

    public Action(Long fireId, Long firefighterId, WarningType action) {
        this.fireId = fireId;
        this.firefighterId = firefighterId;
        this.action = action;
    }

    public Long getFireId() {
        return fireId;
    }

    public void setFireId(Long fireId) {
        this.fireId = fireId;
    }

    public Long getFirefighterId() {
        return firefighterId;
    }

    public void setFirefighterId(Long firefighterId) {
        this.firefighterId = firefighterId;
    }

    public WarningType getAction() {
        return action;
    }

    public void setAction(WarningType action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Action{" +
                "fireId=" + fireId +
                ", firefighterId=" + firefighterId +
                ", action=" + action +
                '}';
    }
}
