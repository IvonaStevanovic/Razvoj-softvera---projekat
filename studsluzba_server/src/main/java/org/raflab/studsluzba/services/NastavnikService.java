package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.response.NastavnikResponse;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NastavnikService {

    private final NastavnikRepository nastavnikRepository;

    public Nastavnik save(Nastavnik n) {
        return nastavnikRepository.save(n);
    }

    public Optional<Nastavnik> findById(Long id) {
        return nastavnikRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<NastavnikResponse> findAllResponses() {
        List<Nastavnik> nastavnici = nastavnikRepository.findAllWithZvanja();
        return nastavnici.stream()
                .map(EntityMappers::toNastavnikResponse)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<NastavnikResponse> findByImeAndPrezime(String ime, String prezime) {
        return nastavnikRepository.findByImeAndPrezime(ime, prezime).stream()
                .map(EntityMappers::toNastavnikResponse)
                .collect(Collectors.toList());
    }
}
