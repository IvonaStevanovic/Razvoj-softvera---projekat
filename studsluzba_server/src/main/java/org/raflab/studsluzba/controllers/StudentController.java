package org.raflab.studsluzba.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentIndeksResponse;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.services.StudentProfileService;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.raflab.studsluzba.utils.ParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
public class StudentController {

    private final StudentPodaciRepository studentPodaciRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final StudentProfileService studentProfileService;
    private final StudentIndeksService studentIndeksService;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final EntityMappers entityMappers;

    @PostMapping("/add")
    public Long addNewStudentPodaci(@RequestBody StudentPodaciRequest studentPodaciRequest) {
        StudentPodaci sp = studentPodaciRepository.save(Converters.toStudentPodaci(studentPodaciRequest));
        return sp.getId();
    }

    @GetMapping("/all")
    public List<StudentPodaciResponse> getAllStudentPodaci() {
        return studentPodaciRepository.findAll()
                .stream()
                .map(entityMappers::fromStudentPodaciToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/svi")
    public Page<StudentPodaciResponse> getAllStudentPodaciPaginated(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        return studentPodaciRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()))
                .map(entityMappers::fromStudentPodaciToResponse);
    }

    @GetMapping("/podaci/{id}")
    public StudentPodaciResponse getStudentPodaci(@PathVariable Long id) {
        return studentPodaciRepository.findById(id)
                .map(entityMappers::fromStudentPodaciToResponse)
                .orElse(null);
    }

    @PostMapping("/saveindeks")
    public Long saveIndeks(@RequestBody StudentIndeksRequest request) {

        StudentIndeks studentIndeks = Converters.toStudentIndeks(request);

        // postavljanje broja indeksa
        int nextBroj = studentIndeksService.findBroj(request.getGodina(), request.getStudProgramOznaka());
        studentIndeks.setBroj(nextBroj);

        // postavljanje studenta
        StudentPodaci studentPodaci = studentPodaciRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        studentIndeks.setStudent(studentPodaci);

        // postavljanje studijskog programa
        StudijskiProgram studijskiProgram = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka())
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Studijski program not found"));
        studentIndeks.setStudijskiProgram(studijskiProgram);

        try {
            StudentIndeks saved = studentIndeksRepository.save(studentIndeks);
            return saved.getId();
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Duplicate entry for broj, godina, studProgramOznaka", e);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving StudentIndeks", e);
        }
    }

    @GetMapping("/indeks/{id}")
    public StudentIndeksResponse getStudentIndeks(@PathVariable Long id) {
        return studentIndeksRepository.findById(id)
                .map(entityMappers::fromStudentIndexToResponse)
                .orElse(null);
    }

    @GetMapping("/indeksi/{idStudentPodaci}")
    public List<StudentIndeksResponse> getIndeksiForStudentPodaciId(@PathVariable Long idStudentPodaci) {
        return studentIndeksRepository.findStudentIndeksiForStudentPodaciId(idStudentPodaci)
                .stream()
                .map(entityMappers::fromStudentIndexToResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/fastsearch")
    public StudentIndeksResponse fastSearch(@RequestParam String indeksShort) {
        String[] parsed = ParseUtils.parseIndeks(indeksShort);
        if (parsed == null) return null;

        StudentIndeks si = studentIndeksRepository.findStudentIndeks(
                parsed[0], 2000 + Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
        return entityMappers.fromStudentIndexToResponse(si);
    }

    @GetMapping("/emailsearch")
    public StudentIndeksResponse emailSearch(@RequestParam String studEmail) {
        String[] parsed = ParseUtils.parseEmail(studEmail);
        if (parsed == null) return null;

        StudentIndeks si = studentIndeksRepository.findStudentIndeks(
                parsed[0], 2000 + Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
        return si != null ? entityMappers.fromStudentIndexToResponse(si) : null;
    }

    @GetMapping("/search")
    public Page<StudentDTO> search(
            @RequestParam(required = false) String ime,
            @RequestParam(required = false) String prezime,
            @RequestParam(required = false) String studProgram,
            @RequestParam(required = false) Integer godina,
            @RequestParam(required = false) Integer broj,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        if (studProgram == null && godina == null && broj == null) {
            // pretraga samo po studentu bez indeksa
            return studentPodaciRepository.findStudent(ime, prezime, PageRequest.of(page, size, Sort.by("id").descending()))
                    .map(EntityMappers::fromStudentPodaciToDTO);
        } else {
            return studentIndeksRepository.findStudentIndeks(
                            ime, prezime, studProgram, godina, broj, PageRequest.of(page, size, Sort.by("id").descending()))
                    .map(EntityMappers::fromStudentIndeksToDTO);
        }
    }

    @GetMapping("/profile/{studentIndeksId}")
    public StudentProfileDTO getStudentProfile(@PathVariable Long studentIndeksId) {
        return studentProfileService.getStudentProfile(studentIndeksId);
    }

    @GetMapping("/webprofile/{studentIndeksId}")
    public StudentWebProfileDTO getStudentWebProfile(@PathVariable Long studentIndeksId) {
        return studentProfileService.getStudentWebProfile(studentIndeksId);
    }

    @GetMapping("/webprofile/email")
    public StudentWebProfileDTO getStudentWebProfileForEmail(@RequestParam String studEmail) {
        String[] parsed = ParseUtils.parseEmail(studEmail);
        if (parsed == null) return null;

        StudentIndeks si = studentIndeksRepository.findStudentIndeks(
                parsed[0], 2000 + Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]));
        return si != null ? studentProfileService.getStudentWebProfile(si.getId()) : null;
    }
}

