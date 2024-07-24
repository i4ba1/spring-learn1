package com.dev.springlearn1.repositories;

import com.dev.springlearn1.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // You can add custom query methods here if needed
}
