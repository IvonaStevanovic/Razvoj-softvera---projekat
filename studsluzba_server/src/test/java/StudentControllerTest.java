import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.StudentController;
import org.raflab.studsluzba.controllers.request.StudentIndeksRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.response.StudentPodaciResponse;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.StudentIndeksService;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock StudentPodaciRepository studentPodaciRepository;
    @Mock StudentIndeksRepository studentIndeksRepository;
    @Mock StudijskiProgramRepository studijskiProgramRepository;
    @Mock StudentIndeksService studentIndeksService;
    @Mock EntityMappers entityMappers;

    @InjectMocks StudentController controller;

    @Captor ArgumentCaptor<StudentPodaci> studentPodaciCaptor;
    @Captor ArgumentCaptor<StudentIndeks> studentIndeksCaptor;

    // Pomoćne metode za kreiranje entiteta i DTO
    private StudentPodaci studentPodaci(Long id, String email) {
        StudentPodaci sp = new StudentPodaci();
        sp.setId(id);
        sp.setIme("Marko");
        sp.setPrezime("Markovic");
        sp.setSrednjeIme("Petar");
        sp.setDatumRodjenja(LocalDate.of(2000, 1, 1));
        sp.setPol('M');
        sp.setEmailFakultet(email);
        sp.setAdresa("Ulica 1");
        sp.setMestoPrebivalista("Beograd");
        sp.setDrzavljanstvo("RS");
        return sp;
    }

    private StudentPodaciRequest request(String email) {
        StudentPodaciRequest r = new StudentPodaciRequest();
        r.setIme("Marko");
        r.setPrezime("Markovic");
        r.setSrednjeIme("Petar");
        r.setDatumRodjenja(LocalDate.of(2000, 1, 1));
        r.setPol('M');
        r.setEmailFakultet(email);
        r.setAdresa("Ulica 1");
        r.setMestoPrebivalista("Beograd");
        r.setDrzavljanstvo("RS");
        return r;
    }

    @Nested
    class AddNewStudent {

        @Test
        @DisplayName("Adds new student successfully when email and JMBG are unique")
        void addNewStudent_success() {
            StudentPodaciRequest req = request("marko@raf.rs");

            when(studentPodaciRepository.findAll())
                    .thenReturn(Collections.<StudentPodaci>emptyList());
            when(studentPodaciRepository.save(any(StudentPodaci.class)))
                    .thenAnswer(invocation -> {
                        StudentPodaci sp = invocation.getArgument(0);
                        sp.setId(1L);
                        return sp;
                    });

            var response = controller.addNewStudentPodaci(req);

            verify(studentPodaciRepository).save(studentPodaciCaptor.capture());
            StudentPodaci saved = studentPodaciCaptor.getValue();

            assertEquals("Marko", saved.getIme());
            assertEquals("marko@raf.rs", saved.getEmailFakultet());
            assertEquals(1L, response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Returns BAD_REQUEST if student email already exists")
        void addNewStudent_emailExists() {
            StudentPodaciRequest req = request("exists@raf.rs");
            when(studentPodaciRepository.findAll())
                    .thenReturn(Collections.<StudentPodaci>singletonList(studentPodaci(1L, "exists@raf.rs")));

            var response = controller.addNewStudentPodaci(req);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("već postoji"));
            verify(studentPodaciRepository, never()).save(any());
        }

        @Test
        @DisplayName("Returns BAD_REQUEST if JMBG already exists")
        void addNewStudent_jmbgExists() {
            StudentPodaciRequest req = request("new@raf.rs");
            req.setJmbg("1234567890123");

            StudentPodaci existing = studentPodaci(1L, "other@raf.rs");
            existing.setJmbg("1234567890123");

            when(studentPodaciRepository.findAll())
                    .thenReturn(Collections.<StudentPodaci>singletonList(existing));

            var response = controller.addNewStudentPodaci(req);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("JMBG"));
            verify(studentPodaciRepository, never()).save(any());
        }
    }

    @Nested
    class GetAllStudentPodaci {

        @Test
        @DisplayName("Returns all student podaci mapped to response DTOs")
        void getAllStudents_success() {
            StudentPodaci sp = studentPodaci(1L, "marko@raf.rs");
            StudentPodaciResponse resp = new StudentPodaciResponse();
            resp.setId(1L);

            when(studentPodaciRepository.findAll())
                    .thenReturn(Collections.<StudentPodaci>singletonList(sp));
            when(entityMappers.fromStudentPodaciToResponse(sp)).thenReturn(resp);

            var result = controller.getAllStudentPodaci();

            assertEquals(1, result.size());
            assertEquals(1L, result.get(0).getId());
        }
    }

    @Nested
    class SaveIndeks {

        @Test
        @DisplayName("Saves new indeks if no existing active indeks")
        void saveIndeks_success() {
            StudentPodaci student = studentPodaci(1L, "marko@raf.rs");

            // Lokalna lista za studijski program
            StudijskiProgram program = new StudijskiProgram();
            //program.setOznaka("INF");
            List<StudijskiProgram> programList = new ArrayList<>();
            programList.add(program);

            when(studentPodaciRepository.findById(1L)).thenReturn(Optional.of(student));
            //when(studijskiProgramRepository.findByOznaka("INF")).thenReturn(programList);
            when(studentIndeksService.findAktivanStudentIndeks(1L, 2023, "INF")).thenReturn(null);
            when(studentIndeksService.findBroj(2023, "INF")).thenReturn(1);
            when(studentIndeksRepository.save(any(StudentIndeks.class)))
                    .thenAnswer(i -> {
                        StudentIndeks si = i.getArgument(0);
                        si.setId(100L);
                        return si;
                    });

            StudentIndeksRequest req = new StudentIndeksRequest();
            req.setStudentId(1L);
            //req.setStudProgramOznaka("INF");
            req.setGodina(2023);

            var response = controller.saveIndeks(req);

            verify(studentIndeksRepository).save(studentIndeksCaptor.capture());
            StudentIndeks saved = studentIndeksCaptor.getValue();

            assertEquals(student, saved.getStudent());
            assertEquals(program, saved.getStudijskiProgram());
            assertEquals(1, saved.getBroj());
            assertEquals(100L, response.getBody());
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Returns CONFLICT if active indeks already exists")
        void saveIndeks_conflict() {
            StudentIndeks existing = new StudentIndeks();
            existing.setId(50L);

            when(studentPodaciRepository.findById(1L)).thenReturn(Optional.of(studentPodaci(1L, "marko@raf.rs")));
           // when(studijskiProgramRepository.findByOznaka("INF"))
                 //   .thenReturn(Collections.<StudijskiProgram>singletonList(new StudijskiProgram()));
            when(studentIndeksService.findAktivanStudentIndeks(1L, 2023, "INF")).thenReturn(existing);

            StudentIndeksRequest req = new StudentIndeksRequest();
            req.setStudentId(1L);
            req.setStudProgramOznaka("INF");
            req.setGodina(2023);

            var response = controller.saveIndeks(req);

            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals(50L, response.getBody());
            verify(studentIndeksRepository, never()).save(any());
        }
    }
}
