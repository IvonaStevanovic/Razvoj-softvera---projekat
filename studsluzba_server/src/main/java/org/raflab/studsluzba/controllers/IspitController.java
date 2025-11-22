package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.services.IspitService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/ispit")
public class IspitController {

    @Autowired
    private IspitService ispitService;

    @Autowired
    private PredmetRepository predmetRepository;

    @Autowired
    private NastavnikRepository nastavnikRepository;

    @Autowired
    private IspitniRokRepository ispitniRokRepository;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid IspitRequest request) {
        try {
            Ispit sacuvan = ispitService.save(
                    Converters.toIspit(request, predmetRepository, nastavnikRepository, ispitniRokRepository)
            );
            return ResponseEntity.ok(Converters.toIspitResponse(sacuvan));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<IspitResponse>> getAllIspiti() {
        List<IspitResponse> lista = Converters.toIspitResponseList(ispitService.findAll());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIspitById(@PathVariable Long id) {
        try {
            Ispit ispit = ispitService.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Ispit sa id " + id + " ne postoji."));
            return ResponseEntity.ok(Converters.toIspitResponse(ispit));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<IspitResponse>> searchIspiti(
            @RequestParam(required = false) Long predmetId,
            @RequestParam(required = false) Long nastavnikId,
            @RequestParam(required = false) Long ispitniRokId
    ) {
        List<Ispit> ispiti = ispitService.findByPredmetNastavnikRok(predmetId, nastavnikId, ispitniRokId);
        return ResponseEntity.ok(Converters.toIspitResponseList(ispiti));
    }
/*
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIspit(@PathVariable Long id) {
        try {
            ispitService.deleteById(id);
            return ResponseEntity.ok("Ispit uspešno obrisan.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

 */
}
