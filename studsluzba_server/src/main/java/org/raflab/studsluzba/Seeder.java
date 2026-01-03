package org.raflab.studsluzba;

import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

//@Component
public class Seeder implements CommandLineRunner {

    @Autowired private StudijskiProgramRepository studijskiProgramRepository;
    @Autowired private PredmetRepository predmetRepository;
    @Autowired private NastavnikRepository nastavnikRepository;
    @Autowired private NastavnikZvanjeRepository nastavnikZvanjeRepository;
    @Autowired private StudentPodaciRepository studentPodaciRepository;
    @Autowired private StudentIndeksRepository studentIndeksRepository;
    @Autowired private DrziPredmetRepository drziPredmetRepository;
    @Autowired private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired private PredispitneObavezeRepository predispitneObavezeRepository;
    @Autowired private PredispitniPoeniRepository predispitniPoeniRepository;
    @Autowired private VrstaStudijaRepository vrstaStudijaRepository;
    @Autowired private IspitRepository ispitRepository;
    @Autowired private IspitniRokRepository ispitniRokRepository;
    @Autowired private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired private PolozeniPredmetiRepository polozeniPredmetiRepository;
    @Autowired private UplataRepository uplataRepository;
    @Autowired private UpisGodineRepository upisGodineRepository;
    @Autowired private ObnovaGodineRepository obnovaGodineRepository;
    @Autowired private SrednjaSkolaRepository srednjaSkolaRepository;
    @Autowired private VisokoskolskaUstanovaRepository ustanovaRepository;
    @Autowired private GrupaRepository grupaRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. VRSTA STUDIJA
        VrstaStudija oas = vrstaStudijaRepository.save(new VrstaStudija("OAS", "Osnovne akademske studije"));
        VrstaStudija mas = vrstaStudijaRepository.save(new VrstaStudija("MAS", "Master akademske studije"));

        // 2. STUDIJSKI PROGRAMI
        StudijskiProgram rn = new StudijskiProgram("RN", "Racunarske nauke", 2019, 8);
        rn.setVrstaStudija(oas);
        studijskiProgramRepository.save(rn);

        StudijskiProgram si = new StudijskiProgram("SI", "Softversko inzenjerstvo", 2020, 8);
        si.setVrstaStudija(oas);
        studijskiProgramRepository.save(si);

        StudijskiProgram ri = new StudijskiProgram("RI", "Racunarsko inzenjerstvo", 2021, 8);
        ri.setVrstaStudija(oas);
        studijskiProgramRepository.save(ri);

        // 1. OSVEŽENE ŠKOLSKE GODINE (2025/26)
        SkolskaGodina sgStara = new SkolskaGodina();
        sgStara.setNaziv("2024/25"); sgStara.setAktivna(false);
        sgStara.setPocetakZimskog(LocalDate.of(2024, 10, 1)); sgStara.setKrajZimskog(LocalDate.of(2025, 2, 1));
        sgStara.setPocetakLetnjeg(LocalDate.of(2025, 2, 20)); sgStara.setKrajLetnjeg(LocalDate.of(2025, 6, 15));
        skolskaGodinaRepository.save(sgStara);

        SkolskaGodina sgAktivna = new SkolskaGodina();
        sgAktivna.setNaziv("2025/26"); sgAktivna.setAktivna(true);
        sgAktivna.setPocetakZimskog(LocalDate.of(2025, 10, 1)); sgAktivna.setKrajZimskog(LocalDate.of(2026, 2, 1));
        sgAktivna.setPocetakLetnjeg(LocalDate.of(2026, 2, 20)); sgAktivna.setKrajLetnjeg(LocalDate.of(2026, 6, 15));
        skolskaGodinaRepository.save(sgAktivna);

        // 4. NASTAVNICI I VIŠE ZVANJA
        List<Nastavnik> nastavnici = new ArrayList<>();
        VisokoskolskaUstanova raf = new VisokoskolskaUstanova();
        raf.setNaziv("RAF"); raf.setMesto("Beograd"); ustanovaRepository.save(raf);

