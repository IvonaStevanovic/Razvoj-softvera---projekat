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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
/*
    @GetMapping(path = "/all")
    public List<IspitResponse> getAll() {
        List<Ispit> ispiti = ispitService.findAll();
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<IspitResponse> getById(@PathVariable Long id) {
        Ispit ispit = ispitService.findById(id).orElse(null);
        if (ispit == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Converters.toIspitResponse(ispit));
    }



    @GetMapping(path = "/predmet/{predmetId}/rok/{rokId}")
    public List<IspitResponse> getByPredmetAndRok(@PathVariable Long predmetId, @PathVariable Long rokId) {
        List<Ispit> ispiti = ispitService.findByPredmetAndRok(predmetId, rokId);
        List<IspitResponse> responses = new ArrayList<>();
        for (Ispit ispit : ispiti) {
            responses.add(Converters.toIspitResponse(ispit));
        }
        return responses;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<IspitResponse> add(@RequestBody @Valid IspitRequest request) {
        Predmet predmet = predmetService.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet not found"));

        IspitniRok rok = ispitniRokService.findById(request.getIspitniRokId())
                .orElseThrow(() -> new RuntimeException("IspitniRok not found"));

        DrziPredmet drziPredmet = drziPredmetService.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new RuntimeException("DrziPredmet not found"));

        Ispit ispit = new Ispit();
        ispit.setPredmet(predmet);
        ispit.setIspitniRok(rok);
        ispit.setDrziPredmet(drziPredmet);
        ispit.setDatumOdrzavanja(request.getDatumOdrzavanja());
        ispit.setVremePocetka(LocalTime.from(request.getVremePocetka()));
        ispit.setNapomena(request.getNapomena());
        ispit.setZakljucen(false);

        Ispit saved = ispitService.save(ispit);

        return ResponseEntity.ok(Converters.toIspitResponse(saved));
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<IspitResponse> update(@PathVariable Long id, @RequestBody @Valid IspitRequest request) {
        if (!ispitService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Predmet predmet = predmetService.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet not found"));

        IspitniRok rok = ispitniRokService.findById(request.getIspitniRokId())
                .orElseThrow(() -> new RuntimeException("IspitniRok not found"));

        DrziPredmet drziPredmet = drziPredmetService.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new RuntimeException("DrziPredmet not found"));

        Ispit ispit = new Ispit();
        ispit.setId(id);
        ispit.setPredmet(predmet);
        ispit.setIspitniRok(rok);
        ispit.setDrziPredmet(drziPredmet);
        ispit.setDatumOdrzavanja(request.getDatumOdrzavanja());
        ispit.setVremePocetka(LocalTime.from(request.getVremePocetka()));
        ispit.setNapomena(request.getNapomena());

        Ispit updated = ispitService.save(ispit);

        return ResponseEntity.ok(Converters.toIspitResponse(updated));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ispitService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ispitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
*/

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
