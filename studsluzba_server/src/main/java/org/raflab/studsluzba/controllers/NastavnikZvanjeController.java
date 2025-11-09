package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.services.NastavnikZvanjeService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/nastavnik-zvanje")
@RequiredArgsConstructor
public class NastavnikZvanjeController {
    private final NastavnikZvanjeService service;

    @PostMapping("/add")
    public Long add(@RequestBody @Valid NastavnikZvanjeRequest request) {
        NastavnikZvanje nz = service.save(request);
        return nz.getId();
    }

    @GetMapping("/all")
    public List<NastavnikZvanjeResponse> getAll() {
        return Converters.toNastavnikZvanjeResponseList(service.findAll());
    }

    @GetMapping("/{id}")
    public NastavnikZvanjeResponse getById(@PathVariable Long id) {
        return Converters.toNastavnikZvanjeResponse(service.findById(id));
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "Obrisano zvanje sa ID " + id;
    }
}
