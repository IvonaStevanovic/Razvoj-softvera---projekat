package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.services.PrijavaIspitaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prijava-ispita")
@AllArgsConstructor
public class PrijavaIspitaController {

    private final PrijavaIspitaService service;

    @GetMapping("/all")
    public List<PrijavaIspitaResponse> getAll() {
        return service.findAll().stream()
                .map(Converters::toPrijavaIspitaResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PrijavaIspitaResponse getById(@PathVariable Long id) {
        PrijavaIspita p = service.findById(id);
        return p != null ? Converters.toPrijavaIspitaResponse(p) : null;
    }

    @PostMapping("/add")
    public PrijavaIspitaResponse create(@RequestBody PrijavaIspitaRequest request) {
        PrijavaIspita p = service.save(request);
        return Converters.toPrijavaIspitaResponse(p);
    }

    @PutMapping("/{id}")
    public PrijavaIspitaResponse update(@PathVariable Long id, @RequestBody PrijavaIspitaRequest request) {
        PrijavaIspita p = service.update(id, request);
        return p != null ? Converters.toPrijavaIspitaResponse(p) : null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
