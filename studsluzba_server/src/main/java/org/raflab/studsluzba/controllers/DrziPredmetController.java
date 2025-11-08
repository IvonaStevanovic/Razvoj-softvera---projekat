package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.web.bind.annotation.*;

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
}
