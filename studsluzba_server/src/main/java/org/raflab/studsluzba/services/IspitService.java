package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IspitRequest;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IspitService {

    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private PredispitniPoeniRepository predispitniPoeniRepository;
    @Autowired
    private PredispitneObavezeRepository predispitneObavezeRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private PolozeniPredmetiRepository polozeniPredmetiRepository;

    @Transactional(readOnly = true)
    // Na serveru u IspitService
    public List<IspitResponse> findAllResponses() {
        return ispitRepository.findAll().stream().map(ispit -> {
            IspitResponse resp = new IspitResponse();
            resp.setId(ispit.getId());
            resp.setPredmetNaziv(ispit.getPredmet().getNaziv());
            // KLJUČNA LINIJA:
            resp.setIspitniRokId(ispit.getIspitniRok().getId());
            resp.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
            return resp;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<IspitResponse> searchIspiti(String predmetNaziv, String ispitniRokNaziv) {
        List<Ispit> ispiti = ispitRepository.findAll();

        return ispiti.stream()
                .filter(i -> (predmetNaziv == null || i.getPredmet().getNaziv().toLowerCase().contains(predmetNaziv.toLowerCase())))
                .filter(i -> (ispitniRokNaziv == null || i.getIspitniRok().getNaziv().toLowerCase().contains(ispitniRokNaziv.toLowerCase())))
                .map(ispit -> {
                    IspitResponse resp = new IspitResponse();
                    resp.setId(ispit.getId());
                    resp.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
                    resp.setVremePocetka(ispit.getVremePocetka());
                    resp.setPredmetNaziv(ispit.getPredmet().getNaziv());
                    resp.setIspitniRokNaziv(ispit.getIspitniRok().getNaziv());
                    resp.setSkolskaGodinaNaziv(ispit.getIspitniRok().getSkolskaGodina().getNaziv());
                    return resp;
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public IspitResponse getIspitResponseById(Long id) {
        Ispit ispit = ispitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        // session je još otvoren, tako da možeš dohvatiti LAZY veze
        IspitResponse resp = new IspitResponse();
        resp.setId(ispit.getId());
        resp.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        resp.setVremePocetka(ispit.getVremePocetka());
        resp.setPredmetNaziv(ispit.getPredmet().getNaziv());
        resp.setIspitniRokNaziv(ispit.getIspitniRok().getNaziv());
        resp.setSkolskaGodinaNaziv(ispit.getIspitniRok().getSkolskaGodina().getNaziv());

        return resp;
    }

    @Transactional
    public Ispit save(Ispit ispit) {
        // Pronađi DrziPredmet preko ID-ja poslatog u Ispit (pretpostavljam da si dodao drziPredmet polje u Ispit)
        DrziPredmet drziPredmet = drziPredmetRepository.findById(ispit.getDrziPredmet().getId())
                .orElseThrow(() -> new EntityNotFoundException("DrziPredmet ne postoji"));

        IspitniRok rok = ispitniRokRepository.findById(ispit.getIspitniRok().getId())
                .orElseThrow(() -> new EntityNotFoundException("Ispitni rok ne postoji"));

        // Postavi predmet i nastavnika iz DrziPredmet
        ispit.setPredmet(drziPredmet.getPredmet());
        ispit.setNastavnik(drziPredmet.getNastavnik());
        ispit.setIspitniRok(rok);

        boolean exists = ispitRepository.existsByDatumOdrzavanjaAndDrziPredmetIdAndIspitniRokId(
                ispit.getDatumOdrzavanja(),
                ispit.getDrziPredmet().getId(),
                rok.getId()
        );

        if (exists) {
            throw new IllegalArgumentException("Ispit već postoji za zadati datum, predmet i nastavnika u ovom roku!");
        }

        return ispitRepository.save(ispit);
    }

    public boolean existsByDatumPredmetNastavnikRok(LocalDate datum, Long predmetId, Long nastavnikId, Long rokId) {
        return ispitRepository.existsByDatumPredmetNastavnikRok(datum, predmetId, nastavnikId, rokId);
    }

    @Transactional
    public Ispit createFromRequest(IspitRequest request) {

        DrziPredmet drziPredmet = drziPredmetRepository.findById(request.getDrziPredmetId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "DrziPredmet ne postoji"));

        IspitniRok rok = ispitniRokRepository.findById(request.getIspitniRokId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ispitni rok ne postoji"));

        boolean exists = ispitRepository
                .existsByDatumOdrzavanjaAndDrziPredmetIdAndIspitniRokId(
                        request.getDatumOdrzavanja(),
                        drziPredmet.getId(),
                        rok.getId()
                );

        if (exists) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ispit već postoji za zadati datum, predmet i nastavnika u ovom roku!"
            );
        }

        Ispit ispit = new Ispit();
        ispit.setDatumOdrzavanja(request.getDatumOdrzavanja());
        ispit.setVremePocetka(request.getVremePocetka());
        ispit.setNapomena(request.getNapomena());
        ispit.setZakljucen(false);

        ispit.setDrziPredmet(drziPredmet);
        ispit.setPredmet(drziPredmet.getPredmet());
        ispit.setIspitniRok(rok);

        return ispitRepository.save(ispit);
    }

    @Transactional
    public IspitResponse createAndMap(IspitRequest request) {

        Ispit ispit = createFromRequest(request);

        IspitResponse resp = new IspitResponse();
        resp.setId(ispit.getId());
        resp.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        resp.setVremePocetka(ispit.getVremePocetka());
        resp.setPredmetNaziv(ispit.getPredmet().getNaziv());
        resp.setIspitniRokNaziv(ispit.getIspitniRok().getNaziv());


        resp.setSkolskaGodinaNaziv(
                ispit.getIspitniRok().getSkolskaGodina().getNaziv()
        );

        return resp;
    }


    // -------------------- PRIJAVA ISPITA --------------------
    public PrijavaIspitaResponse prijaviIspit(PrijavaIspitaRequest request) {
        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        Ispit ispit = ispitRepository.findById(request.getIspitId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        boolean slusa = slusaPredmetRepository.existsByStudentIndeksAndDrziPredmet(student, ispit.getDrziPredmet());
        if (!slusa) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student ne može prijaviti ispit za predmet koji ne sluša!");
        }

        PrijavaIspita prijava = new PrijavaIspita();
        prijava.setStudentIndeks(student);
        prijava.setIspit(ispit);
        prijava.setDatumPrijave(request.getDatumPrijave() != null ? request.getDatumPrijave() : LocalDate.now());
        prijava.setIzasao(false);

        prijava = prijavaIspitaRepository.save(prijava);

        PrijavaIspitaResponse response = new PrijavaIspitaResponse();
        response.setId(prijava.getId());
        response.setDatumPrijave(prijava.getDatumPrijave());
        response.setIspitId(ispit.getId());
        response.setStudentIndeksId(student.getId());

        return response;
    }

    // -------------------- IZLAZAK NA ISPIT --------------------
    public IzlazakNaIspitResponse evidentirajIzlazak(IzlazakNaIspitRequest request) {
        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        Ispit ispit = ispitRepository.findById(request.getIspitId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByStudentIndeksAndIspit(student, ispit);
        if (prijave.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije prijavljen na ispit");
        }
        PrijavaIspita prijava = prijave.get(prijave.size() - 1);

        SlusaPredmet slusaPredmet = null;
        if (request.getSlusaPredmet() != null) {
            slusaPredmet = slusaPredmetRepository.findByIdAndStudentIndeks(request.getSlusaPredmet(), student)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student ne sluša predmet"));
        }

        int ukupnoPredispitni = slusaPredmet != null && slusaPredmet.getPredispitniPoeni() != null
                ? slusaPredmet.getPredispitniPoeni().stream().mapToInt(PredispitniPoeni::getPoeni).sum()
                : 0;

        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setPrijavaIspita(prijava);
        izlazak.setSlusaPredmet(slusaPredmet);
        izlazak.setPoeniPredispitne(ukupnoPredispitni);
        izlazak.setPoeniIspit(request.getOstvarenoNaIspitu());
        izlazak.setNapomena(request.getNapomena());
        izlazak.setPonisteno(request.isPonistio());
        izlazak.setIzasao(request.isIzasao());
        izlazak.setStudentIndeks(student);

        izlazak = izlazakNaIspitRepository.save(izlazak);

        // ---------------- AUTOMATSKO ČUVANJE POLOŽENOG PREDMETA ----------------
        int ukupnoPoena = ukupnoPredispitni + request.getOstvarenoNaIspitu();
        if (ukupnoPoena >= 51) {
            student.dodajPolozeniPredmet(ispit.getPredmet(), ukupnoPoena, izlazak);
        }

        IzlazakNaIspitResponse response = new IzlazakNaIspitResponse();
        response.setId(izlazak.getId());
        response.setOstvarenoNaIspitu(izlazak.getPoeniIspit());
        response.setNapomena(izlazak.getNapomena());
        response.setPonistio(Boolean.TRUE.equals(izlazak.getPonisteno()));
        response.setIzasao(Boolean.TRUE.equals(izlazak.getIzasao()));
        response.setStudentIndeksId(student.getId());
        response.setStudentImePrezime(student.getStudent().getIme() + " " + student.getStudent().getPrezime());
        response.setIspitId(ispit.getId());
        response.setPredmetNaziv(ispit.getPredmet().getNaziv());

        return response;
    }

    // -------------------- SVI IZLASCI ZA STUDENTA --------------------
    public List<IzlazakNaIspitResponse> getIzlasciZaStudenta(Long studentIndeksId) {
        StudentIndeks student = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        return izlazakNaIspitRepository.findByStudentIndeks(student)
                .stream()
                .map(izlazak -> {
                    IzlazakNaIspitResponse resp = new IzlazakNaIspitResponse();
                    resp.setId(izlazak.getId());
                    resp.setOstvarenoNaIspitu(izlazak.getPoeniIspit());
                    resp.setNapomena(izlazak.getNapomena());
                    resp.setPonistio(Boolean.TRUE.equals(izlazak.getPonisteno()));
                    resp.setIzasao(Boolean.TRUE.equals(izlazak.getIzasao()));
                    resp.setStudentIndeksId(student.getId());
                    resp.setStudentImePrezime(student.getStudent().getIme() + " " + student.getStudent().getPrezime());
                    resp.setIspitId(izlazak.getPrijavaIspita().getIspit().getId());
                    resp.setPredmetNaziv(izlazak.getPrijavaIspita().getIspit().getPredmet().getNaziv());
                    return resp;
                })
                .collect(Collectors.toList());
    }

    // -------------------- SVI PRIJAVLJENI STUDENTI ZA ISPIT --------------------
    public List<StudentPodaciResponse> getPrijavljeniZaIspit(Long ispitId) {
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        return prijavaIspitaRepository.findByIspit(ispit)
                .stream()
                .map(p -> {
                    StudentPodaciResponse r = new StudentPodaciResponse();
                    r.setId(p.getStudentIndeks().getId());
                    r.setIme(p.getStudentIndeks().getStudent().getIme());
                    r.setPrezime(p.getStudentIndeks().getStudent().getPrezime());
                    r.setBrojIndeksa(p.getStudentIndeks().getBroj());
                    // Ako ti treba godina, dodaj polje u Response ili je ignoriši ako nije u DTO
                    return r;
                })
                .collect(Collectors.toList());
    }

    // -------------------- PROSEČNA OCENA ISPITA --------------------

    public double getProsekNaIspitu(Long ispitId) {
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        // Dohvati sve izlaske za taj ispit
        List<IzlazakNaIspit> izlasci = izlazakNaIspitRepository.findAll();

        List<Integer> ocene = izlasci.stream()
                // 1. Proveri da li izlazak pripada OVOM ispitu preko prijave
                .filter(z -> z.getPrijavaIspita().getIspit().getId().equals(ispitId))
                // 2. Proveri da li je student izašao i da nije poništio ispit
                .filter(z -> Boolean.TRUE.equals(z.getIzasao()) && !Boolean.TRUE.equals(z.getPonisteno()))
                .map(z -> {
                    int ukupno = (z.getPoeniIspit() != null ? z.getPoeniIspit() : 0) +
                            (z.getPoeniPredispitne() != null ? z.getPoeniPredispitne() : 0);
                    return poeniUocenu(ukupno);
                })
                // 3. Obično se u prosek računaju samo prolazne ocene (> 5)
                .filter(ocena -> ocena > 5)
                .collect(Collectors.toList());

        if (ocene.isEmpty()) return 0.0;

        // Precizno računanje proseka
        return ocene.stream()
                .mapToDouble(Integer::doubleValue)
                .average()
                .orElse(0.0);
    }

    private int poeniUocenu(Integer poeni) {
        if (poeni == null) return 5; // ili minimum ocena
        if (poeni < 51) return 5;
        if (poeni <= 60) return 6;
        if (poeni <= 70) return 7;
        if (poeni <= 80) return 8;
        if (poeni <= 90) return 9;
        return 10;
    }

    // -------------------- DODAJ PREDISPITNE POENE --------------------
    public PredispitniPoeni dodajPredispitnePoene(Long studentIndeksId, Long predispitnaObavezaId, Integer poeni) {
        if (poeni == null || poeni < 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poeni moraju biti >=0");

        StudentIndeks student = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        PredispitneObaveze obaveza = predispitneObavezeRepository.findById(predispitnaObavezaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Predispitna obaveza ne postoji"));

        SlusaPredmet slusaPredmet = slusaPredmetRepository.findByStudentIndeksAndDrziPredmet(student, obaveza.getDrziPredmet())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student ne sluša predmet"));

        PredispitniPoeni pp = new PredispitniPoeni();
        pp.setStudentIndeks(student);
        pp.setPredispitnaObaveza(obaveza);
        pp.setPoeni(poeni);
        pp.setSkolskaGodina(obaveza.getSkolskaGodina());
        pp.setSlusaPredmet(slusaPredmet);

        return predispitniPoeniRepository.save(pp);
    }

    // -------------------- PREUZIMANJE PREDISPITNIH POENA --------------------
    public List<PredispitniPoeniResponse> getPredispitniPoeni(Long studentIndeksId, Long predmetId, Long skolskaGodinaId) {
        StudentIndeks student = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        return predispitniPoeniRepository.findByStudentIndeks(student)
                .stream()
                // Filtriranje po predmetu (ako je poslat predmetId)
                .filter(pp -> predmetId == null ||
                        pp.getPredispitnaObaveza().getDrziPredmet().getPredmet().getId().equals(predmetId))
                // Filtriranje po Å¡kolskoj godini (ako je poslat skolskaGodinaId)
                .filter(pp -> skolskaGodinaId == null ||
                        (pp.getSkolskaGodina() != null && pp.getSkolskaGodina().getId().equals(skolskaGodinaId)))
                .map(pp -> {
                    PredispitniPoeniResponse resp = new PredispitniPoeniResponse();
                    resp.setId(pp.getId());
                    resp.setPoeni(pp.getPoeni());
                    resp.setStudentIndeksId(student.getId());
                    resp.setStudentBrojIndeksa(student.getBroj());
                    resp.setStudentGodinaIndeksa(student.getGodina());
                    resp.setStudentIme(student.getStudent().getIme());
                    resp.setStudentPrezime(student.getStudent().getPrezime());
                    resp.setPredispitnaObavezaId(pp.getPredispitnaObaveza().getId());
                    resp.setObavezaVrsta(pp.getPredispitnaObaveza().getVrsta());
                    resp.setMaxPoena(pp.getPredispitnaObaveza().getMaksPoeni());
                    resp.setPredmetId(pp.getPredispitnaObaveza().getDrziPredmet().getPredmet().getId());
                    resp.setPredmetNaziv(pp.getPredispitnaObaveza().getDrziPredmet().getPredmet().getNaziv());
                    resp.setPredmetSifra(pp.getPredispitnaObaveza().getDrziPredmet().getPredmet().getSifra());
                    if (pp.getSkolskaGodina() != null) {
                        resp.setSkolskaGodinaId(pp.getSkolskaGodina().getId());
                        resp.setSkolskaGodinaNaziv(pp.getSkolskaGodina().getNaziv());
                    }
                    return resp;
                })
                .collect(Collectors.toList());
    }

    // -------------------- BROJ POLAGANJA ZA PREDMET --------------------
    public long getBrojPolaganja(Long studentIndeksId, Long predmetId) {
        StudentIndeks student = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        return izlazakNaIspitRepository.findByStudentIndeks(student)
                .stream()
                .filter(izlazak -> izlazak.getPrijavaIspita().getIspit().getPredmet().getId().equals(predmetId))
                .filter(izlazak -> Boolean.TRUE.equals(izlazak.getIzasao()))
                .count();
    }

    public List<StudentIspitRezultatiResponse> getRezultatiIspita(Long ispitId) {
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        List<IzlazakNaIspit> izlazci = izlazakNaIspitRepository.findAllByIspitId(ispit.getId());

        return izlazci.stream()
                .filter(IzlazakNaIspit::getIzasao)
                .map(z -> {
                    StudentIndeks s = z.getStudentIndeks();
                    int ukupnoPoena = z.getPoeniIspit() + (z.getPoeniPredispitne() != null ? z.getPoeniPredispitne() : 0);

                    StudentIspitRezultatiResponse resp = new StudentIspitRezultatiResponse();
                    resp.setStudentIndeksId(s.getId());
                    resp.setIme(s.getStudent().getIme());
                    resp.setPrezime(s.getStudent().getPrezime());
                    resp.setStudijskiProgram(s.getStudijskiProgram().getNaziv());
                    resp.setGodinaUpisa(s.getGodina());
                    resp.setBrojIndeksa(String.valueOf(s.getBroj()));
                    resp.setUkupnoPoena(ukupnoPoena);
                    return resp;
                })
                .sorted((a, b) -> {
                    int cmp = a.getStudijskiProgram().compareTo(b.getStudijskiProgram());
                    if (cmp != 0) return cmp;
                    cmp = Integer.compare(a.getGodinaUpisa(), b.getGodinaUpisa());
                    if (cmp != 0) return cmp;
                    return a.getBrojIndeksa().compareTo(b.getBrojIndeksa());
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public void obrisiIspit(Long id) {
        // 1. Provera da li ispit postoji
        Ispit ispit = ispitRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ispit sa ID-jem " + id + " ne postoji."));

        // 2. Opciona poslovna logika: Ne dozvoli brisanje ako je ispit zaključen
        // (Ovo je dobro za poene na odbrani jer pokazuje da razmišljaš o integritetu podataka)
        if (ispit.isZakljucen()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nije moguće obrisati ispit koji je već zaključen.");
        }

        // 3. Brisanje - Hibernate sada sam čisti sve povezane tabele
        ispitRepository.delete(ispit);
    }


}