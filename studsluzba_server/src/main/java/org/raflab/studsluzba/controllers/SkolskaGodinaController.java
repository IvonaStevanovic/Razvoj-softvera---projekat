package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SkolskaGodinaRequest;
import org.raflab.studsluzba.controllers.response.SkolskaGodinaResponse;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.services.SkolskaGodinaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skolska-godina")
@AllArgsConstructor
public class SkolskaGodinaController {

    private final SkolskaGodinaService service;

    @PostMapping("/add")
    public SkolskaGodinaResponse create(@RequestBody SkolskaGodinaRequest request) {
        SkolskaGodina godina = Converters.toSkolskaGodina(request);
        return Converters.toSkolskaGodinaResponse(service.create(godina));
    }

    @PutMapping("/{id}")
    public SkolskaGodinaResponse update(@PathVariable Long id, @RequestBody SkolskaGodinaRequest request) {
        SkolskaGodina godinaDetails = Converters.toSkolskaGodina(request);
        return Converters.toSkolskaGodinaResponse(service.update(id, godinaDetails));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public SkolskaGodinaResponse findById(@PathVariable Long id) {
        return service.findById(id)
                .map(Converters::toSkolskaGodinaResponse)
                .orElseThrow(() -> new RuntimeException("Skolska godina not found"));
    }

    @GetMapping("/all")
    public List<SkolskaGodinaResponse> findAll() {
        return Converters.toSkolskaGodinaResponseList(service.findAll());
    }

    @GetMapping("/aktivne")
    public List<SkolskaGodinaResponse> findAktivne() {
        return Converters.toSkolskaGodinaResponseList(service.findAktivne());
    }

    @GetMapping("/naziv/{naziv}")
    public SkolskaGodinaResponse findByNaziv(@PathVariable String naziv) {
        return Converters.toSkolskaGodinaResponse(service.findByNaziv(naziv));
    }
}
