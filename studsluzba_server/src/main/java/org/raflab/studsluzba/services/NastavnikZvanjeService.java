package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.NastavnikZvanjeRequest;
import org.raflab.studsluzba.model.Nastavnik;
import org.raflab.studsluzba.model.NastavnikZvanje;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.NastavnikZvanjeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NastavnikZvanjeService {
    private final NastavnikZvanjeRepository repository;
    private final NastavnikRepository nastavnikRepository;

    @Transactional
    public NastavnikZvanje save(NastavnikZvanjeRequest request) {
        Optional<Nastavnik> nastavnik = nastavnikRepository.findById(request.getNastavnikId());
        if (nastavnik.isEmpty()) {
            return null; // nastavnik ne postoji
        }

        if (repository.existsByNastavnikIdAndZvanjeAndAktivnoTrue(request.getNastavnikId(), request.getZvanje())) {
            return null; // već postoji aktivno zvanje tog tipa
        }

        NastavnikZvanje nz = new NastavnikZvanje();
        nz.setDatumIzbora(request.getDatumIzbora());
        nz.setNaucnaOblast(request.getNaucnaOblast());
        nz.setUzaNaucnaOblast(request.getUzaNaucnaOblast());
        nz.setZvanje(request.getZvanje());
        nz.setAktivno(request.isAktivno());
        nz.setNastavnik(nastavnik.get());

        return repository.save(nz);
    }


    @Transactional(readOnly = true)
    public List<NastavnikZvanje> findAll() {
        List<NastavnikZvanje> lista = repository.findAll();
        // inicijalizacija lazy-nastavnika
        lista.forEach(nz -> nz.getNastavnik().getIme());
        return lista;
    }
    public NastavnikZvanje findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("NastavnikZvanje sa ID " + id + " ne postoji"));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
