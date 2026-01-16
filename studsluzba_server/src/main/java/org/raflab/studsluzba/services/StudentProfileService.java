package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.model.dtos.UpisanaGodinaDTO;
import org.raflab.studsluzba.repositories.*;
import org.raflab.studsluzba.utils.EntityMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@Service
@Transactional
@AllArgsConstructor
public class StudentProfileService  {
    @Autowired
    private  StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private  UpisGodineRepository upisGodineRepository;
    @Autowired
    private  ObnovaGodineRepository obnovaGodineRepository;
    @Autowired
    private  PredmetRepository predmetRepository;
    @Autowired
    private  UplataRepository uplataRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired
    private PolozeniPredmetiRepository polozeniPredmetiRepository;
    @Autowired
    private StudentPodaciRepository studentRepository;
    @Autowired
    private SrednjaSkolaRepository srednjaSkolaRepository;
    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;

    private static final double SKOLARINA_EUR = 3000.0;
    private static final String KURS_API = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";

    public StudentPodaciResponse findById(Long id) {
        StudentPodaci s = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student sa ID " + id + " nije pronađen"));

        // Uzimamo prvi indeks iz seta (obično student ima jedan aktivan, ili prilagodi logiku)
        StudentIndeks indeks = s.getIndeksi().stream()
                .filter(StudentIndeks::isAktivan)
                .findFirst()
                .orElse(s.getIndeksi().isEmpty() ? null : s.getIndeksi().iterator().next());

        return mapToStudentResponse(s, indeks);
    }


