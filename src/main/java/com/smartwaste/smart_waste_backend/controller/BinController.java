package com.smartwaste.smart_waste_backend.controller;

import com.smartwaste.smart_waste_backend.entity.Bin;
import com.smartwaste.smart_waste_backend.service.BinService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bins")
@RequiredArgsConstructor
public class BinController {

    private final BinService binService;

    @GetMapping
    public List<Bin> getAllBins() {
        return binService.getAllBins();
    }

    @GetMapping("/{id}")
    public Bin getBinById(@PathVariable Long id) {
        return binService.getBinById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bin createBin(@Valid @RequestBody Bin bin) {
        return binService.createBin(bin);
    }

    @PutMapping("/{id}")
    public Bin updateBin(@PathVariable Long id, @Valid @RequestBody Bin bin) {
        return binService.updateBin(id, bin);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBin(@PathVariable Long id) {
        binService.deleteBin(id);
    }
}
