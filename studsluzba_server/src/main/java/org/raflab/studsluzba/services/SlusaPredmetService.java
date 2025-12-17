package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.response.SlusaPredmetResponse;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.SkolskaGodinaRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SlusaPredmetService {
/*
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    @Transactional
    public SlusaPredmetResponse create(SlusaPredmetRequest request) {
        boolean exists = slusaPredmetRepository.existsByStudentDrziPredmetGodina(
                request.getStudentIndeksId(),
                request.getDrziPredmetId(),
                request.getSkolskaGodinaId()
        );
        if (exists) throw new RuntimeException("Student već sluša ovaj predmet u toj školskoj godini");

        SlusaPredmet sp = new SlusaPredmet();
        sp.setStudentIndeks(studentIndeksRepository.findById(request.getStudentIndeksId()).orElseThrow());
        sp.setDrziPredmet(drziPredmetRepository.findById(request.getDrziPredmetId()).orElseThrow());
        sp.setSkolskaGodina(skolskaGodinaRepository.findById(request.getSkolskaGodinaId()).orElseThrow());

        SlusaPredmet saved = slusaPredmetRepository.save(sp);
        return Converters.toSlusaPredmetResponse(saved);
    }


    @Transactional
    public SlusaPredmetResponse update(Long id, SlusaPredmetRequest request) {
        // 1. Učitaj postojeći entitet
        SlusaPredmet existing = slusaPredmetRepository.findById(id).orElseThrow(() -> new RuntimeException("SlusaPredmet not found"));
        // 2. Proveri da li student već sluša isti predmet u istoj godini (duplikat)
        boolean exists = slusaPredmetRepository.existsByStudentDrziPredmetGodina(request.getStudentIndeksId(), request.getDrziPredmetId(), request.getSkolskaGodinaId());
        if (exists && !(existing.getStudentIndeks().getId().equals(request.getStudentIndeksId()) && existing.getDrziPredmet().getId().equals(request.getDrziPredmetId()) && existing.getSkolskaGodina().getId().equals(request.getSkolskaGodinaId()))) {
            throw new RuntimeException("Student već sluša ovaj predmet u toj školskoj godini");
        }
        // 3. Ažuriraj polja
        existing.setStudentIndeks(studentIndeksRepository.findById(request.getStudentIndeksId()).orElse(null));
        existing.setDrziPredmet(drziPredmetRepository.findById(request.getDrziPredmetId()).orElse(null));
        existing.setSkolskaGodina(skolskaGodinaRepository.findById(request.getSkolskaGodinaId()).orElse(null));
        // 4. Sačuvaj entitet
        SlusaPredmet updated = slusaPredmetRepository.save(existing);
        // 5. Vrati DTO

        return Converters.toSlusaPredmetResponse(updated);
    }


    public SlusaPredmetResponse getById(Long id) {
        SlusaPredmet sp = slusaPredmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("SlusaPredmet not found"));
        return Converters.toSlusaPredmetResponse(sp);
    }


    @Transactional(readOnly = true)
    public List<SlusaPredmet> findAll() {
        return (List<SlusaPredmet>) slusaPredmetRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<SlusaPredmet> findByStudentIndeks(Long indeksId) {
        return slusaPredmetRepository.getSlusaPredmetForIndeksAktivnaGodina(indeksId);
    }
*/
}
