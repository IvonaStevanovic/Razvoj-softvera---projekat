package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IspitRepository extends JpaRepository<Ispit, Long> {
    /// Svi ispiti za određeni ispitni rok
    @Query("select i from Ispit i where i.ispitniRok.id = :idRoka")
    List<Ispit> findByIspitniRok(Long idRoka);

    ///  Svi ispiti koje drži određeni nastavnik
    @Query("select i from Ispit i where i.nastavnik.id = :idNastavnika")
    List<Ispit> findByNastavnik(Long idNastavnika);

    /// Svi ispiti za određeni predmet
    @Query("select i from Ispit i where i.predmet.id = :idPredmeta")
    List<Ispit> findByPredmet(Long idPredmeta);
}

