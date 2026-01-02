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
    private StudijskiProgramService service;

    @GetMapping("/all")
    public ResponseEntity<List<StudijskiProgramResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudijskiProgramResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<StudijskiProgramResponse> add(@RequestBody @Valid StudijskiProgramRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/prosek-programa")
    public ResponseEntity<Double> getProsekPrograma(@PathVariable Long id,
                                                    @RequestParam int godinaOd,
                                                    @RequestParam int godinaDo) {
        Double prosek = service.getProsecnaOcenaProgramazaPeriod(id, godinaOd, godinaDo);
        return ResponseEntity.ok(prosek);
    }
}