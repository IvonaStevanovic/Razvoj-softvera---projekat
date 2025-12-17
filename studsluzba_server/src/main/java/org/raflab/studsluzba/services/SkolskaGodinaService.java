package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SkolskaGodinaService {
/*
    private final SkolskaGodinaRepository repository;

    @Transactional
    public SkolskaGodina create(SkolskaGodina godina) {
        // Normalize naziv: ukloni whitespace sa početka/kraja i sve u mala slova
        String naziv = godina.getNaziv().trim().toLowerCase();

        // Provera da li već postoji godina sa istim nazivom
        boolean exists = repository.findAll().stream()
                .anyMatch(g -> g.getNaziv().trim().toLowerCase().equals(naziv));

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Skolska godina sa tim nazivom već postoji"
            );
        }

        godina.setNaziv(godina.getNaziv().trim()); // sačuvaj normalizovan naziv
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

 */
}
