package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IspitService {
    @Autowired
    private IspitRepository ispitRepository;

    // Kreiranje ili ažuriranje ispita
    public Ispit save(Ispit ispit) {
        return ispitRepository.save(ispit);
    }

    // Vraća sve ispita iz baze
    public List<Ispit> findAll() {
        return ispitRepository.findAll();
    }

    // Pronalazi ispit po ID-u
    public Optional<Ispit> findById(Long id) {
        return ispitRepository.findById(id);
    }

    // Briše ispit po ID-u
    public void deleteById(Long id) {
        ispitRepository.deleteById(id);
    }

    // Pronalazi sve ispita za određeni predmet
    public List<Ispit> findByPredmet(Long predmetId) {
        return ispitRepository.findByPredmet(predmetId);
    }

    // Pronalazi sve ispita koje drži određeni nastavnik
    public List<Ispit> findByNastavnik(Long nastavnikId) {
        return ispitRepository.findByNastavnik(nastavnikId);
    }

    // Pronalazi sve ispita za određeni ispitni rok
    public List<Ispit> findByIspitniRok(Long ispitniRokId) {
        return ispitRepository.findByIspitniRok(ispitniRokId);
    }

    // Kombinovana pretraga po opcionalnim kriterijumima
    public List<Ispit> findByPredmetNastavnikRok(Long predmetId, Long nastavnikId, Long ispitniRokId) {
        if (predmetId != null)
            return findByPredmet(predmetId);
        else if (nastavnikId != null)
            return findByNastavnik(nastavnikId);
        else if (ispitniRokId != null)
            return findByIspitniRok(ispitniRokId);
        else
            return findAll();
    }
}
