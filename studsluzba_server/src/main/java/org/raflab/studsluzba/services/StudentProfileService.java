package org.raflab.studsluzba.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class StudentProfileService {

    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private UpisGodineRepository upisGodineRepository;
    @Autowired
    private ObnovaGodineRepository obnovaGodineRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private UplataRepository uplataRepository;
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
        r.setMestoPrebivalista(s.getMestoStanovanja());
        r.setDrzavaRodjenja(s.getDrzavaRodjenja());
        r.setDrzavljanstvo(s.getDrzavljanstvo());
        r.setNacionalnost(s.getNacionalnost());
        r.setGodinaUpisa(indeks != null ? indeks.getGodina() : 0);
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

        if (indeks != null) {
            r.setBrojIndeksa(indeks.getBroj());
            // --- OVO JE KLJUČNA ISPRAVKA: Šaljemo pravi ID indeksa klijentu ---
            r.setStudentIndeksId(indeks.getId());
        }

        if (s.getSrednjaSkola() != null) {
            r.setSrednjaSkola(s.getSrednjaSkola().getNaziv());
        }

        return r;
    }

    public StudentPodaciResponse dodajStudenta(StudentPodaciRequest request) {
        if (studentRepository.existsByJmbg(request.getJmbg())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student sa JMBG " + request.getJmbg() + " već postoji.");
        }
        if (studentIndeksRepository.existsByBrojAndGodinaAndStudProgramOznaka(
                request.getBrojIndeksa(), request.getGodinaUpisa(), request.getStudProgramOznaka())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Indeks već postoji u sistemu.");
        }

        StudijskiProgram program = studijskiProgramRepository.findByOznaka(request.getStudProgramOznaka())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Studijski program nije pronađen."));

        SrednjaSkola skola = srednjaSkolaRepository.findById(request.getSrednjaSkolaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Srednja škola nije nađena u šifarniku."));

        StudentPodaci student = new StudentPodaci();
        student.setIme(request.getIme());
        student.setPrezime(request.getPrezime());
        student.setJmbg(request.getJmbg());
        student.setSrednjaSkola(skola);
        student = studentRepository.save(student);

        StudentIndeks indeks = new StudentIndeks();
        indeks.setBroj(request.getBrojIndeksa());
        indeks.setGodina(request.getGodinaUpisa());
        indeks.setStudProgramOznaka(request.getStudProgramOznaka());
        indeks.setStudijskiProgram(program);
        indeks.setStudent(student);
        indeks.setAktivan(true);
        indeks.setVaziOd(LocalDate.now());
        indeks.setNacinFinansiranja(request.getNacinFinansiranja());

        studentIndeksRepository.save(indeks);

        return mapToStudentResponse(student, indeks);
    }

    public StudentPodaciResponse getStudentByBrojIndeksa(Integer brojIndeksa) {
        StudentIndeks indeks = studentIndeksRepository.findByBroj(brojIndeksa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));
        StudentPodaci s = indeks.getStudent();
        return mapToStudentResponse(s, indeks);
    }

    public Page<PolozeniPredmetiResponse> getPolozeniPredmeti(Long studentIndeksId, int page, int size) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indeks nije pronađen"));

        List<PolozeniPredmeti> sviZapisi = polozeniPredmetiRepository.findByStudentIndeks(indeks);

        Map<Long, PolozeniPredmetiResponse> unikatniPolozeni = sviZapisi.stream()
                .filter(p -> p.getOcena() != null && p.getOcena() > 5)
                .map(p -> {
                    PolozeniPredmetiResponse response = new PolozeniPredmetiResponse();
                    response.setId(p.getId());
                    response.setPredmetNaziv(p.getPredmet().getNaziv());
                    response.setOcena(p.getOcena());
                    response.setEspb(p.getPredmet().getEspb());
                    if (p.getIzlazakNaIspit() != null &&
                            p.getIzlazakNaIspit().getPrijavaIspita() != null &&
                            p.getIzlazakNaIspit().getPrijavaIspita().getIspit() != null) {
                        response.setDatumPolaganja(p.getIzlazakNaIspit().getPrijavaIspita().getIspit().getDatumOdrzavanja());
                    }
                    return response;
                })
                .collect(Collectors.toMap(
                        res -> res.getPredmetId(),
                        res -> res,
                        (stari, novi) -> stari
                ));

        List<PolozeniPredmetiResponse> finalnaLista = new ArrayList<>(unikatniPolozeni.values());
        int start = Math.min(page * size, finalnaLista.size());
        int end = Math.min((start + size), finalnaLista.size());

        return new PageImpl<>(finalnaLista.subList(start, end), PageRequest.of(page, size), finalnaLista.size());
    }

    // --- OBAVEZNO PROMENI IMPORT NA VRHU KLASE: ---
    // import org.raflab.studsluzba.controllers.response.NepolozeniPredmetResponse;

    @Transactional(readOnly = true)
    public Page<NepolozeniPredmetResponse> getNepolozeniPredmetiByBroj(Integer brojIndeksa, Pageable pageable) {
        StudentIndeks indeks = studentIndeksRepository.findByBroj(brojIndeksa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Indeks nije pronađen"));

        Set<String> polozeneSifre = polozeniPredmetiRepository.findByStudentIndeks(indeks)
                .stream()
                .filter(pp -> pp.getOcena() != null && pp.getOcena() > 5)
                .map(pp -> pp.getPredmet().getSifra())
                .collect(Collectors.toSet());

        List<SlusaPredmet> slusaList = slusaPredmetRepository.findByStudentIndeks(indeks);

        Map<String, NepolozeniPredmetResponse> filtrirano = slusaList.stream()
                .map(sp -> {
                    Predmet p = sp.getDrziPredmet().getPredmet();

                    // --- KREIRAMO NOVI RESPONSE OBJEKAT ---
                    NepolozeniPredmetResponse resp = new NepolozeniPredmetResponse();
                    resp.setId(sp.getId());
                    resp.setPredmetId(p.getId()); // Popunjavamo ID predmeta
                    resp.setSifraPredmeta(p.getSifra());
                    resp.setNazivPredmeta(p.getNaziv());
                    resp.setEspb(p.getEspb());
                    return resp;
                })
                .filter(dto -> !polozeneSifre.contains(dto.getSifraPredmeta()))
                .collect(Collectors.toMap(
                        NepolozeniPredmetResponse::getSifraPredmeta,
                        dto -> dto,
                        (existing, replacement) -> existing
                ));

        List<NepolozeniPredmetResponse> rezultat = new ArrayList<>(filtrirano.values());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), rezultat.size());
        if (start > end) start = end;

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

        List<Predmet> predmeti = predmetRepository.findAllById(request.getPrenetiPredmetiIds());
        int ukupnoEspb = predmeti.stream().mapToInt(Predmet::getEspb).sum();

        if (ukupnoEspb > 60) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Maksimalni zbir ESPB poena je 60. Pokušano: " + ukupnoEspb);
        }

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

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Školska godina ne postoji"));

        ObnovaGodine obnova = new ObnovaGodine();
        obnova.setStudentIndeks(indeks);
        obnova.setGodinaStudija(request.getGodinaStudija());
        obnova.setDatum(request.getDatum());
        obnova.setNapomena(request.getNapomena());
        obnova.setSkolskaGodina(skolskaGodina);

        List<Predmet> predmeti = new ArrayList<>();
        List<PolozeniPredmeti> nepolozeniPredmeti = polozeniPredmetiRepository
                .findNepolozeniByStudentIndeks(indeks.getId());
        for (PolozeniPredmeti p : nepolozeniPredmeti) {
            predmeti.add(p.getPredmet());
        }

        if (request.getPredmetIds() != null && !request.getPredmetIds().isEmpty()) {
            List<Predmet> narednaGodinaPredmeti = predmetRepository.findAllById(request.getPredmetIds());
            predmeti.addAll(narednaGodinaPredmeti);
        }

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

        ObnovaGodine obnova = new ObnovaGodine();
        obnova.setStudentIndeks(indeks);
        obnova.setGodinaStudija(request.getGodinaStudija());
        obnova.setDatum(request.getDatum());
        obnova.setNapomena(request.getNapomena());

        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Školska godina ne postoji"));
        obnova.setSkolskaGodina(skolskaGodina);

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

    // ------------------ UPLATE I FINANSIJE ------------------

    @Transactional
    public Long createUplata(UplataRequest request) {
        StudentPodaci student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije nađen"));

        double kurs = fetchDanasnjiKurs();
        double iznosRsd = request.getIznosRsd() != null ? request.getIznosRsd() : 0.0;
        double iznosEur = iznosRsd / kurs;

        Uplata uplata = new Uplata();
        uplata.setStudentPodaci(student);
        uplata.setIznosRsd(iznosRsd);
        uplata.setIznosEur(iznosEur);
        uplata.setSrednjiKurs(kurs);
        uplata.setNapomena(request.getNapomena());

        if (request.getDatumUplate() != null) {
            uplata.setDatum(request.getDatumUplate());
        } else {
            uplata.setDatum(LocalDate.now());
        }

        uplataRepository.save(uplata);
        return uplata.getId();
    }

    @Transactional
    public UplataResponse evidentirajUplatu(Long studentId, Double iznosRsd) {
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije nađen"));

        double kurs = fetchDanasnjiKurs();
        double iznosEur = iznosRsd / kurs;

        Uplata uplata = new Uplata();
        uplata.setStudentPodaci(student);
        uplata.setIznosRsd(iznosRsd);
        uplata.setIznosEur(iznosEur);
        uplata.setSrednjiKurs(kurs);
        uplata.setDatum(LocalDate.now());

        uplataRepository.save(uplata);
        return new UplataResponse(LocalDate.now(), iznosEur, iznosRsd, kurs);
    }

    public List<UplataResponse> getSveUplate(Long studentId) {
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student nije nađen"));
        List<Uplata> uplate = uplataRepository.findByStudentPodaciId(studentId);
        return uplate.stream().map(u -> {
            UplataResponse r = new UplataResponse();
            r.setDatumUplate(u.getDatumUplate());
            r.setIznosRsd(u.getIznosRsd());
            r.setIznosEur(u.getIznosEur());
            r.setSrednjiKurs(u.getSrednjiKurs());
            r.setSvrhaUplate(u.getNapomena() != null ? u.getNapomena() : "Uplata");
            return r;
        }).collect(Collectors.toList());
    }

    public IznosPreostaliResponse getPreostaliIznos(Long studentIndeksId) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        final double ukupno = SKOLARINA_EUR;
        List<Uplata> uplate = uplataRepository.findByStudentPodaciId(indeks.getStudent().getId());

        double uplacenoEur = uplate.stream()
                .mapToDouble(u -> u.getIznosEur() != null ? u.getIznosEur() : 0.0)
                .sum();

        double preostaloEur = ukupno - uplacenoEur;
        double danasnjiKurs = fetchDanasnjiKurs();

        IznosPreostaliResponse response = new IznosPreostaliResponse();
        response.setPreostaloEur(preostaloEur);
        response.setPreostaloRsd(preostaloEur * danasnjiKurs);

        return response;
    }

    private double fetchDanasnjiKurs() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = KURS_API;
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("result")) {
                Map<String, Object> result = (Map<String, Object>) response.get("result");
                return Double.parseDouble(result.get("srednji").toString());
            }
        } catch (Exception e) {
            System.out.println("Greška pri dohvatanju kursa: " + e.getMessage());
        }
        return 117.5; // Fallback
    }

    public Page<StudentPodaciResponse> searchStudente(String ime, String prezime, String indeksParam, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("student.ime").ascending());
        Page<StudentIndeks> studenti;

        if (indeksParam != null && !indeksParam.trim().isEmpty()) {
            String cleanIndeks = indeksParam.trim();
            if (cleanIndeks.contains("/")) {
                try {
                    String[] parts = cleanIndeks.split("/");
                    int broj = Integer.parseInt(parts[0]);
                    int godina = Integer.parseInt(parts[1]);
                    studenti = studentIndeksRepository.findByBrojAndGodina(broj, godina, pageable);
                } catch (NumberFormatException e) {
                    studenti = Page.empty();
                }
            } else {
                try {
                    int broj = Integer.parseInt(cleanIndeks);
                    studenti = studentIndeksRepository.findByBroj(broj, pageable);
                } catch (NumberFormatException e) {
                    studenti = Page.empty();
                }
            }
        } else if (ime != null && !ime.isEmpty() && prezime != null && !prezime.isEmpty()) {
            studenti = studentIndeksRepository.findByStudent_ImeContainingIgnoreCaseAndStudent_PrezimeContainingIgnoreCase(ime, prezime, pageable);
        } else if (ime != null && !ime.isEmpty()) {
            studenti = studentIndeksRepository.findByStudent_ImeContainingIgnoreCase(ime, pageable);
        } else if (prezime != null && !prezime.isEmpty()) {
            studenti = studentIndeksRepository.findByStudent_PrezimeContainingIgnoreCase(prezime, pageable);
        } else {
            studenti = studentIndeksRepository.findAll(pageable);
        }

        return studenti.map(indeks -> {
            StudentPodaci s = indeks.getStudent();
            StudentPodaciResponse r = new StudentPodaciResponse();
            r.setId(s.getId());
            r.setIme(s.getIme());
            r.setPrezime(s.getPrezime());
            r.setBrojIndeksa(indeks.getBroj());
            r.setGodinaUpisa(indeks.getGodina());
            r.setEmailFakultet(s.getEmailFakultet());
            r.setJmbg(s.getJmbg());
            if (s.getSrednjaSkola() != null) {
                r.setSrednjaSkola(s.getSrednjaSkola().getNaziv());
            }
            // --- POPUNJAVAMO ID INDEKSA I OVDE ---
            r.setStudentIndeksId(indeks.getId());
            return r;
        });
    }

    public List<StudentPodaciResponse> getStudentiPoSrednjojSkoli(String srednjaSkola) {
        List<StudentIndeks> studenti = studentIndeksRepository.findByStudentSrednjaSkolaNazivContainingIgnoreCase(srednjaSkola);
        List<StudentPodaciResponse> responses = new ArrayList<>();
        for (StudentIndeks indeks : studenti) {
            StudentPodaci s = indeks.getStudent();
            StudentPodaciResponse r = new StudentPodaciResponse();
            r.setId(s.getId());
            r.setIme(s.getIme());
            r.setPrezime(s.getPrezime());
            r.setBrojIndeksa(indeks.getBroj());
            r.setGodinaUpisa(indeks.getGodina());
            r.setJmbg(s.getJmbg());
            r.setEmailFakultet(s.getEmailFakultet());
            if (s.getSrednjaSkola() != null) {
                r.setSrednjaSkola(s.getSrednjaSkola().getNaziv());
            }
            // --- POPUNJAVAMO ID INDEKSA I OVDE ---
            r.setStudentIndeksId(indeks.getId());
            responses.add(r);
        }
        return responses;
    }

    public List<SrednjaSkolaResponse> getAllSrednjeSkole() {
        List<SrednjaSkola> skole = srednjaSkolaRepository.findAll();
        return skole.stream().map(skola -> {
            SrednjaSkolaResponse res = new SrednjaSkolaResponse();
            res.setId(skola.getId());
            res.setNaziv(skola.getNaziv());
            return res;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void obrisiStudenta(Long studentId) {
        StudentPodaci student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Student sa ID-jem " + studentId + " ne postoji."));
        try {
            studentRepository.delete(student);
            studentRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Nije moguće obrisati studenta zbog ograničenja integriteta u bazi podataka.");
        }
    }
}