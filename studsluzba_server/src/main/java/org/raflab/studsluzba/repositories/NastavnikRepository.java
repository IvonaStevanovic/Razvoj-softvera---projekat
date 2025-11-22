package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Nastavnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NastavnikRepository extends JpaRepository<Nastavnik, Long> {

    @Query("SELECT n FROM Nastavnik n LEFT JOIN FETCH n.zvanja")
    List<Nastavnik> findAllWithZvanja();

    @Query("SELECT sp FROM Nastavnik sp WHERE (:ime IS NULL OR LOWER(sp.ime) LIKE :ime) AND (:prezime IS NULL OR LOWER(sp.prezime) LIKE :prezime)")
    List<Nastavnik> findByImeAndPrezime(String ime, String prezime);
    List<Nastavnik> findByEmailIn(List<String> emails);
    boolean existsByEmail(String email);
}



