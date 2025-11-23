package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.response.UpisGodineResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ObnovaGodineService {

    private final ObnovaGodineRepository repository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final PredmetRepository predmetRepository;
    private final UpisGodineRepository upisGodineRepository;
    private final DrziPredmetRepository drziPredmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;

    @Transactional
    public ObnovaGodine save(ObnovaGodineRequest request) {

        // pronalazak indeksa studenta
        StudentIndeks indeks = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException(
                        "StudentIndeks sa id: " + request.getStudentIndeksId() + " ne postoji"
                ));

        // Provera da li postoji obnova za istog studenta i istu godinu studija
        Optional<ObnovaGodine> existing = repository.findByStudentIndeksIdAndGodinaStudija(
                indeks.getId(), request.getGodinaStudija()
        );

        if (existing.isPresent()) {
            throw new RuntimeException("Obnova godine već postoji za ovog studenta i godinu studija");
        }

        ObnovaGodine obnova = new ObnovaGodine();
        obnova.setStudentIndeks(indeks);

        // pronalazak predmeta koje student upisuje
        Set<Predmet> predmeti = new HashSet<>();
        if (request.getPredmetIds() != null && !request.getPredmetIds().isEmpty()) {
            predmeti = new HashSet<>(predmetRepository.findByIdIn(
                    new ArrayList<>(request.getPredmetIds())
            ));
        }
        obnova.setPredmetiKojeUpisuje(predmeti);

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

    @Transactional
    public void delete(Long id){
        repository.deleteById(id);
    }

   /* @Transactional
    public UpisGodineResponse obnoviGodinu(Long studentIndeksId, int trenutnaGodina, Set<Long> predmetIds) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        // Dohvati nepoložene predmete iz prethodnog upisa
        UpisGodine prethodniUpis = upisGodineRepository.findByStudentAndGodina(indeks.getId(), trenutnaGodina);
        Set<Predmet> nepolozeni = prethodniUpis.getPrenetiPredmeti(); // ili filtrirati po SlusaPredmet gde nije polozen

        // Kreiraj novi upis
        UpisGodine upis = new UpisGodine();
        upis.setStudentIndeks(indeks);
        upis.setGodinaStudija(trenutnaGodina + 1);
        upis.setDatum(LocalDate.now());
        upis.setNapomena("Obnova godine");

        // Dodaj predmete iz prethodnog + nove iz predmetIds
        Set<Predmet> noviPredmeti = predmetIds.stream()
                .map(pid -> predmetRepository.findById(pid)
                        .orElseThrow(() -> new RuntimeException("Predmet ne postoji")))
                .collect(Collectors.toSet());

        Set<Predmet> sviPredmeti = new HashSet<>();
        sviPredmeti.addAll(nepolozeni);
        sviPredmeti.addAll(noviPredmeti);

        // Provera ESPB
        int ukupnoEspb = sviPredmeti.stream().mapToInt(Predmet::getEspb).sum();
        if (ukupnoEspb > 60) {
            throw new RuntimeException("Maksimalni zbir ESPB poena je 60");
        }

        upis.setPrenetiPredmeti(sviPredmeti);
        UpisGodine savedUpis = upisGodineRepository.save(upis);

        // Kreiranje SlusaPredmet za svaki predmet
        for (Predmet p : sviPredmeti) {
            DrziPredmet drzi = drziPredmetRepository.findByPredmetId(p.getId())
                    .stream()
                    .filter(dp -> dp.getSkolskaGodina().isAktivna())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Predmet nije dostupan u aktuelnoj godini"));

            SlusaPredmet sp = new SlusaPredmet();
            sp.setStudentIndeks(indeks);
            sp.setDrziPredmet(drzi);
            sp.setSkolskaGodina(drzi.getSkolskaGodina());

            slusaPredmetRepository.save(sp);
        }

        return EntityMappers.toUpisGodineResponse(savedUpis);
    }*/

}

