package org.raflab.studsluzba.controllers;

import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.response.IspitResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.services.IspitService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping(path = "/api/ispit")
public class IspitController {
    @Autowired
    private IspitService ispitService;

    /// Dodavanje novog ispita
    @PostMapping(path = "/add")
    public Long addNewIspit(@RequestBody @Valid IspitRequest ispitRequest) {
        Ispit ispit = ispitService.save(Converters.toIspit(ispitRequest));
        return ispit.getId();
    }

    /// Dohvatanje svih ispita
    @GetMapping(path = "/all")
    public List<IspitResponse> getAllIspiti() {
        return Converters.toIspitResponseList(ispitService.findAll());
    }

    ///Dohvatanje ispita po ID
    @GetMapping(path = "/{id}")
    public IspitResponse getIspitById(@PathVariable Long id) {
        Optional<Ispit> result = ispitService.findById(id);
        return result.map(Converters::toIspitResponse).orElse(null);
    }

    ///Pretraga ispita po predmetu, nastavniku ili ispitnom roku
    @GetMapping(path = "/search")
    public List<IspitResponse> searchIspiti(
            @RequestParam(required = false) Long predmetId,
            @RequestParam(required = false) Long nastavnikId,
            @RequestParam(required = false) Long ispitniRokId
    ) {
        return Converters.toIspitResponseList(
                ispitService.findByPredmetNastavnikRok(predmetId, nastavnikId, ispitniRokId)
        );
    }

    ///Brisanje ispita po ID
    @DeleteMapping(path = "/{id}")
    public void deleteIspit(@PathVariable Long id) {
        ispitService.deleteById(id);
    }
}
