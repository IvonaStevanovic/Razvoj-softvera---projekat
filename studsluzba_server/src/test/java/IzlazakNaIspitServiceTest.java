import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.services.IzlazakNaIspitService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IzlazakNaIspitServiceTest {

    @Mock private IzlazakNaIspitRepository repository;
    @Mock private StudentIndeksRepository studentRepo;
    @Mock private IspitRepository ispitRepo;
    @Mock private SlusaPredmetRepository slusaPredmetRepo;
    @Mock private PredispitniPoeniRepository predispitniRepo;
    @Mock private PolozeniPredmetiRepository polozeniRepo;

    @InjectMocks private IzlazakNaIspitService service;

    private StudentIndeks student;
    private Ispit ispit;
    private SlusaPredmet slusaPredmet;

    @BeforeEach
    void setup() {
        student = new StudentIndeks();
        student.setId(1L);
        student.setBroj(10);
        student.setGodina(2);
        student.setStudijskiProgram(new StudijskiProgram());

        ispit = new Ispit();
        ispit.setId(1L);

        slusaPredmet = new SlusaPredmet();
        slusaPredmet.setId(1L);
        DrziPredmet dp = new DrziPredmet();
        dp.setPredmet(new Predmet());
        slusaPredmet.setDrziPredmet(dp);
        slusaPredmet.setSkolskaGodina(new SkolskaGodina());
    }

    @Test
    void saveIzlazak_success() {
        IzlazakNaIspitRequest req = new IzlazakNaIspitRequest();
        req.setStudentIndeksId(1L);
        req.setIspitId(1L);
        req.setSlusaPredmet(1L);
        req.setOstvarenoNaIspitu(60);

        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(ispitRepo.findById(1L)).thenReturn(Optional.of(ispit));
        when(slusaPredmetRepo.findById(1L)).thenReturn(Optional.of(slusaPredmet));
        when(repository.existsByStudentIndeksAndIspitAndSlusaPredmet(student, ispit, slusaPredmet))
                .thenReturn(false);

        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setId(100L);
        izlazak.setStudentIndeks(student);
        izlazak.setIspit(ispit);
        izlazak.setSlusaPredmet(slusaPredmet);

        when(repository.save(any(IzlazakNaIspit.class))).thenReturn(izlazak);
        when(repository.findById(100L)).thenReturn(Optional.of(izlazak)); // <- ovo fali
        when(predispitniRepo.findByStudentAndSlusaPredmet(1L, 1L)).thenReturn(List.of());

        IzlazakNaIspit saved = service.save(req);

        assertEquals(100L, saved.getId());
        verify(repository).save(any(IzlazakNaIspit.class));
    }

    @Test
    void saveIzlazak_duplicate_throws() {
        IzlazakNaIspitRequest req = new IzlazakNaIspitRequest();
        req.setStudentIndeksId(1L);
        req.setIspitId(1L);
        req.setSlusaPredmet(1L);

        when(studentRepo.findById(1L)).thenReturn(Optional.of(student));
        when(ispitRepo.findById(1L)).thenReturn(Optional.of(ispit));
        when(slusaPredmetRepo.findById(1L)).thenReturn(Optional.of(slusaPredmet));
        when(repository.existsByStudentIndeksAndIspitAndSlusaPredmet(student, ispit, slusaPredmet))
                .thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.save(req));
        assertEquals("Izlazak na ispit za ovog studenta, ispit i predmet već postoji!", ex.getMessage());
    }

    @Test
    void processIzlazak_createsPolozeniPredmet() {
        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setId(1L);
        izlazak.setStudentIndeks(student);
        izlazak.setSlusaPredmet(slusaPredmet);
        izlazak.setOstvarenoNaIspitu(60);

        when(repository.findById(1L)).thenReturn(Optional.of(izlazak));
        when(predispitniRepo.findByStudentAndSlusaPredmet(1L,1L)).thenReturn(List.of());
        when(polozeniRepo.save(any(PolozeniPredmeti.class))).thenAnswer(inv -> inv.getArgument(0));

        PolozeniPredmeti polozeni = service.processIzlazakNaIspit(1L);

        assertNotNull(polozeni);
        assertEquals(student, polozeni.getStudentIndeks());
        assertEquals(slusaPredmet.getDrziPredmet().getPredmet(), polozeni.getPredmet());
        assertTrue(polozeni.isPriznat());
    }

    @Test
    void getProsecnaOcenaZaIspit() {
        IzlazakNaIspit z1 = new IzlazakNaIspit(); z1.setOstvarenoNaIspitu(50);
        IzlazakNaIspit z2 = new IzlazakNaIspit(); z2.setOstvarenoNaIspitu(70);
        when(repository.findByIspit(1L)).thenReturn(List.of(z1, z2));

        Double avg = service.getProsecnaOcenaZaIspit(1L);
        assertEquals(60.0, avg);
    }

    @Test
    void brojIzlazakaNaPredmet_returnsCorrect() {
        IzlazakNaIspit z1 = new IzlazakNaIspit(); z1.setSlusaPredmet(slusaPredmet);
        IzlazakNaIspit z2 = new IzlazakNaIspit(); z2.setSlusaPredmet(slusaPredmet);
        when(repository.findByStudent(1L)).thenReturn(List.of(z1, z2));

        long count = service.brojIzlazakaNaPredmet(1L, 1L);
        assertEquals(2, count);
    }
}
