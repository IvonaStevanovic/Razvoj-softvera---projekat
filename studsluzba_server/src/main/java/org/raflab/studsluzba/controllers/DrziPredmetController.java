package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drzi-predmet")
@RequiredArgsConstructor
public class DrziPredmetController {
/*
    private final DrziPredmetService drziPredmetService;

    @PostMapping("/add")
    public ResponseEntity<?> addDrziPredmet(@RequestBody DrziPredmetRequest request) {
        return drziPredmetService.saveDrziPredmet(request);
    }


    // Dohvatanje svih veza
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(drziPredmetService.getAll());
    }

    // Dohvatanje veza po id nastavnika
    @GetMapping("/nastavnik/{nastavnikId}")
    public ResponseEntity<?> getByNastavnik(@PathVariable Long nastavnikId) {
        return ResponseEntity.ok(drziPredmetService.getByNastavnikId(nastavnikId));
    }

    // Dohvatanje veza po id predmeta
    @GetMapping("/predmet/{predmetId}")
    public ResponseEntity<?> getByPredmet(@PathVariable Long predmetId) {
        return ResponseEntity.ok(drziPredmetService.getByPredmetId(predmetId));
    }

 */
}
