package org.raflab.studsluzba.repositories;

import org.raflab.studsluzba.model.IspitniRok;
import org.raflab.studsluzba.model.SkolskaGodina;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IspitniRokRepository extends CrudRepository<IspitniRok, Long> {
    ///Svi ispitni rokovi za određenu školsku godinu
    @Query("select ir from IspitniRok ir where ir.skolskaGodina.id = :idSkolskeGodine")
    List<IspitniRok> findBySkolskaGodina(Long idSkolskeGodine);

    ///Aktivni ispitni rokovi (po datumu)
    @Query("select ir from IspitniRok ir where :today between ir.datumPocetka and ir.datumZavrsetka")
    List<IspitniRok> findAktivniRokovi(LocalDate today);

    ///Pronađi rok po datumu početka
    @Query("select ir from IspitniRok ir where ir.datumPocetka = :datumPocetka")
    IspitniRok findByDatumPocetka(LocalDate datumPocetka);
/*
    // 4️⃣ (opciono) Provera da li se datumi preklapaju sa postojećim rokovima u istoj školskoj godini
    @Query("select ir from IspitniRok ir where ir.skolskaGodina.id = :idSkolskeGodine and " +
            "(:pocetak between ir.datumPocetka and ir.datumZavrsetka or :zavrsetak between ir.datumPocetka and ir.datumZavrsetka)")
    List<IspitniRok> findRokoviKojiSePreklapaju(Long idSkolskeGodine, LocalDate pocetak, LocalDate zavrsetak);
*/
@Query("select ir from IspitniRok ir where ir.datumPocetka = :datumPocetka and ir.skolskaGodina.id = :godinaId")
Optional<IspitniRok> findByDatumPocetkaAndSkolskaGodinaId(LocalDate datumPocetka, Long godinaId);

}

