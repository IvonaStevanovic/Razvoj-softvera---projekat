package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.response.IzlazakNaIspitResponse;
import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.services.IzlazakNaIspitService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/izlazak-na-ispit")
@RequiredArgsConstructor
public class IzlazakNaIspitController {

    private final IzlazakNaIspitService service;
    private final IzlazakNaIspitRepository repository;

    @PostMapping("/add")
    public IzlazakNaIspitResponse addNew(@RequestBody @Valid IzlazakNaIspitRequest request) {
        IzlazakNaIspit izlazak = service.save(request);
        return Converters.toIzlazakNaIspitResponse(izlazak);
    }

    @GetMapping("/all")
    public List<IzlazakNaIspitResponse> getAll() {
        List<IzlazakNaIspit> izlazi = repository.findAll();
        return izlazi.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/student/{id}")
    public List<IzlazakNaIspitResponse> getByStudent(@PathVariable Long id) {
        List<IzlazakNaIspit> izlazi = repository.findByStudent(id);
        return izlazi.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/ispit/{id}")
    public List<IzlazakNaIspitResponse> getByIspit(@PathVariable Long id) {
        List<IzlazakNaIspit> izlazi = repository.findByIspit(id);
        return izlazi.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/predmet/{id}")
    public List<IzlazakNaIspitResponse> getByPredmet(@PathVariable Long id) {
        List<IzlazakNaIspit> izlazi = repository.findByPredmet(id);
        return izlazi.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/student/{id}/active")
    public List<IzlazakNaIspitResponse> getActiveByStudent(@PathVariable Long id) {
        List<IzlazakNaIspit> izlazi = repository.findActiveByStudent(id);
        return izlazi.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/not-yet-appeared")
    public List<IzlazakNaIspitResponse> getNotYetAppeared() {
        List<IzlazakNaIspit> izlazi = repository.findNotYetAppeared();
        return izlazi.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());
    }
}