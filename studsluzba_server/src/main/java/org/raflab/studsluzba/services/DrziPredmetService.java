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
    public List<DrziPredmetResponse> saveDrziPredmet(DrziPredmetRequest request) {

        List<DrziPredmetNewRequest> drziPredmetList = Optional.ofNullable(request.getDrziPredmet()).orElse(Collections.emptyList());
        List<DrziPredmetNewRequest> newDrziPredmetList = Optional.ofNullable(request.getNewDrziPredmet()).orElse(Collections.emptyList());

        // izvuci sve predmete iz baze po predmetId
        Map<Long, Predmet> predmetMap = predmetRepository.findByIdIn(
                drziPredmetList.stream().map(DrziPredmetNewRequest::getPredmetId).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Predmet::getId, Function.identity()));

        // izvuci sve predmete iz baze po naziv za nove unose
        Map<String, Predmet> newPredmetMap = predmetRepository.findByNazivIn(
                newDrziPredmetList.stream().map(DrziPredmetNewRequest::getPredmetNaziv).collect(Collectors.toList())
        ).stream().collect(Collectors.toMap(Predmet::getNaziv, Function.identity()));

        // izvuci sve nastavnike iz baze po email
        List<String> allEmails = Stream.concat(
                drziPredmetList.stream().map(DrziPredmetNewRequest::getEmailNastavnik),
                newDrziPredmetList.stream().map(DrziPredmetNewRequest::getEmailNastavnik)
        ).distinct().collect(Collectors.toList());

        Map<String, Nastavnik> nastavnikMap = nastavnikRepository.findByEmailIn(allEmails)
                .stream().collect(Collectors.toMap(Nastavnik::getEmail, Function.identity()));

        // kreiranje liste DrziPredmet objekata za cuvanje
        List<DrziPredmet> drziPredmetEntities = new ArrayList<>();

        for (DrziPredmetNewRequest drziPredmetRequest : drziPredmetList) {
            Predmet predmet = predmetMap.get(drziPredmetRequest.getPredmetId());
            Nastavnik nastavnik = nastavnikMap.get(drziPredmetRequest.getEmailNastavnik());

            if (predmet != null && nastavnik != null) {
                DrziPredmet drziPredmet = new DrziPredmet();
                drziPredmet.setPredmet(predmet);
                drziPredmet.setNastavnik(nastavnik);
                drziPredmetEntities.add(drziPredmet);
            }
        }

        for (DrziPredmetNewRequest newDrziPredmetRequest : newDrziPredmetList) {
            Predmet predmet = newPredmetMap.get(newDrziPredmetRequest.getPredmetNaziv());
            Nastavnik nastavnik = nastavnikMap.get(newDrziPredmetRequest.getEmailNastavnik());

            if (predmet != null && nastavnik != null) {
                DrziPredmet drziPredmet = new DrziPredmet();
                drziPredmet.setPredmet(predmet);
                drziPredmet.setNastavnik(nastavnik);
                drziPredmetEntities.add(drziPredmet);
            }
        }

        // sacuvaj sve u bazi
        List<DrziPredmet> saved = (List<DrziPredmet>) drziPredmetRepository.saveAll(drziPredmetEntities);

        // konvertuj u response listu
        return saved.stream().map(Converters::toDrziPredmetResponse).collect(Collectors.toList());
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
