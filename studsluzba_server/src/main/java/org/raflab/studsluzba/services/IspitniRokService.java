package org.raflab.studsluzba.services;

import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class IspitniRokService {
    @Autowired
    private IspitniRokRepository repository;

    public IspitniRok save(IspitniRok rok) {
        return repository.save(rok);
    }

    public List<IspitniRok> findAll() {
        return (List<IspitniRok>) repository.findAll();
    }

    public Optional<IspitniRok> findById(Long id) {
        return repository.findById(id);
    }

    public List<IspitniRok> findBySkolskaGodina(Long skolskaGodinaId) {
        return repository.findBySkolskaGodina(skolskaGodinaId);
    }

    public List<IspitniRok> findAktivniRokovi(LocalDate today) {
        return repository.findAktivniRokovi(today);
    }

    public IspitniRok findByDatumPocetka(LocalDate datum) {
        return repository.findByDatumPocetka(datum);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
