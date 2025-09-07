package com.example.carins.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimRequest {

    @NotNull(message = "claimDate is required")
    private LocalDate claimDate;

    @NotBlank(message = "description is required")
    private String description;

    @NotNull(message = "amount is required")
    private BigDecimal amount;

    public LocalDate getClaimDate() { return claimDate; }
    public void setClaimDate(LocalDate claimDate) { this.claimDate = claimDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
