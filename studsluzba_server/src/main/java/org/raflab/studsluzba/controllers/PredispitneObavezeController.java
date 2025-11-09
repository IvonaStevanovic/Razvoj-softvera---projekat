package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredispitneObavezeRequest;
import org.raflab.studsluzba.controllers.response.PredispitneObavezeResponse;
import org.raflab.studsluzba.model.PredispitneObaveze;
import org.raflab.studsluzba.services.PredispitneObavezeService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predispitne-obaveze")
@AllArgsConstructor
public class PredispitneObavezeController {

    private final PredispitneObavezeService service;

    @PostMapping
    public PredispitneObavezeResponse create(@RequestBody PredispitneObavezeRequest request) {
        PredispitneObaveze obaveza = Converters.toPredispitneObaveze(request);
        return Converters.toPredispitneObavezeResponse(service.create(obaveza));
    }

    @PutMapping("/{id}")
    public PredispitneObavezeResponse update(@PathVariable Long id, @RequestBody PredispitneObavezeRequest request) {
        PredispitneObaveze obavezaDetails = Converters.toPredispitneObaveze(request);
        return Converters.toPredispitneObavezeResponse(service.update(id, obavezaDetails));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}")
    public PredispitneObavezeResponse findById(@PathVariable Long id) {
        return service.findById(id)
                .map(Converters::toPredispitneObavezeResponse)
                .orElseThrow(() -> new RuntimeException("Predispitna obaveza not found"));
    }

    @GetMapping("/all")
    public List<PredispitneObavezeResponse> findAll() {
        return Converters.toPredispitneObavezeResponseList(service.findAll());
    }

    @GetMapping("/drzi-predmet/{idDrziPredmeta}")
    public List<PredispitneObavezeResponse> findByDrziPredmet(@PathVariable Long idDrziPredmeta) {
        return Converters.toPredispitneObavezeResponseList(service.findByDrziPredmet(idDrziPredmeta));
    }

    @GetMapping("/skolska-godina/{idGodine}")
    public List<PredispitneObavezeResponse> findBySkolskaGodina(@PathVariable Long idGodine) {
        return Converters.toPredispitneObavezeResponseList(service.findBySkolskaGodina(idGodine));
    }

    @GetMapping("/vrsta/{vrsta}")
    public List<PredispitneObavezeResponse> findByVrsta(@PathVariable String vrsta) {
        return Converters.toPredispitneObavezeResponseList(service.findByVrsta(vrsta));
    }
}
