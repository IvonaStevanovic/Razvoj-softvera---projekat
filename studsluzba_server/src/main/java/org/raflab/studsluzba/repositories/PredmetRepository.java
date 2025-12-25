package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PredmetRepository extends JpaRepository<Predmet, Long> {

    @Query("select p from Predmet p where p.studProgram.godinaAkreditacije = :godinaAkreditacije")
    List<Predmet> getPredmetForGodinaAkreditacije(@Param("godinaAkreditacije") Integer godinaAkreditacije);

    List<Predmet> getPredmetsByStudProgramAndObavezan(StudijskiProgram studProgram, boolean obavezan);

    List<Predmet> findByIdIn(List<Long> ids);

    List<Predmet> findByNazivIn(List<String> nazivi);

    Optional<Predmet> findBySifra(String sifra);

    boolean existsBySifra(String sifra);

    List<Predmet> findByStudProgram(StudijskiProgram studProgram);

    @Query("SELECT AVG(pp.ocena) FROM PolozeniPredmeti pp " +
            "WHERE pp.predmet.id = :predmetId " +
            "AND YEAR(pp.datumPolaganja) BETWEEN :odGodine AND :doGodine")
    Double getAverageOcenaForPredmetInRange(@Param("predmetId") Long predmetId,
                                            @Param("odGodine") Integer odGodine,
                                            @Param("doGodine") Integer doGodine);

    @Query("SELECT COUNT(pp) FROM PolozeniPredmeti pp " +
            "WHERE pp.predmet.id = :predmetId " +
            "AND YEAR(pp.datumPolaganja) BETWEEN :odGodine AND :doGodine")
    Long countPolaganjaForPredmetInRange(@Param("predmetId") Long predmetId,
                                         @Param("odGodine") Integer odGodine,
                                         @Param("doGodine") Integer doGodine);
}

	/*
	@Query("select p from Predmet p where p.studProgram.godinaAkreditacije = :godinaAkreditacije")
	List<Predmet> getPredmetForGodinaAkreditacije(Integer godinaAkreditacije);

	List<Predmet> getPredmetsByStudProgramAndObavezan(StudijskiProgram studProgram, boolean obavezan);
    boolean existsBySifra(String sifra);
    List<Predmet> findByIdIn(List<Long> ids);
	List<Predmet> findByNazivIn(List<String> nazivi);
    List<Predmet> findByStudProgram_Id(Long studProgramId);


*/


