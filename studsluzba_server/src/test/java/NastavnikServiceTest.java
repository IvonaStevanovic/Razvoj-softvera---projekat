import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.services.NastavnikService;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NastavnikServiceTest {

    @Mock
    NastavnikRepository nastavnikRepository;

    @InjectMocks
    NastavnikService service;

    @Captor
    ArgumentCaptor<Nastavnik> nastavnikCaptor;

    // Helper metoda za kreiranje nastavnika
    private Nastavnik nastavnik(Long id, String ime, String prezime, String email) {
        Nastavnik n = new Nastavnik();
        n.setId(id);
        n.setIme(ime);
        n.setPrezime(prezime);
        n.setEmail(email);
        n.setDatumRodjenja(LocalDate.of(1980, 1, 1));
        n.setPol('M');
        return n;
    }

    @Nested
    class SaveNastavnik {

        @Test
        @DisplayName("Saves new Nastavnik if email does not exist")
        void savesNewNastavnik() {
            Nastavnik n = nastavnik(null, "Marko", "Markovic", "marko@raf.rs");

            when(nastavnikRepository.existsByEmail("marko@raf.rs")).thenReturn(false);
            when(nastavnikRepository.save(any(Nastavnik.class))).thenAnswer(i -> {
                Nastavnik arg = i.getArgument(0);
                arg.setId(1L); // simulacija ID-a nakon cuvanja
                return arg;
            });

            Optional<Nastavnik> saved = service.save(n);

            assertTrue(saved.isPresent());
            assertEquals(1L, saved.get().getId());
            assertEquals("Marko", saved.get().getIme());
            verify(nastavnikRepository).existsByEmail("marko@raf.rs");
            verify(nastavnikRepository).save(nastavnikCaptor.capture());
            assertEquals("marko@raf.rs", nastavnikCaptor.getValue().getEmail());
        }

        @Test
        @DisplayName("Returns empty if Nastavnik with email already exists")
        void returnsEmptyIfEmailExists() {
            Nastavnik n = nastavnik(null, "Jana", "Jankovic", "jana@raf.rs");

            when(nastavnikRepository.existsByEmail("jana@raf.rs")).thenReturn(true);

            Optional<Nastavnik> saved = service.save(n);

            assertTrue(saved.isEmpty());
            verify(nastavnikRepository).existsByEmail("jana@raf.rs");
            verify(nastavnikRepository, never()).save(any());
        }
    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("Returns Nastavnik if found")
        void returnsNastavnik() {
            Nastavnik n = nastavnik(1L, "Petar", "Petrovic", "petar@raf.rs");
            when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(n));

            Optional<Nastavnik> result = service.findById(1L);

            assertTrue(result.isPresent());
            assertEquals("Petar", result.get().getIme());
            verify(nastavnikRepository).findById(1L);
        }

        @Test
        @DisplayName("Returns empty if Nastavnik not found")
        void returnsEmpty() {
            when(nastavnikRepository.findById(2L)).thenReturn(Optional.empty());

            Optional<Nastavnik> result = service.findById(2L);

            assertTrue(result.isEmpty());
            verify(nastavnikRepository).findById(2L);
        }
    }

    @Nested
    class FindAllResponsesTest {

        @Test
        @DisplayName("Returns all NastavnikResponses")
        void returnsAllResponses() {
            Nastavnik n1 = nastavnik(1L, "Ana", "Anic", "ana@raf.rs");
            Nastavnik n2 = nastavnik(2L, "Bojan", "Bojic", "bojan@raf.rs");

            when(nastavnikRepository.findAllWithZvanja()).thenReturn(List.of(n1, n2));

            List<NastavnikResponse> responses = service.findAllResponses();

            assertEquals(2, responses.size());
            assertEquals("Ana", responses.get(0).getIme());
            assertEquals("Bojan", responses.get(1).getIme());
            verify(nastavnikRepository).findAllWithZvanja();
        }
    }

    @Nested
    class FindByImeAndPrezimeTest {

        @Test
        @DisplayName("Returns filtered NastavnikResponses")
        void returnsFilteredResponses() {
            Nastavnik n = nastavnik(1L, "Milan", "Milic", "milan@raf.rs");

            when(nastavnikRepository.findByImeAndPrezime("Milan", "Milic"))
                    .thenReturn(List.of(n));

            List<NastavnikResponse> responses = service.findByImeAndPrezime("Milan", "Milic");

            assertEquals(1, responses.size());
            assertEquals("Milan", responses.get(0).getIme());
            assertEquals("Milic", responses.get(0).getPrezime());
            verify(nastavnikRepository).findByImeAndPrezime("Milan", "Milic");
        }

        @Test
        @DisplayName("Returns empty list if no match")
        void returnsEmptyIfNoMatch() {
            when(nastavnikRepository.findByImeAndPrezime("Noname", "Noprezime"))
                    .thenReturn(Collections.emptyList());

            List<NastavnikResponse> responses = service.findByImeAndPrezime("Noname", "Noprezime");

            assertTrue(responses.isEmpty());
            verify(nastavnikRepository).findByImeAndPrezime("Noname", "Noprezime");
        }
    }
}
