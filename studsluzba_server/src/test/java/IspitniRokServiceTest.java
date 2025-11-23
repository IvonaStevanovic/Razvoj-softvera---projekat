import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.services.IspitniRokService;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IspitniRokServiceTest {

    @Mock
    private IspitniRokRepository repository;

    @InjectMocks
    private IspitniRokService service;

    @Captor
    private ArgumentCaptor<IspitniRok> rokCaptor;

    private SkolskaGodina skolskaGodina(Long id, String naziv) {
        SkolskaGodina sg = new SkolskaGodina();
        sg.setId(id);
        sg.setNaziv(naziv);
        return sg;
    }

    private IspitniRok makeRok(Long id, LocalDate pocetak, LocalDate kraj, Long godinaId) {
        IspitniRok r = new IspitniRok();
        r.setId(id);
        r.setDatumPocetka(pocetak);
        r.setDatumZavrsetka(kraj);
        r.setSkolskaGodina(skolskaGodina(godinaId, "Godina " + godinaId));
        return r;
    }

    @Nested
    class SaveRok {

        @Test
        @DisplayName("Successfully saves a new IspitniRok")
        void savesValidRok() {
            IspitniRok input = makeRok(null, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);

            when(repository.findByDatumPocetkaAndSkolskaGodinaId(input.getDatumPocetka(), 1L))
                    .thenReturn(Optional.empty());
            when(repository.save(any(IspitniRok.class))).thenAnswer(inv -> inv.getArgument(0));

            IspitniRok saved = service.save(input);

            verify(repository).save(rokCaptor.capture());
            IspitniRok captured = rokCaptor.getValue();

            assertEquals(input.getDatumPocetka(), captured.getDatumPocetka());
            assertEquals(input.getDatumZavrsetka(), captured.getDatumZavrsetka());
            assertEquals(input.getSkolskaGodina().getId(), captured.getSkolskaGodina().getId());
        }

        @Test
        @DisplayName("Throws exception if duplicate IspitniRok for same start date and year")
        void duplicateRok() {
            IspitniRok input = makeRok(null, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            when(repository.findByDatumPocetkaAndSkolskaGodinaId(input.getDatumPocetka(), 1L))
                    .thenReturn(Optional.of(input));

            IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                    () -> service.save(input));
            assertEquals("Ispitni rok već postoji za datu školsku godinu!", ex.getMessage());
        }
    }

    @Nested
    class FindMethods {

        @Test
        @DisplayName("Find all IspitniRok")
        void findAll() {
            IspitniRok r1 = makeRok(1L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            IspitniRok r2 = makeRok(2L, LocalDate.of(2025, 12, 20), LocalDate.of(2025, 12, 25), 1L);

            when(repository.findAll()).thenReturn(List.of(r1, r2));

            List<IspitniRok> all = service.findAll();
            assertEquals(2, all.size());
        }

        @Test
        @DisplayName("Find by ID")
        void findById() {
            IspitniRok r = makeRok(1L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            when(repository.findById(1L)).thenReturn(Optional.of(r));

            Optional<IspitniRok> found = service.findById(1L);
            assertTrue(found.isPresent());
            assertEquals(r.getDatumPocetka(), found.get().getDatumPocetka());
        }

        @Test
        @DisplayName("Find by SkolskaGodina")
        void findBySkolskaGodina() {
            IspitniRok r = makeRok(1L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            when(repository.findBySkolskaGodina(1L)).thenReturn(List.of(r));

            List<IspitniRok> lista = service.findBySkolskaGodina(1L);
            assertEquals(1, lista.size());
            assertEquals(r.getSkolskaGodina().getId(), lista.get(0).getSkolskaGodina().getId());
        }

        @Test
        @DisplayName("Find active IspitniRoks")
        void findAktivniRokovi() {
            LocalDate today = LocalDate.of(2025, 12, 2);
            IspitniRok r = makeRok(1L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            when(repository.findAktivniRokovi(today)).thenReturn(List.of(r));

            List<IspitniRok> aktivni = service.findAktivniRokovi(today);
            assertEquals(1, aktivni.size());
        }
    }

    @Nested
    class DeleteRok {

        @Test
        @DisplayName("Successfully deletes IspitniRok without child Ispiti")
        void deleteValid() {
            IspitniRok r = makeRok(1L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            r.setIspiti(null);

            when(repository.findById(1L)).thenReturn(Optional.of(r));

            assertDoesNotThrow(() -> service.deleteById(1L));
            verify(repository).delete(r);
        }

        @Test
        @DisplayName("Throws exception when deleting IspitniRok with child Ispiti")
        void deleteWithChild() {
            IspitniRok r = makeRok(1L, LocalDate.of(2025, 12, 1), LocalDate.of(2025, 12, 15), 1L);
            r.setIspiti(Set.of(mock(org.raflab.studsluzba.model.Ispit.class)));

            when(repository.findById(1L)).thenReturn(Optional.of(r));

            IllegalStateException ex = assertThrows(IllegalStateException.class,
                    () -> service.deleteById(1L));
            assertEquals("Ne može se obrisati ispitni rok koji ima ispita.", ex.getMessage());
        }

        @Test
        @DisplayName("Throws exception when IspitniRok does not exist")
        void deleteNonexistent() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                    () -> service.deleteById(1L));
            assertEquals("Ispitni rok sa id 1 ne postoji.", ex.getMessage());
        }
    }
}