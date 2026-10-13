package com.smartwaste.smart_waste_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bin_code", nullable = false, unique = true)
    @NotBlank
    private String binCode;

    private String name;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "bin_type", nullable = false)
    @NotBlank
    @Pattern(regexp = "REAL|SIMULATED")
    private String binType;

    @Column(name = "capacity_liters")
    @Positive
    private Integer capacityLiters;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "lid_state", nullable = false, length = 10)
    @Builder.Default
    private String lidState = "CLOSED";

    @Column(name = "lid_command", nullable = false, length = 10)
    @Builder.Default
    private String lidCommand = "AUTO";

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}