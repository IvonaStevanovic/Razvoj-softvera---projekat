package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.SlusaPredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/slusa-predmet")
@AllArgsConstructor
public class SlusaPredmetController {
/*
    private final SlusaPredmetService slusaPredmetService;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    @GetMapping("/all")
    public List<SlusaPredmetResponse> getAll() {
        return slusaPredmetService.findAll()
                .stream()
                .map(Converters::toSlusaPredmetResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody SlusaPredmetRequest request) {
        try {
            SlusaPredmetResponse spResponse = slusaPredmetService.create(request);
            return ResponseEntity.ok(spResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SlusaPredmetRequest request) {
        try {
            SlusaPredmetResponse spResponse = slusaPredmetService.update(id, request);
            return ResponseEntity.ok(spResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlusaPredmetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(slusaPredmetService.getById(id));
    }

    @GetMapping("/student/{indeksId}")
    public ResponseEntity<List<SlusaPredmet>> getByStudent(@PathVariable Long indeksId) {
        return ResponseEntity.ok(slusaPredmetService.findByStudentIndeks(indeksId));
    }

 */


}
