package com.ftn.sbnz.service.dto;

import com.ftn.sbnz.model.models.users.User;

public class UserDataDTO {
    private String name;
    private String surname;
    private User.UserRole role;
    private String email;

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

    public User.UserRole getRole() {
        return role;
    }

    public void setRole(User.UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDataDTO(String name, String surname, User.UserRole role, String email) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.email = email;
    }

    public UserDataDTO() {
    }

    public UserDataDTO(User user) {
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = user.getRole();
        this.email = user.getEmail();
    }
}
