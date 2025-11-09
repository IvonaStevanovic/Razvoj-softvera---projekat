package org.raflab.studsluzba.controllers;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/student-indeks")
@AllArgsConstructor
public class StudentIndeksController {

    private final StudentIndeksService service;
    private final StudentIndeksRepository repository;
    private final EntityMappers entityMappers;

    @PostMapping("/add")
    public StudentIndeksResponse add(@RequestBody StudentIndeksRequest request) {
        StudentIndeks indeks = new StudentIndeks();
        indeks.setGodina(request.getGodina());
        indeks.setStudProgramOznaka(request.getStudProgramOznaka());
        indeks.setNacinFinansiranja(request.getNacinFinansiranja());
        indeks.setAktivan(request.isAktivan());
        indeks.setVaziOd(request.getVaziOd());
        // student se postavlja preko servisa ili repository-ja ako je već poznat
        StudentIndeks saved = repository.save(indeks);
        return entityMappers.fromStudentIndexToResponse(saved);
    }

    @GetMapping("/{id}")
    public StudentIndeksResponse getById(@PathVariable Long id) {
        Optional<StudentIndeks> indeks = repository.findById(id);
        return indeks.map(entityMappers::fromStudentIndexToResponse).orElse(null);
    }

    @GetMapping("/all")
    public List<StudentIndeksResponse> getAll() {
        return repository.findAll()
                .stream()
                .map(entityMappers::fromStudentIndexToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/page")
    public Page<StudentIndeksResponse> getAllPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return repository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(entityMappers::fromStudentIndexToResponse);
    }

    @PutMapping("/update/{id}")
    public StudentIndeksResponse update(@PathVariable Long id, @RequestBody StudentIndeksRequest request) {
        Optional<StudentIndeks> optional = repository.findById(id);
        if (optional.isEmpty()) return null;

        StudentIndeks indeks = optional.get();
        indeks.setGodina(request.getGodina());
        indeks.setStudProgramOznaka(request.getStudProgramOznaka());
        indeks.setNacinFinansiranja(request.getNacinFinansiranja());
        indeks.setAktivan(request.isAktivan());
        indeks.setVaziOd(request.getVaziOd());

        StudentIndeks updated = repository.save(indeks);
        return entityMappers.fromStudentIndexToResponse(updated);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
