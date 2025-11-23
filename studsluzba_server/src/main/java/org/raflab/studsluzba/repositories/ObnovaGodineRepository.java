package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.ObnovaGodine;
import org.raflab.studsluzba.model.StudentIndeks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObnovaGodineRepository extends JpaRepository<ObnovaGodine, Long> {


    Optional<ObnovaGodine> findByStudentIndeksIdAndGodinaStudija(Long studentIndeksId, int godinaStudija);

    @Query("SELECT o FROM ObnovaGodine o JOIN FETCH o.predmetiKojeUpisuje JOIN FETCH o.studentIndeks")
    List<ObnovaGodine> findAllWithPredmetiAndStudent();

    @Query("select o from ObnovaGodine o where o.studentIndeks.id = :idStudenta")
    List<ObnovaGodine> findByStudent(Long idStudenta);

    @Query("select o from ObnovaGodine o where o.godinaStudija = :godina")
    List<ObnovaGodine> findByGodinaStudija(Integer godina);

    @Query("select o from ObnovaGodine o join o.predmetiKojeUpisuje p where p.id = :idPredmeta")
    List<ObnovaGodine> findByPredmet(Long idPredmeta);
}
