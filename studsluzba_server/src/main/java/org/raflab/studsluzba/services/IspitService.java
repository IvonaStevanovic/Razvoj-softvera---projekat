package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.response.PrijavaIspitaResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IspitService {
/*
    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    public Ispit save(Ispit ispit) {
        Predmet predmet = predmetRepository.findById(ispit.getPredmet().getId())
                .orElseThrow(() -> new EntityNotFoundException("Predmet ne postoji"));
        Nastavnik nastavnik = nastavnikRepository.findById(ispit.getNastavnik().getId())
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik ne postoji"));
        IspitniRok rok = ispitniRokRepository.findById(ispit.getIspitniRok().getId())
                .orElseThrow(() -> new EntityNotFoundException("Ispitni rok ne postoji"));

        ispit.setPredmet(predmet);
        ispit.setNastavnik(nastavnik);
        ispit.setIspitniRok(rok);

        boolean exists = ispitRepository.existsByDatumOdrzavanjaAndPredmetIdAndNastavnikIdAndIspitniRokId(
                ispit.getDatumOdrzavanja(),
                predmet.getId(),
                nastavnik.getId(),
                rok.getId()
        );

        if (exists) {
            throw new IllegalArgumentException("Ispit već postoji za zadati datum, predmet i nastavnika u ovom roku!");
        }

        return ispitRepository.save(ispit);
    }
    @Transactional(readOnly = true)
    public List<PrijavaIspitaResponse> getPrijavljeniStudenti(Long ispitId) {
        // Pronađi ispit po ID-ju
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        // Pronađi sve prijave za taj ispit
        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByIspit(ispitId);

        // Konvertuj svaku prijavu u response objekat
        return prijave.stream()
                .map(this::convertToPrijavaResponse)
                .collect(Collectors.toList());

    }

    private PrijavaIspitaResponse convertToPrijavaResponse(PrijavaIspita p) {
        PrijavaIspitaResponse response = new PrijavaIspitaResponse();
        response.setId(p.getId());
        response.setDatumPrijave(p.getDatumPrijave());
        response.setIspitId(p.getIspit() != null ? p.getIspit().getId() : null);
        response.setStudentIndeksId(p.getStudentIndeks() != null ? p.getStudentIndeks().getId() : null);
        return response;
    }
    public List<Ispit> findAll() {
        return ispitRepository.findAll();
    }

    public Optional<Ispit> findById(Long id) {
        return ispitRepository.findById(id);
    }
/*
    @Transactional
    public void deleteById(Long id) {
        Ispit ispit = ispitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispit sa id " + id + " ne postoji."));
        ispitRepository.delete(ispit);
    }

    public List<Ispit> findByPredmet(Long predmetId) {
        return ispitRepository.findByPredmet(predmetId);
    }

    public List<Ispit> findByNastavnik(Long nastavnikId) {
        return ispitRepository.findByNastavnik(nastavnikId);
    }

    public List<Ispit> findByIspitniRok(Long ispitniRokId) {
        return ispitRepository.findByIspitniRok(ispitniRokId);
    }

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
    */
}

