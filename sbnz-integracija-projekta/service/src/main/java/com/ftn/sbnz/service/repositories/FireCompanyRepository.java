package com.ftn.sbnz.service.repositories;


import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FireCompanyRepository extends JpaRepository<FireCompany, Integer> {
    Optional<FireCompany> findByCaptain(User captain);
    Optional<FireCompany> findByFirefightersContains(User firefighter);
}