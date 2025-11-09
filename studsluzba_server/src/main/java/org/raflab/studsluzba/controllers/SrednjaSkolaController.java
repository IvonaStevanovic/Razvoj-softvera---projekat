package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SrednjaSkolaRequest;
import org.raflab.studsluzba.controllers.response.SrednjaSkolaResponse;
import org.raflab.studsluzba.model.SrednjaSkola;
import org.raflab.studsluzba.services.SrednjaSkolaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/srednja-skola")
@AllArgsConstructor
public class SrednjaSkolaController {

    private final SrednjaSkolaService service;

    @GetMapping("/all")
    public List<SrednjaSkolaResponse> getAll() {
        return service.getAll().stream()
                .map(Converters::toSrednjaSkolaResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SrednjaSkolaResponse> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(Converters::toSrednjaSkolaResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SrednjaSkolaResponse create(@RequestBody SrednjaSkolaRequest request) {
        SrednjaSkola entity = Converters.toSrednjaSkola(request);
        return Converters.toSrednjaSkolaResponse(service.create(entity));
    }

    @PutMapping("/{id}")
    public SrednjaSkolaResponse update(@PathVariable Long id, @RequestBody SrednjaSkolaRequest request) {
        SrednjaSkola entity = Converters.toSrednjaSkola(request);
        return Converters.toSrednjaSkolaResponse(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mesto/{mesto}")
    public List<SrednjaSkolaResponse> findByMesto(@PathVariable String mesto) {
        return service.findByMesto(mesto).stream()
                .map(Converters::toSrednjaSkolaResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/vrsta/{vrsta}")
    public List<SrednjaSkolaResponse> findByVrsta(@PathVariable String vrsta) {
        return service.findByVrsta(vrsta).stream()
                .map(Converters::toSrednjaSkolaResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/naziv")
    public List<SrednjaSkolaResponse> findByNaziv(@RequestParam String naziv) {
        return service.findByNazivContaining(naziv).stream()
                .map(Converters::toSrednjaSkolaResponse)
                .collect(Collectors.toList());
    }
}
