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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UpisGodineService {

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

        // Ručno inicijalizuj prenete predmete (da ne ostanu lazy)
        upisi.forEach(u -> {
            if (u.getPrenetiPredmeti() != null)
                u.getPrenetiPredmeti().size(); // forsira učitavanje
        });

        return upisi;
    }
    @Transactional
    public UpisGodine save(UpisGodineRequest request) {
        StudentIndeks student = studentRepository.findById(request.getStudentIndeksId()).orElse(null);
        Set<Predmet> predmeti = request.getPrenetiPredmetiIds() != null ?
                request.getPrenetiPredmetiIds().stream()
                        .map(id -> predmetRepository.findById(id).orElse(null))
                        .collect(Collectors.toSet())
                : null;

        UpisGodine upis = Converters.toUpisGodine(request, student, predmeti);
        return repository.save(upis);
    }

    @Transactional
    public UpisGodine update(Long id, UpisGodineRequest request) {
        UpisGodine existing = repository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setGodinaStudija(request.getGodinaStudija());
        existing.setDatum(request.getDatum());
        existing.setNapomena(request.getNapomena());
        existing.setStudentIndeks(studentRepository.findById(request.getStudentIndeksId()).orElse(null));

        Set<Predmet> predmeti = request.getPrenetiPredmetiIds() != null ?
                request.getPrenetiPredmetiIds().stream()
                        .map(pid -> predmetRepository.findById(pid).orElse(null))
                        .collect(Collectors.toSet())
                : null;
        existing.setPrenetiPredmeti(predmeti);

        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<UpisGodine> findByStudent(Long studentId) {
        return repository.findByStudentId(studentId);
    }

    public UpisGodine findByStudentAndGodina(Long studentId, Integer godina) {
        return repository.findByStudentAndGodina(studentId, godina);
    }

    public List<UpisGodine> findByGodinaStudija(Integer godina) {
        return repository.findByGodinaStudija(godina);
    }

    public List<UpisGodine> findWithPrenetiPredmeti() {
        return repository.findWithPrenetiPredmeti();
    }

    @Transactional
    public UpisGodineResponse upisiStudentaNaGodinu(Long studentIndeksId, int godinaStudija, Set<Long> predmetIds) {
        StudentIndeks indeks = studentRepository.findById(studentIndeksId)
                .orElseThrow(() -> new RuntimeException("StudentIndeks ne postoji"));

        // Kreiraj novi upis
        UpisGodine upis = new UpisGodine();
        upis.setStudentIndeks(indeks);
        upis.setGodinaStudija(godinaStudija);
        upis.setDatum(LocalDate.now());
        upis.setNapomena("Prvi upis za godinu");
        upis.setPrenetiPredmeti(Set.of());

        // Sačuvaj upis
        UpisGodine savedUpis = repository.save(upis);

        // Kreiranje SlusaPredmet za svaki predmet
        for (Long predmetId : predmetIds) {
            DrziPredmet drzi = drziPredmetRepository.findByPredmetId(predmetId)
                    .stream()
                    .filter(dp -> dp.getSkolskaGodina().isAktivna()) // ako postoji aktivna godina
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

}
