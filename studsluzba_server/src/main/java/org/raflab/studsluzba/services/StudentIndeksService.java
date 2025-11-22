package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class StudentIndeksService {

    private final StudentIndeksRepository studentIndeksRepository;

    @Transactional(readOnly = true)
    public List<StudentIndeks> findAll() {
        List<StudentIndeks> lista = studentIndeksRepository.findAll();
        // inicijalizacija lazy polja
        lista.forEach(si -> si.getStudent().getIndeksi().size());
        return lista;
    }

    @Transactional(readOnly = true)
    public void validateNewIndex(Long studentId, String program, Integer godina) {

        boolean exists = studentIndeksRepository
                .findDuplicateIndex(studentId, program, godina)
                .isPresent();

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Student already has an index for this program and year."
            );
        }
    }

    public StudentIndeks findAktivanStudentIndeks(Long studentPodaciId, int godina, String studProgramOznaka) {
        return studentIndeksRepository.findAktivanStudentIndeks(studentPodaciId, godina, studProgramOznaka);
    }

    @Transactional(readOnly = true)
    public int findBroj(int godina, String studProgramOznaka) {
        List<Integer> brojeviList = studentIndeksRepository.
                findBrojeviByGodinaAndStudProgramOznaka(godina, studProgramOznaka);

        return findNextAvailableNumber(brojeviList);
    }

    private int findNextAvailableNumber(List<Integer> brojeviList) {
        if (brojeviList == null || brojeviList.isEmpty()) return 1;

        List<Integer> sorted = brojeviList.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList()); // <= works on JDK 8+

        int expected = 1;
        for (int num : sorted) {
            if (num != expected) return expected;
            expected++;
        }
        return expected;
    }

    public StudentIndeks findByStudentIdAndAktivan(Long studentPodaciId) {
        return studentIndeksRepository.findAktivanStudentIndeksiByStudentPodaciId(studentPodaciId);
    }
}
