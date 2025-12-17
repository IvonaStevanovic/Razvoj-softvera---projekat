package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.VrstaStudijaRequest;
import org.raflab.studsluzba.controllers.response.VrstaStudijaResponse;
import org.raflab.studsluzba.model.VrstaStudija;
import org.raflab.studsluzba.services.VrstaStudijaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vrsta-studija")
@AllArgsConstructor
public class VrstaStudijaController {
/*
    private final VrstaStudijaService service;

    @PostMapping("/add")
    public ResponseEntity<?> create(@RequestBody VrstaStudijaRequest request) {
        VrstaStudija vrsta = Converters.toVrstaStudija(request);
        try {
            VrstaStudija saved = service.create(vrsta);
            return ResponseEntity.ok(Converters.toVrstaStudijaResponse(saved));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public VrstaStudijaResponse update(@PathVariable Long id, @RequestBody VrstaStudijaRequest request) {
        VrstaStudija vrstaDetails = Converters.toVrstaStudija(request);
        return Converters.toVrstaStudijaResponse(service.update(id, vrstaDetails));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public VrstaStudijaResponse findById(@PathVariable Long id) {
        return service.findById(id)
                .map(Converters::toVrstaStudijaResponse)
                .orElseThrow(() -> new RuntimeException("Vrsta studija not found"));
    }

    @GetMapping("/all")
    public List<VrstaStudijaResponse> findAll() {
        return Converters.toVrstaStudijaResponseList(service.findAll());
    }

    @GetMapping("/oznaka/{oznaka}")
    public VrstaStudijaResponse findByOznaka(@PathVariable String oznaka) {
        return Converters.toVrstaStudijaResponse(service.findByOznaka(oznaka));
    }

    @GetMapping("/pun-naziv/{punNaziv}")
    public VrstaStudijaResponse findByPunNaziv(@PathVariable String punNaziv) {
        return Converters.toVrstaStudijaResponse(service.findByPunNaziv(punNaziv));
    }

    @GetMapping("/pretraga/{deoNaziva}")
    public List<VrstaStudijaResponse> findByPunNazivContaining(@PathVariable String deoNaziva) {
        return Converters.toVrstaStudijaResponseList(service.findByPunNazivContaining(deoNaziva));
    }

 */
}
