package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.Uplata;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UplataRepository extends CrudRepository<Uplata, Long> {

    List<Uplata> findAll();
    List<Uplata> findByStudentIndeksId(Long studentIndeksId);

    @Query("SELECT SUM(u.iznos) FROM Uplata u WHERE u.studentIndeks.id = :studentIndeksId")
    Double sumIznosByStudentIndeks(@Param("studentIndeksId") Long studentIndeksId);


    @Query("SELECT SUM(u.iznos * u.srednjiKurs) FROM Uplata u WHERE u.studentIndeks.id = :studentIndeksId")
    Double sumIznosDinByStudentIndeks(@Param("studentIndeksId") Long studentIndeksId);
}
