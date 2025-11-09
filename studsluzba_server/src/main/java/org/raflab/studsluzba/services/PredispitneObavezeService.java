package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.model.PredispitneObaveze;
import org.raflab.studsluzba.repositories.PredispitneObavezeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PredispitneObavezeService {

    private final PredispitneObavezeRepository repository;

    public PredispitneObaveze create(PredispitneObaveze obaveza) {
        return repository.save(obaveza);
    }

    public PredispitneObaveze update(Long id, PredispitneObaveze obavezaDetails) {
        PredispitneObaveze obaveza = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predispitna obaveza not found"));
        obaveza.setVrsta(obavezaDetails.getVrsta());
        obaveza.setMaksPoeni(obavezaDetails.getMaksPoeni());
        obaveza.setDrziPredmet(obavezaDetails.getDrziPredmet());
        obaveza.setSkolskaGodina(obavezaDetails.getSkolskaGodina());
        return repository.save(obaveza);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<PredispitneObaveze> findById(Long id) {
        return repository.findById(id);
    }

    public List<PredispitneObaveze> findAll() {
        return repository.findAll();
    }

    public List<PredispitneObaveze> findByDrziPredmet(Long idDrziPredmeta) {
        return repository.findByDrziPredmet(idDrziPredmeta);
    }

    public List<PredispitneObaveze> findBySkolskaGodina(Long idGodine) {
        return repository.findBySkolskaGodina(idGodine);
    }

    public List<PredispitneObaveze> findByVrsta(String vrsta) {
        return repository.findByVrsta(vrsta);
    }
}
