package com.smartwaste.smart_waste_backend.controller;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record FillReadingRequest(
        @DecimalMin(value = "0.0", inclusive = true)
        @DecimalMax(value = "100.0", inclusive = true)
        BigDecimal fillPercentage,
        @DecimalMin(value = "0.01", inclusive = true)
        BigDecimal distanceCm,
        BigDecimal temperatureC,
        OffsetDateTime recordedAt
) {
    @AssertTrue(message = "Either fillPercentage or distanceCm must be provided")
    public boolean hasMeasurement() {
        return fillPercentage != null || distanceCm != null;
    }
}