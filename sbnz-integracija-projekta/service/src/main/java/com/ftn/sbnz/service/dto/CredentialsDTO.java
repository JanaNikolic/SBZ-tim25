package com.ftn.sbnz.service.dto;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CredentialsDTO {

    @NotNull
    @NotEmpty(message="is required")
    @NotBlank
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    String email;

    @NotEmpty(message="is required")
    String password;

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

    public CredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public CredentialsDTO() {
    }
}
