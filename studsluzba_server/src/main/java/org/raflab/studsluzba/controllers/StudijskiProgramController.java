package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.mappers.StudijskiProgramConverter;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.VrstaStudija;
import org.raflab.studsluzba.services.StudijskiProgramService;
import org.raflab.studsluzba.services.VrstaStudijaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/studijski-programi")
@CrossOrigin
public class StudijskiProgramController {

    @Autowired
    private StudijskiProgramService studijskiProgramService;

    @Autowired
    private VrstaStudijaService vrstaStudijaService;

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<StudijskiProgramResponse> getById(@PathVariable Long id) {
        StudijskiProgram program = studijskiProgramService.findById(id).orElse(null);
        if (program == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(StudijskiProgramConverter.toResponse(program));
    }

    // ADD
    @PostMapping
    public ResponseEntity<StudijskiProgramResponse> add(
            @RequestBody @Valid StudijskiProgramRequest request) {

        if (studijskiProgramService.existsByOznaka(request.getOznaka())) {
            return ResponseEntity.badRequest().build();
        }

        VrstaStudija vrstaStudija = null;
        if (request.getVrstaStudijaId() != null) {
            vrstaStudija = vrstaStudijaService.findById(request.getVrstaStudijaId())
                    .orElseThrow(() -> new RuntimeException("Vrsta studija ne postoji"));
        }

        StudijskiProgram program =
                StudijskiProgramConverter.toEntity(request, vrstaStudija);

        StudijskiProgram saved = studijskiProgramService.save(program);

        return ResponseEntity.ok(StudijskiProgramConverter.toResponse(saved));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!studijskiProgramService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        studijskiProgramService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

/*
    private final StudijskiProgramService service;

    @PostMapping("/add")
    public StudijskiProgramResponse add(@RequestBody StudijskiProgramRequest request) {
        return service.save(request);
    }

    @PutMapping("/update/{id}")
    public StudijskiProgramResponse update(@PathVariable Long id, @RequestBody StudijskiProgramRequest request) {
        // dodaj ID u entitet za update
        return service.save(request);
    }

    @GetMapping("/all")
    public List<StudijskiProgramResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public StudijskiProgramResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok("Obrisan studijski program sa ID " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

 */


