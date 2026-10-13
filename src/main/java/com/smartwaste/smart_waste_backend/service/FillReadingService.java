package com.smartwaste.smart_waste_backend.service;

import com.smartwaste.smart_waste_backend.entity.FillReading;
import com.smartwaste.smart_waste_backend.exception.ResourceNotFoundException;
import com.smartwaste.smart_waste_backend.repository.BinRepository;
import com.smartwaste.smart_waste_backend.repository.FillReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FillReadingService {

    private final FillReadingRepository fillReadingRepository;
    private final BinRepository binRepository;

    @Transactional(readOnly = true)
    public List<FillReading> getReadingsForBin(Long binId) {
        ensureBinExists(binId);
        return fillReadingRepository.findByBinIdOrderByRecordedAtDesc(binId);
    }

    @Transactional
    public FillReading createReading(Long binId, FillReading reading) {
        ensureBinExists(binId);
        reading.setBinId(binId);
        if (reading.getRecordedAt() == null) {
            reading.setRecordedAt(OffsetDateTime.now());
        }
        return fillReadingRepository.save(reading);
    }

    private void ensureBinExists(Long binId) {
        if (!binRepository.existsById(binId)) {
            throw new ResourceNotFoundException("Bin not found: " + binId);
        }
    }
}
