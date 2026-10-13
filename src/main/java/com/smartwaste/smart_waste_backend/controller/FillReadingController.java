package com.smartwaste.smart_waste_backend.controller;

import com.smartwaste.smart_waste_backend.entity.FillReading;
import com.smartwaste.smart_waste_backend.service.FillReadingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bins/{binId}/readings")
@RequiredArgsConstructor
public class FillReadingController {

    private final FillReadingService fillReadingService;

    @GetMapping
    public List<FillReading> getReadingsForBin(@PathVariable Long binId) {
        return fillReadingService.getReadingsForBin(binId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FillReading createReading(@PathVariable Long binId,
                                     @Valid @RequestBody FillReading reading) {
        return fillReadingService.createReading(binId, reading);
    }
}
