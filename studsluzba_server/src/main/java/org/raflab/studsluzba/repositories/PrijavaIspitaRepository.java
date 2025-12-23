package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.PrijavaIspita;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrijavaIspitaRepository extends JpaRepository<PrijavaIspita, Long> {

    List<PrijavaIspita> findByIspit(Ispit ispit);

    List<PrijavaIspita> findByStudentIndeks(StudentIndeks studentIndeks);

    @Query(" SELECT COUNT(p) FROM PrijavaIspita p WHERE p.studentIndeks.id = :studentIndeksId AND p.ispit.predmet.id = :predmetId")
    Long countPokusajaPolaganja(
            @Param("studentIndeksId") Long studentIndeksId,
            @Param("predmetId") Long predmetId
    );
    List<PrijavaIspita> findByStudentIndeksAndIspit(StudentIndeks studentIndeks, Ispit ispit);


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
