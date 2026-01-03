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

    public void deletePredmet(Long id) {
        if (!predmetRepository.existsById(id)) {
            throw new RuntimeException("Predmet ne postoji");
        }
        predmetRepository.deleteById(id);
    }

    public List<PredmetResponse> getPredmetiByProgram(Long programId) {
        return predmetRepository.findByStudProgramId(programId).stream()
                .map(PredmetConverter::toResponse)
                .collect(Collectors.toList());
    }

    public Double getProsek(Long id, Integer odG, Integer doG) {
        Double prosek = predmetRepository.getAverageOcenaForPredmetInRange(id, odG, doG);
        return prosek != null ? prosek : 0.0;
    }
}