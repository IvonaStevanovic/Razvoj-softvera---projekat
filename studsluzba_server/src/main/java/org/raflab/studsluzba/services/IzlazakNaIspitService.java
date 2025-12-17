package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.IzlazakNaIspitDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IzlazakNaIspitService {/*
    private final IzlazakNaIspitRepository repository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final IspitRepository ispitRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
    private final PredispitniPoeniRepository predispitniPoeniRepository;
    private final PolozeniPredmetiRepository polozeniPredmetiRepository;

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

        // Provera da li već postoji isti izlazak
        boolean exists = repository.existsByStudentIndeksAndIspitAndSlusaPredmet(student, ispit, slusaPredmet);
        if (exists) {
            throw new IllegalArgumentException("Izlazak na ispit za ovog studenta, ispit i predmet već postoji!");
        }

        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setStudentIndeks(student);
        izlazak.setIspit(ispit);
        izlazak.setSlusaPredmet(slusaPredmet);
        izlazak.setOstvarenoNaIspitu(request.getOstvarenoNaIspitu());
        izlazak.setNapomena(request.getNapomena());
        izlazak.setPonistio(request.isPonistio());
        izlazak.setIzasao(request.isIzasao());

        IzlazakNaIspit saved = repository.save(izlazak);
        processIzlazakNaIspit(saved.getId());
        return saved;
    }



    @Transactional
    public PolozeniPredmeti processIzlazakNaIspit(Long izlazakId) {

        IzlazakNaIspit izlazak = repository.findById(izlazakId)
                .orElseThrow(() -> new RuntimeException("IzlazakNaIspit sa ID " + izlazakId + " ne postoji"));

        StudentIndeks student = izlazak.getStudentIndeks();
        SlusaPredmet slusaPredmet = izlazak.getSlusaPredmet();


        List<PredispitniPoeni> predispitniPoeniList = predispitniPoeniRepository.findByStudentAndSlusaPredmet(
                student.getId(), slusaPredmet.getId()
        );
        int totalPredispitni = predispitniPoeniList.stream()
                .mapToInt(PredispitniPoeni::getPoeni)
                .sum();


        int ukupnoPoeni = totalPredispitni + (izlazak.getOstvarenoNaIspitu() != null ? izlazak.getOstvarenoNaIspitu() : 0);


        if (ukupnoPoeni >= 51) {
            PolozeniPredmeti polozeni = new PolozeniPredmeti();
            polozeni.setStudentIndeks(student);
            polozeni.setPredmet(slusaPredmet.getDrziPredmet().getPredmet());
            polozeni.setIzlazakNaIspit(izlazak);


            int ocena;
            if (ukupnoPoeni >= 91) ocena = 10;
            else if (ukupnoPoeni >= 81) ocena = 9;
            else if (ukupnoPoeni >= 71) ocena = 8;
            else if (ukupnoPoeni >= 61) ocena = 7;
            else ocena = 6;

            polozeni.setOcena(ocena);
            polozeni.setPriznat(true);

            return polozeniPredmetiRepository.save(polozeni);
        }

        return null;
    }

    public Double getProsecnaOcenaZaIspit(Long ispitId) {
        List<IzlazakNaIspit> izlazi = repository.findByIspit(ispitId);
        return izlazi.stream()
                .filter(z -> z.getOstvarenoNaIspitu() != null)
                .mapToInt(IzlazakNaIspit::getOstvarenoNaIspitu)
                .average()
                .orElse(0.0);
    }

    public long brojIzlazakaNaPredmet(Long studentId, Long slusaPredmetId) {
        return repository.findByStudent(studentId).stream()
                .filter(z -> z.getSlusaPredmet().getId().equals(slusaPredmetId))
                .count();
    }
    private int ukupnoPredispitnihPoena(Long studentId, Long slusaPredmetId, Long skolskaGodinaId) {
        List<PredispitniPoeni> poeni = predispitniPoeniRepository.findByStudentAndSlusaPredmet(studentId, slusaPredmetId);
        return poeni.stream()
                .filter(p -> p.getSkolskaGodina() != null && p.getSkolskaGodina().getId().equals(skolskaGodinaId))
                .mapToInt(PredispitniPoeni::getPoeni)
                .sum();
    }

    public double prosecnaOcenaUkupno(Long ispitId) {
        List<IzlazakNaIspit> izlazi = repository.findByIspit(ispitId);
        List<Integer> ocene = new ArrayList<>();
        for(IzlazakNaIspit z : izlazi){
            int predispitni = ukupnoPredispitnihPoena(z.getStudentIndeks().getId(), z.getSlusaPredmet().getId(), z.getSlusaPredmet().getSkolskaGodina().getId());
            int ukupno = predispitni + (z.getOstvarenoNaIspitu() != null ? z.getOstvarenoNaIspitu() : 0);
            if(ukupno >= 51){
                int ocena;
                if(ukupno >= 91) ocena = 10;
                else if(ukupno >= 81) ocena = 9;
                else if(ukupno >= 71) ocena = 8;
                else if(ukupno >= 61) ocena = 7;
                else ocena = 6;
                ocene.add(ocena);
            }
        }
        return ocene.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public List<IzlazakNaIspitDTO> rezultatiIspitaSortirano(Long ispitId) {
        List<IzlazakNaIspit> izlazi = repository.findByIspit(ispitId);
        List<IzlazakNaIspitDTO> dtoLista = new ArrayList<>();

        for (IzlazakNaIspit z : izlazi) {
            int predispitni = ukupnoPredispitnihPoena(
                    z.getStudentIndeks().getId(),
                    z.getSlusaPredmet().getId(),
                    z.getSlusaPredmet().getSkolskaGodina().getId()
            );
            int ukupno = predispitni + (z.getOstvarenoNaIspitu() != null ? z.getOstvarenoNaIspitu() : 0);

            // Uključi samo ako je student položio (ukupno >= 51)
            if (ukupno >= 51) {
                IzlazakNaIspitDTO dto = new IzlazakNaIspitDTO();
                dto.setStudent(z.getStudentIndeks());
                dto.setUkupnoPoeni(ukupno);
                dto.setIspit(z.getIspit());
                dto.setSlusaPredmet(z.getSlusaPredmet());

                dtoLista.add(dto);
            }
        }
        dtoLista.sort((a, b) -> {
            int cmp = a.getStudent().getStudijskiProgram().getNaziv()
                    .compareTo(b.getStudent().getStudijskiProgram().getNaziv());
            if (cmp != 0) return cmp;
            cmp = Integer.compare(a.getStudent().getGodina(), b.getStudent().getGodina());
            if (cmp != 0) return cmp;
            return Integer.compare(a.getStudent().getBroj(), b.getStudent().getBroj());
        });

        return dtoLista;
    }
*/


}