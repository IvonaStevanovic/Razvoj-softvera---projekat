package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.PredispitniPoeni;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PredispitniPoeniRepository extends JpaRepository<PredispitniPoeni, Long> {

}