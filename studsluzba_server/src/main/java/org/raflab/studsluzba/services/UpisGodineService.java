package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.response.UpisGodineResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
public class UpisGodineService {
/*
    private final UpisGodineRepository repository;
    private final StudentIndeksRepository studentRepository;
    private final PredmetRepository predmetRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final DrziPredmetRepository drziPredmetRepository;

    public List<UpisGodine> findAll() {
        return repository.findAll();
    }

    public UpisGodine findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UpisGodine> getAll() {
        List<UpisGodine> upisi = repository.findAll();
        upisi.forEach(u -> {
            if (u.getPrenetiPredmeti() != null)
                u.getPrenetiPredmeti().size(); // forsira učitavanje lazy kolekcije
        });
        return upisi;
    }

    @Transactional
    public UpisGodine save(UpisGodineRequest request) {
        StudentIndeks student = studentRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        // Provera da li već postoji upis za istog studenta i istu godinu
        Optional<UpisGodine> existing = repository.findByStudentIndeksAndGodinaStudija(student, request.getGodinaStudija());
        if (existing.isPresent()) {
            throw new RuntimeException("Upis godine već postoji za ovog studenta i godinu studija");
        }

        Set<Predmet> predmeti = request.getPrenetiPredmetiIds() != null ?
                request.getPrenetiPredmetiIds().stream()
                        .map(id -> predmetRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Predmet ne postoji: " + id)))
                        .collect(Collectors.toSet())
                : Set.of();

        UpisGodine upis = Converters.toUpisGodine(request, student, predmeti);
        return repository.save(upis);
    }

    @Transactional
    public UpisGodineResponse upisiStudentaNaGodinu(Long studentIndeksId, int godinaStudija, Set<Long> predmetIds) {
        StudentIndeks indeks = studentRepository.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        // Provera duplikata
        Optional<UpisGodine> existing = repository.findByStudentIndeksAndGodinaStudija(indeks, godinaStudija);
        if (existing.isPresent()) {
            throw new RuntimeException("Upis godine već postoji za ovog studenta i godinu studija");
        }

        // Kreiraj novi upis
        UpisGodine upis = new UpisGodine();
        upis.setStudentIndeks(indeks);
        upis.setGodinaStudija(godinaStudija);
        upis.setDatum(LocalDate.now());
        upis.setNapomena("Prvi upis za godinu");
        upis.setPrenetiPredmeti(Set.of());

        UpisGodine savedUpis = repository.save(upis);

        // Kreiranje SlusaPredmet za svaki predmet
        for (Long predmetId : predmetIds) {
            DrziPredmet drzi = drziPredmetRepository.findByPredmetId(predmetId)
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
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

 */
}
