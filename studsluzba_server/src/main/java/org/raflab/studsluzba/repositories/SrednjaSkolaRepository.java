package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.SkolskaGodina;
import org.raflab.studsluzba.model.SrednjaSkola;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SrednjaSkolaRepository extends JpaRepository<SrednjaSkola, Long> {
    /// Sve škole iz određenog mesta
    @Query("select s from SrednjaSkola s where lower(s.mesto) = lower(:mesto)")
    List<SrednjaSkola> findByMesto(String mesto);

    /// Sve škole određene vrste (npr. gimnazija, tehnička...)
    @Query("select s from SrednjaSkola s where lower(s.vrsta) = lower(:vrsta)")
    List<SrednjaSkola> findByVrsta(String vrsta);

    /// Pretraga po delu naziva (case-insensitive)
    @Query("select s from SrednjaSkola s where lower(s.naziv) like lower(concat('%', :naziv, '%'))")
    List<SrednjaSkola> findByNazivContaining(String naziv);

    @Query("select s from SkolskaGodina s where s.naziv = :naziv")
    List<SkolskaGodina> findByNaziv(String naziv);

    @Query("select s from SkolskaGodina s where s.aktivna = true")
    List<SkolskaGodina> findAktivne();


}
