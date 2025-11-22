package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.GrupaRequest;
import org.raflab.studsluzba.controllers.response.GrupaResponse;
import org.raflab.studsluzba.model.Grupa;
import org.raflab.studsluzba.services.GrupaService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/grupa")
@RequiredArgsConstructor
public class GrupaController {
    private final GrupaService grupaService;

    @PostMapping("/add")
    public ResponseEntity<?> addGrupa(@RequestBody GrupaRequest request) {
        try {
            Grupa grupa = grupaService.save(request);
            return ResponseEntity.ok(grupa.getId());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }


    @GetMapping("/all")
    public List<GrupaResponse> getAllGrupe() {
        return Converters.toGrupaResponseList(grupaService.findAll());
    }

    @GetMapping("/{id}")
    public GrupaResponse getGrupaById(@PathVariable Long id) {
        Optional<Grupa> grupa = grupaService.findById(id);
        return grupa.map(Converters::toGrupaResponse).orElse(null);
    }

    @GetMapping("/studijski-program/{idPrograma}")
    public List<GrupaResponse> getGrupeByStudijskiProgram(@PathVariable Long idPrograma) {
        return Converters.toGrupaResponseList(grupaService.findByStudijskiProgram(idPrograma));
    }

    @GetMapping("/predmet/{idPredmeta}")
    public List<GrupaResponse> getGrupeByPredmet(@PathVariable Long idPredmeta) {
        return Converters.toGrupaResponseList(grupaService.findByPredmet(idPredmeta));
    }

    @DeleteMapping("/{id}")
    public void deleteGrupa(@PathVariable Long id) {
        grupaService.delete(id);
    }
}
