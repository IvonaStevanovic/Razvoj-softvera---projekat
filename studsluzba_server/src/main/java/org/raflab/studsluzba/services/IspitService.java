package org.raflab.studsluzba.services;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.IzlazakNaIspitRequest;
import org.raflab.studsluzba.controllers.request.PrijavaIspitaRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class IspitService {

    private final IspitRepository ispitRepository;
    private final PrijavaIspitaRepository prijavaIspitaRepository;
    private final IzlazakNaIspitRepository izlazakNaIspitRepository;
    private final PredispitniPoeniRepository predispitniPoeniRepository;
    private final PredispitneObavezeRepository predispitneObavezeRepository;
    private final StudentIndeksRepository studentIndeksRepository;
    private final SlusaPredmetRepository slusaPredmetRepository;
/*
    public List<Ispit> findAll() {
        return (List<Ispit>) ispitRepository.findAll();
    }

    public Optional<Ispit> findById(Long id) {
        return ispitRepository.findById(id);
    }



    public List<Ispit> findByPredmetAndRok(Long predmetId, Long rokId) {
        return ispitRepository.findByPredmetIdAndIspitniRokId(predmetId, rokId);
    }

    public Ispit save(Ispit ispit) {
        return ispitRepository.save(ispit);
    }

    public void deleteById(Long id) {
        ispitRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return ispitRepository.existsById(id);
    }
*/
    // -------------------- PRIJAVA ISPITA --------------------
    public PrijavaIspitaResponse prijaviIspit(PrijavaIspitaRequest request) {
        StudentIndeks student = studentIndeksRepository.findById(request.getStudentIndeksId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student indeks ne postoji"));

        Ispit ispit = ispitRepository.findById(request.getIspitId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ispit ne postoji"));

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student indeks ne postoji"));

        Ispit ispit = ispitRepository.findById(request.getIspitId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Ispit ne postoji"));

        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByStudentIndeksAndIspit(student, ispit);
        if (prijave.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Student nije prijavljen na ispit");
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student indeks ne postoji"));

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
                .map(p -> new StudentPodaciResponse(p.getStudentIndeks().getId(),
                        p.getStudentIndeks().getStudent().getIme(),
                        p.getStudentIndeks().getStudent().getPrezime(),
                        p.getStudentIndeks().getBroj(),
                        p.getStudentIndeks().getGodina()))
                .collect(Collectors.toList());
    }

    // -------------------- PROSEČNA OCENA ISPITA --------------------

    public double getProsekNaIspitu(Long ispitId) {
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ispit ne postoji"));

        List<IzlazakNaIspit> izlazci = izlazakNaIspitRepository.findAllByIspitId(ispitId);

        // filtriramo studente koji su izašli
        List<Integer> ocene = izlazci.stream()
                .filter(z -> Boolean.TRUE.equals(z.getIzasao()))
                .map(z -> poeniUocenu(z.getPoeniIspit() + z.getPoeniPredispitne()))
                .collect(Collectors.toList());

        if (ocene.isEmpty()) return 0.0;

        return ocene.stream()
                .mapToInt(Integer::intValue)
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
    public List<PredispitniPoeniResponse> getPredispitniPoeni(Long studentIndeksId) {
        StudentIndeks student = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student indeks ne postoji"));

        return predispitniPoeniRepository.findByStudentIndeks(student)
                .stream()
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Student indeks ne postoji"));

        return izlazakNaIspitRepository.findByStudentIndeks(student)
                .stream()
                .filter(izlazak -> izlazak.getPrijavaIspita().getIspit().getPredmet().getId().equals(predmetId))
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

/*
    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;

    public Ispit save(Ispit ispit) {
        Predmet predmet = predmetRepository.findById(ispit.getPredmet().getId())
                .orElseThrow(() -> new EntityNotFoundException("Predmet ne postoji"));
        Nastavnik nastavnik = nastavnikRepository.findById(ispit.getNastavnik().getId())
                .orElseThrow(() -> new EntityNotFoundException("Nastavnik ne postoji"));
        IspitniRok rok = ispitniRokRepository.findById(ispit.getIspitniRok().getId())
                .orElseThrow(() -> new EntityNotFoundException("Ispitni rok ne postoji"));

        ispit.setPredmet(predmet);
        ispit.setNastavnik(nastavnik);
        ispit.setIspitniRok(rok);

        boolean exists = ispitRepository.existsByDatumOdrzavanjaAndPredmetIdAndNastavnikIdAndIspitniRokId(
                ispit.getDatumOdrzavanja(),
                predmet.getId(),
                nastavnik.getId(),
                rok.getId()
        );

        if (exists) {
            throw new IllegalArgumentException("Ispit već postoji za zadati datum, predmet i nastavnika u ovom roku!");
        }

        return ispitRepository.save(ispit);
    }
    @Transactional(readOnly = true)
    public List<PrijavaIspitaResponse> getPrijavljeniStudenti(Long ispitId) {
        // Pronađi ispit po ID-ju
        Ispit ispit = ispitRepository.findById(ispitId)
                .orElseThrow(() -> new RuntimeException("Ispit ne postoji"));

        // Pronađi sve prijave za taj ispit
        List<PrijavaIspita> prijave = prijavaIspitaRepository.findByIspit(ispitId);

        // Konvertuj svaku prijavu u response objekat
        return prijave.stream()
                .map(this::convertToPrijavaResponse)
                .collect(Collectors.toList());

    }

    private PrijavaIspitaResponse convertToPrijavaResponse(PrijavaIspita p) {
        PrijavaIspitaResponse response = new PrijavaIspitaResponse();
        response.setId(p.getId());
        response.setDatumPrijave(p.getDatumPrijave());
        response.setIspitId(p.getIspit() != null ? p.getIspit().getId() : null);
        response.setStudentIndeksId(p.getStudentIndeks() != null ? p.getStudentIndeks().getId() : null);
        return response;
    }
    public List<Ispit> findAll() {
        return ispitRepository.findAll();
    }

    public Optional<Ispit> findById(Long id) {
        return ispitRepository.findById(id);
    }
/*
    @Transactional
    public void deleteById(Long id) {
        Ispit ispit = ispitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ispit sa id " + id + " ne postoji."));
        ispitRepository.delete(ispit);
    }

    public List<Ispit> findByPredmet(Long predmetId) {
        return ispitRepository.findByPredmet(predmetId);
    }

    public List<Ispit> findByNastavnik(Long nastavnikId) {
        return ispitRepository.findByNastavnik(nastavnikId);
    }

    public List<Ispit> findByIspitniRok(Long ispitniRokId) {
        return ispitRepository.findByIspitniRok(ispitniRokId);
    }

    public List<Ispit> findByPredmetNastavnikRok(Long predmetId, Long nastavnikId, Long ispitniRokId) {
        if (predmetId != null)
            return findByPredmet(predmetId);
        else if (nastavnikId != null)
            return findByNastavnik(nastavnikId);
        else if (ispitniRokId != null)
            return findByIspitniRok(ispitniRokId);
        else
            return findAll();
    }
    */
}

