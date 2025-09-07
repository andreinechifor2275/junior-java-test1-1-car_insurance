package com.example.carins.web;

import com.example.carins.web.dto.ClaimRequest;
import com.example.carins.model.Car;
import com.example.carins.model.InsuranceClaim;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsuranceClaimRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
public class ClaimController {

    private final CarRepository carRepository;
    private final InsuranceClaimRepository claimRepository;

    public ClaimController(CarRepository carRepository, InsuranceClaimRepository claimRepository) {
        this.carRepository = carRepository;
        this.claimRepository = claimRepository;
    }

    //POST request - /api/cars/{carId}/claims
    @PostMapping("/{carId}/claims")
    public ResponseEntity<?> createClaim(
            @PathVariable Long carId,
            @Valid @RequestBody ClaimRequest request) {

        Optional<Car> carOpt = carRepository.findById(carId);
        if (carOpt.isEmpty()) return ResponseEntity.notFound().build();

        InsuranceClaim claim = new InsuranceClaim();
        claim.setCar(carOpt.get());
        claim.setClaimDate(request.getClaimDate());
        claim.setDescription(request.getDescription());
        claim.setAmount(request.getAmount());

        InsuranceClaim saved = claimRepository.save(claim);

        return ResponseEntity
                .created(URI.create("/api/cars/" + carId + "/claims/" + saved.getId()))
                .body(saved);
    }

    //GET request - /api/cars/{carId}/history
    @GetMapping("/{carId}/history")
    public ResponseEntity<?> getHistory(@PathVariable Long carId) {
        Optional<Car> carOpt = carRepository.findById(carId);
        if (carOpt.isEmpty()) return ResponseEntity.notFound().build();

        List<InsuranceClaim> claims = claimRepository.findByCarIdOrderByClaimDateAsc(carId);
        return ResponseEntity.ok(claims);
    }
}
