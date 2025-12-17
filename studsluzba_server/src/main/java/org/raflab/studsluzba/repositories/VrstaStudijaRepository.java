package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.VrstaStudija;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VrstaStudijaRepository extends JpaRepository<VrstaStudija, Long> {
    /*
    /// Pronađi vrstu studija po oznaci (npr. "OAS")
    @Query("select v from VrstaStudija v where lower(v.oznaka) = lower(:oznaka)")
    VrstaStudija findByOznaka(String oznaka);

    /// Pronađi vrstu studija po punom nazivu
    @Query("select v from VrstaStudija v where lower(v.punNaziv) = lower(:punNaziv)")
    VrstaStudija findByPunNaziv(String punNaziv);

    /// Pronađi sve vrste studija koje sadrže deo naziva (case-insensitive)
    @Query("select v from VrstaStudija v where lower(v.punNaziv) like lower(concat('%', :deoNaziva, '%'))")
    List<VrstaStudija> findByPunNazivContaining(String deoNaziva);
*/
}
