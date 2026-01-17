package org.raflab.studsluzba.controllers;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.controllers.response.ProsekIzvestajResponse;
import org.raflab.studsluzba.model.PolozeniPredmeti;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.services.PredmetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/predmeti")
@CrossOrigin
public class PredmetController {

    @Autowired
    private PredmetService predmetService;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @GetMapping("/all")
    public List<PredmetResponse> getAll() {
        return predmetService.findAll().stream().map(p -> {
            PredmetResponse r = new PredmetResponse();
            r.setId(p.getId());
            r.setNaziv(p.getNaziv());
            return r;
        }).collect(Collectors.toList());
    }
    @GetMapping("/drzi-predmet-id")
    public Long getDrziPredmetId(@RequestParam Long predmetId, @RequestParam Long nastavnikId) {
        return drziPredmetRepository.findIdByPredmetAndNastavnik(predmetId, nastavnikId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veza nije pronađena"));
    }
    @GetMapping("/{id}")
    public ResponseEntity<PredmetResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(predmetService.getPredmetById(id));
    }
    @GetMapping("/izvestaj/predmet-statistika/{predmetId}")
    public ResponseEntity<byte[]> stampajStatistikuPredmeta(
            @PathVariable Long predmetId,
            @RequestParam Integer odG,
            @RequestParam Integer doG) throws Exception {

        // 1. Dobavi pojedinačne stavke iz baze (moraš dodati ovu metodu u repository)
        // Pretpostavljamo da vraća List<PolozeniPredmeti>
        List<PolozeniPredmeti> stavke = predmetRepository.getStavkeZaProsek(predmetId, odG, doG);
        Predmet p = predmetService.findById(predmetId);

        // Izračunaj ukupni prosek za zaglavlje
        Double ukupniProsek = stavke.stream()
                .mapToInt(PolozeniPredmeti::getOcena)
                .average().orElse(0.0);

        // 2. Mapiraj u DTO koji Jasper razume
        List<ProsekIzvestajResponse> podaci = stavke.stream().map(s -> new ProsekIzvestajResponse(
                        p.getNaziv(),
                        odG + " - " + doG,
                ukupniProsek,
                s.getStudentIndeks().getBroj() + "/" + s.getStudentIndeks().getGodina(),
                s.getStudentIndeks().getStudent().getIme() + " " + s.getStudentIndeks().getStudent().getPrezime(),
                s.getOcena()
                )).collect(Collectors.toList());

        // 3. Učitavanje fajla - OVDE SE DEFINIŠE reportStream
        InputStream reportStream = getClass().getResourceAsStream("/reports/prosek_predmeta.jrxml");
        if (reportStream == null) {
            throw new RuntimeException("Fajl prosek_predmeta.jrxml nije pronađen u resources/reports!");
        }

        // 4. Generisanje PDF-a
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(podaci);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=statistika.pdf")
                .body(JasperExportManager.exportReportToPdf(jasperPrint));
    }

    @PostMapping("/add")
    public ResponseEntity<PredmetResponse> add(@RequestBody @Valid PredmetRequest request) {
        return ResponseEntity.ok(predmetService.createPredmet(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            predmetService.deletePredmet(id);
            return ResponseEntity.noContent().build(); // Status 204 ako je uspešno
        } catch (RuntimeException e) {
            // Vraća status 400 i tvoju poruku: "Ne možete obrisati predmet..."
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/program/{programId}")
    public ResponseEntity<List<PredmetResponse>> getByProgram(@PathVariable Long programId) {
        return ResponseEntity.ok(predmetService.getPredmetiByProgram(programId));
    }

    @GetMapping("/{id}/prosek")
    public ResponseEntity<Double> getProsek(@PathVariable Long id,
                                            @RequestParam Integer odGodina,
                                            @RequestParam Integer doGodina) {
        return ResponseEntity.ok(predmetService.getProsek(id, odGodina, doGodina));
    }
    @GetMapping("/statistika-detaljno/{predmetId}")
    public List<ProsekIzvestajResponse> getStatistikaDetaljno(
            @PathVariable Long predmetId,
            @RequestParam Integer odG,
            @RequestParam Integer doG) {

        List<PolozeniPredmeti> stavke = predmetRepository.getStavkeZaProsek(predmetId, odG, doG);
        Predmet p = predmetService.findById(predmetId);

        Double ukupniProsek = stavke.stream()
                .mapToInt(PolozeniPredmeti::getOcena)
                .average().orElse(0.0);

        return stavke.stream().map(s -> new ProsekIzvestajResponse(
                        p.getNaziv(),
                        odG + "-" + doG,
                ukupniProsek,
                s.getStudentIndeks().getBroj() + "/" + s.getStudentIndeks().getGodina(),
                s.getStudentIndeks().getStudent().getIme() + " " + s.getStudentIndeks().getStudent().getPrezime(),
                s.getOcena()
                )).collect(Collectors.toList());
    }
}
