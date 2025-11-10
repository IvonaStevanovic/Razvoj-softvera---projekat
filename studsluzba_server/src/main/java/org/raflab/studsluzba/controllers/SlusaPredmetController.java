package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.services.SlusaPredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/slusa-predmet")
@AllArgsConstructor
public class SlusaPredmetController {

    private final SlusaPredmetService slusaPredmetService;

    @GetMapping("/all")
    public List<SlusaPredmetResponse> getAll() {
        return slusaPredmetService.findAll()
                .stream()
                .map(Converters::toSlusaPredmetResponse)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<SlusaPredmet> create(@RequestBody SlusaPredmet slusaPredmet) {
        return ResponseEntity.ok(slusaPredmetService.create(slusaPredmet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SlusaPredmet> update(@PathVariable Long id, @RequestBody SlusaPredmet slusaPredmet) {
        SlusaPredmet updated = slusaPredmetService.update(id, slusaPredmet);
        if (updated != null) return ResponseEntity.ok(updated);
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        slusaPredmetService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlusaPredmet> getById(@PathVariable Long id) {
        SlusaPredmet sp = slusaPredmetService.findById(id);
        if (sp != null) return ResponseEntity.ok(sp);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/student/{indeksId}")
    public ResponseEntity<List<SlusaPredmet>> getByStudent(@PathVariable Long indeksId) {
        return ResponseEntity.ok(slusaPredmetService.findByStudentIndeks(indeksId));
    }

    @GetMapping("/predmet/{predmetId}/nastavnik/{nastavnikId}")
    public ResponseEntity<List<?>> getStudentiPoPredmetu(@PathVariable Long predmetId, @PathVariable Long nastavnikId) {
        return ResponseEntity.ok(slusaPredmetService.findStudentiPoPredmetu(predmetId, nastavnikId));
    }

    @GetMapping("/ne-slusaju/{drziPredmetId}")
    public ResponseEntity<List<?>> getStudentiNeSlusajuPredmet(@PathVariable Long drziPredmetId) {
        return ResponseEntity.ok(slusaPredmetService.findStudentiNeSlusajuPredmet(drziPredmetId));
    }
}
