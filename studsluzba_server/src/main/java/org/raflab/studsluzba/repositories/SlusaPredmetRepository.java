package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlusaPredmetRepository extends CrudRepository<SlusaPredmet, Long> {
	
	@Query("select sp from SlusaPredmet sp where sp.studentIndeks.id = :indeksId1")
	List<SlusaPredmet> getSlusaPredmetForIndeksAktivnaGodina(Long indeksId);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.predmet.id = :idPredmeta "
			+ "and sp.drziPredmet.nastavnik.id = :idNastavnika  ")
	List<StudentIndeks> getStudentiSlusaPredmetAktivnaGodina(Long idPredmeta, Long idNastavnika);
	
	
	@Query("select sp.studentIndeks from SlusaPredmet sp where sp.drziPredmet.id = :idDrziPredmet")
	List<StudentIndeks> getStudentiSlusaPredmetZaDrziPredmet(Long idDrziPredmet);
	
	
	/*
	 * TODO dodati slicne operacije koja vracaju sve studente za stud program/ godinu studija koje ne slusaju predmet
	 */
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



}
