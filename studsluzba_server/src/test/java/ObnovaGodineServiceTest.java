
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.model.ObnovaGodine;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.ObnovaGodineRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.services.ObnovaGodineService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ObnovaGodineServiceTest {

    @Mock
    private ObnovaGodineRepository obnovaGodineRepository;

    @Mock
    private StudentIndeksRepository studentIndeksRepository;

    @Mock
    private PredmetRepository predmetRepository;

    @InjectMocks
    private ObnovaGodineService service;

    private StudentIndeks studentIndeks;
    private Predmet predmet;

    @BeforeEach
    void setUp() {
        studentIndeks = new StudentIndeks();
        studentIndeks.setId(1L);

        predmet = new Predmet();
        predmet.setId(10L);
        predmet.setNaziv("Matematika");
    }

    @Test
    void save_success() {
        // priprema requesta
        ObnovaGodineRequest request = new ObnovaGodineRequest();
        request.setStudentIndeksId(1L);
        request.setGodinaStudija(2);
        request.setDatum(LocalDate.of(2025, 11, 23));
        request.setNapomena("Test napomena");
        request.setPredmetIds(Set.of(10L));

        // stubovanje repozitorijuma
        when(studentIndeksRepository.findById(1L)).thenReturn(Optional.of(studentIndeks));
        when(obnovaGodineRepository.findByStudentIndeksIdAndGodinaStudija(1L, 2))
                .thenReturn(Optional.empty());
        when(predmetRepository.findByIdIn(any())).thenReturn(List.of(predmet));
        when(obnovaGodineRepository.save(any(ObnovaGodine.class))).thenAnswer(i -> {
            ObnovaGodine o = i.getArgument(0);
            o.setId(100L);
            return o;
        });

        // poziv metode
        ObnovaGodine result = service.save(request);

        // provera
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals(studentIndeks, result.getStudentIndeks());
        assertEquals(1, result.getPredmetiKojeUpisuje().size());
        verify(obnovaGodineRepository, times(1)).save(any(ObnovaGodine.class));
    }

    @Test
    void save_duplicate_throwsException() {
        ObnovaGodineRequest request = new ObnovaGodineRequest();
        request.setStudentIndeksId(1L);
        request.setGodinaStudija(2);

        when(studentIndeksRepository.findById(1L)).thenReturn(Optional.of(studentIndeks));
        when(obnovaGodineRepository.findByStudentIndeksIdAndGodinaStudija(1L, 2))
                .thenReturn(Optional.of(new ObnovaGodine()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.save(request));
        assertTrue(exception.getMessage().contains("već postoji"));
    }
}
