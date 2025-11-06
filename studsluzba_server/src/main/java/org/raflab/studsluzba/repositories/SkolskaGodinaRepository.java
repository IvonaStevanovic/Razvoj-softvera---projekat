package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkolskaGodinaRepository extends JpaRepository<SkolskaGodina, Long> {
    /// Sve aktivne školske godine
    @Query("select s from SkolskaGodina s where s.aktivna = true")
    List<SkolskaGodina> findAktivne();

    /// Školska godina po nazivu
    @Query("select s from SkolskaGodina s where s.naziv = :naziv")
    SkolskaGodina findByNaziv(String naziv);
}

