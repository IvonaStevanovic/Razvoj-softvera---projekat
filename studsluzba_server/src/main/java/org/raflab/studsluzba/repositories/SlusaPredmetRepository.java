package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SlusaPredmetRepository extends CrudRepository<SlusaPredmet, Long> {


    List<SlusaPredmet> findByStudentIndeks(StudentIndeks studentIndeks);
    Optional<SlusaPredmet> findByStudentIndeksAndDrziPredmet(StudentIndeks studentIndeks, DrziPredmet drziPredmet);
    Optional<SlusaPredmet> findByIdAndStudentIndeks(Long id, StudentIndeks studentIndeks);
/*
    @Query("SELECT sp FROM SlusaPredmet sp WHERE sp.studentIndeks.id = :indeksId AND sp.skolskaGodina.aktivna = true")
    List<SlusaPredmet> getSlusaPredmetForIndeksAktivnaGodina(@Param("indeksId") Long indeksId);

    @Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.predmet.id = :idPredmeta "
			+ "and sp.drziPredmet.nastavnik.id = :idNastavnika  ")
	List<StudentIndeks> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet")
	List<StudentIndeks> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet);
	
	

	 ///TODO dodati slicne operacije koja vracaju sve studente za stud program/ godinu studija koje ne slusaju predmet

	@Query("select si from StudentIndeks si where not exists "
			+ "(select sp from SlusaPredmet sp where sp.studentIndeks=si and sp.drziPredmet.id = :idDrziPredmet) ")
	List<StudentIndeks> getStudentiNeSlusajuDrziPredmet(Long idDrziPredmet);

    /// studenti koji ne slušaju predmet, filtrirano po studijskom programu i godini
    @Query("select si from StudentIndeks si where si.studProgramOznaka = :studProgramOznaka and si.godina = :godina "
            + "and not exists (select sp from SlusaPredmet sp where sp.studentIndeks=si and sp.drziPredmet.id = :idDrziPredmet)")
    List<StudentIndeks> getStudentiNeSlusajuDrziPredmetFilter(String studProgramOznaka, Integer godina, Long idDrziPredmet);

    /// studenti koji slušaju predmet, filtrirano po studijskom programu i godini
    @Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet "
            + "and sp.studentIndeks.studProgramOznaka = :studProgramOznaka and sp.studentIndeks.godina = :godina")
    List<StudentIndeks> getStudentiSlusajuDrziPredmetFilter(String studProgramOznaka, Integer godina, Long idDrziPredmet);

    @Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :studentId and sp.drziPredmet.id = :predmetId and sp.skolskaGodina.id = :godinaId")
    Optional<SlusaPredmet> findByStudentPredmetGodina(@Param("studentId") Long studentId,
                                                      @Param("predmetId") Long predmetId,
                                                      @Param("godinaId") Long godinaId);

    @Query(
            "SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END " +
                    "FROM SlusaPredmet sp " +
                    "WHERE sp.studentIndeks.id = :studentIndeksId " +
                    "AND sp.drziPredmet.id = :drziPredmetId " +
                    "AND sp.skolskaGodina.id = :skolskaGodinaId"
    )
    boolean existsDuplikat(
            @Param("studentIndeksId") Long studentIndeksId,
            @Param("drziPredmetId") Long drziPredmetId,
            @Param("skolskaGodinaId") Long skolskaGodinaId
    );

    @Query("SELECT CASE WHEN COUNT(sp) > 0 THEN true ELSE false END " +
            "FROM SlusaPredmet sp " +
            "WHERE sp.studentIndeks.id = :studentId " +
            "AND sp.drziPredmet.id = :drziPredmetId " +
            "AND sp.skolskaGodina.id = :godinaId")
    boolean existsByStudentDrziPredmetGodina(@Param("studentId") Long studentId,
                                             @Param("drziPredmetId") Long drziPredmetId,
                                             @Param("godinaId") Long godinaId);

*/
}
