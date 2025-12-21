package org.raflab.studsluzba.services;

import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzba.model.dtos.StudentProfileDTO;
import org.raflab.studsluzba.model.dtos.StudentWebProfileDTO;
import org.raflab.studsluzba.model.dtos.UpisanaGodinaDTO;
import org.raflab.studsluzba.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final double SKOLARINA_EUR = 3000.0;
    private static final String KURS_API = "https://kurs.resenje.org/api/v1/currencies/eur/rates/today";

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("datumPolaganja").descending());

        Page<PolozeniPredmeti> polozeniPredmeti = polozeniPredmetiRepository.findByStudentIndeks(indeks, pageable);

        return polozeniPredmeti.map(p -> {
            PolozeniPredmetiResponse response = new PolozeniPredmetiResponse();
            response.setId(p.getId());
            response.setStudentIndeksId(indeks.getId());
            response.setStudentImePrezime(indeks.getStudent().getIme() + " " + indeks.getStudent().getPrezime());
            response.setPredmetId(p.getPredmet().getId());
            response.setPredmetNaziv(p.getPredmet().getNaziv());
            response.setOcena(p.getOcena());
            response.setPriznat(p.isPriznat());
            response.setIzlazakNaIspitId(p.getIzlazakNaIspit() != null ? p.getIzlazakNaIspit().getId() : null);
            return response;
        });
    }

    public Page<PolozeniPredmetiResponse> getNepolozeniPredmeti(Integer brojIndeksa, int page, int size) {
        StudentIndeks indeks = studentIndeksRepository.findByBroj(brojIndeksa)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("predmet.naziv").ascending());

        Page<PolozeniPredmeti> nepolozeni = polozeniPredmetiRepository.findByStudentIndeksAndOcenaIsNull(indeks, pageable);

        return nepolozeni.map(p -> {
            PolozeniPredmetiResponse r = new PolozeniPredmetiResponse();
            r.setId(p.getId());
            r.setStudentIndeksId(indeks.getId());
            r.setStudentImePrezime(indeks.getStudent().getIme() + " " + indeks.getStudent().getPrezime());
            r.setPredmetId(p.getPredmet().getId());
            r.setPredmetNaziv(p.getPredmet().getNaziv());
            r.setOcena(p.getOcena()); // biće null
            r.setPriznat(p.isPriznat());
            r.setIzlazakNaIspitId(p.getIzlazakNaIspit() != null ? p.getIzlazakNaIspit().getId() : null);
            return r;
        });
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

        UpisGodine upis = new UpisGodine();
        upis.setStudentIndeks(indeks);
        upis.setGodinaStudija(request.getGodinaStudija());
        upis.setDatumUpisa(request.getDatum());
        upis.setNapomena(request.getNapomena());
        SkolskaGodina skolskaGodina = skolskaGodinaRepository.findByAktivnaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Školska godina ne postoji"));


        upis.setSkolskaGodina(skolskaGodina);


        if (request.getPrenetiPredmetiIds() != null && !request.getPrenetiPredmetiIds().isEmpty()) {
            List<Predmet> predmeti = predmetRepository.findAllById(request.getPrenetiPredmetiIds());
            if (predmeti.size() != request.getPrenetiPredmetiIds().size()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Neki predmeti ne postoje");
            }
            upis.setPredmeti(predmeti);
        } else {
            upis.setPredmeti(new ArrayList<>()); // inicijalno prazna lista
        }

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
    public UplataResponse dodajUplatu(Long studentIndeksId, UplataRequest request) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        double kurs = fetchEuroKurs();
        Uplata uplata = new Uplata(indeks.getStudent(), request.getDatumUplate(), request.getIznos() * kurs, kurs);

        uplataRepository.save(uplata);

        UplataResponse response = new UplataResponse();
        response.setDatumUplate(uplata.getDatumUplate());
        response.setIznosEur(request.getIznos());
        response.setIznosRsd(uplata.getIznosRsd());
        response.setSrednjiKurs(kurs);
        return response;
    }
    public IznosPreostaliResponse getPreostaliIznos(Long studentIndeksId) {
        StudentIndeks indeks = studentIndeksRepository.findById(studentIndeksId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student indeks ne postoji"));

        // Ukupan iznos školarine u EUR
        double ukupno = SKOLARINA_EUR;

        // Suma svih uplata u EUR
        List<Uplata> uplate = uplataRepository.findByStudentPodaci_Id(indeks.getStudent().getId());
        double uplacenoEur = uplate.stream().mapToDouble(u -> u.getIznosRsd() / u.getSrednjiKurs()).sum();

        double preostaloEur = ukupno - uplacenoEur;
        double preostaloRsd = preostaloEur * (uplate.isEmpty() ? 120.0 : uplate.get(uplate.size() - 1).getSrednjiKurs());

        IznosPreostaliResponse response = new IznosPreostaliResponse();
        response.setPreostaloEur(preostaloEur);
        response.setPreostaloRsd(preostaloRsd);
        return response;
    }
    public Page<StudentPodaciResponse> searchStudente(String ime, String prezime, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("student.ime").ascending());
        Page<StudentIndeks> studenti;

        if (ime != null && !ime.isEmpty() && prezime != null && !prezime.isEmpty()) {
            studenti = studentIndeksRepository
                    .findByStudent_ImeContainingIgnoreCaseAndStudent_PrezimeContainingIgnoreCase(ime, prezime, pageable);
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
            r.setBrojIndeksa(indeks.getBroj()); // polje broj iz StudentIndeks
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
            r.setId(s.getId());
            r.setIme(s.getIme());
            r.setPrezime(s.getPrezime());
            r.setBrojIndeksa(indeks.getBroj());
            if (s.getSrednjaSkola() != null) {
                r.setSrednjaSkola(s.getSrednjaSkola().getNaziv()); // ili .getIme() zavisi od imena polja u entitetu
            }

            responses.add(r);
        }
        return responses;
    }
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
