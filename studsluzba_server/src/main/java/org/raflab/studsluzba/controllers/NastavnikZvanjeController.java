package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.controllers.response.NastavnikZvanjeResponse;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.raflab.studsluzba.services.NastavnikZvanjeService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nastavnik-zvanje")
@RequiredArgsConstructor
public class NastavnikZvanjeController {
    private final NastavnikZvanjeService service;
    private final NastavnikZvanjeRepository repository;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid NastavnikZvanjeRequest request) {
        NastavnikZvanje nz = service.save(request);

        if (nz == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Greška: nastavnik ne postoji ili već ima aktivno zvanje koje ste uneli.");
        }

        return ResponseEntity.ok(nz.getId());
    }


    @GetMapping("/all")
    public List<NastavnikZvanjeResponse> getAll() {
        return Converters.toNastavnikZvanjeResponseList(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<NastavnikZvanje> nz = repository.findById(id);

        if (nz.isEmpty()) {
            return ResponseEntity.badRequest().body("Zvanje sa ID " + id + " ne postoji.");
        }

        return ResponseEntity.ok(Converters.toNastavnikZvanjeResponse(nz.get()));
    }

}
