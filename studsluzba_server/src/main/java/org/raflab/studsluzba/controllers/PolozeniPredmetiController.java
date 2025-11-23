package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PolozeniPredmetiRequest;
import org.raflab.studsluzba.controllers.response.PolozeniPredmetiResponse;
import org.raflab.studsluzba.model.PolozeniPredmeti;
import org.raflab.studsluzba.services.PolozeniPredmetiService;
import org.raflab.studsluzba.utils.EntityMappers;
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
}
