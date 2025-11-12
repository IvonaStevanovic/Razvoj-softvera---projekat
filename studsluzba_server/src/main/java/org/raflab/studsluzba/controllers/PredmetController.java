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

    @PostMapping("/add")
    public ResponseEntity<PredmetResponse> addPredmet(@RequestBody PredmetRequest request) {
        PredmetResponse response = predmetService.createPredmet(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public List<PredmetResponse> getAll() {
        return predmetService.getAllPredmeti();
    }

    @GetMapping("/studijski-program/{id}")
    public List<PredmetResponse> getPredmetiByStudijskiProgram(@PathVariable Long id) {
        return predmetService.getPredmetiByStudijskiProgram(id);
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

    @GetMapping("/{predmetId}/prosecna-ocena")
    public Double getProsecnaOcena(@PathVariable Long predmetId,
                                   @RequestParam int pocetna,
                                   @RequestParam int krajnja) {
        return predmetService.getProsecnaOcena(predmetId, pocetna, krajnja);
    }

}
