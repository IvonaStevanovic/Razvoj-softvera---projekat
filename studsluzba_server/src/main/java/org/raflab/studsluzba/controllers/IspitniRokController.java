package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.services.IspitniRokService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/ispitni-rok")
public class IspitniRokController {
    @Autowired
    private IspitniRokService service;

    @PostMapping("/add")
    public Long addIspitniRok(@RequestBody @Valid IspitniRokRequest request) {
        IspitniRok rok = service.save(Converters.toIspitniRok(request));
        return rok.getId();
    }

    @GetMapping("/all")
    public List<IspitniRokResponse> getAllRokovi() {
        return Converters.toIspitniRokResponseList(service.findAll());
    }

    @GetMapping("/{id}")
    public IspitniRokResponse getRokById(@PathVariable Long id) {
        Optional<IspitniRok> rok = service.findById(id);
        return rok.map(Converters::toIspitniRokResponse).orElse(null);
    }

    @GetMapping("/skolska-godina/{id}")
    public List<IspitniRokResponse> getRokoviPoGodini(@PathVariable Long id) {
        return Converters.toIspitniRokResponseList(service.findBySkolskaGodina(id));
    }

    @GetMapping("/aktivni")
    public List<IspitniRokResponse> getAktivniRokovi() {
        return Converters.toIspitniRokResponseList(service.findAktivniRokovi(LocalDate.now()));
    }

    @DeleteMapping("/{id}")
    public void deleteRok(@PathVariable Long id) {
        service.deleteById(id);
    }
}
