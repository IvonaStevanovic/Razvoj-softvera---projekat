package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IspitRepository extends JpaRepository<Ispit, Long> {


/*
    @Query("select i from Ispit i where i.ispitniRok.id = :idRoka")
    List<Ispit> findByIspitniRok(Long idRoka);

    @Query("select i from Ispit i where i.nastavnik.id = :idNastavnika")
    List<Ispit> findByNastavnik(Long idNastavnika);

    @Query("select i from Ispit i where i.predmet.id = :idPredmeta")
    List<Ispit> findByPredmet(Long idPredmeta);

    @Query("select i from Ispit i where i.datumOdrzavanja = :datum and i.predmet.id = :predmetId and i.nastavnik.id = :nastavnikId and i.ispitniRok.id = :rokId")
    Optional<Ispit> findDuplicate(LocalDate datum, Long predmetId, Long nastavnikId, Long rokId);

    @Query("select case when count(i) > 0 then true else false end from Ispit i " +
            "where i.datumOdrzavanja = :datum and i.predmet.id = :predmetId " +
            "and i.nastavnik.id = :nastavnikId and i.ispitniRok.id = :rokId")
    boolean existsByDatumOdrzavanjaAndPredmetIdAndNastavnikIdAndIspitniRokId(
            LocalDate datum, Long predmetId, Long nastavnikId, Long rokId
    );*/
}