        for(int i=1; i<=3; i++) {
            Nastavnik n = new Nastavnik();
            n.setIme("Nastavnik" + i); n.setPrezime("Prezime" + i); n.setEmail("n"+i+"@raf.rs"); n.setPol('M');
            n.setJmbg("111111111111"+i); n.setObrazovanja(Collections.singleton(raf));
            nastavnikRepository.save(n);
            nastavnici.add(n);

            NastavnikZvanje nz1 = new NastavnikZvanje();
            nz1.setDatumIzbora(LocalDate.of(2015, 1, 1)); nz1.setZvanje("Docent");
            nz1.setNaucnaOblast("Informatika"); nz1.setAktivno(false); nz1.setNastavnik(n);
            nastavnikZvanjeRepository.save(nz1);

            NastavnikZvanje nz2 = new NastavnikZvanje();
            nz2.setDatumIzbora(LocalDate.of(2020, 1, 1)); nz2.setZvanje("Profesor");
            nz2.setNaucnaOblast("Informatika"); nz2.setAktivno(true); nz2.setNastavnik(n);
            nastavnikZvanjeRepository.save(nz2);
        }

        // 5. PREDMETI I PREDISPITNE OBAVEZE (Masovno popunjavanje)
        List<Predmet> predmeti = new ArrayList<>();
        List<DrziPredmet> drziList = new ArrayList<>();
        for(int i=1; i<=8; i++) {
            Predmet p = new Predmet();
            p.setSifra("P" + i); p.setNaziv("Predmet " + i); p.setEspb(6); p.setStudProgram(rn);
            predmetRepository.save(p);
            predmeti.add(p);

            DrziPredmet dp = new DrziPredmet();
            dp.setPredmet(p); dp.setNastavnik(nastavnici.get(i % 3)); dp.setSkolskaGodina(sgAktivna);
            drziPredmetRepository.save(dp);
            drziList.add(dp);

            // Popunjavamo tabelu PREDISPITNE OBAVEZE (po 2 za svaki predmet)
            PredispitneObaveze po1 = new PredispitneObaveze();
            po1.setVrsta("Kolokvijum 1"); po1.setMaksPoeni(20); po1.setDrziPredmet(dp); po1.setSkolskaGodina(sgAktivna);
            predispitneObavezeRepository.save(po1);

            PredispitneObaveze po2 = new PredispitneObaveze();
            po2.setVrsta("Kolokvijum 2"); po2.setMaksPoeni(20); po2.setDrziPredmet(dp); po2.setSkolskaGodina(sgAktivna);
            predispitneObavezeRepository.save(po2);
        }
        List<Grupa> grupe = new ArrayList<>();
        for(int i = 1; i <= 2; i++) {
            Grupa g = new Grupa();
            g.setStudijskiProgram(rn); // Povezujemo sa programom (npr. RN)

            // Dodela predmeta grupi (puni tabelu grupa_predmeti)
            // Grupa 1 dobija prva 4 predmeta, Grupa 2 ostale
            if(i == 1) {
                g.setPredmeti(predmeti.subList(0, 4));
            } else {
                g.setPredmeti(predmeti.subList(4, 8));
            }

            grupe.add(grupaRepository.save(g));
        }

        // 6. ROKOVI
        String[] naziviRokova = {"Januar", "Februar", "Jun", "Jul", "Septembar"};
        List<IspitniRok> rokovi = new ArrayList<>();
        for(String r : naziviRokova) {
            IspitniRok ir = new IspitniRok();
            ir.setNaziv(r + " 2024"); ir.setSkolskaGodina(sgAktivna); ir.setAktivan(true);
            ir.setDatumPocetka(LocalDate.now()); ir.setDatumZavrsetka(LocalDate.now().plusDays(15));
            rokovi.add(ispitniRokRepository.save(ir));
        }

