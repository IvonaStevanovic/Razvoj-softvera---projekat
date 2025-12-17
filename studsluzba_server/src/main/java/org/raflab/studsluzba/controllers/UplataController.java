package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.services.UplataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/uplate")
@RequiredArgsConstructor
public class UplataController {
    /*
    private final UplataService uplataService;

    @PostMapping("/add")
    public String addUplata(@RequestBody UplataRequest request) {
        uplataService.createUplata(request);
        return "Uspešno sačuvana uplata!";
    }

    @GetMapping("/all")
    public List<UplataResponse> getAllUplate() {
        return uplataService.getAll();
    }

    @GetMapping("/{id}")
    public UplataResponse getUplata(@PathVariable Long id) {
        return uplataService.getById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUplata(@PathVariable Long id) {
        uplataService.delete(id);
        return "Uplata obrisana!";
    }

     */
}
