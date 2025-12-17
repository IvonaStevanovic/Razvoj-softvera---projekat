package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PrijavaIspita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrijavaIspitaRepository extends JpaRepository<PrijavaIspita, Long> {
    /*
    /// Sve prijave određenog studenta
    @Query("select p from PrijavaIspita p where p.studentIndeks.id = :idStudenta")
    List<PrijavaIspita> findByStudent(Long idStudenta);

    /// Sve prijave za određeni ispit
    @Query("select p from PrijavaIspita p where p.ispit.id = :idIspita")
    List<PrijavaIspita> findByIspit(Long idIspita);

    /// Prijava određenog studenta na određeni ispit (jedinstvena)
    @Query("select p from PrijavaIspita p where p.studentIndeks.id = :idStudenta and p.ispit.id = :idIspita")
    PrijavaIspita findByStudentAndIspit(Long idStudenta, Long idIspita);

     */
}
