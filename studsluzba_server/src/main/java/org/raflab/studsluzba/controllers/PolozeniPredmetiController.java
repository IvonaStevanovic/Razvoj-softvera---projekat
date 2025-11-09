package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PolozeniPredmetiRequest;
import org.raflab.studsluzba.controllers.response.PolozeniPredmetiResponse;
import org.raflab.studsluzba.services.PolozeniPredmetiService;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/polozeni-predmeti")
@AllArgsConstructor
public class PolozeniPredmetiController {
    private final PolozeniPredmetiService service;
    private final EntityMappers mappers;

    @PostMapping("/add")
    public PolozeniPredmetiResponse add(@RequestBody PolozeniPredmetiRequest request) {
        return mappers.fromPolozeniPredmetiToResponse(service.save(request));
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
