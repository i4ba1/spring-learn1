package com.dev.springlearn1.services;

import com.dev.springlearn1.models.Organization;
import com.dev.springlearn1.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Organization createOrganization(Organization organization) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = getUserIdFromAuthentication(auth);
        organization.setCreatedBy(userId);
        organization.setCreatedAt(LocalDateTime.now());
        return organizationRepository.save(organization);
    }

    public List<Organization> getAllOrganizations() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"))) {
            return organizationRepository.findAll();
        } else {
            Long userId = getUserIdFromAuthentication(auth);
            return organizationRepository.findAll().stream()
                    .filter(org -> org.getCreatedBy().equals(userId))
                    .collect(Collectors.toList());
        }
    }

    public Optional<Organization> getOrganizationById(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Organization> organization = organizationRepository.findById(id);

        if (organization.isPresent()) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN")) ||
                    organization.get().getCreatedBy().equals(getUserIdFromAuthentication(auth))) {
                return organization;
            }
        }
        return Optional.empty();
    }

    public Organization updateOrganization(Organization organization) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN")) ||
                organization.getCreatedBy().equals(getUserIdFromAuthentication(auth))) {
            organization.setUpdatedAt(LocalDateTime.now());
            return organizationRepository.save(organization);
        }
        throw new SecurityException("You don't have permission to update this organization");
    }

    public void deleteOrganization(Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<Organization> organization = organizationRepository.findById(id);

        if (organization.isPresent()) {
            if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN")) ||
                    organization.get().getCreatedBy().equals(getUserIdFromAuthentication(auth))) {
                organizationRepository.deleteById(id);
                return;
            }
        }
        throw new SecurityException("You don't have permission to delete this organization");
    }

    private Long getUserIdFromAuthentication(Authentication auth) {
        // This method should extract the user ID from the Authentication object
        // The implementation depends on how you store user details in your application
        // For example:
        // return ((CustomUserDetails) auth.getPrincipal()).getId();
        // Replace this with your actual implementation
        return 0L; // Placeholder
    }
}
