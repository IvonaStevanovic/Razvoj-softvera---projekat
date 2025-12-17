package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.VisokoskolskaUstanova;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisokoskolskaUstanovaRepository extends JpaRepository<VisokoskolskaUstanova, Long> {
    /*
    /// Pronađi ustanove po mestu
    @Query("select v from VisokoskolskaUstanova v where v.mesto = :mesto")
    List<VisokoskolskaUstanova> findByMesto(String mesto);

    /// Pronađi ustanove po vrsti (npr. fakultet, akademija, visoka škola)
    @Query("select v from VisokoskolskaUstanova v where v.vrsta = :vrsta")
    List<VisokoskolskaUstanova> findByVrsta(String vrsta);

    /// Pronađi ustanovu po nazivu
    @Query("select v from VisokoskolskaUstanova v where v.naziv = :naziv")
    VisokoskolskaUstanova findByNaziv(String naziv);

    /// Pronađi sve ustanove u određenom mestu i određene vrste
    @Query("select v from VisokoskolskaUstanova v where v.mesto = :mesto and v.vrsta = :vrsta")
    List<VisokoskolskaUstanova> findByMestoAndVrsta(String mesto, String vrsta);

     */


}
