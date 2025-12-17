package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.response.UpisGodineResponse;
import org.raflab.studsluzba.model.UpisGodine;
import org.raflab.studsluzba.repositories.UpisGodineRepository;
import org.raflab.studsluzba.services.UpisGodineService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/upis-godine")
@AllArgsConstructor
public class UpisGodineController {
/*
    private final UpisGodineService service;
    private final UpisGodineRepository upisGodineRepository;

    @GetMapping("/all")
    public List<UpisGodineResponse> getAll() {
        return upisGodineRepository.findAllWithPrenetiPredmeti()
                .stream()
                .map(EntityMappers::toUpisGodineResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody UpisGodineRequest request) {
        try {
            UpisGodine upis = service.save(request);
            return ResponseEntity.ok(Converters.toUpisGodineResponse(upis));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("već postoji")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }*/
}
