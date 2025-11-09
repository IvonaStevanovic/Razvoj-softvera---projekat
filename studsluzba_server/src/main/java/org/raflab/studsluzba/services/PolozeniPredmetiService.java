package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PolozeniPredmetiRequest;
import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.raflab.studsluzba.model.PolozeniPredmeti;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.repositories.PolozeniPredmetiRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@AllArgsConstructor
public class PolozeniPredmetiService {
    private final PolozeniPredmetiRepository repository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final PredmetRepository predmetRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;

    @Transactional
    public PolozeniPredmeti save(PolozeniPredmetiRequest request) {
        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks sa ID " + request.getStudentIndeksId() + " ne postoji"));

        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet sa ID " + request.getPredmetId() + " ne postoji"));

        IzlazakNaIspit izlazak = null;
        if(request.getIzlazakNaIspitId() != null) {
            izlazak = izlazakNaIspitRepository.findById(request.getIzlazakNaIspitId())
                    .orElseThrow(() -> new RuntimeException("IzlazakNaIspit sa ID " + request.getIzlazakNaIspitId() + " ne postoji"));
        }

        PolozeniPredmeti polozeni = new PolozeniPredmeti();
        polozeni.setStudentIndeks(student);
        polozeni.setPredmet(predmet);
        polozeni.setOcena(request.getOcena());
        polozeni.setPriznat(request.isPriznat());
        polozeni.setIzlazakNaIspit(izlazak);

        return repository.save(polozeni);
    }

    public List<PolozeniPredmeti> getAll() {
        return repository.findAll();
    }

    public PolozeniPredmeti getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PolozeniPredmeti sa ID " + id + " ne postoji"));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
