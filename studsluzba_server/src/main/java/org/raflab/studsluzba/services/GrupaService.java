package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.GrupaRequest;
import org.raflab.studsluzba.model.Grupa;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.GrupaRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrupaService {

    private final GrupaRepository grupaRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final PredmetRepository predmetRepository;

    public Grupa save(GrupaRequest request) {
        Optional<StudijskiProgram> studijskiProgram = studijskiProgramRepository.findById(request.getStudijskiProgramId());
        List<Predmet> predmeti = predmetRepository.findByIdIn(request.getPredmetiId());

        if (studijskiProgram.isEmpty() || predmeti.isEmpty()) {
            throw new IllegalArgumentException("Nevalidni podaci: studijski program ili predmeti nisu pronađeni.");
        }

        Grupa grupa = Converters.toGrupa(request, studijskiProgram.get(), predmeti);
        return grupaRepository.save(grupa);
    }

    @Transactional(readOnly = true)
    public List<Grupa> findAll() {
        List<Grupa> grupe = grupaRepository.findAll();
        grupe.forEach(g -> g.getPredmeti().size()); // inicijalizuje lazy kolekciju
        return grupe;
    }


    public Optional<Grupa> findById(Long id) {
        return grupaRepository.findById(id);
    }

    public List<Grupa> findByStudijskiProgram(Long idPrograma) {
        return grupaRepository.findByStudijskiProgram(idPrograma);
    }

    public List<Grupa> findByPredmet(Long idPredmeta) {
        return grupaRepository.findByPredmet(idPredmeta);
    }

    public void delete(Long id) {
        grupaRepository.deleteById(id);
    }
}
