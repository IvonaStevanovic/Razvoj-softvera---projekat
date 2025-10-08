package org.raflab.studsluzba;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootstrapData implements CommandLineRunner {

    private final PredmetRepository predmetRepository;

    public BootstrapData(PredmetRepository predmetRepository) {
        this.predmetRepository = predmetRepository;
    }

    @Override
    public void run(String[] args) {
//        Predmet p = new Predmet();
//        p.setSifra("PR2");
//        p.setNaziv("Softverske komponente");
//        p.setOpis("Neki opis");
//        p.setEspb(6);
//        p.setObavezan(true);
//
//        predmetRepository.save(p);
//
//
//        System.out.println("Dodat predmet u bazu " + p.getNaziv());

    }
}