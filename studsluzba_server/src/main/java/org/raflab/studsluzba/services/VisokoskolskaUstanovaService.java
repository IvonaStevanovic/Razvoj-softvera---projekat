package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.raflab.studsluzba.repositories.VisokoskolskaUstanovaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VisokoskolskaUstanovaService {

    private final VisokoskolskaUstanovaRepository repository;

    @Transactional
    public VisokoskolskaUstanova create(VisokoskolskaUstanova ustanova) {
        // Provera da li ustanova sa istim nazivom već postoji
        VisokoskolskaUstanova existing = repository.findByNaziv(ustanova.getNaziv());
        if (existing != null) {
            // Umesto da baci 500, samo vrati postojeću ili baci kontrolisanu grešku
            throw new RuntimeException("Ustanova sa ovim nazivom već postoji: " + ustanova.getNaziv());
        }
        return repository.save(ustanova);
    }

    @Transactional
    public VisokoskolskaUstanova update(Long id, VisokoskolskaUstanova ustanovaDetails) {
        VisokoskolskaUstanova ustanova = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ustanova ne postoji: " + id));

        // Provera da li naziv koji se pokušava postaviti već postoji kod druge ustanove
        VisokoskolskaUstanova duplicate = repository.findByNaziv(ustanovaDetails.getNaziv());
        if (duplicate != null && !duplicate.getId().equals(id)) {
            throw new RuntimeException("Ustanova sa ovim nazivom već postoji: " + ustanovaDetails.getNaziv());
        }

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
