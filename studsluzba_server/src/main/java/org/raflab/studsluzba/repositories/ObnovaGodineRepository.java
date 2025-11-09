package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.ObnovaGodine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObnovaGodineRepository extends JpaRepository<ObnovaGodine, Long> {

    /// Sve obnove za određenog studenta
    @Query("select o from ObnovaGodine o where o.studentIndeks.id = :idStudenta")
    List<ObnovaGodine> findByStudent(Long idStudenta);

    /// Sve obnove za određenu godinu studija
    @Query("select o from ObnovaGodine o where o.godinaStudija = :godina")
    List<ObnovaGodine> findByGodinaStudija(Integer godina);

    /// Sve obnove koje uključuju određeni predmet
    @Query("select o from ObnovaGodine o join o.predmetiKojeUpisuje p where p.id = :idPredmeta")
    List<ObnovaGodine> findByPredmet(Long idPredmeta);

    @Query("select o from ObnovaGodine o left join fetch o.predmetiKojeUpisuje")
    List<ObnovaGodine> findAllWithPredmeti();
}