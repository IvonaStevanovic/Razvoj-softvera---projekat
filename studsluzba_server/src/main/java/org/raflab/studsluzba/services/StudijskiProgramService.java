package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.mappers.StudijskiProgramConverter;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.repositories.VrstaStudijaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudijskiProgramService {

    @Autowired
    private StudijskiProgramRepository studProgramRepo;

    @Autowired
    private VrstaStudijaRepository vrstaStudijaRepo;

    // 1. Pretraga po ID
    public StudijskiProgramResponse getById(Long id) {
        return studProgramRepo.findById(id)
                .map(StudijskiProgramConverter::toResponse)
                .orElseThrow(() -> new RuntimeException("Studijski program nije pronađen!"));
    }

    // 2. Izlistavanje svih
    public List<StudijskiProgramResponse> getAll() {
        List<StudijskiProgram> programi = (List<StudijskiProgram>) studProgramRepo.findAll();
        return programi.stream()
                .map(StudijskiProgramConverter::toResponse)
                .collect(Collectors.toList());
    }

    // 3. Dodavanje novog
    public StudijskiProgramResponse save(StudijskiProgramRequest request) {
        if (studProgramRepo.existsByOznaka(request.getOznaka())) {
            throw new RuntimeException("Studijski program sa oznakom " + request.getOznaka() + " već postoji!");
        }

        VrstaStudija vs = vrstaStudijaRepo.findById(request.getVrstaStudijaId())
                .orElseThrow(() -> new RuntimeException("Vrsta studija nije pronađena"));

        StudijskiProgram sp = StudijskiProgramConverter.toEntity(request, vs);
        return StudijskiProgramConverter.toResponse(studProgramRepo.save(sp));
    }

    // 4. Brisanje po ID - Usklađeno sa proverom integriteta
    public void delete(Long id) {
        // 1. Pronalaženje programa ili bacanje greške ako ne postoji
        StudijskiProgram sp = studProgramRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Studijski program ne postoji sa ID: " + id));

        // 2. Provera povezanosti (Integritet baze)
        // Prema tvom modelu StudijskiProgram, veza ka predmetima se zove 'predmeti'
        if (sp.getPredmeti() != null && !sp.getPredmeti().isEmpty()) {
            throw new RuntimeException("Ne možete obrisati program jer su za njega vezani predmeti.");
        }

        // 3. Brisanje ako je "čist"
        studProgramRepo.delete(sp);
    }

    @Transactional(readOnly = true) // Važno za stabilnost čitanja
    public Double getProsecnaOcenaProgramazaPeriod(Long programId, int godinaOd, int godinaDo) {
        StudijskiProgram sp = studProgramRepo.findById(programId)
                .orElseThrow(() -> new RuntimeException("Studijski program nije pronađen"));

        // Hibernate.initialize(sp.getPredmeti()); // Opciono, ako i dalje puca

        List<Integer> sveOcene = new ArrayList<>();

        // Koristimo obične petlje umesto streama radi lakšeg debagovanja i transakcione sigurnosti
        if (sp.getPredmeti() != null) {
            for (Predmet p : sp.getPredmeti()) {
                if (p.getSlusaPredmetSet() != null) {
                    for (SlusaPredmet sl : p.getSlusaPredmetSet()) {
                        if (sl.getIzlazakNaIspite() != null) {
                            for (IzlazakNaIspit iz : sl.getIzlazakNaIspite()) {
                                // Provera godine ispita
                                if (iz.getPrijavaIspita() != null && iz.getPrijavaIspita().getIspit() != null) {
                                    int godina = iz.getPrijavaIspita().getIspit().getDatumOdrzavanja().getYear();
                                    if (godina >= godinaOd && godina <= godinaDo && iz.getOcena() > 5) {
                                        sveOcene.add(iz.getOcena());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return sveOcene.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }
}
