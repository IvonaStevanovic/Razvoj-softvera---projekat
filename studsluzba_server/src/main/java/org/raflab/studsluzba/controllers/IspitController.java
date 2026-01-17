package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.io.InputStream;
import java.time.LocalTime;
import java.util.*;
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

    @PostMapping
    public void save(@RequestBody IspitRequest request) {
        ispitService.saveNewIspit(request);
    }
    @GetMapping("/search/rok")
    public List<IspitResponse> searchByRok(@RequestParam Long ispitniRokId) {
        // Koristimo tvoju postojeću metodu iz IspitService da dohvatimo sve
        // i filtriramo po roku koji je klijent selektovao u ComboBox-u
        return ispitService.findAllResponses().stream()
                .filter(i -> i.getIspitniRokId() != null && i.getIspitniRokId().equals(ispitniRokId))
                .collect(Collectors.toList());
    }
  /*  @PostMapping("/add")
    public ResponseEntity<IspitResponse> createIspit(@RequestBody @Valid IspitRequest request) {

        IspitResponse response = ispitService.createAndMap(request); // vraća IspitResponse

        return ResponseEntity.ok(response);
    }
*/
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

    @PostMapping("/prijavi")
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
    public ResponseEntity<List<PredispitniPoeniResponse>> getPredispitniPoeni(
            @PathVariable Long studentIndeksId,
            @RequestParam(required = false) Long predmetId,
            @RequestParam(required = false) Long skolskaGodinaId) {
        return ResponseEntity.ok(ispitService.getPredispitniPoeni(studentIndeksId, predmetId, skolskaGodinaId));
    }

    @GetMapping("/broj-polaganja")
    public ResponseEntity<Long> getBrojPolaganja(@RequestParam Long studentIndeksId, @RequestParam Long predmetId) {
        return ResponseEntity.ok(ispitService.getBrojPolaganja(studentIndeksId, predmetId));
    }

    @GetMapping("/{ispitId}/rezultati")
    public ResponseEntity<List<StudentIspitRezultatiResponse>> getRezultatiIspita(@PathVariable Long ispitId) {
        return ResponseEntity.ok(ispitService.getRezultatiIspita(ispitId));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> obrisiIspit(@PathVariable Long id) {
        ispitService.obrisiIspit(id);
        return ResponseEntity.ok("Ispit sa ID-jem " + id + " i svi povezani podaci su uspešno obrisani.");
    }
    @GetMapping("/izvestaj/zapisnik/{ispitId}")
    public ResponseEntity<byte[]> stampajZapisnik(@PathVariable Long ispitId) throws Exception {
        // 1. Dohvatanje podataka o studentima (već imaš ovu metodu)
        List<StudentIspitRezultatiResponse> rezultati = ispitService.getRezultatiIspita(ispitId);

        // 2. Dohvatanje podataka o ispitu za zaglavlje
        IspitResponse ispit = ispitService.getIspitResponseById(ispitId);

        // 3. Podešavanje parametara za Jasper
        Map<String, Object> params = new HashMap<>();
        params.put("predmetNaziv", ispit.getPredmetNaziv());
        params.put("ispitniRok", ispit.getIspitniRokNaziv());

        // 4. Kompajliranje i popunjavanje
        InputStream reportStream = getClass().getResourceAsStream("/reports/zapisnik_ispita.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(rezultati);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "attachment; filename=zapisnik.pdf")
                .body(pdfBytes);
    }
}
