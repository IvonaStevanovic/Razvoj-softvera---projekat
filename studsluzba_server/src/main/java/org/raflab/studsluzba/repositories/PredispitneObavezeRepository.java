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
/*
    @Query("select p from PredispitneObaveze p where p.drziPredmet.id = :idDrziPredmeta")
    List<PredispitneObaveze> findByDrziPredmet(Long idDrziPredmeta);

    @Query("select p from PredispitneObaveze p where p.skolskaGodina.id = :idGodine")
    List<PredispitneObaveze> findBySkolskaGodina(Long idGodine);

    @Query("select p from PredispitneObaveze p where p.vrsta = :vrsta")
    List<PredispitneObaveze> findByVrsta(String vrsta);

    /// Za proveru duplikata: isti predmet, ista školska godina i ista vrsta
    @Query("select p from PredispitneObaveze p where p.drziPredmet.id = :idDrziPredmeta " +
            "and p.skolskaGodina.id = :idGodine and lower(p.vrsta) = lower(:vrsta)")
    Optional<PredispitneObaveze> findDuplicate(Long idDrziPredmeta, Long idGodine, String vrsta);

 */
}
