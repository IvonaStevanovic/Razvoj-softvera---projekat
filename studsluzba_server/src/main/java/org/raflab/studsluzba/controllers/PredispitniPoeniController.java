package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredispitniPoeniRequest;
import org.raflab.studsluzba.controllers.response.PredispitniPoeniResponse;
import org.raflab.studsluzba.model.PredispitniPoeni;
import org.raflab.studsluzba.services.PredispitniPoeniService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/predispitni-poeni")
@AllArgsConstructor
public class PredispitniPoeniController {
/*
    private final PredispitniPoeniService service;

    @GetMapping("/all")
    public List<PredispitniPoeniResponse> getAll() {
        return service.findAll().stream()
                .map(Converters::toPredispitniPoeniResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PredispitniPoeniResponse getById(@PathVariable Long id) {
        PredispitniPoeni p = service.findById(id);
        return p != null ? Converters.toPredispitniPoeniResponse(p) : null;
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody PredispitniPoeniRequest request) {
        try {
            PredispitniPoeni p = service.save(request);
            return ResponseEntity.ok(Converters.toPredispitniPoeniResponse(p));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public PredispitniPoeniResponse update(@PathVariable Long id, @RequestBody PredispitniPoeniRequest request) {
        PredispitniPoeni p = service.update(id, request);
        return p != null ? Converters.toPredispitniPoeniResponse(p) : null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

 */
}
