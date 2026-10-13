package com.smartwaste.smart_waste_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record LidCommandRequest(
        @NotBlank
        @Pattern(regexp = "AUTO|OPEN|CLOSED")
        String command
) {
}