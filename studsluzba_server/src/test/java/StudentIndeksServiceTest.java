import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentIndeksServiceTest {

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private StudentPodaciRepository studentPodaciRepository;

    @Mock
    private StudijskiProgramRepository studijskiProgramRepository;

    @InjectMocks
    private StudentIndeksService studentIndeksService;

    @Test
    void createStudentIndeks_success() {
        StudentIndeksRequest request = new StudentIndeksRequest();
        request.setGodina(1);
        request.setStudProgramOznaka("INF");
        request.setNacinFinansiranja("budzet");
        request.setAktivan(true);
        request.setStudentId(10L);
        request.setVaziOd(LocalDate.of(2025, 1, 1));

        StudentPodaci student = new StudentPodaci();
        student.setId(10L);

        StudijskiProgram program = new StudijskiProgram();
        program.setOznaka("INF");

        when(studentIndeksRepository.findDuplicateIndex(10L, "INF", 1))
                .thenReturn(Optional.empty());
        when(studentPodaciRepository.findById(10L)).thenReturn(Optional.of(student));
        when(studijskiProgramRepository.findByOznaka("INF")).thenReturn(Optional.of(program));
        when(studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(1, "INF"))
                .thenReturn(Collections.emptyList());

        StudentIndeks savedIndeks = new StudentIndeks();
        savedIndeks.setId(100L);
        when(studentIndeksRepository.save(any(StudentIndeks.class))).thenReturn(savedIndeks);

        StudentIndeks result = studentIndeksService.createStudentIndeks(request);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        verify(studentIndeksRepository, times(1)).save(any(StudentIndeks.class));
    }

    @Test
    void createStudentIndeks_duplicate_throwsException() {
        StudentIndeksRequest request = new StudentIndeksRequest();
        request.setGodina(1);
        request.setStudProgramOznaka("INF");
        request.setStudentId(10L);

        when(studentIndeksRepository.findDuplicateIndex(10L, "INF", 1))
                .thenReturn(Optional.of(new StudentIndeks()));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> {
            studentIndeksService.createStudentIndeks(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
    }

    @Test
    void updateStudentIndeks_success() {
        Long id = 1L;
        StudentIndeks existing = new StudentIndeks();
        existing.setId(id);

        StudentIndeksRequest request = new StudentIndeksRequest();
        request.setStudentId(10L); // OBAVEZNO, da se poklapa sa stubom
        request.setStudProgramOznaka("INF");
        request.setGodina(2);

        when(studentIndeksRepository.findById(id)).thenReturn(Optional.of(existing));
        when(studentIndeksRepository.findDuplicateIndex(10L, "INF", 2))
                .thenReturn(Optional.empty());
        when(studentIndeksRepository.save(existing)).thenReturn(existing);

        StudentIndeks updated = studentIndeksService.updateStudentIndeks(id, request);
        assertEquals(2, updated.getGodina());
        assertTrue(updated.isAktivan());
        verify(studentIndeksRepository, times(1)).save(existing);
    }

    @Test
    void findById_success() {
        StudentIndeks indeks = new StudentIndeks();
        indeks.setId(1L);

        when(studentIndeksRepository.findById(1L)).thenReturn(Optional.of(indeks));
        StudentIndeks result = studentIndeksService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_notFound_throwsException() {
        when(studentIndeksRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> studentIndeksService.findById(99L));
    }

    @Test
    void findBroj_returnsNextAvailable() {
        when(studentIndeksRepository.findBrojeviByGodinaAndStudProgramOznaka(1, "INF"))
                .thenReturn(Arrays.asList(1, 2, 3));
        int next = studentIndeksService.findBroj(1, "INF");
        assertEquals(4, next);
    }

    @Test
    void findAktivanStudentIndeks_success() {
        StudentIndeks indeks = new StudentIndeks();
        when(studentIndeksRepository.findAktivanStudentIndeks(10L, 1, "INF"))
                .thenReturn(indeks);
        StudentIndeks result = studentIndeksService.findAktivanStudentIndeks(10L, 1, "INF");
        assertEquals(indeks, result);
    }

    @Test
    void findDuplicateIndex_success() {
        StudentIndeks indeks = new StudentIndeks();
        when(studentIndeksRepository.findDuplicateIndex(10L, "INF", 1))
                .thenReturn(Optional.of(indeks));
        Optional<StudentIndeks> result = studentIndeksService.findDuplicateIndex(10L, "INF", 1);
        assertTrue(result.isPresent());
        assertEquals(indeks, result.get());
    }
}
