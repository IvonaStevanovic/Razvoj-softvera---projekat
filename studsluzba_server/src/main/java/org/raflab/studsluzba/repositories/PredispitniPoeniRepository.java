package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitneObaveze;
import org.raflab.studsluzba.model.PredispitniPoeni;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredispitniPoeniRepository extends JpaRepository<PredispitniPoeni, Long> {
    /// Svi poeni određenog studenta
    @Query("select p from PredispitniPoeni p where p.studentIndeks.id = :idStudenta")
    List<PredispitniPoeni> findByStudent(Long idStudenta);

    /// Svi poeni za određenu predispitnu obavezu
    @Query("select p from PredispitniPoeni p where p.predispitnaObaveza.id = :idObaveze")
    List<PredispitniPoeni> findByPredispitnaObaveza(Long idObaveze);

    /// Svi poeni određenog studenta za određeni predmet u školskoj godini
    @Query("select p from PredispitniPoeni p where p.studentIndeks.id = :idStudenta and p.slusaPredmet.id = :idSlusaPredmet")
    List<PredispitniPoeni> findByStudentAndSlusaPredmet(Long idStudenta, Long idSlusaPredmet);

    /// Svi poeni u određenoj školskoj godini
    @Query("select p from PredispitniPoeni p where p.skolskaGodina.id = :idGodine")
    List<PredispitniPoeni> findBySkolskaGodina(Long idGodine);

}