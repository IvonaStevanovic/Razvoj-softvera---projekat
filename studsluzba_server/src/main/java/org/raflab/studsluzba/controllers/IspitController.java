package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.services.IspitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/ispit")
@RequiredArgsConstructor
public class IspitController {
    /*
    private final IspitService ispitService;

    // Endpoint za kreiranje ispita
    @PostMapping
    public ResponseEntity<Ispit> createIspit(@RequestBody Ispit ispit) {
        Ispit saved = ispitService.save(ispit);
        return ResponseEntity.ok(saved);
    }

    // Endpoint za dobijanje svih ispita
    @GetMapping
    public ResponseEntity<List<Ispit>> getAllIspiti() {
        return ResponseEntity.ok(ispitService.findAll());
    }

    // Endpoint za dobijanje ispita po ID-ju
    @GetMapping("/{id}")
    public ResponseEntity<Ispit> getIspitById(@PathVariable Long id) {
        Optional<Ispit> ispit = ispitService.findById(id);
        return ispit.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint za dobijanje prijavljenih studenata na određeni ispit
    @GetMapping("/{ispitId}/studenti")
    public ResponseEntity<List<PrijavaIspitaResponse>> getPrijavljeniStudenti(
            @PathVariable Long ispitId) {

        List<PrijavaIspitaResponse> studenti = ispitService.getPrijavljeniStudenti(ispitId);
        return ResponseEntity.ok(studenti);
    }

    // Endpointi za filtriranje po predmetu, nastavniku ili ispitnom roku
    @GetMapping("/filter")
    public ResponseEntity<List<Ispit>> filterIspiti(
            @RequestParam(required = false) Long predmetId,
            @RequestParam(required = false) Long nastavnikId,
            @RequestParam(required = false) Long ispitniRokId) {

        List<Ispit> ispiti = ispitService.findByPredmetNastavnikRok(predmetId, nastavnikId, ispitniRokId);
        return ResponseEntity.ok(ispiti);
    }

     */
}
