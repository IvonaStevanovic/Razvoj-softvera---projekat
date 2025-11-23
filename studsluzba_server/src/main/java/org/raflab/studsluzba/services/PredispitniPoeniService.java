package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PredispitniPoeniRequest;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PredispitniPoeniService {

    private final PredispitniPoeniRepository repository;
    private final StudentIndeksRepository studentRepository;
    private final PredispitneObavezeRepository obavezeRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final SkolskaGodinaRepository skolskaGodinaRepository;

    public List<PredispitniPoeni> findAll() {
        return repository.findAll();
    }

    public PredispitniPoeni findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public PredispitniPoeni save(PredispitniPoeniRequest request) {
        StudentIndeks student = studentRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("Student ne postoji"));
        PredispitneObaveze obaveza = obavezeRepository.findById(request.getPredispitnaObavezaId())
                .orElseThrow(() -> new RuntimeException("Predispitna obaveza ne postoji"));
        SlusaPredmet slusa = slusaPredmetRepository.findById(request.getSlusaPredmetId())
                .orElseThrow(() -> new RuntimeException("SlusaPredmet ne postoji"));
        SkolskaGodina godina = skolskaGodinaRepository.findById(request.getSkolskaGodinaId())
                .orElseThrow(() -> new RuntimeException("Skolska godina ne postoji"));

        // 🔹 Provera duplikata
        Optional<PredispitniPoeni> existing = repository.findDuplicate(
                student.getId(),
                obaveza.getId(),
                slusa.getId(),
                godina.getId()
        );
        if (existing.isPresent()) {
            throw new RuntimeException("Predispitni poeni za ovog studenta, predispitnu obavezu, predmet i školsku godinu već postoje");
        }

        PredispitniPoeni p = Converters.toPredispitniPoeni(request, student, obaveza, slusa, godina);
        return repository.save(p);
    }

    @Transactional
    public PredispitniPoeni update(Long id, PredispitniPoeniRequest request) {
        PredispitniPoeni existing = repository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setPoeni(request.getPoeni());
        existing.setStudentIndeks(studentRepository.findById(request.getStudentIndeksId()).orElse(null));
        existing.setPredispitnaObaveza(obavezeRepository.findById(request.getPredispitnaObavezaId()).orElse(null));
        existing.setSlusaPredmet(slusaPredmetRepository.findById(request.getSlusaPredmetId()).orElse(null));
        existing.setSkolskaGodina(skolskaGodinaRepository.findById(request.getSkolskaGodinaId()).orElse(null));

        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<PredispitniPoeni> findByStudent(Long studentId) {
        return repository.findByStudent(studentId);
    }

    public List<PredispitniPoeni> findByPredispitnaObaveza(Long obavezaId) {
        return repository.findByPredispitnaObaveza(obavezaId);
    }

    public List<PredispitniPoeni> findByStudentAndSlusaPredmet(Long studentId, Long slusaId) {
        return repository.findByStudentAndSlusaPredmet(studentId, slusaId);
    }

    public List<PredispitniPoeni> findBySkolskaGodina(Long godinaId) {
        return repository.findBySkolskaGodina(godinaId);
    }
}
