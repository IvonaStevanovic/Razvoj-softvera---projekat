package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PolozeniPredmetiRequest;
import org.raflab.studsluzba.controllers.response.PolozeniPredmetiResponse;
import org.raflab.studsluzba.model.PolozeniPredmeti;
import org.raflab.studsluzba.model.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzba.services.PolozeniPredmetiService;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;@RestController
@RequestMapping("/api/polozeni-predmeti")
@AllArgsConstructor
public class PolozeniPredmetiController {

    private final PolozeniPredmetiService polozeniPredmetiService;
    private final PolozeniPredmetiService predmetService;

    @GetMapping("/indeks/{brojIndeksa}/polozeni")
    public ResponseEntity<Page<PolozeniPredmetiResponse>> getPolozeniPredmeti(
            @PathVariable Integer brojIndeksa,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "datumPolaganja") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<PolozeniPredmetiResponse> result =
                polozeniPredmetiService.getPolozeniPredmetiPoBrojuIndeksa(brojIndeksa, pageable);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    public ResponseEntity<PolozeniPredmetiResponse> dodajPolozeniPredmet(
            @RequestBody PolozeniPredmetiRequest request) {

        PolozeniPredmetiResponse response = polozeniPredmetiService.dodajPolozeniPredmet(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{studentIndeksId}/nepolozeni")
    public Page<NepolozeniPredmetDTO> getNepolozeniPredmeti(
            @PathVariable Long studentIndeksId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return predmetService.getNepolozeniPredmeti(studentIndeksId, pageable);
    }

/*
    private final PolozeniPredmetiService service;
    private final EntityMappers mappers;

    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> add(@RequestBody PolozeniPredmetiRequest request) {
        try {
            PolozeniPredmeti saved = service.save(request);
            Map<String, String> response = new HashMap<>();
            response.put("id", saved.getId().toString());
            response.put("message", "Položeni predmet uspešno dodat");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("već postoji")) {
                Map<String, String> error = new HashMap<>();
                error.put("message", e.getMessage());
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error); // 409 + poruka
            }
            throw e; // ostali problemi -> 500
        }
    }


    @GetMapping("/all")
    public List<PolozeniPredmetiResponse> getAll() {
        return service.getAll().stream()
                .map(mappers::fromPolozeniPredmetiToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PolozeniPredmetiResponse getById(@PathVariable Long id) {
        return mappers.fromPolozeniPredmetiToResponse(service.getById(id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

 */
}
