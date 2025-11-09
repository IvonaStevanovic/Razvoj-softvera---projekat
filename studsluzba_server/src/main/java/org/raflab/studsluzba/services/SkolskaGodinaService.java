package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SkolskaGodinaService {

    private final SkolskaGodinaRepository repository;

    public SkolskaGodina create(SkolskaGodina godina) {
        return repository.save(godina);
    }

    public SkolskaGodina update(Long id, SkolskaGodina godinaDetails) {
        SkolskaGodina godina = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skolska godina not found"));
        godina.setNaziv(godinaDetails.getNaziv());
        godina.setAktivna(godinaDetails.isAktivna());
        return repository.save(godina);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<SkolskaGodina> findById(Long id) {
        return repository.findById(id);
    }

    public List<SkolskaGodina> findAll() {
        return repository.findAll();
    }

    public List<SkolskaGodina> findAktivne() {
        return repository.findAktivne();
    }

    public SkolskaGodina findByNaziv(String naziv) {
        return repository.findByNaziv(naziv);
    }
}
