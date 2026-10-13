package com.smartwaste.smart_waste_backend.service;

import com.smartwaste.smart_waste_backend.entity.Bin;
import com.smartwaste.smart_waste_backend.exception.ResourceNotFoundException;
import com.smartwaste.smart_waste_backend.repository.BinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BinService {

    private final BinRepository binRepository;

    @Transactional(readOnly = true)
    public List<Bin> getAllBins() {
        return binRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Bin getBinById(Long id) {
        return findBin(id);
    }

    @Transactional
    public Bin createBin(Bin bin) {
        if (bin.getActive() == null) {
            bin.setActive(true);
        }
        if (bin.getCreatedAt() == null) {
            bin.setCreatedAt(OffsetDateTime.now());
        }
        return binRepository.save(bin);
    }

    @Transactional
    public Bin updateBin(Long id, Bin bin) {
        Bin existingBin = findBin(id);
        existingBin.setBinCode(bin.getBinCode());
        existingBin.setName(bin.getName());
        existingBin.setLatitude(bin.getLatitude());
        existingBin.setLongitude(bin.getLongitude());
        existingBin.setBinType(bin.getBinType());
        existingBin.setCapacityLiters(bin.getCapacityLiters());
        if (bin.getActive() != null) {
            existingBin.setActive(bin.getActive());
        }
        return binRepository.save(existingBin);
    }

    @Transactional
    public void deleteBin(Long id) {
        Bin bin = findBin(id);
        binRepository.delete(bin);
    }

    private Bin findBin(Long id) {
        return binRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bin not found: " + id));
    }
}
