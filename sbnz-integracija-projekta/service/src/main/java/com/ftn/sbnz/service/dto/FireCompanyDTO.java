package com.ftn.sbnz.service.dto;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class FireCompanyDTO {
    @NotEmpty(message="is required")
    private String captain;

    @NotEmpty(message="is required")
    private List<String> firefighters;

    public String getCaptain() {
        return captain;
    }

    public void setCaptain(String captain) {
        this.captain = captain;
    }

    public List<String> getFirefighters() {
        return firefighters;
    }

    public void setFirefighters(List<String> firefighters) {
        this.firefighters = firefighters;
    }

    public FireCompanyDTO(String captain, List<String> firefighters) {
        this.captain = captain;
        this.firefighters = firefighters;
    }

    public FireCompanyDTO() {
    }
}
