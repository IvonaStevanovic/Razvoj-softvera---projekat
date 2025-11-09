package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.VisokoskolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoskolskaUstanovaResponse;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.raflab.studsluzba.services.VisokoskolskaUstanovaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visokoskolska-ustanova")
@AllArgsConstructor
public class VisokoskolskaUstanovaController {

    private final VisokoskolskaUstanovaService service;

    @PostMapping
    public VisokoskolskaUstanovaResponse create(@RequestBody VisokoskolskaUstanovaRequest request) {
        VisokoskolskaUstanova ustanova = Converters.toVisokoskolskaUstanova(request);
        return Converters.toVisokoskolskaUstanovaResponse(service.create(ustanova));
    }

    @PutMapping("/{id}")
    public VisokoskolskaUstanovaResponse update(@PathVariable Long id, @RequestBody VisokoskolskaUstanovaRequest request) {
        VisokoskolskaUstanova ustanovaDetails = Converters.toVisokoskolskaUstanova(request);
        return Converters.toVisokoskolskaUstanovaResponse(service.update(id, ustanovaDetails));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public VisokoskolskaUstanovaResponse findById(@PathVariable Long id) {
        return service.findById(id)
                .map(Converters::toVisokoskolskaUstanovaResponse)
                .orElseThrow(() -> new RuntimeException("Ustanova not found"));
    }

    @GetMapping("/all")
    public List<VisokoskolskaUstanovaResponse> findAll() {
        return Converters.toVisokoskolskaUstanovaResponseList(service.findAll());
    }

    @GetMapping("/mesto/{mesto}")
    public List<VisokoskolskaUstanovaResponse> findByMesto(@PathVariable String mesto) {
        return Converters.toVisokoskolskaUstanovaResponseList(service.findByMesto(mesto));
    }

    @GetMapping("/vrsta/{vrsta}")
    public List<VisokoskolskaUstanovaResponse> findByVrsta(@PathVariable String vrsta) {
        return Converters.toVisokoskolskaUstanovaResponseList(service.findByVrsta(vrsta));
    }
}
