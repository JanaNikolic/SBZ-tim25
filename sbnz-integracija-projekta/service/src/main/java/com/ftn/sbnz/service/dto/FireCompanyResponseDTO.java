package com.ftn.sbnz.service.dto;

import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.users.User;

import java.util.HashSet;
import java.util.Set;

public class FireCompanyResponseDTO {
    private UserDataDTO captain;
    private Set<UserDataDTO> firefighters;

    public UserDataDTO getCaptain() {
        return captain;
    }

    public void setCaptain(UserDataDTO captain) {
        this.captain = captain;
    }

    public Set<UserDataDTO> getFirefighters() {
        return firefighters;
    }

    public void setFirefighters(Set<UserDataDTO> firefighters) {
        this.firefighters = firefighters;
    }

    public FireCompanyResponseDTO(UserDataDTO captain, Set<UserDataDTO> firefighters) {
        this.captain = captain;
        this.firefighters = firefighters;
    }

    public FireCompanyResponseDTO() {
    }

    public FireCompanyResponseDTO(FireCompany fireCompany) {
        this.captain = new UserDataDTO(fireCompany.getCaptain());
        this.firefighters = new HashSet<>();
        for (User f : fireCompany.getFirefighters()) {
            this.firefighters.add(new UserDataDTO(f));
        }
    }
}
