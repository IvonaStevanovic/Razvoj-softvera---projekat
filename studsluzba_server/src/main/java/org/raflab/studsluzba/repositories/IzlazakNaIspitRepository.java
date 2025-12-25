package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<IzlazakNaIspit, Long> {

    Optional<IzlazakNaIspit> findByPrijavaIspita(PrijavaIspita prijavaIspita);

    boolean existsByPrijavaIspita(PrijavaIspita prijavaIspita);
    List<IzlazakNaIspit> findByStudentIndeks(StudentIndeks studentIndeks);
    @Query("SELECT z FROM IzlazakNaIspit z WHERE z.prijavaIspita.ispit.id = :ispitId")
    List<IzlazakNaIspit> findByIspitId(@Param("ispitId") Long ispitId);
    @Query("SELECT i FROM IzlazakNaIspit i WHERE i.prijavaIspita.ispit.id = :ispitId")
    List<IzlazakNaIspit> findAllByIspitId(@Param("ispitId") Long ispitId);

    /*
    /// Svi izlazi na ispit za određenog studenta
    @Query("select z from IzlazakNaIspit z where z.studentIndeks.id = :idStudenta")
    List<IzlazakNaIspit> findByStudent(Long idStudenta);

    /// Svi izlazi na ispit za određeni ispit
    @Query("select z from IzlazakNaIspit z where z.ispit.id = :idIspita")
    List<IzlazakNaIspit> findByIspit(Long idIspita);

    /// Svi izlazi na ispit za određeni predmet (preko DrziPredmet -> Predmet)
    @Query("select z from IzlazakNaIspit z " +
            "where z.slusaPredmet.drziPredmet.predmet.id = :idPredmeta")
    List<IzlazakNaIspit> findByPredmet(Long idPredmeta);

    /// Svi izlazi na ispit koje student nije poništio
    @Query("select z from IzlazakNaIspit z where z.studentIndeks.id = :idStudenta and z.ponistio = false")
    List<IzlazakNaIspit> findActiveByStudent(Long idStudenta);

    /// Svi izlazi na ispit gde student nije izašao
    @Query("select z from IzlazakNaIspit z where z.izasao = false")
    List<IzlazakNaIspit> findNotYetAppeared();

    /// Provera da li već postoji izlazak na ispit za istog studenta, isti ispit i isti predmet
    boolean existsByStudentIndeksAndIspitAndSlusaPredmet(StudentIndeks studentIndeks, Ispit ispit, SlusaPredmet slusaPredmet);
*/
}
