package com.ftn.sbnz.service.repositories;


import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FireCompanyRepository extends JpaRepository<FireCompany, Integer> {
    FireCompany findByCaptain(User captain);
    Set<FireCompany> findByFirefightersContains(User firefighter);
}