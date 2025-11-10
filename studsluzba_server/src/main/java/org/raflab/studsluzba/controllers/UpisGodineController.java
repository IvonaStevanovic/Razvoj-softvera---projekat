package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.response.UpisGodineResponse;
import org.raflab.studsluzba.model.UpisGodine;
import org.raflab.studsluzba.repositories.UpisGodineRepository;
import org.raflab.studsluzba.services.UpisGodineService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/upis-godine")
@AllArgsConstructor
public class UpisGodineController {

    private final UpisGodineService service;
    private final UpisGodineRepository upisGodineRepository;
    @GetMapping("/all")
    public List<UpisGodineResponse> getAll() {
        return upisGodineRepository.findAllWithPrenetiPredmeti()
                .stream()
                .map(EntityMappers::toUpisGodineResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UpisGodineResponse getById(@PathVariable Long id) {
        UpisGodine upis = service.findById(id);
        return upis != null ? Converters.toUpisGodineResponse(upis) : null;
    }

    @PostMapping
    public UpisGodineResponse create(@RequestBody UpisGodineRequest request) {
        return Converters.toUpisGodineResponse(service.save(request));
    }

    @PutMapping("/{id}")
    public UpisGodineResponse update(@PathVariable Long id, @RequestBody UpisGodineRequest request) {
        UpisGodine upis = service.update(id, request);
        return upis != null ? Converters.toUpisGodineResponse(upis) : null;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
