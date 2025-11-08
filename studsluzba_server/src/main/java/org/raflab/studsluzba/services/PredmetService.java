package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.repositories.PredmetRepository;
import org.raflab.studsluzba.repositories.StudijskiProgramRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredmetService {
    private final PredmetRepository predmetRepository;
    private final StudijskiProgramRepository studProgramRepository;

    public PredmetService(PredmetRepository predmetRepository, StudijskiProgramRepository studProgramRepository) {
        this.predmetRepository = predmetRepository;
        this.studProgramRepository = studProgramRepository;
    }

    // Entity -> ResponseDTO
    private PredmetResponse toResponseDTO(Predmet predmet) {
        PredmetResponse dto = new PredmetResponse();
        dto.setId(predmet.getId());
        dto.setSifra(predmet.getSifra());
        dto.setNaziv(predmet.getNaziv());
        dto.setOpis(predmet.getOpis());
        dto.setEspb(predmet.getEspb());
        dto.setFondPredavanja(predmet.getFondPredavanja());
        dto.setFondVezbe(predmet.getFondVezbe());
        dto.setObavezan(predmet.isObavezan());
        if (predmet.getStudProgram() != null) {
            dto.setStudProgramNaziv(predmet.getStudProgram().getNaziv());
        }
        return dto;
    }

    // RequestDTO -> Entity
    private Predmet fromRequestDTO(PredmetRequest dto) {
        Predmet predmet = new Predmet();
        predmet.setSifra(dto.getSifra());
        predmet.setNaziv(dto.getNaziv());
        predmet.setOpis(dto.getOpis());
        predmet.setEspb(dto.getEspb());
        predmet.setFondPredavanja(dto.getFondPredavanja());
        predmet.setFondVezbe(dto.getFondVezbe());
        predmet.setObavezan(dto.isObavezan());
        if (dto.getStudProgramId() != null) {
            StudijskiProgram sp = studProgramRepository.findById(dto.getStudProgramId())
                    .orElseThrow(() -> new RuntimeException("Studijski program ne postoji"));
            predmet.setStudProgram(sp);
        }
        return predmet;
    }

    public List<PredmetResponse> getAllPredmeti() {
        List<PredmetResponse> dtos = new ArrayList<>();
        for (Predmet p : predmetRepository.findAll()) {
            dtos.add(toResponseDTO(p));
        }
        return dtos;

    }

    public PredmetResponse getPredmetById(Long id) {
        Predmet p = predmetRepository.findById(id).orElseThrow(() -> new RuntimeException("Predmet ne postoji"));
        return toResponseDTO(p);
    }

    public PredmetResponse createPredmet(PredmetRequest dto) {
        Predmet p = fromRequestDTO(dto);
        return toResponseDTO(predmetRepository.save(p));
    }

    public PredmetResponse updatePredmet(Long id, PredmetRequest dto) {
        Predmet p = predmetRepository.findById(id).orElseThrow(() -> new RuntimeException("Predmet ne postoji"));
        p.setSifra(dto.getSifra());
        p.setNaziv(dto.getNaziv());
        p.setOpis(dto.getOpis());
        p.setEspb(dto.getEspb());
        p.setFondPredavanja(dto.getFondPredavanja());
        p.setFondVezbe(dto.getFondVezbe());
        p.setObavezan(dto.isObavezan());
        if (dto.getStudProgramId() != null) {
            StudijskiProgram sp = studProgramRepository.findById(dto.getStudProgramId())
                    .orElseThrow(() -> new RuntimeException("Studijski program ne postoji"));
            p.setStudProgram(sp);
        }
        return toResponseDTO(predmetRepository.save(p));
    }

    public void deletePredmet(Long id) {
        predmetRepository.deleteById(id);
    }
}
