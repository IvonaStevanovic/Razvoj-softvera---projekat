package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitneObaveze;
import org.raflab.studsluzba.model.PredispitniPoeni;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PredispitniPoeniRepository extends JpaRepository<PredispitniPoeni, Long> {

    // pronađi sve predispitne poene za studenta po predmetu i školskoj godini
    @Query("SELECT p FROM PredispitniPoeni p " +
            "WHERE p.studentIndeks.id = :studentIndeksId " +
            "AND p.predispitnaObaveza.drziPredmet.predmet.id = :predmetId " +
            "AND p.skolskaGodina.id = :skolskaGodinaId")
    List<PredispitniPoeni> findByStudentPredmetGodina(
            @Param("studentIndeksId") Long studentIndeksId,
            @Param("predmetId") Long predmetId,
            @Param("skolskaGodinaId") Long skolskaGodinaId
    );

    // opcionalno, pronađi po studentu i slusaPredmet
    @Query("SELECT p FROM PredispitniPoeni p " +
            "WHERE p.studentIndeks = :student AND p.slusaPredmet = :slusaPredmet")
    List<PredispitniPoeni> findByStudentIndeksAndSlusaPredmet(
            @Param("student") StudentIndeks student,
            @Param("slusaPredmet") SlusaPredmet slusaPredmet
    );
    List<PredispitniPoeni> findByStudentIndeks(StudentIndeks studentIndeks);

    /*
    @Query("select p from PredispitniPoeni p where p.studentIndeks.id = :idStudenta")
    List<PredispitniPoeni> findByStudent(Long idStudenta);

    @Query("select p from PredispitniPoeni p where p.predispitnaObaveza.id = :idObaveze")
    List<PredispitniPoeni> findByPredispitnaObaveza(Long idObaveze);

    @Query("select p from PredispitniPoeni p where p.studentIndeks.id = :idStudenta and p.slusaPredmet.id = :idSlusaPredmet")
    List<PredispitniPoeni> findByStudentAndSlusaPredmet(Long idStudenta, Long idSlusaPredmet);

    @Query("select p from PredispitniPoeni p where p.skolskaGodina.id = :idGodine")
    List<PredispitniPoeni> findBySkolskaGodina(Long idGodine);

    /// Provera duplikata: isti student, ista predispitna obaveza, isti SlusaPredmet i ista školska godina
    @Query("select p from PredispitniPoeni p where p.studentIndeks.id = :studentId " +
            "and p.predispitnaObaveza.id = :obavezaId " +
            "and p.slusaPredmet.id = :slusaId " +
            "and p.skolskaGodina.id = :godinaId")
    Optional<PredispitniPoeni> findDuplicate(Long studentId, Long obavezaId, Long slusaId, Long godinaId);

 */
}
