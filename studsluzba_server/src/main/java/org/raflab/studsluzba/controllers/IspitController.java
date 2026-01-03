package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.raflab.studsluzba.services.IspitService;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.services.PredmetService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ispit")
@RequiredArgsConstructor
public class IspitController {
    @Autowired
    private IspitService ispitService;

    @Autowired
    private PredmetService predmetService;

    @Autowired
    private IspitniRokService ispitniRokService;

    @Autowired
    private DrziPredmetService drziPredmetService;

    @PostMapping("/add")
    public ResponseEntity<IspitResponse> createIspit(@RequestBody @Valid IspitRequest request) {

        IspitResponse response = ispitService.createAndMap(request); // vraća IspitResponse

        return ResponseEntity.ok(response);
    }
    // Pretraga ispita po nazivu predmeta ili roku
    @GetMapping("/search")
    public ResponseEntity<List<IspitResponse>> searchIspiti(
            @RequestParam(required = false) String predmet,
            @RequestParam(required = false) String rok) {
        return ResponseEntity.ok(ispitService.searchIspiti(predmet, rok));
    }

    // FindById već imaš, ali evo čisto da potvrdimo putanju
    @GetMapping("/{id}")
    public ResponseEntity<IspitResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ispitService.getIspitResponseById(id));
    }
    @GetMapping("/all")
    public ResponseEntity<List<IspitResponse>> getAllIspiti() {
        return ResponseEntity.ok(ispitService.findAllResponses());
    }

    @PostMapping("/prijava")
    public ResponseEntity<PrijavaIspitaResponse> prijaviIspit(@RequestBody PrijavaIspitaRequest request) {
        return ResponseEntity.ok(ispitService.prijaviIspit(request));
    }

    @PostMapping("/izlazak")
    public ResponseEntity<IzlazakNaIspitResponse> evidentirajIzlazak(@RequestBody IzlazakNaIspitRequest request) {
        return ResponseEntity.ok(ispitService.evidentirajIzlazak(request));
    }

    @GetMapping("/izlasci/student/{studentIndeksId}")
    public ResponseEntity<List<IzlazakNaIspitResponse>> getIzlasciZaStudenta(@PathVariable Long studentIndeksId) {
        return ResponseEntity.ok(ispitService.getIzlasciZaStudenta(studentIndeksId));
    }

    @GetMapping("/prijavljeni/{ispitId}")
    public ResponseEntity<List<StudentPodaciResponse>> getPrijavljeniZaIspit(@PathVariable Long ispitId) {
        return ResponseEntity.ok(ispitService.getPrijavljeniZaIspit(ispitId));
    }

    @GetMapping("/prosek/{ispitId}")
    public ResponseEntity<Double> getProsekNaIspitu(@PathVariable Long ispitId) {
        return ResponseEntity.ok(ispitService.getProsekNaIspitu(ispitId));
    }

    @PostMapping("/predispitni-poeni")
    public ResponseEntity<PredispitniPoeniResponse> dodajPredispitnePoene(@RequestBody DodajPredispitnePoeneRequest request) {
        PredispitniPoeni pp = ispitService.dodajPredispitnePoene(
                request.getStudentIndeksId(),
                request.getPredispitnaObavezaId(),
                request.getPoeni()
        );
        PredispitniPoeniResponse resp = new PredispitniPoeniResponse();
        resp.setId(pp.getId());
        resp.setPoeni(pp.getPoeni());
        resp.setStudentIndeksId(pp.getStudentIndeks().getId());
        resp.setPredispitnaObavezaId(pp.getPredispitnaObaveza().getId());
        resp.setSkolskaGodinaId(pp.getSkolskaGodina() != null ? pp.getSkolskaGodina().getId() : null);
        resp.setPredmetId(pp.getPredispitnaObaveza().getDrziPredmet().getPredmet().getId());
        resp.setPredmetNaziv(pp.getPredispitnaObaveza().getDrziPredmet().getPredmet().getNaziv());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/predispitni-poeni/student/{studentIndeksId}")
    public ResponseEntity<List<PredispitniPoeniResponse>> getPredispitniPoeni(@PathVariable Long studentIndeksId) {
        return ResponseEntity.ok(ispitService.getPredispitniPoeni(studentIndeksId));
    }

    @GetMapping("/broj-polaganja")
    public ResponseEntity<Long> getBrojPolaganja(@RequestParam Long studentIndeksId, @RequestParam Long predmetId) {
        return ResponseEntity.ok(ispitService.getBrojPolaganja(studentIndeksId, predmetId));
    }

    @GetMapping("/rezultati/{ispitId}")
    public ResponseEntity<List<StudentIspitRezultatiResponse>> getRezultatiIspita(@PathVariable Long ispitId) {
        return ResponseEntity.ok(ispitService.getRezultatiIspita(ispitId));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> obrisiIspit(@PathVariable Long id) {
        ispitService.obrisiIspit(id);
        return ResponseEntity.ok("Ispit sa ID-jem " + id + " i svi povezani podaci su uspešno obrisani.");
    }
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
     /* @GetMapping(path = "/predmet/{predmetId}/rok/{rokId}")
    public List<IspitResponse> getByPredmetAndRok(@PathVariable Long predmetId, @PathVariable Long rokId) {
        List<Ispit> ispiti = ispitService.findByPredmetAndRok(predmetId, rokId);
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }
    */

}
