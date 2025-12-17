package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.VisokoskolskaUstanovaRequest;
import org.raflab.studsluzba.controllers.response.VisokoskolskaUstanovaResponse;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.raflab.studsluzba.services.VisokoskolskaUstanovaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/visokoskolska-ustanova")
@AllArgsConstructor
public class VisokoskolskaUstanovaController {
/*
    private final VisokoskolskaUstanovaService service;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody VisokoskolskaUstanovaRequest request) {
        try {
            VisokoskolskaUstanova ustanova = Converters.toVisokoskolskaUstanova(request);
            VisokoskolskaUstanova saved = service.create(ustanova);
            return ResponseEntity.ok(Converters.toVisokoskolskaUstanovaResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", "Ustanova već postoji",
                            "message", e.getMessage()
                    ));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody VisokoskolskaUstanovaRequest request) {
        try {
            VisokoskolskaUstanova ustanovaDetails = Converters.toVisokoskolskaUstanova(request);
            VisokoskolskaUstanova updated = service.update(id, ustanovaDetails);
            return ResponseEntity.ok(Converters.toVisokoskolskaUstanovaResponse(updated));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of(
                            "error", "Greška pri update-u",
                            "message", e.getMessage()
                    ));
        }
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

 */
}
