package org.raflab.studsluzba.repositories;

import java.util.List;

import org.raflab.studsluzba.model.Nastavnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NastavnikRepository extends JpaRepository<Nastavnik, Long> {	//	nasljedjene implementacije poput findById i findByAll

	List<Nastavnik> findByEmailIn(List<String> emails);
	
}

