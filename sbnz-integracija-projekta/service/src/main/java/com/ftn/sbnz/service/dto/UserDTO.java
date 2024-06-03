package com.ftn.sbnz.service.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class UserDTO {

    @NotEmpty(message="is required")
    @Pattern(regexp = "^([a-zA-Zčćđžš ]*)$", message="format is not valid")
    private String name;

    @NotEmpty(message="is required")
    @Pattern(regexp = "^([a-zA-Zčćđžš ]*)$", message="format is not valid")
    private String lastname;

    @NotEmpty(message="is required")
    @Email(message="format is not valid")
    private String email;

    @NotEmpty(message="is required")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDTO(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
    }
}
