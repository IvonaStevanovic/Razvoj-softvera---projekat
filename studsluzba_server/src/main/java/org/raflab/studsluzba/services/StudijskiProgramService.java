package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.VrstaStudija;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.raflab.studsluzba.repositories.VrstaStudijaRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudijskiProgramService {

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;

    public Optional<StudijskiProgram> findById(Long id) {
        return studijskiProgramRepository.findById(id);
    }

    public StudijskiProgram save(StudijskiProgram program) {
        return studijskiProgramRepository.save(program);
    }

    public void deleteById(Long id) {
        studijskiProgramRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return studijskiProgramRepository.existsById(id);
    }

    public boolean existsByOznaka(String oznaka) {
        return studijskiProgramRepository.existsByOznaka(oznaka);
    }
}

/*
    private final StudijskiProgramRepository studProgramRepo;

    private final VrstaStudijaRepository vrstaStudijaRepo;

    private final EntityMappers entityMappers;

    public StudijskiProgramResponse save(StudijskiProgramRequest request) {
        StudijskiProgram sp = new StudijskiProgram();
        if (request.getVrstaStudijaId() != null) {
            Optional<VrstaStudija> vrsta = vrstaStudijaRepo.findById(request.getVrstaStudijaId());
            vrsta.ifPresent(sp::setVrstaStudija);
        }
        sp.setOznaka(request.getOznaka());
        sp.setNaziv(request.getNaziv());
        sp.setGodinaAkreditacije(request.getGodinaAkreditacije());
        sp.setZvanje(request.getZvanje());
        sp.setTrajanjeGodina(request.getTrajanjeGodina());
        sp.setTrajanjeSemestara(request.getTrajanjeSemestara());
        sp.setUkupnoEspb(request.getUkupnoEspb());

        StudijskiProgram saved = studProgramRepo.save(sp);
        return entityMappers.fromStudijskiProgramToResponse(saved);
    }

    public StudijskiProgramResponse getById(Long id) {
        Optional<StudijskiProgram> sp = studProgramRepo.findById(id);
        return sp.map(entityMappers::fromStudijskiProgramToResponse).orElse(null);
    }

    public List<StudijskiProgramResponse> getAll() {
        List<StudijskiProgram> list = (List<StudijskiProgram>) studProgramRepo.findAll();
        return list.stream()
                .map(entityMappers::fromStudijskiProgramToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        StudijskiProgram sp = studProgramRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Studijski program ne postoji: " + id));

        if ((sp.getPredmeti() != null && !sp.getPredmeti().isEmpty())
                || (sp.getStudenti() != null && !sp.getStudenti().isEmpty())) {
            throw new RuntimeException("Ne možete obrisati program jer postoje povezani predmeti ili studenti.");
        }

        studProgramRepo.delete(sp);
    }

 */


