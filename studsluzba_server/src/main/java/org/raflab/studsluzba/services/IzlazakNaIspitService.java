package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.repositories.IspitRepository;
import org.raflab.studsluzba.repositories.IzlazakNaIspitRepository;
import org.raflab.studsluzba.repositories.SlusaPredmetRepository;
import org.raflab.studsluzba.repositories.StudentIndeksRepository;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IzlazakNaIspitService {
    private final IzlazakNaIspitRepository repository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final IspitRepository ispitRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;

    @Transactional
    public IzlazakNaIspit save(IzlazakNaIspitRequest request) {
        if (request.getStudentIndeksId() == null) {
            throw new IllegalArgumentException("studentIndeksId ne sme biti null");
        }
        if (request.getIspitId() == null) {
            throw new IllegalArgumentException("ispitId ne sme biti null");
        }
        if (request.getSlusaPredmet() == null) {
            throw new IllegalArgumentException("slusaPredmetId ne sme biti null");
        }

        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks sa ID " + request.getStudentIndeksId() + " ne postoji"));

        Ispit ispit = ispitRepository.findById(request.getIspitId())
                .orElseThrow(() -> new RuntimeException("Ispit sa ID " + request.getIspitId() + " ne postoji"));

        SlusaPredmet slusaPredmet = slusaPredmetRepository.findById(request.getSlusaPredmet())
                .orElseThrow(() -> new RuntimeException("SlusaPredmet sa ID " + request.getSlusaPredmet() + " ne postoji"));

        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setStudentIndeks(student);
        izlazak.setIspit(ispit);
        izlazak.setSlusaPredmet(slusaPredmet);
        izlazak.setOstvarenoNaIspitu(request.getOstvarenoNaIspitu());
        izlazak.setNapomena(request.getNapomena());
        izlazak.setPonistio(request.isPonistio());
        izlazak.setIzasao(request.isIzasao());

        return repository.save(izlazak);
    }
}