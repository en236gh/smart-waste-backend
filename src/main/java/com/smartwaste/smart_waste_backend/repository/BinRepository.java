package com.smartwaste.smart_waste_backend.repository;

import com.smartwaste.smart_waste_backend.entity.Bin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BinRepository extends JpaRepository<Bin, Long> {
}
