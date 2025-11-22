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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
        // Provera duplikata pre insert-a
        Optional<StudentIndeks> existing = repository.findDuplicateIndex(
                request.getStudentId(),
                request.getStudProgramOznaka(),
                request.getGodina()
        );

        if (existing.isPresent()) {
            return null; // ili možeš vratiti neki response sa porukom
        }

        try {
            StudentIndeks indeks = new StudentIndeks();
            indeks.setGodina(request.getGodina());
            indeks.setStudProgramOznaka(request.getStudProgramOznaka());
            indeks.setNacinFinansiranja(request.getNacinFinansiranja());
            indeks.setAktivan(request.isAktivan());
            indeks.setVaziOd(request.getVaziOd());
            // indeks.setStudent(...) // obavezno postaviti StudentPodaci ako je potrebno

            StudentIndeks saved = repository.save(indeks);
            return entityMappers.fromStudentIndexToResponse(saved);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return null; // neće baciti 500, možeš i ovde vratiti DTO sa porukom
        }
    }

    @GetMapping("/{id}")
    public StudentIndeksResponse getById(@PathVariable Long id) {
        StudentIndeks indeks = repository.findById(id).orElse(null);
        return indeks != null ? entityMappers.fromStudentIndexToResponse(indeks) : null;
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

   /* @PutMapping("/update/{id}")
    public StudentIndeksResponse update(@PathVariable Long id, @RequestBody StudentIndeksRequest request) {
        StudentIndeks indeks = repository.findById(id).orElse(null);
        if (indeks == null) {
            return null; // ili DTO sa porukom "not found"
        }

        // Provera duplikata: postoji li drugi indeks istog studenta, godine i programa
        Optional<StudentIndeks> duplicate = repository.findDuplicateIndex(
                request.getStudentId(),
                request.getStudProgramOznaka(),
                request.getGodina()
        );

        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            return null; // ili DTO sa porukom "duplicate exists"
        }

        try {
            indeks.setGodina(request.getGodina());
            indeks.setStudProgramOznaka(request.getStudProgramOznaka());
            indeks.setNacinFinansiranja(request.getNacinFinansiranja());
            indeks.setAktivan(request.isAktivan());
            indeks.setVaziOd(request.getVaziOd());
            // indeks.setStudent(...) // ako treba postaviti student

            StudentIndeks updated = repository.save(indeks);
            return entityMappers.fromStudentIndexToResponse(updated);

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return null; // neće baciti 500, možeš i ovde vratiti DTO sa porukom
        }
    } */

   /* @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }*/
}