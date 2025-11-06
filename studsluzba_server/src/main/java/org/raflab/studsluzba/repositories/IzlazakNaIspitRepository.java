package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IzlazakNaIspit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IzlazakNaIspitRepository extends JpaRepository<IzlazakNaIspit, Long> {
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
}
