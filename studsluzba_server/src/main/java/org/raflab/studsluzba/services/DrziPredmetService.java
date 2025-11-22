package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.DrziPredmetNewRequest;
import org.raflab.studsluzba.controllers.request.DrziPredmetRequest;
import org.raflab.studsluzba.controllers.response.DrziPredmetResponse;
import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.repositories.DrziPredmetRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DrziPredmetService {

    private final DrziPredmetRepository drziPredmetRepository;
    private final PredmetRepository predmetRepository;
    private final NastavnikRepository nastavnikRepository;

    @Transactional
    public ResponseEntity<?> saveDrziPredmet(DrziPredmetRequest request) {

        List<DrziPredmetNewRequest> drziPredmetList = Optional.ofNullable(request.getDrziPredmet())
                .orElse(Collections.emptyList());
        List<DrziPredmetNewRequest> newDrziPredmetList = Optional.ofNullable(request.getNewDrziPredmet())
                .orElse(Collections.emptyList());

        // Kombinujemo sve emailove nastavnika
        List<String> allEmails = Stream.concat(
                        drziPredmetList.stream().map(DrziPredmetNewRequest::getEmailNastavnik),
                        newDrziPredmetList.stream().map(DrziPredmetNewRequest::getEmailNastavnik)
                ).filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // Kreiramo mape za postojeće i nove predmete
        Map<Long, Predmet> predmetMap = predmetRepository.findByIdIn(
                drziPredmetList.stream()
                        .map(DrziPredmetNewRequest::getPredmetId)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Predmet::getId, Function.identity()));

        Map<String, Predmet> newPredmetMap = predmetRepository.findByNazivIn(
                newDrziPredmetList.stream()
                        .map(DrziPredmetNewRequest::getPredmetNaziv)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Predmet::getNaziv, Function.identity()));

        // Mapa nastavnika
        Map<String, Nastavnik> nastavnikMap = nastavnikRepository.findByEmailIn(allEmails)
                .stream().collect(Collectors.toMap(Nastavnik::getEmail, Function.identity()));

        List<DrziPredmet> drziPredmetEntities = new ArrayList<>();

        // Postojeći predmeti
        for (DrziPredmetNewRequest dp : drziPredmetList) {
            if (dp.getPredmetId() == null) continue;

            Predmet predmet = predmetMap.get(dp.getPredmetId());
            Nastavnik nastavnik = nastavnikMap.get(dp.getEmailNastavnik());

            if (predmet != null && nastavnik != null) {
                DrziPredmet postoji = drziPredmetRepository.getDrziPredmetNastavnikPredmet(
                        predmet.getId(), nastavnik.getId()
                );
                if (postoji == null) {
                    DrziPredmet newDp = new DrziPredmet();
                    newDp.setPredmet(predmet);
                    newDp.setNastavnik(nastavnik);
                    drziPredmetEntities.add(newDp);
                }
            }
        }

        // Novi predmeti
        for (DrziPredmetNewRequest dp : newDrziPredmetList) {
            if (dp.getPredmetNaziv() == null) continue;

            Predmet predmet = newPredmetMap.get(dp.getPredmetNaziv());
            Nastavnik nastavnik = nastavnikMap.get(dp.getEmailNastavnik());

            if (predmet != null && nastavnik != null) {
                DrziPredmet postoji = drziPredmetRepository.getDrziPredmetNastavnikPredmet(
                        predmet.getId(), nastavnik.getId()
                );
                if (postoji == null) {
                    DrziPredmet newDp = new DrziPredmet();
                    newDp.setPredmet(predmet);
                    newDp.setNastavnik(nastavnik);
                    drziPredmetEntities.add(newDp);
                }
            }
        }

        if (drziPredmetEntities.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Nema novih veza za dodavanje. Sve postoji u bazi!");
        }

        drziPredmetRepository.saveAll(drziPredmetEntities);

        return ResponseEntity.ok("Uspešno sačuvano " + drziPredmetEntities.size() + " novih veza DrziPredmet!");
    }

    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> getAll() {
        return drziPredmetRepository.findAll().stream()
                .map(Converters::toDrziPredmetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> getByNastavnikId(Long nastavnikId) {
        return drziPredmetRepository.findByNastavnikId(nastavnikId).stream()
                .map(Converters::toDrziPredmetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DrziPredmetResponse> getByPredmetId(Long predmetId) {
        return drziPredmetRepository.findByPredmetId(predmetId).stream()
                .map(Converters::toDrziPredmetResponse)
                .collect(Collectors.toList());
    }
}
