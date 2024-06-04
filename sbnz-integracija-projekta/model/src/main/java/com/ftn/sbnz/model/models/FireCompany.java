package com.ftn.sbnz.model.models;

import com.ftn.sbnz.model.models.users.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class FireCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "captain_id", nullable = false, unique = true)
    private User captain;
    @ManyToMany
    @JoinTable(
            name = "firecompany_firefighters",
            joinColumns = @JoinColumn(name = "firecompany_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> firefighters = new HashSet<>();

    public FireCompany(Integer id, User captain, Set<User> firefighters) {
        this.id = id;
        this.captain = captain;
        this.firefighters = firefighters;
    }

    public FireCompany() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getCaptain() {
        return captain;
    }

    public void setCaptain(User captain) {
        this.captain = captain;
    }

    public Set<User> getFirefighters() {
        return firefighters;
    }

    public void setFirefighters(Set<User> firefighters) {
        this.firefighters = firefighters;
    }
}
