package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Grupa;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrupaRepository extends JpaRepository<Grupa, Long> {
    /// sve grupe na odredjenom stud. programu
    @Query("select g from Grupa g where g.studijskiProgram.id = :idPrograma")
    List<Grupa> findByStudijskiProgram(Long idPrograma);

    @Query("select distinct g from Grupa g left join fetch g.predmeti")
    List<Grupa> findAllWithPredmeti();


    /// sve grupe koje slusaju odredjeni predmet
    @Query("select g from Grupa g join g.predmeti p where p.id = :idPredmeta")
    List<Grupa> findByPredmet(Long idPredmeta);

    /// svi predmeti iz grupe
    @Query("select p from Grupa g join g.predmeti p where g.id = :idGrupe")
    List<Predmet> findPredmetiZaGrupu(Long idGrupe);

    @Query("select g from Grupa g where g.studijskiProgram.id = :studijskiProgramId and g.predmeti = :predmeti")
    Optional<Grupa> findByStudijskiProgramAndPredmeti(Long studijskiProgramId, List<Predmet> predmeti);

}
