package com.smartwaste.smart_waste_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LidStateRequest(
        @NotBlank
        @Pattern(regexp = "OPEN|CLOSED")
        String state
) {
}