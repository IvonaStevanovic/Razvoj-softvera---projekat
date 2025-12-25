import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.services.NastavnikZvanjeService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NastavnikZvanjeServiceTest {
/*
    @Mock
    private NastavnikZvanjeRepository repository;

    @Mock
    private NastavnikRepository nastavnikRepository;

    @InjectMocks
    private NastavnikZvanjeService service;

    @Captor
    private ArgumentCaptor<NastavnikZvanje> zvanjeCaptor;

    // Helper metode
    private Nastavnik nastavnik(Long id, String ime, String prezime) {
        Nastavnik n = new Nastavnik();
        n.setId(id);
        n.setIme(ime);
        n.setPrezime(prezime);
        return n;
    }

    private NastavnikZvanjeRequest request(Long nastavnikId, String zvanje) {
        NastavnikZvanjeRequest r = new NastavnikZvanjeRequest();
        r.setNastavnikId(nastavnikId);
        r.setDatumIzbora(LocalDate.of(2020, 1, 1));
        r.setZvanje(zvanje);
        r.setAktivno(true);
        return r;
    }

    @Nested
    class SaveTests {

        @Test
        @DisplayName("Saves NastavnikZvanje when valid")
        void savesValidZvanje() {
            Nastavnik n = nastavnik(1L, "Marko", "Markovic");
            NastavnikZvanjeRequest req = request(1L, "Docent");

            when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(n));
            when(repository.existsByNastavnikIdAndZvanjeAndAktivnoTrue(1L, "Docent"))
                    .thenReturn(false);
            when(repository.save(any(NastavnikZvanje.class))).thenAnswer(i -> {
                NastavnikZvanje nz = i.getArgument(0);
                nz.setId(100L);
                return nz;
            });

            NastavnikZvanje saved = service.save(req);

            assertNotNull(saved);
            assertEquals(100L, saved.getId());
            assertEquals("Docent", saved.getZvanje());
            assertEquals(n, saved.getNastavnik());

            verify(repository).existsByNastavnikIdAndZvanjeAndAktivnoTrue(1L, "Docent");
            verify(repository).save(zvanjeCaptor.capture());
            assertEquals("Docent", zvanjeCaptor.getValue().getZvanje());
        }

        @Test
        @DisplayName("Returns null if Nastavnik does not exist")
        void returnsNullIfNastavnikMissing() {
            NastavnikZvanjeRequest req = request(1L, "Docent");
            when(nastavnikRepository.findById(1L)).thenReturn(Optional.empty());

            NastavnikZvanje saved = service.save(req);

            assertNull(saved);
            verify(repository, never()).save(any());
        }

        @Test
        @DisplayName("Returns null if aktivno zvanje already exists")
        void returnsNullIfAktivnoExists() {
            Nastavnik n = nastavnik(1L, "Marko", "Markovic");
            NastavnikZvanjeRequest req = request(1L, "Docent");

            when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(n));
            when(repository.existsByNastavnikIdAndZvanjeAndAktivnoTrue(1L, "Docent")).thenReturn(true);

            NastavnikZvanje saved = service.save(req);

            assertNull(saved);
            verify(repository, never()).save(any());
        }
    }

    @Nested
    class FindAllTests {

        @Test
        @DisplayName("Returns all NastavnikZvanje with initialized Nastavnik")
        void returnsAll() {
            Nastavnik n1 = nastavnik(1L, "Ana", "Anic");
            Nastavnik n2 = nastavnik(2L, "Bojan", "Bojic");

            NastavnikZvanje nz1 = new NastavnikZvanje();
            nz1.setId(1L);
            nz1.setNastavnik(n1);

            NastavnikZvanje nz2 = new NastavnikZvanje();
            nz2.setId(2L);
            nz2.setNastavnik(n2);

            when(repository.findAll()).thenReturn(List.of(nz1, nz2));

            List<NastavnikZvanje> all = service.findAll();

            assertEquals(2, all.size());
            assertEquals("Ana", all.get(0).getNastavnik().getIme());
            assertEquals("Bojan", all.get(1).getNastavnik().getIme());
            verify(repository).findAll();
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        @DisplayName("Returns NastavnikZvanje if found")
        void returnsIfFound() {
            NastavnikZvanje nz = new NastavnikZvanje();
            nz.setId(1L);
            when(repository.findById(1L)).thenReturn(Optional.of(nz));

            NastavnikZvanje result = service.findById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("Throws exception if not found")
        void throwsIfNotFound() {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class, () -> service.findById(1L));
            assertEquals("NastavnikZvanje sa ID 1 ne postoji", ex.getMessage());
            verify(repository).findById(1L);
        }
    }
*/
}