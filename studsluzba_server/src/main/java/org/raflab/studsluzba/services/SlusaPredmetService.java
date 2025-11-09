package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SlusaPredmetService {

    private final SlusaPredmetRepository slusaPredmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    @Transactional
    public SlusaPredmet create(SlusaPredmet request) {
        return slusaPredmetRepository.save(request);
    }

    @Transactional
    public SlusaPredmet update(Long id, SlusaPredmet updated) {
        Optional<SlusaPredmet> opt = slusaPredmetRepository.findById(id);
        if (opt.isPresent()) {
            SlusaPredmet sp = opt.get();
            sp.setStudentIndeks(updated.getStudentIndeks());
            sp.setDrziPredmet(updated.getDrziPredmet());
            sp.setSkolskaGodina(updated.getSkolskaGodina());
            return slusaPredmetRepository.save(sp);
        }
        return null; // ili baci exception
    }

    public void delete(Long id) {
        slusaPredmetRepository.deleteById(id);
    }

    public SlusaPredmet findById(Long id) {
        return slusaPredmetRepository.findById(id).orElse(null);
    }

    public List<SlusaPredmet> findAll() {
        return (List<SlusaPredmet>) slusaPredmetRepository.findAll();
    }

    public List<SlusaPredmet> findByStudentIndeks(Long indeksId) {
        return slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(indeksId);
    }

    public List<StudentIndeks> findStudentiPoPredmetu(Long predmetId, Long nastavnikId) {
        return slusaPredmetRepository.getStudentiSlusaPredmetAktivnaGodina(predmetId, nastavnikId);
    }

    public List<StudentIndeks> findStudentiNeSlusajuPredmet(Long drziPredmetId) {
        return slusaPredmetRepository.getStudentiNeSlusajuDrziPredmet(drziPredmetId);
    }
}
