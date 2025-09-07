package com.example.carins.service;

import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Service
public class PolicyExpiryLogger {

    private final InsurancePolicyRepository policyRepository;
    private final Set<Long> loggedPolicies = new HashSet<>();

    public PolicyExpiryLogger(InsurancePolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void logExpiredPolicies() {
        LocalDate now = LocalDate.now();
        for (InsurancePolicy policy : policyRepository.findAll()) {
            if (!loggedPolicies.contains(policy.getId())
                    && policy.getEndDate() != null
                    && !policy.getEndDate().isAfter(now)) {
                System.out.println("Policy " + policy.getId() + " for car " + policy.getCar().getId()
                        + " expired on " + policy.getEndDate());
                loggedPolicies.add(policy.getId());
            }
        }
    }
}
