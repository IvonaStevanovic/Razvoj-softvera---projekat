package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.model.ObnovaGodine;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.ObnovaGodineRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ObnovaGodineService {
    private final ObnovaGodineRepository repository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final PredmetRepository predmetRepository;

    @Transactional
    public ObnovaGodine save(ObnovaGodineRequest request) {
        ObnovaGodine obnova = new ObnovaGodine();

        // pronalazak indeksa studenta
        StudentIndeks indeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException(
                        "StudentIndeks sa id: " + request.getStudentIndeksId() + " ne postoji"
                ));
        obnova.setStudentIndeks(indeks);

        // pronalazak predmeta koje student upisuje
        Set<Predmet> predmeti = new HashSet<>();
        if (request.getPredmetIds() != null && !request.getPredmetIds().isEmpty()) {
            predmeti = new HashSet<>(predmetRepository.findByIdIn(
                    new java.util.ArrayList<>(request.getPredmetIds())
            ));
        }
        obnova.setPredmetiKojeUpisuje(predmeti);

        // ostala polja
        obnova.setGodinaStudija(request.getGodinaStudija());
        obnova.setDatum(request.getDatum());
        obnova.setNapomena(request.getNapomena());

        return repository.save(obnova);
    }


    @Transactional
    public ObnovaGodine findById(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ObnovaGodine ne postoji: "+id));
    }

    @Transactional
    public List<ObnovaGodine> findAll(){
        return repository.findAllWithPredmetiAndStudent();
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}

