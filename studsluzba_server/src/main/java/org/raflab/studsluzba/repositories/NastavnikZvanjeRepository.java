package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.NastavnikZvanje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NastavnikZvanjeRepository extends JpaRepository<NastavnikZvanje, Long> {
    /// Sva zvanja određenog nastavnika
    @Query("select nz from NastavnikZvanje nz where nz.nastavnik.id = :idNastavnika")
    List<NastavnikZvanje> findByNastavnik(Long idNastavnika);

    /// Sva aktivna zvanja određenog nastavnika
    @Query("select nz from NastavnikZvanje nz where nz.nastavnik.id = :idNastavnika and nz.aktivno = true")
    List<NastavnikZvanje> findActiveByNastavnik(Long idNastavnika);

    /// Sva zvanja u određenoj naučnoj oblasti
    @Query("select nz from NastavnikZvanje nz where nz.naucnaOblast = :oblast")
    List<NastavnikZvanje> findByNaucnaOblast(String oblast);

    /// Sva zvanja u određenoj užoj naučnoj oblasti
    @Query("select nz from NastavnikZvanje nz where nz.uzaNaucnaOblast = :uzaOblast")
    List<NastavnikZvanje> findByUzaNaucnaOblast(String uzaOblast);

    /// Sva zvanja određenog tipa zvanja
    @Query("select nz from NastavnikZvanje nz where nz.zvanje = :zvanje")
    List<NastavnikZvanje> findByZvanje(String zvanje);
}
