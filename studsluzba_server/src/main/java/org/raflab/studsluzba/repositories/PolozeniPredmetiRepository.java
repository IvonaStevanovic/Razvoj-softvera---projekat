package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PolozeniPredmeti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PolozeniPredmetiRepository extends JpaRepository<PolozeniPredmeti, Long> {

}