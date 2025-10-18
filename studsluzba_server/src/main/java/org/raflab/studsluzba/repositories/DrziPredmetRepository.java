package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.DrziPredmet;
import org.raflab.studsluzba.model.Predmet;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrziPredmetRepository extends CrudRepository<DrziPredmet, Long> {
	
	@Query("select dp.predmet from DrziPredmet dp where dp.nastavnik.id = :idNastavnika")
	List<Predmet> getPredmetiZaNastavnikaUAktivnojSkolskojGodini(Long idNastavnika);
	
	@Query("select dp from DrziPredmet dp where dp.nastavnik.id = :idNastavnik "
			+ "and dp.predmet.id = :idPredmet")
	DrziPredmet getDrziPredmetNastavnikPredmet(Long idPredmet, Long idNastavnik);

}
