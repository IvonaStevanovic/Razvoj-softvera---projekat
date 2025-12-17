package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.SrednjaSkola;
import org.raflab.studsluzba.repositories.SrednjaSkolaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SrednjaSkolaService {
/*
    private final SrednjaSkolaRepository repository;

    @Transactional(readOnly = true)
    public List<SrednjaSkola> getAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SrednjaSkola> getById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public SrednjaSkola create(SrednjaSkola s) {
        return repository.save(s);
    }

    @Transactional
    public SrednjaSkola update(Long id, SrednjaSkola s) {
        return repository.findById(id).map(existing -> {
            existing.setNaziv(s.getNaziv());
            existing.setMesto(s.getMesto());
            existing.setVrsta(s.getVrsta());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("SrednjaSkola not found"));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SrednjaSkola> findByMesto(String mesto) {
        return repository.findByMesto(mesto);
    }

    @Transactional(readOnly = true)
    public List<SrednjaSkola> findByVrsta(String vrsta) {
        return repository.findByVrsta(vrsta);
    }

    @Transactional(readOnly = true)
    public List<SrednjaSkola> findByNazivContaining(String naziv) {
        return repository.findByNazivContaining(naziv);
    }

 */
}

