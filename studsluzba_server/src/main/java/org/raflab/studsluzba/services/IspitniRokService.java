package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.IspitniRokRequest;
import org.raflab.studsluzba.controllers.response.IspitniRokResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IspitniRokService {
    @Autowired
    private IspitniRokRepository repository;
    @Autowired
    private  SkolskaGodinaRepository skolskaGodinaRepository;
    @Transactional(readOnly = true)
    public List<IspitniRokResponse> findAllResponses() {
        List<IspitniRok> rokovi = (List<IspitniRok>) repository.findAll();

        return rokovi.stream().map(rok -> {
            IspitniRokResponse resp = new IspitniRokResponse();
            resp.setId(rok.getId());
            resp.setNaziv(rok.getNaziv());

            // Mapiranje školske godine - važno za prikaz u klijentu
            if (rok.getSkolskaGodina() != null) {
                resp.setSkolskaGodinaId(rok.getSkolskaGodina().getId());
                resp.setSkolskaGodinaNaziv(rok.getSkolskaGodina().getNaziv());
            }

            resp.setPocetak(rok.getDatumPocetka());
            resp.setKraj(rok.getDatumZavrsetka());
            resp.setAktivan(rok.getAktivan());
            return resp;
        }).collect(Collectors.toList());
    }

    public List<IspitniRok> findAll() {
        return (List<IspitniRok>) repository.findAll();
    }

    public Optional<IspitniRok> findById(Long id) {
        return repository.findById(id);
    }

    //public List<IspitniRok> findBySkolskaGodina(Long godinaId) {
       // return repository.findBySkolskaGodinaId(godinaId);
 //   }
    @Transactional
    public IspitniRok saveNewRok(IspitniRokRequest request) {
        IspitniRok rok = new IspitniRok();
        rok.setNaziv(request.getNaziv());
        rok.setDatumPocetka(request.getPocetak());
        rok.setDatumZavrsetka(request.getKraj());

        // Pronalaženje školske godine (ako šalješ ID sa klijenta)
        if (request.getSkolskaGodinaId() != null) {
            rok.setSkolskaGodina(skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                    .orElseThrow(() -> new RuntimeException("Školska godina nije pronađena")));
        }

        return repository.save(rok);
    }
    public IspitniRok save(IspitniRok rok) {
        return repository.save(rok);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
/*
    @Autowired
    private IspitniRokRepository repository;

    // Dodavanje sa proverom duplikata
    public IspitniRok save(IspitniRok rok) {
        Optional<IspitniRok> existing = repository.findByDatumPocetkaAndSkolskaGodinaId(
                rok.getDatumPocetka(),
                rok.getSkolskaGodina().getId()
        );

        if (existing.isPresent()) {
            throw new IllegalArgumentException("Ispitni rok već postoji za datu školsku godinu!");
        }

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

    @Transactional
    public void deleteById(Long id) {
        IspitniRok rok = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispitni rok sa id " + id + " ne postoji."));

        // Provera da li ima child entiteta
        if (rok.getIspiti() != null && !rok.getIspiti().isEmpty()) {
            throw new IllegalStateException("Ne može se obrisati ispitni rok koji ima ispita.");
        }

        repository.delete(rok);
    }

 */
}

