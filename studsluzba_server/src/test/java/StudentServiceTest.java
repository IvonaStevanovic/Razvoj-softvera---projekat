import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.StudentPodaciRepository;
import org.raflab.studsluzba.services.StudentProfileService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentIndeksRepository studentIndeksRepo;

    @Mock
    private StudentPodaciRepository studentPodaciRepo;

    @Mock
    private SlusaPredmetRepository slusaPredmetRepo;

    @Mock
    private PredmetRepository predmetRepo;

    @InjectMocks
    private StudentProfileService studentProfileService;

    @Test
    void getStudentProfile_success() {
        // Priprema
        Long indeksId = 1L;
        StudentIndeks indeks = new StudentIndeks();
        indeks.setId(indeksId);
        StudentPodaci student = new StudentPodaci();
        student.setId(10L);
        indeks.setStudent(student);

        List<SlusaPredmet> slusaPredmeti = new ArrayList<>();
        SlusaPredmet sp1 = new SlusaPredmet();
        sp1.setId(100L);
        slusaPredmeti.add(sp1);

        // Mock
        when(studentIndeksRepo.findById(indeksId)).thenReturn(Optional.of(indeks));
        when(slusaPredmetRepo.getSlusaPredmetForIndeksAktivnaGodina(indeksId)).thenReturn(slusaPredmeti);

        // Poziv metode
        StudentProfileDTO profile = studentProfileService.getStudentProfile(indeksId);

        // Provere
        assertNotNull(profile);
        assertEquals(indeks, profile.getIndeks());
        assertEquals(1, profile.getSlusaPredmete().size());
        assertEquals(sp1, profile.getSlusaPredmete().get(0));
    }

    @Test
    void getStudentWebProfile_success() {
        Long indeksId = 1L;
        StudentIndeks indeks = new StudentIndeks();
        indeks.setId(indeksId);
        StudentPodaci student = new StudentPodaci();
        student.setId(10L);
        indeks.setStudent(student);

        StudentIndeks aktivanIndeks = new StudentIndeks();
        aktivanIndeks.setId(2L);

        List<SlusaPredmet> slusaPredmeti = new ArrayList<>();
        SlusaPredmet sp1 = new SlusaPredmet();
        sp1.setId(101L);
        slusaPredmeti.add(sp1);

        when(studentIndeksRepo.findById(indeksId)).thenReturn(Optional.of(indeks));
        when(studentPodaciRepo.getAktivanIndeks(student.getId())).thenReturn(aktivanIndeks);
        when(slusaPredmetRepo.getSlusaPredmetForIndeksAktivnaGodina(indeksId)).thenReturn(slusaPredmeti);

        StudentWebProfileDTO webProfile = studentProfileService.getStudentWebProfile(indeksId);

        assertNotNull(webProfile);
        assertEquals(aktivanIndeks, webProfile.getAktivanIndeks());
        assertEquals(1, webProfile.getSlusaPredmete().size());
        assertEquals(sp1, webProfile.getSlusaPredmete().get(0));
    }

    @Test
    void getStudentProfile_notFound_throwsException() {
        Long indeksId = 999L;
        when(studentIndeksRepo.findById(indeksId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            studentProfileService.getStudentProfile(indeksId);
        });
    }

}
