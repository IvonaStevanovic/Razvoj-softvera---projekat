package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.NastavnikRequest;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.services.NastavnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/nastavnik")
public class NastavnikController {

    @Autowired
    private NastavnikService nastavnikService;

    @PostMapping("/add")
    public Long addNewNastavnik(@RequestBody @Valid NastavnikRequest request) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(request.getIme());
        nastavnik.setPrezime(request.getPrezime());
        nastavnik.setSrednjeIme(request.getSrednjeIme());
        nastavnik.setEmail(request.getEmail());
        nastavnik.setBrojTelefona(request.getBrojTelefona());
        nastavnik.setAdresa(request.getAdresa());
        nastavnik.setDatumRodjenja(request.getDatumRodjenja());
        nastavnik.setPol(request.getPol());
        nastavnik.setJmbg(request.getJmbg());

        if (request.getZvanja() != null) {
            Set<NastavnikZvanje> zvanja = request.getZvanja().stream()
                    .map(z -> {
                        NastavnikZvanje nz = new NastavnikZvanje();
                        nz.setZvanje(z);
                        nz.setNastavnik(nastavnik);
                        return nz;
                    })
                    .collect(Collectors.toSet());
            nastavnik.setZvanja(zvanja);
        }

        return nastavnikService.save(nastavnik).getId();
    }

    @GetMapping("/all")
    public java.util.List<NastavnikResponse> getAllNastavnik() {
        return nastavnikService.findAllResponses();
    }

    @GetMapping("/search")
    public java.util.List<NastavnikResponse> searchNastavnik(
            @RequestParam(required = false) String ime,
            @RequestParam(required = false) String prezime) {
        return nastavnikService.findByImeAndPrezime(ime, prezime);
    }
}
