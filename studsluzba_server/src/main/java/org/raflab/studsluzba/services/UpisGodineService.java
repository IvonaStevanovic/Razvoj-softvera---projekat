package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.UpisGodine;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.repositories.UpisGodineRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UpisGodineService {

    private final UpisGodineRepository repository;
    private final StudentIndeksRepository studentRepository;
    private final PredmetRepository predmetRepository;

    public List<UpisGodine> findAll() {
        return repository.findAll();
    }

    public UpisGodine findById(Long id) {
        return repository.findById(id).orElse(null);
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
}