        // 7. STUDENTI, INDEKSI, UPIS I POENI
        SrednjaSkola ss = srednjaSkolaRepository.save(new SrednjaSkola("Gimnazija", "Beograd", "Gimnazija"));
        for(int i=1; i<=5; i++) {
            StudentPodaci sp = new StudentPodaci();
            sp.setIme("Ime"+i); sp.setPrezime("Prezime"+i); sp.setJmbg("010199971000"+i);
            sp.setAdresaStanovanja("Ulica "+i); sp.setMestoStanovanja("Beograd");
            sp.setSrednjaSkola(ss); sp.setUspehSrednjaSkola(5.0); sp.setUspehPrijemni(100.0);
            sp.setPol('M'); sp.setEmailFakultet("s"+i+"@raf.rs");
            studentPodaciRepository.save(sp);

            // Neaktivan i aktivan indeks
            StudentIndeks siStar = new StudentIndeks();
            siStar.setBroj(i+100); siStar.setGodina(2022); siStar.setAktivan(false); siStar.setStudent(sp);
            studentIndeksRepository.save(siStar);

            StudentIndeks siAktivan = new StudentIndeks();
            siAktivan.setBroj(i); siAktivan.setGodina(2023); siAktivan.setAktivan(true);
            siAktivan.setStudent(sp); siAktivan.setStudijskiProgram(rn); siAktivan.setStudProgramOznaka("RN");
            studentIndeksRepository.save(siAktivan);

            // UPIS GODINE I PREDMETA (Tabela upis_godine_predmet)
            UpisGodine upis = new UpisGodine();
            upis.setStudentIndeks(siAktivan); upis.setSkolskaGodina(sgAktivna);
            upis.setGodinaStudija(1); upis.setDatumUpisa(LocalDate.now());
            for(int j=0; j<5; j++) upis.addPredmet(predmeti.get(j));
            upisGodineRepository.save(upis);

            // UPLATE
            Uplata u = new Uplata();
            u.setStudentPodaci(sp); u.setIznosRsd(50000.0); u.setSrednjiKurs(117.2); u.setDatumUplate(LocalDate.now());
            uplataRepository.save(u);

            // 8. SLUSA PREDMET I PREDISPITNI POENI (Tabela predispitni_poeni)
            for(int j=0; j<5; j++) {
                SlusaPredmet sl = new SlusaPredmet();
                sl.setStudentIndeks(siAktivan); sl.setDrziPredmet(drziList.get(j)); sl.setSkolskaGodina(sgAktivna);
                slusaPredmetRepository.save(sl);

                // Dodela poena za obaveze na tom predmetu
                DrziPredmet dp = drziList.get(j);
                List<PredispitneObaveze> obaveze = predispitneObavezeRepository.findAll();
                for(PredispitneObaveze po : obaveze) {
                    if(po.getDrziPredmet().getId().equals(dp.getId())) {
                        PredispitniPoeni pp = new PredispitniPoeni();
                        pp.setPoeni(18); pp.setStudentIndeks(siAktivan); pp.setSkolskaGodina(sgAktivna);
                        pp.setPredispitnaObaveza(po); pp.setSlusaPredmet(sl);
                        predispitniPoeniRepository.save(pp);
                    }
                }

                // 9. ISPITI I POLAGANJA (5-6 po studentu za prosek)
                Ispit isp = new Ispit(LocalDate.now(), predmeti.get(j), dp, LocalTime.of(9,0), true, "Sala 1", rokovi.get(0));
                ispitRepository.save(isp);

                PrijavaIspita prijava = new PrijavaIspita();
                prijava.setIspit(isp); prijava.setStudentIndeks(siAktivan); prijava.setIzasao(true);
                prijavaIspitaRepository.save(prijava);

                IzlazakNaIspit izl = new IzlazakNaIspit();
                izl.setPrijavaIspita(prijava); izl.setSlusaPredmet(sl); izl.setStudentIndeks(siAktivan);
                izl.setPoeniPredispitne(36); izl.setPoeniIspit(40); // Ukupno 76 -> Ocena 8
                izl.setIzasao(true);
                izlazakNaIspitRepository.save(izl);

                PolozeniPredmeti pol = new PolozeniPredmeti();
                pol.setStudentIndeks(siAktivan); pol.setPredmet(predmeti.get(j));
                pol.setOcena(8); pol.setDatumPolaganja(LocalDate.now()); pol.setIzlazakNaIspit(izl);
                polozeniPredmetiRepository.save(pol);
            }
        }

        // 10. OBNOVA GODINE
        ObnovaGodine obn = new ObnovaGodine();
        obn.setStudentIndeks(studentIndeksRepository.findAll().get(1)); // Bilo koji student
        obn.setSkolskaGodina(sgAktivna); obn.setGodinaStudija(1); obn.setDatum(LocalDate.now());
        obn.setPredmetiKojeUpisuje(Collections.singletonList(predmeti.get(0)));
        obnovaGodineRepository.save(obn);

        System.out.println("SEEDER: Baza je kompletno popunjena (Predispitne obaveze, Poeni, Upis predmeta, Položeni)!");
    }
}
