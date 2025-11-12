package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozeniPredmeti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolozeniPredmetiRepository extends JpaRepository<PolozeniPredmeti, Long> {
    /// Svi položeni predmeti za određenog studenta
    @Query("select p from PolozeniPredmeti p where p.studentIndeks.id = :idStudenta")
    List<PolozeniPredmeti> findByStudent(Long idStudenta);

    /// Svi studenti koji su položili određeni predmet
    @Query("select p from PolozeniPredmeti p where p.predmet.id = :idPredmeta")
    List<PolozeniPredmeti> findByPredmet(Long idPredmeta);

    /// Svi priznati polozeni predmeti
    @Query("select p from PolozeniPredmeti p where p.priznat = true")
    List<PolozeniPredmeti> findPriznati();

    /// Polozeni predmet po izlasku na ispit
    @Query("select p from PolozeniPredmeti p where p.izlazakNaIspit.id = :idIzlaska")
    PolozeniPredmeti findByIzlazakNaIspit(Long idIzlaska);

    @Query("SELECT AVG(p.ocena) FROM PolozeniPredmeti p " +
            "WHERE p.predmet.id = :predmetId " +
            "AND p.studentIndeks.godina BETWEEN :pocetna AND :krajnja")
    Double findProsecnaOcenaZaPredmetURasponuGodina(Long predmetId, int pocetna, int krajnja);

}