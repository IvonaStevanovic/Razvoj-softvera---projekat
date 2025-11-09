package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.raflab.studsluzba.repositories.VisokoskolskaUstanovaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VisokoskolskaUstanovaService {

    private final VisokoskolskaUstanovaRepository repository;

    public VisokoskolskaUstanova create(VisokoskolskaUstanova ustanova) {
        return repository.save(ustanova);
    }

    public VisokoskolskaUstanova update(Long id, VisokoskolskaUstanova ustanovaDetails) {
        VisokoskolskaUstanova ustanova = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ustanova not found"));
        ustanova.setNaziv(ustanovaDetails.getNaziv());
        ustanova.setMesto(ustanovaDetails.getMesto());
        ustanova.setVrsta(ustanovaDetails.getVrsta());
        return repository.save(ustanova);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<VisokoskolskaUstanova> findById(Long id) {
        return repository.findById(id);
    }

    public List<VisokoskolskaUstanova> findAll() {
        return repository.findAll();
    }

    public List<VisokoskolskaUstanova> findByMesto(String mesto) {
        return repository.findByMesto(mesto);
    }

    public List<VisokoskolskaUstanova> findByVrsta(String vrsta) {
        return repository.findByVrsta(vrsta);
    }
}
