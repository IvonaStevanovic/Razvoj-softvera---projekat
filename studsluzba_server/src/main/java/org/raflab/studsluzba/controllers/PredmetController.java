package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.services.PredmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predmet")
@RequiredArgsConstructor
public class PredmetController {

    private final PredmetService predmetService;

    @GetMapping("/all")
    public List<PredmetResponse> getAll() {
        return predmetService.getAllPredmeti();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredmetResponse> getById(@PathVariable Long id) {
        PredmetResponse response = predmetService.getPredmetById(id);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PredmetResponse> create(@RequestBody PredmetRequest dto) {
        PredmetResponse response = predmetService.createPredmet(dto);
        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PredmetResponse> update(@PathVariable Long id, @RequestBody PredmetRequest dto) {
        PredmetResponse response = predmetService.updatePredmet(id, dto);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        predmetService.deletePredmet(id);
        return ResponseEntity.noContent().build();
    }
}
