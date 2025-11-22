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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GrupaService {

    private final GrupaRepository grupaRepository;
    private final StudijskiProgramRepository studijskiProgramRepository;
    private final PredmetRepository predmetRepository;

    public Grupa save(GrupaRequest request) {
        StudijskiProgram program = studijskiProgramRepository.findById(request.getStudijskiProgramId())
                .orElseThrow(() -> new RuntimeException("Studijski program ne postoji"));

        List<Predmet> predmeti = (List<Predmet>) predmetRepository.findAllById(request.getPredmetiId());

        // Provera da li grupa već postoji
        List<Grupa> sveGrupe = grupaRepository.findByStudijskiProgram(request.getStudijskiProgramId());
        for (Grupa g : sveGrupe) {
            Set<Long> existingPredmetiIds = g.getPredmeti().stream().map(Predmet::getId).collect(Collectors.toSet());
            Set<Long> newPredmetiIds = predmeti.stream().map(Predmet::getId).collect(Collectors.toSet());

            if (existingPredmetiIds.equals(newPredmetiIds)) {
                throw new RuntimeException("Grupa sa ovim predmetima već postoji u ovom studijskom programu");
            }
        }

        Grupa grupa = new Grupa();
        grupa.setStudijskiProgram(program);
        grupa.setPredmeti(predmeti);

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
