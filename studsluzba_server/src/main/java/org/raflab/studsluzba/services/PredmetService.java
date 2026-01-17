package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.mappers.PredmetConverter;
import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PredmetService {

    @Autowired
    private PredmetRepository predmetRepository;

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;

    @Transactional(readOnly = true)
    public List<Predmet> findAll() {
        // Kastujemo u List jer findAll() po defaultu vraća Iterable
        return (List<Predmet>) predmetRepository.findAll();
    }
    public List<PredmetResponse> getAllPredmeti() {
        return predmetRepository.findAll().stream()
                .map(PredmetConverter::toResponse)
                .collect(Collectors.toList());
    }

    public PredmetResponse getPredmetById(Long id) {
        Predmet p = predmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predmet nije pronađen"));
        return PredmetConverter.toResponse(p);
    }

    public PredmetResponse createPredmet(PredmetRequest request) {
        if (predmetRepository.existsBySifra(request.getSifra())) {
            throw new RuntimeException("Šifra predmeta već postoji!");
        }

        StudijskiProgram sp = null;
        if (request.getStudijskiProgramId() != null) {
            sp = studijskiProgramRepository.findById(request.getStudijskiProgramId())
                    .orElseThrow(() -> new RuntimeException("Studijski program nije pronađen"));
        }

        Predmet predmet = PredmetConverter.toEntity(request, sp);
        return PredmetConverter.toResponse(predmetRepository.save(predmet));
    }
    @Transactional
    public void deletePredmet(Long id) {
        // 1. Pronalaženje predmeta ili bacanje greške ako ne postoji
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predmet ne postoji"));

        // 2. Provera povezanosti na osnovu tvog modela
        // Proveravamo setove 'slusaPredmetSet' i 'drziPredmetSet' koji su definisani u tvojoj Predmet klasi
        boolean imaStudenteKojiSlusaju = predmet.getSlusaPredmetSet() != null && !predmet.getSlusaPredmetSet().isEmpty();
        boolean imaProfesoreKojiDrze = predmet.getDrziPredmetSet() != null && !predmet.getDrziPredmetSet().isEmpty();

        if (imaStudenteKojiSlusaju || imaProfesoreKojiDrze) {
            throw new RuntimeException("Ne možete obrisati predmet jer postoje studenti koji ga slušaju ili profesori koji ga drže.");
        }

        // 3. Brisanje ako nema prepreka
        predmetRepository.delete(predmet);
    }

    public List<PredmetResponse> getPredmetiByProgram(Long programId) {
        return predmetRepository.findByStudProgramId(programId).stream()
                .map(PredmetConverter::toResponse)
                .collect(Collectors.toList());
    }

    public Double izracunajProsek(Long id, Integer odG, Integer doG) {
        Double prosek = predmetRepository.getAverageOcenaForPredmetInRange(id, odG, doG);
        return prosek != null ? prosek : 0.0;
    }
    public Predmet findById(Long id) {
        return predmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predmet sa ID-jem " + id + " ne postoji."));
    }
    public Double getProsek(Long id, Integer odG, Integer doG) {
        return izracunajProsek(id, odG, doG);
    }
}
