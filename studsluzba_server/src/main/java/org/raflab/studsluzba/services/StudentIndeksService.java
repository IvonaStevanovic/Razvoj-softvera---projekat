package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class StudentIndeksService {

    private final StudentIndeksRepository studentIndeksRepository;
    private final StudentPodaciRepository studentPodaciRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;

    // ---------------- CREATE ----------------
    @Transactional
    public StudentIndeks createStudentIndeks(StudentIndeksRequest request) {
        // Provera duplikata
        boolean exists = studentIndeksRepository
                .findDuplicateIndex(request.getStudentId(), request.getStudProgramOznaka(), request.getGodina())
                .isPresent();

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student already has an index for this program and year."
            );
        }

        // Dohvati student i studijski program
        var student = studentPodaciRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student not found"));

        var studProgram = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka())
                .orElse(null); // može biti null ako program ne postoji

        // Generiši sledeći broj indeksa
        int broj = findBroj(request.getGodina(), request.getStudProgramOznaka());

        // Kreiraj indeks
        StudentIndeks indeks = new StudentIndeks();
        indeks.setGodina(request.getGodina());
        indeks.setStudProgramOznaka(request.getStudProgramOznaka());
        indeks.setNacinFinansiranja(request.getNacinFinansiranja());
        indeks.setAktivan(request.isAktivan());
        indeks.setVaziOd(request.getVaziOd() != null ? request.getVaziOd() : LocalDate.now());
        indeks.setBroj(broj);
        indeks.setStudent(student);
        indeks.setStudijskiProgram(studProgram);

        return studentIndeksRepository.save(indeks);
    }

    // ---------------- UPDATE ----------------
    @Transactional
    public StudentIndeks updateStudentIndeks(Long id, StudentIndeksRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student index not found"));

        // Provera duplikata (osim samog indeksa koji update-ujemo)
        Optional<StudentIndeks> duplicate = studentIndeksRepository
                .findDuplicateIndex(request.getStudentId(), request.getStudProgramOznaka(), request.getGodina());

        if (duplicate.isPresent() && !duplicate.get().getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student already has an index for this program and year."
            );
        }

        // Update polja
        indeks.setGodina(request.getGodina());
        indeks.setStudProgramOznaka(request.getStudProgramOznaka());
        indeks.setNacinFinansiranja(request.getNacinFinansiranja());
        indeks.setAktivan(request.isAktivan());
        indeks.setVaziOd(request.getVaziOd() != null ? request.getVaziOd() : LocalDate.now());

        return studentIndeksRepository.save(indeks);
    }

    // ---------------- READ ----------------
    @Transactional(readOnly = true)
    public List<StudentIndeks> findAll() {
        List<StudentIndeks> lista = studentIndeksRepository.findAll();
        // inicijalizacija lazy polja ako je potrebno
        lista.forEach(si -> {
            if (si.getStudent() != null) si.getStudent().getIndeksi().size();
        });
        return lista;
    }

    @Transactional(readOnly = true)
    public StudentIndeks findById(Long id) {
        return studentIndeksRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student index not found"));
    }

    @Transactional(readOnly = true)
    public int findBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository
                .findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);
        return findNextAvailableNumber(brojeviList);
    }

    // ---------------- HELPERS ----------------
    private int findNextAvailableNumber(List<Integer> brojeviList) {
        if (brojeviList == null || brojeviList.isEmpty()) return 1;

        List<Integer> sorted = brojeviList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        int expected = 1;
        for (int num : sorted) {
            if (num != expected) return expected;
            expected++;
        }
        return expected;
    }

    @Transactional(readOnly = true)
    public StudentIndeks findAktivanStudentIndeks(Long studentPodaciId, int godina, String studProgramOznaka) {
        return studentIndeksRepository.findAktivanStudentIndeks(studentPodaciId, godina, studProgramOznaka);
    }

    @Transactional(readOnly = true)
    public Optional<StudentIndeks> findDuplicateIndex(Long studentId, String program, Integer godina) {
        return studentIndeksRepository.findDuplicateIndex(studentId, program, godina);
    }
}