    private StudentPodaciResponse mapToStudentResponse(StudentPodaci s, StudentIndeks indeks) {
        StudentPodaciResponse r = new StudentPodaciResponse();
        r.setId(s.getId());
        r.setIme(s.getIme());
        r.setPrezime(s.getPrezime());
        r.setSrednjeIme(s.getSrednjeIme());
        r.setJmbg(s.getJmbg());
        r.setDatumRodjenja(s.getDatumRodjenja());
        r.setMestoRodjenja(s.getMestoRodjenja());
        r.setMestoPrebivalista(s.getMestoStanovanja()); // pazi na imena polja u entitetu
        r.setDrzavaRodjenja(s.getDrzavaRodjenja());
        r.setDrzavljanstvo(s.getDrzavljanstvo());
        r.setNacionalnost(s.getNacionalnost());
        r.setGodinaUpisa(indeks.getGodina());
        r.setPol(s.getPol());
        r.setAdresa(s.getAdresa());
        r.setBrojTelefonaMobilni(s.getBrojTelefonaMobilni());
        r.setBrojTelefonaFiksni(s.getBrojTelefonaFiksni());
        r.setEmailFakultet(s.getEmailFakultet());
        r.setEmailPrivatni(s.getEmailPrivatni());
        r.setBrojLicneKarte(s.getBrojLicneKarte());
        r.setLicnuKartuIzdao(s.getLicnuKartuIzdao());
        r.setMestoStanovanja(s.getMestoStanovanja());
        r.setAdresaStanovanja(s.getAdresaStanovanja());

        // Podaci iz Indeksa
        if (indeks != null) {
            r.setBrojIndeksa(indeks.getBroj());
        }

        // Podaci iz Šifarnika (Srednja škola)
        if (s.getSrednjaSkola() != null) {
            r.setSrednjaSkola(s.getSrednjaSkola().getNaziv());
        }

        return r;
    }
    public StudentPodaciResponse dodajStudenta(StudentPodaciRequest request) {
        // 1. Provera duplikata (JMBG i Indeks)
        if (studentRepository.existsByJmbg(request.getJmbg())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student sa JMBG " + request.getJmbg() + " već postoji.");
        }
        // Pretraga indeksa mora biti specifična (broj + godina + program) prema specifikaciji
        if (studentIndeksRepository.existsByBrojAndGodinaAndStudProgramOznaka(
                request.getBrojIndeksa(), request.getGodinaUpisa(), request.getStudProgramOznaka())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indeks već postoji u sistemu.");
        }

        // 2. Pronalaženje zavisnih entiteta (Šifarnici)
        StudijskiProgram program = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Studijski program nije pronađen."));

        SrednjaSkola skola = srednjaSkolaRepository.findById(request.getSrednjaSkolaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Srednja škola nije nađena u šifarniku."));

        // 3. Kreiranje i čuvanje StudentPodaci
        StudentPodaci student = new StudentPodaci();
        // Mapiranje osnovnih polja (ime, prezime, jmbg...)
        student.setIme(request.getIme());
        student.setPrezime(request.getPrezime());
        student.setJmbg(request.getJmbg());
        student.setSrednjaSkola(skola); // Povezivanje sa šifarnikom škola
        // ... podesi ostala polja iz request-a ...

        student = studentRepository.save(student);

        // 4. Kreiranje Indeksa (Ključni deo specifikacije)
        StudentIndeks indeks = new StudentIndeks();
        indeks.setBroj(request.getBrojIndeksa());
        indeks.setGodina(request.getGodinaUpisa());
        indeks.setStudProgramOznaka(request.getStudProgramOznaka());
        indeks.setStudijskiProgram(program); // Direktna veza ka programu
        indeks.setStudent(student);
        indeks.setAktivan(true);
        indeks.setVaziOd(LocalDate.now());
        indeks.setNacinFinansiranja(request.getNacinFinansiranja()); // Budžet/Samofinansiranje

        studentIndeksRepository.save(indeks);

        // 5. Mapiranje u Response
        return mapToStudentResponse(student, indeks);
    }

    // ------------------ STUDENT ------------------
    public StudentPodaciResponse getStudentByBrojIndeksa(Integer brojIndeksa) {
        StudentIndeks indeks = studentIndeksRepository.findByBroj(brojIndeksa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));
        StudentPodaci s = indeks.getStudent();
        StudentPodaciResponse response = new StudentPodaciResponse();
        response.setId(s.getId());
        response.setIme(s.getIme());
        response.setPrezime(s.getPrezime());
        response.setSrednjeIme(s.getSrednjeIme());
        response.setJmbg(s.getJmbg());
        response.setDatumRodjenja(s.getDatumRodjenja());
        response.setMestoRodjenja(s.getMestoRodjenja());
        response.setMestoPrebivalista(s.getMestoStanovanja());
        response.setDrzavaRodjenja(s.getDrzavaRodjenja());
        response.setDrzavljanstvo(s.getDrzavljanstvo());
        response.setNacionalnost(s.getNacionalnost());
        response.setPol(s.getPol());
        response.setAdresa(s.getAdresa());
        response.setBrojTelefonaMobilni(s.getBrojTelefonaMobilni());
        response.setBrojTelefonaFiksni(s.getBrojTelefonaFiksni());
        response.setEmailFakultet(s.getEmailFakultet());
        response.setEmailPrivatni(s.getEmailPrivatni());
        response.setBrojLicneKarte(s.getBrojLicneKarte());
        response.setLicnuKartuIzdao(s.getLicnuKartuIzdao());
        response.setMestoStanovanja(s.getMestoStanovanja());
        response.setAdresaStanovanja(s.getAdresaStanovanja());
        return response;
    }

    public Page<PolozeniPredmetiResponse> getPolozeniPredmeti(Long studentIndeksId, int page, int size) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indeks nije pronađen"));

        // 1. Dobijamo sve zapise za položene predmete
        List<PolozeniPredmeti> sviZapisi = polozeniPredmetiRepository.findByStudentIndeks(indeks);

        // 2. Filtriranje duplikata na osnovu ŠIFRE ili ID-a PREDMETA
        // Koristimo Mapu: ključ je ID predmeta, vrednost je DTO
        Map<Long, PolozeniPredmetiResponse> unikatniPolozeni = sviZapisi.stream()
                .filter(p -> p.getOcena() != null && p.getOcena() > 5)
                .map(p -> {
                    PolozeniPredmetiResponse response = new PolozeniPredmetiResponse();
                    response.setId(p.getId());
                    response.setPredmetNaziv(p.getPredmet().getNaziv());
                    response.setOcena(p.getOcena());
                    response.setEspb(p.getPredmet().getEspb());

                    // Putanja do datuma
                    if (p.getIzlazakNaIspit() != null &&
                            p.getIzlazakNaIspit().getPrijavaIspita() != null &&
                            p.getIzlazakNaIspit().getPrijavaIspita().getIspit() != null) {
                        response.setDatumPolaganja(p.getIzlazakNaIspit().getPrijavaIspita().getIspit().getDatumOdrzavanja());
                    }
                    return response;
                })
                .collect(Collectors.toMap(
                        res -> res.getPredmetId(), // Koristi ID predmeta kao ključ
                        res -> res,
                        (stari, novi) -> stari // Ako postoji duplikat, zadrži prvi na koji naiđeš
                ));

