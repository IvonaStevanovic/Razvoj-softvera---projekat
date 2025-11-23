
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.DrziPredmetNewRequest;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;

import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.services.DrziPredmetService;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DrziPredmetServiceTest {

    @Mock
    private DrziPredmetRepository drziPredmetRepository;

    @Mock
    private PredmetRepository predmetRepository;

    @Mock
    private NastavnikRepository nastavnikRepository;

    @InjectMocks
    private DrziPredmetService drziPredmetService;

    private DrziPredmetRequest request;

    private Predmet predmet1;
    private Predmet predmet2;
    private Nastavnik nastavnik1;

    @BeforeEach
    void setUp() {
        // Predmet i nastavnik za test
        predmet1 = new Predmet();
        predmet1.setId(1L);
        predmet1.setNaziv("Matematika");

        predmet2 = new Predmet();
        predmet2.setId(2L);
        predmet2.setNaziv("Fizika");

        nastavnik1 = new Nastavnik();
        nastavnik1.setId(1L);
        nastavnik1.setEmail("nastavnik@test.com");

        // Kreiranje zahteva sa postojećim i novim predmetima
        DrziPredmetNewRequest dpExisting = new DrziPredmetNewRequest();
        dpExisting.setPredmetId(1L);
        dpExisting.setEmailNastavnik("nastavnik@test.com");

        DrziPredmetNewRequest dpNew = new DrziPredmetNewRequest();
        dpNew.setPredmetNaziv("Fizika");
        dpNew.setEmailNastavnik("nastavnik@test.com");

        request = new DrziPredmetRequest();
        request.setDrziPredmet(Collections.singletonList(dpExisting));
        request.setNewDrziPredmet(Collections.singletonList(dpNew));
    }

    @Test
    void saveDrziPredmet_success() {
        // Mock metoda za predmete i nastavnika
        when(predmetRepository.findByIdIn(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(predmet1));

        when(predmetRepository.findByNazivIn(Collections.singletonList("Fizika")))
                .thenReturn(Collections.singletonList(predmet2));

        when(nastavnikRepository.findByEmailIn(Collections.singletonList("nastavnik@test.com")))
                .thenReturn(Collections.singletonList(nastavnik1));

        when(drziPredmetRepository.getDrziPredmetNastavnikPredmet(1L, 1L)).thenReturn(null);
        when(drziPredmetRepository.getDrziPredmetNastavnikPredmet(2L, 1L)).thenReturn(null);

        when(drziPredmetRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<?> response = drziPredmetService.saveDrziPredmet(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Uspešno sačuvano 2 novih veza"));
        verify(drziPredmetRepository, times(1)).saveAll(anyList());
    }

    @Test
    void saveDrziPredmet_noNewEntities_returnsBadRequest() {
        // Sve postoji u bazi
        when(predmetRepository.findByIdIn(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(predmet1));
        when(predmetRepository.findByNazivIn(Collections.singletonList("Fizika")))
                .thenReturn(Collections.singletonList(predmet2));
        when(nastavnikRepository.findByEmailIn(Collections.singletonList("nastavnik@test.com")))
                .thenReturn(Collections.singletonList(nastavnik1));
        when(drziPredmetRepository.getDrziPredmetNastavnikPredmet(anyLong(), anyLong()))
                .thenReturn(new DrziPredmet()); // već postoji

        ResponseEntity<?> response = drziPredmetService.saveDrziPredmet(request);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Nema novih veza za dodavanje"));
        verify(drziPredmetRepository, never()).saveAll(anyList());
    }



}
