package com.smartwaste.smart_waste_backend.repository;

import com.smartwaste.smart_waste_backend.entity.FillReading;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FillReadingRepository extends JpaRepository<FillReading, Long> {

    List<FillReading> findByBinIdOrderByRecordedAtDesc(Long binId);
}
