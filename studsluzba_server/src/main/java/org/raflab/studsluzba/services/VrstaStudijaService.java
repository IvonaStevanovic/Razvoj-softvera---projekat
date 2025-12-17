package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.VrstaStudija;
import org.raflab.studsluzba.repositories.VrstaStudijaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class VrstaStudijaService {
/*
    private final VrstaStudijaRepository repository;

    public VrstaStudija create(VrstaStudija vrsta) {
        // Provera duplikata po oznaci
        VrstaStudija byOznaka = repository.findByOznaka(vrsta.getOznaka());
        if (byOznaka != null) {
            throw new RuntimeException("Vrsta studija sa ovom oznakom već postoji");
        }

        // Provera duplikata po punom nazivu
        VrstaStudija byPunNaziv = repository.findByPunNaziv(vrsta.getPunNaziv());
        if (byPunNaziv != null) {
            throw new RuntimeException("Vrsta studija sa ovim punim nazivom već postoji");
        }

        return repository.save(vrsta);
    }

    public VrstaStudija update(Long id, VrstaStudija vrstaDetails) {
        VrstaStudija vrsta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vrsta studija not found"));
        vrsta.setOznaka(vrstaDetails.getOznaka());
        vrsta.setPunNaziv(vrstaDetails.getPunNaziv());
        return repository.save(vrsta);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<VrstaStudija> findById(Long id) {
        return repository.findById(id);
    }

    public List<VrstaStudija> findAll() {
        return repository.findAll();
    }

    public VrstaStudija findByOznaka(String oznaka) {
        return repository.findByOznaka(oznaka);
    }

    public VrstaStudija findByPunNaziv(String punNaziv) {
        return repository.findByPunNaziv(punNaziv);
    }

    public List<VrstaStudija> findByPunNazivContaining(String deoNaziva) {
        return repository.findByPunNazivContaining(deoNaziva);
    }

 */
}
