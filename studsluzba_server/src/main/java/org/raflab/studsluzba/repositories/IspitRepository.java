package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.controllers.response.ProsekPoGodiniResponse;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IspitRepository extends JpaRepository<Ispit, Long> {
    @Query("select case when count(i) > 0 then true else false end " +
            "from Ispit i " +
            "where i.datumOdrzavanja = :datum " +
            "and i.drziPredmet.id = :drziPredmetId " +
            "and i.ispitniRok.id = :rokId")
    boolean existsByDatumOdrzavanjaAndDrziPredmetIdAndIspitniRokId(
            @Param("datum") LocalDate datumOdrzavanja,
            @Param("drziPredmetId") Long drziPredmetId,
            @Param("rokId") Long ispitniRokId
    );
    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END " +
            "FROM Ispit i " +
            "WHERE i.datumOdrzavanja = :datum " +
            "AND i.predmet.id = :predmetId " +
            "AND i.drziPredmet.nastavnik.id = :nastavnikId " +
            "AND i.ispitniRok.id = :rokId")
    boolean existsByDatumPredmetNastavnikRok(@Param("datum") LocalDate datum,
                                             @Param("predmetId") Long predmetId,
                                             @Param("nastavnikId") Long nastavnikId,
                                             @Param("rokId") Long rokId);

    @Query("SELECT new org.raflab.studsluzba.controllers.response.ProsekPoGodiniResponse(" +
            "i.ispitniRok.skolskaGodina.naziv, " +
            "AVG(CAST(CASE " +
            "WHEN (iz.poeniIspit + COALESCE(iz.poeniPredispitne, 0)) >= 91 THEN 10 " +
            "WHEN (iz.poeniIspit + COALESCE(iz.poeniPredispitne, 0)) >= 81 THEN 9 " +
            "WHEN (iz.poeniIspit + COALESCE(iz.poeniPredispitne, 0)) >= 71 THEN 8 " +
            "WHEN (iz.poeniIspit + COALESCE(iz.poeniPredispitne, 0)) >= 61 THEN 7 " +
            "ELSE 6 END as double))) " +
            "FROM IzlazakNaIspit iz " +
            "JOIN iz.prijavaIspita p " +
            "JOIN p.ispit i " +
            "WHERE i.predmet.id = :predmetId " +
            "AND (iz.poeniIspit + COALESCE(iz.poeniPredispitne, 0)) >= 51 " +
            "GROUP BY i.ispitniRok.skolskaGodina.naziv")
    List<ProsekPoGodiniResponse> findAverageGradeByYear(@Param("predmetId") Long predmetId);
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


