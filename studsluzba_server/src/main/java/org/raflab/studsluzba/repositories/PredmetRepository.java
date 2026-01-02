package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredmetRepository extends JpaRepository<Predmet, Long> {

    boolean existsBySifra(String sifra);

    // Pronalazi predmete po ID-u studijskog programa (veza u tvom modelu je studProgram)
    List<Predmet> findByStudProgramId(Long studProgramId);

    // Query za prosečnu ocenu - koristi naziv tvoje klase za položene predmete (npr. PolozeniPredmeti)
    @Query("SELECT AVG(pp.ocena) FROM PolozeniPredmeti pp " +
            "WHERE pp.predmet.id = :predmetId " +
            "AND YEAR(pp.datumPolaganja) BETWEEN :odGodine AND :doGodine")
    Double getAverageOcenaForPredmetInRange(@Param("predmetId") Long predmetId,
                                            @Param("odGodine") Integer odGodine,
                                            @Param("doGodine") Integer doGodine);
}