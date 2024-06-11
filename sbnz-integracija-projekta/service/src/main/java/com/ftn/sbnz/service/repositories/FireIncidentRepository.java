package com.ftn.sbnz.service.repositories;

import com.ftn.sbnz.model.models.FireCompany;
import com.ftn.sbnz.model.models.FireIncident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FireIncidentRepository extends JpaRepository<FireIncident, Long> {
    boolean existsByFireCompanyAndFinishedIsFalse(FireCompany fireCompany);
    Optional<FireIncident> findByFinishedFalse();
    List<FireIncident> findAllByFireCompanyAndFinishedIsTrue(FireCompany fireCompany);
    Optional<FireIncident> findByFinishedFalseAndFireCompany(FireCompany fireCompany);
}
