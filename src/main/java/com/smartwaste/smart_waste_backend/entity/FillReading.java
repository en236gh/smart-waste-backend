package com.smartwaste.smart_waste_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "fill_readings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FillReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bin_id", nullable = false)
    private Long binId;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "100.0", inclusive = true)
    @Column(name = "fill_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal fillPercentage;

    @Column(name = "distance_cm", precision = 8, scale = 2)
    private BigDecimal distanceCm;

    @Column(name = "temperature_c", precision = 5, scale = 2)
    private BigDecimal temperatureC;

    @Column(name = "recorded_at", nullable = false)
    private OffsetDateTime recordedAt;
}
