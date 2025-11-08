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
    public ObnovaGodine save(ObnovaGodineRequest request){
        ObnovaGodine obnova=new ObnovaGodine();
        StudentIndeks indeks=studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(()->new RuntimeException("StudentIndeks sa id: "+request.getStudentIndeksId()+" ne postoji"));
        obnova.setStudentIndeks(indeks);

        Set<Predmet> predmeti = new HashSet<>();
        if(request.getPredmetIds()!=null){
            predmeti = new HashSet<>(predmetRepository.findByIdIn((List<Long>) request.getPredmetIds()));
        }
        obnova.setPredmetiKojeUpisuje(predmeti);
        obnova.setGodinaStudija(request.getGodinaStudija());
        obnova.setDatum(request.getDatum());
        obnova.setNapomena(request.getNapomena());

        return repository.save(obnova);
    }

    public ObnovaGodine findById(Long id){
        return repository.findById(id).orElseThrow(()->new RuntimeException("ObnovaGodine ne postoji: "+id));
    }

    public java.util.List<ObnovaGodine> findAll(){
        return repository.findAll();
    }

    public void delete(Long id){
        repository.deleteById(id);
    }
}

