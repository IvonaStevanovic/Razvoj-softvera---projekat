package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/ispitni-rok")
public class IspitniRokController {
/*
    @Autowired
    private IspitniRokService service;

    @PostMapping("/add")
    public ResponseEntity<IspitniRokResponse> add(@Valid @RequestBody IspitniRokRequest request) {
        IspitniRok r = service.save(Converters.toIspitniRok(request));
        return ResponseEntity.ok(Converters.toIspitniRokResponse(r));
    }

    // Dohvatanje svih ispitnih rokova
    @GetMapping("/all")
    public ResponseEntity<List<IspitniRokResponse>> getAllRokovi() {
        List<IspitniRokResponse> responseList = Converters.toIspitniRokResponseList(service.findAll());
        return ResponseEntity.ok(responseList);
    }

    // Dohvatanje po ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getRokById(@PathVariable Long id) {
        Optional<IspitniRok> rok = service.findById(id);
        if (rok.isPresent()) {
            IspitniRokResponse response = Converters.toIspitniRokResponse(rok.get());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Ispitni rok sa id " + id + " ne postoji.");
        }
    }

    // Dohvatanje rokova po školskoj godini
    @GetMapping("/skolska-godina/{id}")
    public ResponseEntity<List<IspitniRokResponse>> getRokoviPoGodini(@PathVariable Long id) {
        List<IspitniRokResponse> responseList = Converters.toIspitniRokResponseList(service.findBySkolskaGodina(id));
        return ResponseEntity.ok(responseList);
    }

    // Dohvatanje aktivnih rokova (po trenutnom datumu)
    @GetMapping("/aktivni")
    public ResponseEntity<List<IspitniRokResponse>> getAktivniRokovi() {
        List<IspitniRokResponse> responseList = Converters.toIspitniRokResponseList(service.findAktivniRokovi(LocalDate.now()));
        return ResponseEntity.ok(responseList);
    }

    // Brisanje roka
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRok(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.ok("Ispitni rok obrisan.");
        } catch (IllegalStateException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Došlo je do greške prilikom brisanja roka.");
        }
    }

 */
}

