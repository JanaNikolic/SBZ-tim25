package com.ftn.sbnz.service.dto;

import com.ftn.sbnz.model.models.enums.WarningType;

public class ActionDTO {
    private Long id;
    private WarningType action;
    private String name;
    private String surname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WarningType getAction() {
        return action;
    }

    public void setAction(WarningType action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public ActionDTO(Long id, WarningType action, String name, String surname) {
        this.id = id;
        this.action = action;
        this.name = name;
        this.surname = surname;
    }

    public ActionDTO() {
    }
}
