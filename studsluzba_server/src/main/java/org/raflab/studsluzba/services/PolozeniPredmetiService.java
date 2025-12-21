package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.PolozeniPredmetiRequest;
import org.raflab.studsluzba.controllers.response.PolozeniPredmetiResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PolozeniPredmetiService {
    @Autowired
    private  StudentIndeksRepository indeksRepo;
    @Autowired
    private  PolozeniPredmetiRepository polozeniPredmetRepo;
    @Autowired
    private  PredmetRepository predmetRepo;
    @Autowired
    private  IzlazakNaIspitRepository izlazakRepo;
    @Autowired
    private  SlusaPredmetRepository slusaPredmetRepo;

    @Transactional(readOnly = true)
    public Page<PolozeniPredmetiResponse> getPolozeniPredmetiPoBrojuIndeksa(
            Integer brojIndeksa, Pageable pageable) {

        StudentIndeks indeks = indeksRepo.findByBroj(brojIndeksa)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student sa datim brojem indeksa ne postoji"));

        Page<PolozeniPredmeti> polozeniPage = polozeniPredmetRepo.findByStudentIndeks(indeks, pageable);

        return polozeniPage.map(pp -> {
            PolozeniPredmetiResponse dto = new PolozeniPredmetiResponse();
            dto.setId(pp.getId());
            dto.setStudentIndeksId(indeks.getId());
            dto.setStudentImePrezime(
                    indeks.getStudent().getIme() + " " + indeks.getStudent().getPrezime()
            );
            dto.setPredmetId(pp.getPredmet().getId());
            dto.setPredmetNaziv(pp.getPredmet().getNaziv());
            dto.setOcena(pp.getOcena());
            dto.setPriznat(pp.isPriznat());
            dto.setIzlazakNaIspitId(pp.getIzlazakNaIspit() != null ? pp.getIzlazakNaIspit().getId() : null);
            return dto;
        });
    }


    @Transactional
    public PolozeniPredmetiResponse dodajPolozeniPredmet(PolozeniPredmetiRequest request) {

        StudentIndeks indeks = indeksRepo.findById(request.getStudentIndeksId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji")
                );

        Predmet predmet = predmetRepo.findById(request.getPredmetId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Predmet ne postoji")
                );

        PolozeniPredmeti pp = new PolozeniPredmeti();
        pp.setStudentIndeks(indeks);
        pp.setPredmet(predmet);
        pp.setOcena(request.getOcena());
        pp.setPriznat(request.isPriznat());
        pp.setDatumPolaganja(LocalDate.now());  // ili možeš dodati datum u request

        if (request.getIzlazakNaIspitId() != null) {
            IzlazakNaIspit izlazak = izlazakRepo.findById(request.getIzlazakNaIspitId())
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, "Izlazak na ispit ne postoji")
                    );
            pp.setIzlazakNaIspit(izlazak);
        }

        PolozeniPredmeti sacuvan = polozeniPredmetRepo.save(pp);

        // mapiranje u response DTO
        PolozeniPredmetiResponse response = new PolozeniPredmetiResponse();
        response.setId(sacuvan.getId());
        response.setStudentIndeksId(indeks.getId());
        response.setStudentImePrezime(
                indeks.getStudent().getIme() + " " + indeks.getStudent().getPrezime()
        );
        response.setPredmetId(predmet.getId());
        response.setPredmetNaziv(predmet.getNaziv());
        response.setOcena(sacuvan.getOcena());
        response.setPriznat(sacuvan.isPriznat());
        response.setIzlazakNaIspitId(
                sacuvan.getIzlazakNaIspit() != null ? sacuvan.getIzlazakNaIspit().getId() : null
        );

        return response;
    }

    @Transactional(readOnly = true)
    public Page<NepolozeniPredmetDTO> getNepolozeniPredmeti(Long studentIndeksId, Pageable pageable) {

        // 1. Traži indeks
        StudentIndeks indeks = indeksRepo.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        // 2. Svi predmeti koje student sluša
        List<SlusaPredmet> slusaPredmeti = slusaPredmetRepo.findByStudentIndeks(indeks);

        // 3. Svi položeni predmeti
        List<PolozeniPredmeti> polozeniPredmeti = polozeniPredmetRepo.findByStudentIndeks(indeks);

        // 4. ID-evi položenih predmeta
        List<Long> polozeniIds = polozeniPredmeti.stream()
                .map(pp -> pp.getPredmet().getId())
                .collect(Collectors.toList());

        // 5. Filtriranje nepoloženih i mapiranje u DTO
        List<NepolozeniPredmetDTO> nepolozeni = slusaPredmeti.stream()
                .filter(sp -> !polozeniIds.contains(sp.getDrziPredmet().getPredmet().getId()))
                .map(sp -> {
                    Predmet predmet = sp.getDrziPredmet().getPredmet();

                    NepolozeniPredmetDTO dto = new NepolozeniPredmetDTO();
                    dto.setId(sp.getId());
                    dto.setSifraPredmeta(predmet.getSifra());
                    dto.setNazivPredmeta(predmet.getNaziv());
                    dto.setEspb(predmet.getEspb());

                    if (sp.getDrziPredmet().getNastavnik() != null) {
                        dto.setImeNastavnika(sp.getDrziPredmet().getNastavnik().getIme());
                        dto.setPrezimeNastavnika(sp.getDrziPredmet().getNastavnik().getPrezime());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        // 6. Paginacija ručno
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), nepolozeni.size());
        List<NepolozeniPredmetDTO> pageContent = start > end ? new ArrayList<>() : nepolozeni.subList(start, end);

        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, nepolozeni.size());
    }










    /*
    private final PolozeniPredmetiRepository repository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final PredmetRepository predmetRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;

    @Transactional
    public PolozeniPredmeti save(PolozeniPredmetiRequest request) {
        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new RuntimeException("StudentIndeks sa ID " + request.getStudentIndeksId() + " ne postoji"));

        Predmet predmet = predmetRepository.findById(request.getPredmetId())
                .orElseThrow(() -> new RuntimeException("Predmet sa ID " + request.getPredmetId() + " ne postoji"));

        final IzlazakNaIspit izlazak = request.getIzlazakNaIspitId() != null
                ? izlazakNaIspitRepository.findById(request.getIzlazakNaIspitId())
                .orElseThrow(() -> new RuntimeException("IzlazakNaIspit sa ID " + request.getIzlazakNaIspitId() + " ne postoji"))
                : null;

        // 🔹 Provera duplikata
        Optional<PolozeniPredmeti> existing = repository.findExisting(
                student.getId(),
                predmet.getId(),
                izlazak != null ? izlazak.getId() : null
        );

        if (existing.isPresent()) {
            throw new RuntimeException("Položeni predmet za ovog studenta i predmet već postoji" +
                    (izlazak != null ? " za dati izlazak na ispit" : ""));
        }

        // Kreiranje novog unosa
        PolozeniPredmeti polozeni = new PolozeniPredmeti();
        polozeni.setStudentIndeks(student);
        polozeni.setPredmet(predmet);
        polozeni.setOcena(request.getOcena());
        polozeni.setPriznat(request.isPriznat());
        polozeni.setIzlazakNaIspit(izlazak);

        return repository.save(polozeni);
    }

    public List<PolozeniPredmeti> getAll() {
        return repository.findAll();
    }

    public PolozeniPredmeti getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("PolozeniPredmeti sa ID " + id + " ne postoji"));
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

     */
}
