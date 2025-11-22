package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.PredispitneObaveze;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredispitneObavezeRepository extends JpaRepository<PredispitneObaveze, Long> {
    /// Sve predispitne obaveze za određeni predmet (DrziPredmet)
    @Query("select p from PredispitneObaveze p where p.drziPredmet.id = :idDrziPredmeta")
    List<PredispitneObaveze> findByDrziPredmet(Long idDrziPredmeta);

    /// Sve predispitne obaveze u određenoj školskoj godini
    @Query("select p from PredispitneObaveze p where p.skolskaGodina.id = :idGodine")
    List<PredispitneObaveze> findBySkolskaGodina(Long idGodine);

    /// Sve predispitne obaveze određene vrste (test, kolokvijum, zadatak...)
    @Query("select p from PredispitneObaveze p where p.vrsta = :vrsta")
    List<PredispitneObaveze> findByVrsta(String vrsta);

}