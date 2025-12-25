package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.mappers.PredmetConverter;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.services.StudijskiProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


    @RestController
    @RequestMapping("/api/predmeti")
    @CrossOrigin
    public class PredmetController {

        @Autowired
        private PredmetService predmetService;

        @Autowired
        private StudijskiProgramService studijskiProgramService;

        // GET BY ID
        @GetMapping("/{id}")
        public ResponseEntity<PredmetResponse> getById(@PathVariable Long id) {
            Predmet predmet = predmetService.findById(id).orElse(null);
            if (predmet == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(PredmetConverter.toResponse(predmet));
        }

        // ADD
        @PostMapping
        public ResponseEntity<PredmetResponse> add(@RequestBody @Valid PredmetRequest request) {

            if (predmetService.existsBySifra(request.getSifra())) {
                return ResponseEntity.badRequest().build();
            }

            StudijskiProgram studijskiProgram = null;
            if (request.getStudijskiProgramId() != null) {
                studijskiProgram = studijskiProgramService.findById(request.getStudijskiProgramId())
                        .orElseThrow(() -> new RuntimeException("Studijski program ne postoji"));
            }

            Predmet predmet = PredmetConverter.toEntity(request, studijskiProgram);
            Predmet saved = predmetService.save(predmet);

            return ResponseEntity.ok(PredmetConverter.toResponse(saved));
        }

        // DELETE
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
            if (!predmetService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            predmetService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
    }

/*
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

 */

