package com.example.carins;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.CarService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CarInsuranceApplicationTests {

    @Autowired
    CarService service;

    @Test
    void insuranceValidityBasic() {
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2024-06-01")));
        assertTrue(service.isInsuranceValid(1L, LocalDate.parse("2025-06-01")));
        assertFalse(service.isInsuranceValid(2L, LocalDate.parse("2025-02-01")));
    }


    @Autowired
    InsurancePolicyRepository policyRepository;

    @Autowired
    CarRepository carRepository;

    @Test
    void shouldNotSavePolicyWithoutEndDate() {
        Car car = carRepository.findById(1L).orElseThrow();
        InsurancePolicy policy = new InsurancePolicy();
        policy.setCar(car);
        policy.setStartDate(LocalDate.now());

        assertThrows(ConstraintViolationException.class, () -> {
            policyRepository.saveAndFlush(policy);
        });
    }

    @Test
    void nonExistingCarShouldReturnFalse() {
        assertFalse(service.isInsuranceValid(999L, LocalDate.parse("2025-06-01")));
    }

    @Test
    void impossibleDateShouldReturnFalse() {
        assertFalse(service.isInsuranceValid(1L, LocalDate.parse("1800-01-01")));
        assertFalse(service.isInsuranceValid(1L, LocalDate.now().plusYears(20)));
    }

    @Test
    void nullInputsShouldReturnFalse() {
        assertFalse(service.isInsuranceValid(null, LocalDate.now()));
        assertFalse(service.isInsuranceValid(1L, null));
        assertFalse(service.isInsuranceValid(null, null));
    }
}
