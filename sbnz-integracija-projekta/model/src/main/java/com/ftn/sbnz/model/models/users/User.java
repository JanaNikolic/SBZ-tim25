package com.ftn.sbnz.model.models.users;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;

@Table(name = "users")
@Entity
public class User implements UserDetails {
    public enum UserRole {
        ADMIN, USER
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false)
    @NotNull
    @NotEmpty
    @NotBlank
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @Column(name = "name", nullable = false)
    @NotNull
    @NotEmpty
    @NotBlank
    private String name;

    @Column(name = "surname", nullable = false)
    @NotNull
    @NotEmpty
    @NotBlank
    private String surname;


    @Column(name = "password", nullable = false)
    @NotNull
    @NotEmpty
    @NotBlank
    private String password;

    @Column(name = "role", nullable = false)
    @NotNull
    private UserRole role;

    @Column(name="is_enabled")
    private boolean isEnabled;

    @Column(name="for_change")
    private boolean forChange;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    public User() {
    }

    public User(Integer id, String email, String name, String surname, String password, UserRole role, boolean isEnabled, boolean forChange) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.role = role;
        this.isEnabled = isEnabled;
        this.forChange = forChange;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isForChange() {
        return forChange;
    }

    public void setForChange(boolean forChange) {
        this.forChange = forChange;
    }
}

