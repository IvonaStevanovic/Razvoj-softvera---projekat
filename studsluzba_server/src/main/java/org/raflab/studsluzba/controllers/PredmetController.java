package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.services.PredmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predmeti")
public class PredmetController {
    private final PredmetService predmetService;

    public PredmetController(PredmetService predmetService) {
        this.predmetService = predmetService;
    }

    @GetMapping
    public List<PredmetResponse> getAll() {
        return predmetService.getAllPredmeti();
    }

    @GetMapping("/{id}")
    public PredmetResponse getById(@PathVariable Long id) {
        return predmetService.getPredmetById(id);
    }

    @PostMapping
    public PredmetResponse create(@RequestBody PredmetRequest dto) {
        return predmetService.createPredmet(dto);
    }

    @PutMapping("/{id}")
    public PredmetResponse update(@PathVariable Long id, @RequestBody PredmetRequest dto) {
        return predmetService.updatePredmet(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        predmetService.deletePredmet(id);
        return ResponseEntity.noContent().build();
    }
}
