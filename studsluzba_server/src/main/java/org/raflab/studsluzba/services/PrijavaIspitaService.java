package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.PrijavaIspitaRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PrijavaIspitaService {

    private final PrijavaIspitaRepository repository;
    private final IspitRepository ispitRepository;
    private final StudentIndeksRepository studentIndeksRepository;

    public List<PrijavaIspita> findAll() {
        return repository.findAll();
    }

    public PrijavaIspita findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public PrijavaIspita save(PrijavaIspitaRequest request) {
        Ispit ispit = ispitRepository.findById(request.getIspitId()).orElse(null);
        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId()).orElse(null);
        PrijavaIspita p = Converters.toPrijavaIspita(request, ispit, student);
        return repository.save(p);
    }

    @Transactional
    public PrijavaIspita update(Long id, PrijavaIspitaRequest request) {
        PrijavaIspita existing = repository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setDatumPrijave(request.getDatumPrijave());
        existing.setIspit(ispitRepository.findById(request.getIspitId()).orElse(null));
        existing.setStudentIndeks(studentIndeksRepository.findById(request.getStudentIndeksId()).orElse(null));

        return repository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<PrijavaIspita> findByStudent(Long studentId) {
        return repository.findByStudent(studentId);
    }

    public List<PrijavaIspita> findByIspit(Long ispitId) {
        return repository.findByIspit(ispitId);
    }

    public PrijavaIspita findByStudentAndIspit(Long studentId, Long ispitId) {
        return repository.findByStudentAndIspit(studentId, ispitId);
    }
}
