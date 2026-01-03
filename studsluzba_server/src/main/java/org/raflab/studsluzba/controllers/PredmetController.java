package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.services.PredmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/predmet")
@CrossOrigin
public class PredmetController {

    @Autowired
    private PredmetService predmetService;

    @GetMapping("/all")
    public ResponseEntity<List<PredmetResponse>> getAll() {
        return ResponseEntity.ok(predmetService.getAllPredmeti());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PredmetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(predmetService.getPredmetById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<PredmetResponse> add(@RequestBody @Valid PredmetRequest request) {
        return ResponseEntity.ok(predmetService.createPredmet(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            predmetService.deletePredmet(id);
            return ResponseEntity.noContent().build(); // Status 204 ako je uspešno
        } catch (RuntimeException e) {
            // Vraća status 400 i tvoju poruku: "Ne možete obrisati predmet..."
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<PredmetResponse>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(predmetService.getPredmetiByProgram(programId));
    }

    @GetMapping("/{id}/prosek")
    public ResponseEntity<Double> getProsek(@PathVariable Long id,
                                            @RequestParam Integer odGodina,
                                            @RequestParam Integer doGodina) {
        return ResponseEntity.ok(predmetService.getProsek(id, odGodina, doGodina));
    }
}
