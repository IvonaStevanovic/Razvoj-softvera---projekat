package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/drzi-predmet")
@RequiredArgsConstructor
public class DrziPredmetController {
    private final DrziPredmetService drziPredmetService;

    @PostMapping("/add")
    public String addDrziPredmet(@RequestBody DrziPredmetRequest request) {
        drziPredmetService.saveDrziPredmet(request);
        return "Uspešno sačuvano DrziPredmet!";
    }

    @GetMapping("/all")
    public List<DrziPredmetResponse> getAllDrziPredmet() {
        return drziPredmetService.getAll();
    }

    @GetMapping("/nastavnik/{nastavnikId}")
    public List<DrziPredmetResponse> getByNastavnik(@PathVariable Long nastavnikId) {
        return drziPredmetService.getByNastavnikId(nastavnikId);
    }

    @GetMapping("/predmet/{predmetId}")
    public List<DrziPredmetResponse> getByPredmet(@PathVariable Long predmetId) {
        return drziPredmetService.getByPredmetId(predmetId);
    }
}
