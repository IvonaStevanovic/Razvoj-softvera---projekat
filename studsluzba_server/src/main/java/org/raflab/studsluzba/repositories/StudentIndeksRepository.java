package org.raflab.studsluzba.repositories;

import java.util.List;
import java.util.Optional;

import org.raflab.studsluzba.model.StudentIndeks;
import org.raflab.studsluzba.model.StudentPodaci;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentIndeksRepository extends JpaRepository<StudentIndeks, Long> {

    @Override
    List<StudentIndeks> findAll();
    Optional<StudentIndeks> findByBroj(Integer broj);

    // Ova metoda sprečava da se unese isti broj indeksa za istu godinu na istom smeru
    boolean existsByBrojAndGodinaAndStudProgramOznaka(Integer broj, int godina, String oznaka);

    // Za pretragu po imenu i prezimenu (već imaš deo, ali proveri nazive)
    Page<StudentIndeks> findByStudent_ImeContainingIgnoreCaseAndStudent_PrezimeContainingIgnoreCase(
            String ime, String prezime, Pageable pageable);

    Page<StudentIndeks> findByStudent_ImeContainingIgnoreCase(String ime, Pageable pageable);

    Page<StudentIndeks> findByStudent_PrezimeContainingIgnoreCase(String prezime, Pageable pageable);

    // Pretraga po srednjoj školi (iz specifikacije)
    @Query("SELECT si FROM StudentIndeks si WHERE LOWER(si.student.srednjaSkola.naziv) LIKE LOWER(CONCAT('%', :naziv, '%'))")
    List<StudentIndeks> findByStudentSrednjaSkolaNazivContainingIgnoreCase(@Param("naziv") String naziv);

    List<StudentIndeks> findByStudent(StudentPodaci student);

    boolean existsByBroj(Integer broj);





    /*
	@Query("select indeks from StudentIndeks indeks where indeks.studProgramOznaka like ?1 and indeks.godina = ?2 "
			+ "and indeks.broj = ?3 ")
    StudentIndeks findStudentIndeks(String studProgramOznaka, int godina, int broj);

    //vraca najvise jedan aktivan indekspo studentu/godini/programu
    @Query("select si from StudentIndeks si " +
            "where si.student.id = :idStudentPodaci " +
            "and si.aktivan = true " +
            "and si.godina = :godina " +
            "and lower(si.studijskiProgram.oznaka) = lower(:studProgramOznaka)")
    StudentIndeks findAktivanStudentIndeks(@Param("idStudentPodaci") Long idStudentPodaci,
                                           @Param("godina") int godina,
                                           @Param("studProgramOznaka") String studProgramOznaka);

    //dodato za proveru duplikata indeksa
    @Query("SELECT si FROM StudentIndeks si "
            + "WHERE si.student.id = :studentId "
            + "AND si.studProgramOznaka = :program "
            + "AND si.godina = :godina")
    Optional<StudentIndeks> findDuplicateIndex(
            @Param("studentId") Long studentId,
            @Param("program") String program,
            @Param("godina") Integer godina
    );

    //TODO dodati da se gledaju samo aktivni indeksi
	@Query("select indeks from StudentIndeks indeks where "
			+ "(:ime is null or lower(indeks.student.ime) like :ime) and "
			+ "(:prezime is null or lower(indeks.student.prezime) like :prezime) and "
			+ "(:studProgramOznaka is null or lower(indeks.studProgramOznaka) like :studProgramOznaka) and"
			+ "(:godina is null or indeks.godina = :godina) and "
			+ "(:broj is null or indeks.broj = :broj)")
	Page<StudentIndeks> findStudentIndeks(String ime, String prezime, String studProgramOznaka, Integer godina, Integer broj, Pageable pageable);
	
	@Query("select si from StudentIndeks si where si.student.id = :idStudentPodaci")
	List<StudentIndeks> findStudentIndeksiForStudentPodaciId(Long idStudentPodaci);

	@Query("select si from StudentIndeks si where si.student.id = :idStudentPodaci and si.aktivan = true")
	StudentIndeks findAktivanStudentIndeksiByStudentPodaciId(Long idStudentPodaci);

	@Query("SELECT s.broj FROM StudentIndeks s WHERE s.godina = :godina AND s.studProgramOznaka = :studProgramOznaka AND s.aktivan = true ORDER BY s.broj ASC")
	List<Integer> findBrojeviByGodinaAndStudProgramOznaka(@Param("godina") int godina, @Param("studProgramOznaka") String studProgramOznaka);
*/

}
