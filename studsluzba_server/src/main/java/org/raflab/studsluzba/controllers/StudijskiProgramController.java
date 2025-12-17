package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.services.StudijskiProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/studijski-program")
@AllArgsConstructor
public class StudijskiProgramController {
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

}
