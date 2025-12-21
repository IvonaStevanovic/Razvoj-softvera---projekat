package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.UpisGodine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpisGodineRepository extends JpaRepository<UpisGodine, Long> {


    List<UpisGodine> findByStudentIndeksOrderByGodinaStudijaAsc(StudentIndeks studentIndeks);
    void deleteByStudentIndeks(StudentIndeks indeks);


    /*
    /// Svi upisi za određenog studenta
    @Query("select u from UpisGodine u where u.studentIndeks.id = :studentId")
    List<UpisGodine> findByStudentId(Long studentId);

    /// Upisi za određenog studenta i godinu studija
    @Query("select u from UpisGodine u where u.studentIndeks.id = :studentId and u.godinaStudija = :godinaStudija")
    UpisGodine findByStudentAndGodina(Long studentId, Integer godinaStudija);

    /// Svi upisi za konkretnu godinu studija
    @Query("select u from UpisGodine u where u.godinaStudija = :godinaStudija")
    List<UpisGodine> findByGodinaStudija(Integer godinaStudija);

    /// Svi upisi koji sadrže prenešene predmete
    @Query("select distinct u from UpisGodine u join u.prenetiPredmeti p")
    List<UpisGodine> findWithPrenetiPredmeti();

    @Query("SELECT u FROM UpisGodine u LEFT JOIN FETCH u.prenetiPredmeti")
    List<UpisGodine> findAllWithPrenetiPredmeti();
    Optional<UpisGodine> findByStudentIndeksAndGodinaStudija(StudentIndeks si, int godinaStudija);

     */
}