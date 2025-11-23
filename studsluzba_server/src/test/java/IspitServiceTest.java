import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.services.IspitService;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IspitServiceTest {

    @Mock
    private IspitRepository ispitRepository;

    @Mock
    private PredmetRepository predmetRepository;

    @Mock
    private NastavnikRepository nastavnikRepository;

    @Mock
    private IspitniRokRepository ispitniRokRepository;

    @InjectMocks
    private IspitService service;

    @Captor
    private ArgumentCaptor<Ispit> ispitCaptor;

    private Predmet predmet(long id, String naziv) {
        Predmet p = new Predmet();
        p.setId(id);
        p.setNaziv(naziv);
        return p;
    }

    private Nastavnik nastavnik(long id, String ime) {
        Nastavnik n = new Nastavnik();
        n.setId(id);
        n.setIme(ime);
        return n;
    }

    private IspitniRok rok(long id) {
        IspitniRok r = new IspitniRok();
        r.setId(id);
        return r;
    }

    private Ispit makeIspit(long predmetId, long nastavnikId, long rokId) {
        Ispit i = new Ispit();
        i.setDatumOdrzavanja(LocalDate.of(2025, 11, 23));
        i.setVremePocetka(LocalTime.of(10, 0));
        i.setPredmet(predmet(predmetId, "Predmet " + predmetId));
        i.setNastavnik(nastavnik(nastavnikId, "Nastavnik " + nastavnikId));
        i.setIspitniRok(rok(rokId));
        i.setZakljucen(false);
        return i;
    }

    @Nested
    class HappyPath {

        @Test
        @DisplayName("Successfully saves a new Ispit")
        void savesValidIspit() {
            Ispit input = makeIspit(1L, 1L, 1L);

            when(predmetRepository.findById(1L)).thenReturn(Optional.of(input.getPredmet()));
            when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(input.getNastavnik()));
            when(ispitniRokRepository.findById(1L)).thenReturn(Optional.of(input.getIspitniRok()));
            when(ispitRepository.existsByDatumOdrzavanjaAndPredmetIdAndNastavnikIdAndIspitniRokId(
                    any(), anyLong(), anyLong(), anyLong())).thenReturn(false);
            when(ispitRepository.save(any(Ispit.class))).thenAnswer(inv -> inv.getArgument(0));

            Ispit saved = service.save(input);

            verify(ispitRepository).save(ispitCaptor.capture());
            Ispit captured = ispitCaptor.getValue();
            assertEquals(input.getPredmet().getId(), captured.getPredmet().getId());
            assertEquals(input.getNastavnik().getId(), captured.getNastavnik().getId());
            assertEquals(input.getIspitniRok().getId(), captured.getIspitniRok().getId());
            assertEquals(input.getDatumOdrzavanja(), captured.getDatumOdrzavanja());
        }
    }

    @Nested
    class HandlesErrors {

        @Test
        @DisplayName("Throws exception if Predmet does not exist")
        void predmetMissing() {
            Ispit input = makeIspit(1L, 1L, 1L);
            when(predmetRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                    () -> service.save(input));
            assertEquals("Predmet ne postoji", ex.getMessage());
        }

        @Test
        @DisplayName("Throws exception if Nastavnik does not exist")
        void nastavnikMissing() {
            Ispit input = makeIspit(1L, 1L, 1L);
            when(predmetRepository.findById(1L)).thenReturn(Optional.of(input.getPredmet()));
            when(nastavnikRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                    () -> service.save(input));
            assertEquals("Nastavnik ne postoji", ex.getMessage());
        }

        @Test
        @DisplayName("Throws exception if IspitniRok does not exist")
        void rokMissing() {
            Ispit input = makeIspit(1L, 1L, 1L);
            when(predmetRepository.findById(1L)).thenReturn(Optional.of(input.getPredmet()));
            when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(input.getNastavnik()));
            when(ispitniRokRepository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                    () -> service.save(input));
            assertEquals("Ispitni rok ne postoji", ex.getMessage());
        }

        @Test
        @DisplayName("Throws exception if Ispit already exists for same date, predmet, nastavnik, rok")
        void duplicateIspit() {
            Ispit input = makeIspit(1L, 1L, 1L);
            when(predmetRepository.findById(1L)).thenReturn(Optional.of(input.getPredmet()));
            when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(input.getNastavnik()));
            when(ispitniRokRepository.findById(1L)).thenReturn(Optional.of(input.getIspitniRok()));
            when(ispitRepository.existsByDatumOdrzavanjaAndPredmetIdAndNastavnikIdAndIspitniRokId(
                    any(), anyLong(), anyLong(), anyLong())).thenReturn(true);

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.save(input));
            assertEquals("Ispit već postoji za zadati datum, predmet i nastavnika u ovom roku!", ex.getMessage());
        }
    }

    @Nested
    class FindMethods {

        @Test
        @DisplayName("Find all ispiti")
        void findAll() {
            Ispit i1 = makeIspit(1L, 1L, 1L);
            Ispit i2 = makeIspit(2L, 2L, 2L);
            when(ispitRepository.findAll()).thenReturn(List.of(i1, i2));

            List<Ispit> lista = service.findAll();
            assertEquals(2, lista.size());
        }

        @Test
        @DisplayName("Find by id")
        void findById() {
            Ispit i1 = makeIspit(1L, 1L, 1L);
            when(ispitRepository.findById(1L)).thenReturn(Optional.of(i1));

            Optional<Ispit> found = service.findById(1L);
            assertTrue(found.isPresent());
            assertEquals(i1.getPredmet().getId(), found.get().getPredmet().getId());
        }
    }
}
