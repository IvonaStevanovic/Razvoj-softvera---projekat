import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.model.Grupa;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.GrupaRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.services.GrupaService;
import org.raflab.studsluzba.controllers.request.GrupaRequest;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrupaServiceTest {
/*
    @Mock
    private GrupaRepository grupaRepository;

    @Mock
    private StudijskiProgramRepository studijskiProgramRepository;

    @Mock
    private PredmetRepository predmetRepository;

    @InjectMocks
    private GrupaService service;

    @Captor
    private ArgumentCaptor<Grupa> grupaCaptor;

    // Helper metode
    private StudijskiProgram program(Long id, String naziv) {
        StudijskiProgram p = new StudijskiProgram();
        p.setId(id);
        p.setNaziv(naziv);
        return p;
    }

    private Predmet predmet(Long id, String naziv) {
        Predmet p = new Predmet();
        p.setId(id);
        p.setNaziv(naziv);
        return p;
    }

    private GrupaRequest request(Long programId, List<Long> predmetiIds) {
        GrupaRequest r = new GrupaRequest(1L, List.of(2L, 3L));
        r.setStudijskiProgramId(programId);
        r.setPredmetiId(predmetiIds);
        return r;
    }

    @Nested
    class SaveTests {

        @Test
        @DisplayName("Saves a new Grupa when valid")
        void savesValidGrupa() {
            StudijskiProgram p = program(1L, "IT");
            List<Predmet> predmeti = List.of(predmet(1L, "Algoritmi"), predmet(2L, "Matematika"));

            GrupaRequest req = request(1L, List.of(1L, 2L));

            when(studijskiProgramRepository.findById(1L)).thenReturn(Optional.of(p));
            when(predmetRepository.findAllById(List.of(1L, 2L))).thenReturn(predmeti);
            when(grupaRepository.findByStudijskiProgram(1L)).thenReturn(Collections.emptyList());
            when(grupaRepository.save(any(Grupa.class))).thenAnswer(i -> {
                Grupa g = i.getArgument(0);
                g.setId(100L);
                return g;
            });

            Grupa saved = service.save(req);

            assertNotNull(saved);
            assertEquals(100L, saved.getId());
            assertEquals(p, saved.getStudijskiProgram());
            assertEquals(2, saved.getPredmeti().size());
            verify(grupaRepository).save(grupaCaptor.capture());
            List<Long> savedPredmetiIds = grupaCaptor.getValue().getPredmeti().stream().map(Predmet::getId).collect(Collectors.toList());
            assertTrue(savedPredmetiIds.containsAll(List.of(1L, 2L)));
        }

        @Test
        @DisplayName("Throws exception if StudijskiProgram does not exist")
        void throwsIfProgramMissing() {
            GrupaRequest req = request(1L, List.of(1L, 2L));
            when(studijskiProgramRepository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.save(req));
            assertEquals("Studijski program ne postoji", ex.getMessage());
            verify(grupaRepository, never()).save(any());
        }

        @Test
        @DisplayName("Throws exception if Grupa with same predmeti already exists")
        void throwsIfGrupaExists() {
            StudijskiProgram p = program(1L, "IT");
            Predmet pred1 = predmet(1L, "Algoritmi");
            Predmet pred2 = predmet(2L, "Matematika");
            List<Predmet> predmeti = List.of(pred1, pred2);

            Grupa existingGrupa = new Grupa();
            existingGrupa.setId(50L);
            existingGrupa.setStudijskiProgram(p);
            existingGrupa.setPredmeti(predmeti);

            GrupaRequest req = request(1L, List.of(1L, 2L));

            when(studijskiProgramRepository.findById(1L)).thenReturn(Optional.of(p));
            when(predmetRepository.findAllById(List.of(1L, 2L))).thenReturn(predmeti);
            when(grupaRepository.findByStudijskiProgram(1L)).thenReturn(List.of(existingGrupa));

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.save(req));
            assertEquals("Grupa sa ovim predmetima već postoji u ovom studijskom programu", ex.getMessage());
            verify(grupaRepository, never()).save(any());
        }
    }

    @Nested
    class FindAllTests {

        @Test
        @DisplayName("Returns all Grupa with initialized predmeti")
        void returnsAll() {
            StudijskiProgram p = program(1L, "IT");
            Predmet pred1 = predmet(1L, "Algoritmi");
            Predmet pred2 = predmet(2L, "Matematika");

            Grupa g1 = new Grupa();
            g1.setId(1L);
            g1.setStudijskiProgram(p);
            g1.setPredmeti(List.of(pred1));

            Grupa g2 = new Grupa();
            g2.setId(2L);
            g2.setStudijskiProgram(p);
            g2.setPredmeti(List.of(pred2));

            when(grupaRepository.findAll()).thenReturn(List.of(g1, g2));

            List<Grupa> all = service.findAll();

            assertEquals(2, all.size());
            assertEquals(1L, all.get(0).getId());
            assertEquals(2L, all.get(1).getId());
            verify(grupaRepository).findAll();
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        @DisplayName("Returns Grupa if found")
        void returnsIfFound() {
            Grupa g = new Grupa();
            g.setId(1L);
            when(grupaRepository.findById(1L)).thenReturn(Optional.of(g));

            Optional<Grupa> result = service.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(1L, result.get().getId());
            verify(grupaRepository).findById(1L);
        }

        @Test
        @DisplayName("Returns empty if not found")
        void returnsEmptyIfNotFound() {
            when(grupaRepository.findById(1L)).thenReturn(Optional.empty());

            Optional<Grupa> result = service.findById(1L);

            assertTrue(result.isEmpty());
            verify(grupaRepository).findById(1L);
        }
    }

    @Nested
    class FindByStudijskiProgramTests {

        @Test
        @DisplayName("Returns all Grupa for a given StudijskiProgram")
        void returnsByStudijskiProgram() {
            StudijskiProgram p = program(1L, "IT");
            Grupa g1 = new Grupa(); g1.setId(1L); g1.setStudijskiProgram(p);
            Grupa g2 = new Grupa(); g2.setId(2L); g2.setStudijskiProgram(p);

            when(grupaRepository.findByStudijskiProgram(1L)).thenReturn(List.of(g1, g2));

            List<Grupa> result = service.findByStudijskiProgram(1L);

            assertEquals(2, result.size());
            verify(grupaRepository).findByStudijskiProgram(1L);
        }
    }

    @Nested
    class FindByPredmetTests {

        @Test
        @DisplayName("Returns all Grupa for a given Predmet")
        void returnsByPredmet() {
            Predmet pred = predmet(1L, "Algoritmi");
            Grupa g1 = new Grupa(); g1.setId(1L); g1.setPredmeti(List.of(pred));
            Grupa g2 = new Grupa(); g2.setId(2L); g2.setPredmeti(List.of(pred));

            when(grupaRepository.findByPredmet(1L)).thenReturn(List.of(g1, g2));

            List<Grupa> result = service.findByPredmet(1L);

            assertEquals(2, result.size());
            verify(grupaRepository).findByPredmet(1L);
        }
    }

    @Nested
    class DeleteTests {

        @Test
        @DisplayName("Deletes Grupa by ID")
        void deletesById() {
            service.delete(1L);
            verify(grupaRepository).deleteById(1L);
        }
    }*/
}
