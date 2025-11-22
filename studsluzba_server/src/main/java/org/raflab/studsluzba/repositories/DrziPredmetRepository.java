package org.raflab.studsluzba.repositories;

import java.util.List;


import org.raflab.studsluzba.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrziPredmetRepository extends CrudRepository<DrziPredmet, Long> {

    // Postojeće metode
    @Query("select dp.predmet from DrziPredmet dp where dp.nastavnik.id = :idNastavnika")
    List<Predmet> getPredmetiZaNastavnikaUAktivnojSkolskojGodini(Long idNastavnika);

    @Query("select dp from DrziPredmet dp where dp.nastavnik.id = :idNastavnik "
            + "and dp.predmet.id = :idPredmet")
    DrziPredmet getDrziPredmetNastavnikPredmet(
            @Param("idPredmet") Long idPredmet,
            @Param("idNastavnik") Long idNastavnik
    );


    // Nove metode koje servis koristi
    List<DrziPredmet> findAll(); // za getAll

    List<DrziPredmet> findByNastavnikId(Long nastavnikId); // za getByNastavnikId

    List<DrziPredmet> findByPredmetId(Long predmetId); // za getByPredmetId

    List<DrziPredmet> findByPredmetIdIn(List<Long> predmetIds); // za mapiranje predmeta pri POST
}