        List<PolozeniPredmetiResponse> finalnaLista = new ArrayList<>(unikatniPolozeni.values());

        // 3. Ručna paginacija za povratni Page objekat
        int start = Math.min(page * size, finalnaLista.size());
        int end = Math.min((start + size), finalnaLista.size());

        return new PageImpl<>(finalnaLista.subList(start, end), PageRequest.of(page, size), finalnaLista.size());
    }

    @Transactional(readOnly = true)
    public Page<NepolozeniPredmetDTO> getNepolozeniPredmetiByBroj(Integer brojIndeksa, Pageable pageable) {
        StudentIndeks indeks = studentIndeksRepository.findByBroj(brojIndeksa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indeks nije pronađen"));

        // 1. Skup ŠIFARA položenih predmeta (najsigurniji način)
        Set<String> polozeneSifre = polozeniPredmetiRepository.findByStudentIndeks(indeks)
                .stream()
                .filter(pp -> pp.getOcena() != null && pp.getOcena() > 5)
                .map(pp -> pp.getPredmet().getSifra())
                .collect(Collectors.toSet());

        // 2. Sve što student sluša
        List<SlusaPredmet> slusaList = slusaPredmetRepository.findByStudentIndeks(indeks);

        // 3. Mapiranje i filtriranje preko Mape da se izbegnu duplikati
        Map<String, NepolozeniPredmetDTO> filtrirano = slusaList.stream()
                .map(sp -> {
                    Predmet p = sp.getDrziPredmet().getPredmet();
                    NepolozeniPredmetDTO dto = new NepolozeniPredmetDTO();
                    dto.setId(sp.getId());
                    dto.setSifraPredmeta(p.getSifra());
                    dto.setNazivPredmeta(p.getNaziv());
                    dto.setEspb(p.getEspb());
                    return dto;
                })
                // Izbacujemo one čija je šifra u skupu položenih
                .filter(dto -> !polozeneSifre.contains(dto.getSifraPredmeta()))
                // Garantujemo unikatnost (šifra je ključ)
                .collect(Collectors.toMap(
                        NepolozeniPredmetDTO::getSifraPredmeta,
                        dto -> dto,
                        (existing, replacement) -> existing
                ));

        List<NepolozeniPredmetDTO> rezultat = new ArrayList<>(filtrirano.values());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), rezultat.size());
        return new PageImpl<>(rezultat.subList(start, end), pageable, rezultat.size());
    }
    // ------------------ UPIS GODINE ------------------
    public List<UpisGodineResponse> getUpisaneGodine(Long studentIndeksId) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));
        List<UpisGodine> upisi = upisGodineRepository.findByStudentIndeksOrderByGodinaStudijaAsc(indeks);
        List<UpisGodineResponse> responses = new ArrayList<>();
        for (UpisGodine u : upisi) {
            responses.add(mapToUpisGodineResponse(u));
        }
        return responses;
    }
    public UpisGodineResponse upisStudentaNaGodinu(Long studentIndeksId, UpisGodineRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        // --- DODATA LOGIKA ZA ESPB LIMIT ---
        List<Predmet> predmeti = predmetRepository.findAllById(request.getPrenetiPredmetiIds());
        int ukupnoEspb = predmeti.stream().mapToInt(Predmet::getEspb).sum();

        if (ukupnoEspb > 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maksimalni zbir ESPB poena je 60. Pokušano: " + ukupnoEspb);
        }
        // ------------------------------------

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aktivna školska godina nije definisana"));

        UpisGodine upis = new UpisGodine();
        upis.setStudentIndeks(indeks);
        upis.setGodinaStudija(request.getGodinaStudija());
        upis.setDatumUpisa(request.getDatum());
        upis.setNapomena(request.getNapomena());
        upis.setSkolskaGodina(skolskaGodina);
        upis.setPredmeti(predmeti);

        upisGodineRepository.save(upis);
        // ... ostatak tvoje logike za polozeniPredmetiRepository ...
        return mapToUpisGodineResponse(upis);
    }

    private UpisGodineResponse mapToUpisGodineResponse(UpisGodine u) {
        UpisGodineResponse r = new UpisGodineResponse();
        r.setId(u.getId());
        r.setGodinaStudija(u.getGodinaStudija());
        r.setDatumUpisa(u.getDatumUpisa());
        r.setNapomena(u.getNapomena());
        r.setSkolskaGodina(u.getSkolskaGodina().getNaziv());

        List<PredmetResponse> predmeti = new ArrayList<>();
        if (u.getPredmeti() != null) {
            for (Predmet p : u.getPredmeti()) {
                predmeti.add(new PredmetResponse(p.getId(), p.getNaziv(), p.getEspb()));
            }
        }
        r.setPredmeti(predmeti);
        return r;
    }

    // ------------------ OBNOVA GODINE ------------------
    public List<ObnovaGodineResponse> getObnovljeneGodine(Long studentIndeksId) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));
        List<ObnovaGodine> obnove = obnovaGodineRepository.findByStudentIndeks(indeks);
        List<ObnovaGodineResponse> responses = new ArrayList<>();
        for (ObnovaGodine o : obnove) {
            responses.add(mapToObnovaGodineResponse(o));
        }
        return responses;
    }

    public ObnovaGodineResponse obnovaGodine(Long studentIndeksId, ObnovaGodineRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        // Dohvatanje aktivne školske godine
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Školska godina ne postoji"));

        // Kreiranje nove obnove godine
        ObnovaGodine obnova = new ObnovaGodine();
        obnova.setStudentIndeks(indeks);
        obnova.setGodinaStudija(request.getGodinaStudija());
        obnova.setDatum(request.getDatum());
        obnova.setNapomena(request.getNapomena());
        obnova.setSkolskaGodina(skolskaGodina);

        // Lista predmeta koji se dodaju na obnovu
        List<Predmet> predmeti = new ArrayList<>();

        // Dodaj nepoložene predmete iz prethodnih godina
        List<PolozeniPredmeti> nepolozeniPredmeti = polozeniPredmetiRepository
                .findNepolozeniByStudentIndeks(indeks.getId());
        for (PolozeniPredmeti p : nepolozeniPredmeti) {
            predmeti.add(p.getPredmet());
        }

        // Dodaj predmete iz naredne godine ako su prosleđeni
        if (request.getPredmetIds() != null && !request.getPredmetIds().isEmpty()) {
            List<Predmet> narednaGodinaPredmeti = predmetRepository.findAllById(request.getPredmetIds());
            predmeti.addAll(narednaGodinaPredmeti);
        }

        // Provera maksimalnog ESPB zbirnog limita
        int ukupnoEspb = predmeti.stream().mapToInt(Predmet::getEspb).sum();
        if (ukupnoEspb > 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ukupan zbir ESPB poena ne može biti veći od 60. Trenutno: " + ukupnoEspb);
        }

        obnova.setPredmeti(predmeti);
        obnovaGodineRepository.save(obnova);

        return mapToObnovaGodineResponse(obnova);
    }

    public ObnovaGodineResponse obnovaGodineSaESPB(Long studentIndeksId, ObnovaGodineRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        // Kreiranje nove obnove
        ObnovaGodine obnova = new ObnovaGodine();
        obnova.setStudentIndeks(indeks);
        obnova.setGodinaStudija(request.getGodinaStudija());
        obnova.setDatum(request.getDatum());
        obnova.setNapomena(request.getNapomena());

        // Dohvati aktivnu školsku godinu
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Školska godina ne postoji"));
        obnova.setSkolskaGodina(skolskaGodina);

        // Dohvati predmete koje student može upisati (iz naredne godine i nepoložene)
        List<Predmet> predmeti = predmetRepository.findAllById(request.getPredmetIds());
        int totalEspb = predmeti.stream().mapToInt(Predmet::getEspb).sum();
        if (totalEspb > 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maksimalni zbir ESPB poena je 60");
        }
        obnova.setPredmeti(predmeti);

        obnovaGodineRepository.save(obnova);

        return mapToObnovaGodineResponse(obnova);
    }


    private ObnovaGodineResponse mapToObnovaGodineResponse(ObnovaGodine o) {
        ObnovaGodineResponse r = new ObnovaGodineResponse();
        r.setId(o.getId());
        r.setGodinaStudija(o.getGodinaStudija());
        r.setDatum(o.getDatum());
        r.setNapomena(o.getNapomena());
        r.setStudentIndeksId(o.getStudentIndeks().getId());
        r.setStudentImePrezime(o.getStudentIndeks().getStudent().getIme() + " " + o.getStudentIndeks().getStudent().getPrezime());

        Set<String> predmetiNazivi = new HashSet<>();
        if (o.getPredmeti() != null) {
            for (Predmet p : o.getPredmeti()) {
                predmetiNazivi.add(p.getNaziv());
            }
        }
        r.setPredmetiNazivi(predmetiNazivi);
        return r;
    }

    // ------------------ UPLATA ------------------
    // U StudentProfileService.java

    @Transactional
    public UplataResponse evidentirajUplatu(Long studentId, Double iznosRsd) {
        // 1. Koristimo studentRepository jer Uplata prima StudentPodaci entitet
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije nađen"));

        // 2. Pozivamo tvoju postojeću metodu fetchDanasnjiKurs (koja je već u fajlu)
        double kurs = fetchDanasnjiKurs();
        double iznosEur = iznosRsd / kurs;

        // 3. Kreiranje uplate - koristimo TAČNA imena polja iz tvog Uplata.java modela
        Uplata uplata = new Uplata();
        uplata.setStudentPodaci(student); //
        uplata.setIznosRsd(iznosRsd);      //
        uplata.setIznosEur(iznosEur);      //
        uplata.setSrednjiKurs(kurs);       //
        uplata.setDatum(LocalDate.now());  //

        uplataRepository.save(uplata);

        // 4. Vraćamo odgovor koristeći tvoj konstruktor iz UplataResponse (datum, eur, rsd, kurs)
        // Proveri da li tvoj UplataResponse ima ovaj konstruktor
        return new UplataResponse(LocalDate.now(), iznosEur, iznosRsd, kurs);
    }

    public IznosPreostaliResponse getPreostaliIznos(Long studentIndeksId) {
        // 1. Pronalazimo indeks da bismo došli do studenta
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        final double ukupno = 3000.0; // SKOLARINA_EUR iz specifikacije

        // 2. Koristimo tvoju metodu findByStudentPodaciId iz UplataRepository
        List<Uplata> uplate = uplataRepository.findByStudentPodaciId(indeks.getStudent().getId());

        // 3. Sumiramo iznosEur (polje iz tvog modela)
        double uplacenoEur = uplate.stream()
                .mapToDouble(u -> u.getIznosEur() != null ? u.getIznosEur() : 0.0)
                .sum();

        double preostaloEur = ukupno - uplacenoEur;
        double danasnjiKurs = fetchDanasnjiKurs();

        // 4. Mapiramo na tvoj IznosPreostaliResponse
        IznosPreostaliResponse response = new IznosPreostaliResponse();
        response.setPreostaloEur(preostaloEur);
        response.setPreostaloRsd(preostaloEur * danasnjiKurs);

        return response;
    }
    private double fetchDanasnjiKurs() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            // Zvanični API iz specifikacije
            String url = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                Map<String, Object> result = (Map<String, Object>) response.get("result");
                // Uzimamo srednji kurs iz API odgovora
                return Double.parseDouble(result.get("srednji").toString());
            }
        } catch (Exception e) {
            // Logika u slučaju da API nije dostupan (fallback na 117.5)
            System.out.println("Greška pri dohvatanju kursa: " + e.getMessage());
        }
        return 117.5;
    }
    // Dodaj String indeksParam u argumente metode
    public Page<StudentPodaciResponse> searchStudente(String ime, String prezime, String indeksParam, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("student.ime").ascending());
        Page<StudentIndeks> studenti;

        // 1. LOGIKA ZA PRETRAGU PO INDEKSU (Najviši prioritet)
        if (indeksParam != null && !indeksParam.trim().isEmpty()) {
            String cleanIndeks = indeksParam.trim();

            if (cleanIndeks.contains("/")) {
                // Slučaj: 12/2023
                try {
                    String[] parts = cleanIndeks.split("/");
                    int broj = Integer.parseInt(parts[0]);
                    int godina = Integer.parseInt(parts[1]);
                    studenti = studentIndeksRepository.findByBrojAndGodina(broj, godina, pageable);
                } catch (NumberFormatException e) {
                    // Ako format nije dobar, vrati prazno ili sve
                    studenti = Page.empty();
                }
            } else {
                // Slučaj: samo 12
                try {
                    int broj = Integer.parseInt(cleanIndeks);
                    studenti = studentIndeksRepository.findByBroj(broj, pageable);
                } catch (NumberFormatException e) {
                    studenti = Page.empty();
                }
            }
        }
        // 2. LOGIKA ZA IME I PREZIME (Ako nije unet indeks)
        else if (ime != null && !ime.isEmpty() && prezime != null && !prezime.isEmpty()) {
            studenti = studentIndeksRepository
                    .findByStudent_ImeContainingIgnoreCaseAndStudent_PrezimeContainingIgnoreCase(ime, prezime, pageable);
        } else if (ime != null && !ime.isEmpty()) {
            studenti = studentIndeksRepository.findByStudent_ImeContainingIgnoreCase(ime, pageable);
        } else if (prezime != null && !prezime.isEmpty()) {
            studenti = studentIndeksRepository.findByStudent_PrezimeContainingIgnoreCase(prezime, pageable);
        } else {
            // Ako ništa nije uneto, vrati sve
            studenti = studentIndeksRepository.findAll(pageable);
        }

        // Mapiranje rezultata
        return studenti.map(indeks -> {
            StudentPodaci s = indeks.getStudent();
            StudentPodaciResponse r = new StudentPodaciResponse();
            r.setId(s.getId());
            r.setIme(s.getIme());
            r.setPrezime(s.getPrezime());
            r.setBrojIndeksa(indeks.getBroj()); // Uzmi broj direktno iz pronađenog indeksa
            r.setGodinaUpisa(indeks.getGodina());
            r.setEmailFakultet(s.getEmailFakultet());
            r.setJmbg(s.getJmbg());
            if (s.getSrednjaSkola() != null) {
                r.setSrednjaSkola(s.getSrednjaSkola().getNaziv());
            }
            return r;
        });

    }


    private double fetchEuroKurs() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.getForObject(KURS_API, Map.class);
            if (response != null && response.containsKey("srednji")) {
                return Double.parseDouble(response.get("srednji").toString());
            }
        } catch (Exception ignored) {}
        return 120.0; // fallback
    }
    public List<StudentPodaciResponse> getStudentiPoSrednjojSkoli(String srednjaSkola) {
        List<StudentIndeks> studenti = studentIndeksRepository.findByStudentSrednjaSkolaNazivContainingIgnoreCase(srednjaSkola);

        List<StudentPodaciResponse> responses = new ArrayList<>();
        for (StudentIndeks indeks : studenti) {
            StudentPodaci s = indeks.getStudent();
            StudentPodaciResponse r = new StudentPodaciResponse();

            // Osnovni podaci
            r.setId(s.getId());
            r.setIme(s.getIme());
            r.setPrezime(s.getPrezime());

            // Podaci o indeksu (OVO JE FALILO)
            r.setBrojIndeksa(indeks.getBroj());
            r.setGodinaUpisa(indeks.getGodina()); // Dodaj ovo da bi se video format 12/2023

            // Dodatni podaci (OVO JE FALILO)
            r.setJmbg(s.getJmbg()); // Setuješ JMBG iz entiteta StudentPodaci
            r.setEmailFakultet(s.getEmailFakultet()); // Setuješ email (pazi na naziv polja u entitetu)

            // Srednja škola
            if (s.getSrednjaSkola() != null) {
                r.setSrednjaSkola(s.getSrednjaSkola().getNaziv());
            }

            responses.add(r);
        }
        return responses;
    }
    public List<SrednjaSkolaResponse> getAllSrednjeSkole() {
        // 1. Povuci sve škole iz baze preko repozitorijuma
        List<SrednjaSkola> skole = srednjaSkolaRepository.findAll();

        // 2. Mapiraj entitete u Response objekte
        return skole.stream().map(skola -> {
            SrednjaSkolaResponse res = new SrednjaSkolaResponse();
            res.setId(skola.getId());
            res.setNaziv(skola.getNaziv());
            // Dodaj i ostala polja ako tvoj Response objekat to zahteva (npr. mesto)
            return res;
        }).collect(Collectors.toList());
    }
    @Transactional
    public void obrisiStudenta(Long studentId) {
        // 1. Provera postojanja
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student sa ID-jem " + studentId + " ne postoji."));

        // 2. Ručno raskidanje veza (opciono, ali sigurnije kod nekih verzija Hibernate-a)
        // Ako imaš polja koja nisu pokrivena kaskadom, ovde ih čistiš.

        try {
            studentRepository.delete(student);
            studentRepository.flush(); // Forsira bazu da izvrši SQL odmah kako bi uhvatili error ovde
        } catch (DataIntegrityViolationException e) {
            // Ako i pored kaskade baza ne dozvoljava brisanje (npr. zbog nekih trećih tabela)
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Nije moguće obrisati studenta zbog ograničenja integriteta u bazi podataka.");
        }
    }
    public List<UplataResponse> getSveUplate(Long studentId) {
        // 1. Provera postojanja studenta
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije nađen"));

        // 2. Povlačenje uplata iz baze
        List<Uplata> uplate = uplataRepository.findByStudentPodaciId(studentId);

        // 3. Mapiranje u Response
        return uplate.stream().map(u -> {
            UplataResponse r = new UplataResponse();
            r.setDatumUplate(u.getDatumUplate());
            r.setIznosRsd(u.getIznosRsd());
            r.setIznosEur(u.getIznosEur());
            r.setSrednjiKurs(u.getSrednjiKurs());
            // Mapiramo napomenu u svrhuUplate
            r.setSvrhaUplate(u.getNapomena() != null ? u.getNapomena() : "Uplata");
            return r;
        }).collect(Collectors.toList());
    }
    /*
    @Transactional
    public void obrisiStudenta(Long studentId) {
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student ne postoji"));

        // 1. Obrisi sve uplate
        uplataRepository.deleteByStudentPodaci(student);

        // 2. Obrisi sve indekse i zavisne entitete
        List<StudentIndeks> indeksi = studentIndeksRepository.findByStudent(student);
        for (StudentIndeks indeks : indeksi) {
            polozeniPredmetiRepository.deleteByStudentIndeks(indeks);
            upisGodineRepository.deleteByStudentIndeks(indeks);
            obnovaGodineRepository.deleteByStudentIndeks(indeks);
            studentIndeksRepository.delete(indeks);
        }

        // 3. Na kraju obrisi studenta
        studentRepository.delete(student);

    }
*/
    /*
	@Autowired
	StudentIndeksRepository studentIndeksRepo;
	
	@Autowired
	StudentPodaciRepository studentPodaciRepo;

	@Autowired
	SlusaPredmetRepository slusaPredmetRepo;
	
	@Autowired
	PredmetRepository predmetRepo;

	public StudentProfileDTO getStudentProfile(Long indeksId) {
		StudentProfileDTO retVal = new StudentProfileDTO();
		StudentIndeks studIndeks = studentIndeksRepo.findById(indeksId).get();		
		retVal.setIndeks(studIndeks);		
		retVal.setSlusaPredmete(slusaPredmetRepo.getSlusaPredmetForIndeksAktivnaGodina(indeksId));
		return retVal;
	}
	
	public StudentWebProfileDTO getStudentWebProfile(Long indeksId) {
		StudentWebProfileDTO retVal = new StudentWebProfileDTO();
		StudentIndeks studIndeks = studentIndeksRepo.findById(indeksId).get();
		Long studPodaciId = studIndeks.getStudent().getId();
		retVal.setAktivanIndeks(studentPodaciRepo.getAktivanIndeks(studPodaciId));		
		retVal.setSlusaPredmete(slusaPredmetRepo.getSlusaPredmetForIndeksAktivnaGodina(indeksId));
		return retVal;
	}

     */

}
