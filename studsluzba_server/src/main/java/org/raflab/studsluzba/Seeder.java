package org.raflab.studsluzba;

import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.*;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    private StudijskiProgramRepository studijskiProgramRepository;
    @Autowired
    private PredmetRepository predmetRepository;
    @Autowired
    private NastavnikRepository nastavnikRepository;
    @Autowired
    private NastavnikZvanjeRepository nastavnikZvanjeRepository;
    @Autowired
    private StudentPodaciRepository studentPodaciRepository;
    @Autowired
    private StudentIndeksRepository studentIndeksRepository;
    @Autowired
    private DrziPredmetRepository drziPredmetRepository;
    @Autowired
    private SlusaPredmetRepository slusaPredmetRepository;
    @Autowired
    private GrupaRepository grupaRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired
    private PredispitneObavezeRepository predispitneObevezeRepository;
    @Autowired
    private VrstaStudijaRepository vrstaStudijaRepository;
    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private PredispitniPoeniRepository predispitniPoeniRepository;
    @Autowired
    private UplataRepository uplataRepository;
    @Autowired
    private UpisGodineRepository upisGodineRepository;
    @Autowired
    private ObnovaGodineRepository obnovaGodineRepository;
    @Autowired
    private SrednjaSkolaRepository srednjaSkolaRepository;

    @Override
    public void run(String... args) throws Exception {

        // ========== POSTOJEĆI KOD (OSTAVI KAO ŠTO JE) ==========

        List<StudijskiProgram> spList = new ArrayList<>();
        VrstaStudija oas = new VrstaStudija("OAS", "Osnovne akademske studije");
        vrstaStudijaRepository.save(oas);

        for (int i = 1; i <= 5; i++) {
            StudijskiProgram sp = new StudijskiProgram();
            sp.setOznaka("SP" + i);
            sp.setNaziv("Program " + i);
            sp.setGodinaAkreditacije(2020 + i);
            sp.setNaziv("Bachelor");
            sp.setTrajanjeSemestara(8);
            sp.setVrstaStudija(oas);
            spList.add(studijskiProgramRepository.save(sp));
        }

        List<Predmet> predmetList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {  //  POVEĆAJ NA 10 PREDMETA
            Predmet p = new Predmet();
            p.setSifra("PR" + i);
            p.setNaziv("Predmet " + i);
            p.setOpis("Opis predmeta " + i);
            p.setEspb(6 + (i % 5));
            p.setStudProgram(spList.get((i - 1) % spList.size()));
            p.setObavezan(i % 2 == 0);
            predmetList.add(predmetRepository.save(p));
        }

        List<Nastavnik> nastavnikList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Nastavnik n = new Nastavnik();
            n.setIme("Nastavnik" + i);
            n.setPrezime("Prezime" + i);
            n.setSrednjeIme("Srednje" + i);
            n.setEmail("nastavnik" + i + "@example.com");
            n.setBrojTelefona("06012345" + i);
            n.setAdresa("Adresa " + i);
            n.setDatumRodjenja(LocalDate.of(1980 + i, i, i));
            n.setPol(i % 2 == 0 ? 'M' : 'F');
            n.setJmbg("80010123456" + i);
            nastavnikList.add(nastavnikRepository.save(n));
        }

        List<NastavnikZvanje> zvanjeList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            NastavnikZvanje nz = new NastavnikZvanje();
            nz.setDatumIzbora(LocalDate.of(2020 + i, i, i));
            nz.setNaucnaOblast("Oblast " + i);
            nz.setUzaNaucnaOblast("Uza oblast " + i);
            nz.setZvanje("Zvanje " + i);
            nz.setAktivno(i % 2 == 0);
            nz.setNastavnik(nastavnikList.get(i - 1));
            zvanjeList.add(nastavnikZvanjeRepository.save(nz));
        }

        //  DODAJ SREDNJE ŠKOLE
        List<SrednjaSkola> srednjeSkolee = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            SrednjaSkola ss = new SrednjaSkola();
            ss.setNaziv("Srednja škola " + i);
            ss.setMesto("Grad " + i);
            ss.setVrsta(i == 1 ? "Gimnazija" : "Stručna škola");
            srednjeSkolee.add(srednjaSkolaRepository.save(ss));
        }

        List<StudentPodaci> studentPodaciList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {  //  POVEĆAJ NA 10 STUDENATA
            StudentPodaci s = new StudentPodaci();
            s.setIme("Student" + i);
            s.setPrezime("Prezime" + i);
            s.setSrednjeIme("Srednje" + i);
            s.setJmbg("00101012345" + i);
            s.setDatumRodjenja(LocalDate.of(2000 + i, (i % 12) + 1, (i % 28) + 1));
            s.setMestoRodjenja("Mesto" + i);
            s.setMestoStanovanja("Prebivaliste" + i);
            s.setDrzavaRodjenja("Srbija");
            s.setDrzavljanstvo("Srbija");
            s.setNacionalnost("Srpska");
            s.setPol(i % 2 == 0 ? 'F' : 'M');
            s.setAdresa("Adresa " + i);
            s.setBrojTelefonaMobilni("06123456" + i);
            s.setEmailFakultet("student" + i + "@raf.rs");
            s.setEmailPrivatni("student" + i + "@example.com");
            s.setSrednjaSkola(srednjeSkolee.get(i % 3));  //  DODAJ SREDNJU ŠKOLU
            studentPodaciList.add(studentPodaciRepository.save(s));
        }

        List<StudentIndeks> indeksList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {  //  POVEĆAJ NA 10 INDEKSA
            StudentIndeks si = new StudentIndeks();
            si.setBroj(i);
            si.setGodina(2023);
            si.setStudProgramOznaka(spList.get((i - 1) % spList.size()).getOznaka());
            si.setNacinFinansiranja(i % 2 == 0 ? "Budzet" : "Samofinansiranje");
            si.setAktivan(true);
            si.setVaziOd(LocalDate.of(2023, 10, (i % 28) + 1));
            si.setStudent(studentPodaciList.get(i - 1));
            si.setStudijskiProgram(spList.get((i - 1) % spList.size()));
            si.setOstvarenoEspb(0);
            indeksList.add(studentIndeksRepository.save(si));
        }

        List<DrziPredmet> drziPredmetList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {  //  POVEĆAJ NA 10
            DrziPredmet dp = new DrziPredmet();
            dp.setNastavnik(nastavnikList.get((i - 1) % nastavnikList.size()));
            dp.setPredmet(predmetList.get(i - 1));
            drziPredmetList.add(drziPredmetRepository.save(dp));
        }

        //  POVEĆAJ SLUSA PREDMET - Svaki student sluša 3-5 predmeta
        List<SlusaPredmet> slusaPredmetList = new ArrayList<>();
        for (int i = 0; i < indeksList.size(); i++) {
            for (int j = 0; j < 5; j++) {  // Svaki student sluša 5 predmeta
                SlusaPredmet sl = new SlusaPredmet();
                sl.setStudentIndeks(indeksList.get(i));
                sl.setDrziPredmet(drziPredmetList.get(j % drziPredmetList.size()));
                slusaPredmetList.add(slusaPredmetRepository.save(sl));
            }
        }

        for (int i = 1; i <= 5; i++) {
            Grupa g = new Grupa();
            g.setStudijskiProgram(spList.get(i - 1));
            g.setPredmeti(Collections.singletonList(predmetList.get(i - 1)));
            grupaRepository.save(g);
        }

        // Školske godine
        SkolskaGodina godina1 = new SkolskaGodina();
        godina1.setNaziv("2023/2024");
        godina1.setPocetakZimskog(LocalDate.of(2023, 10, 1));
        godina1.setKrajZimskog(LocalDate.of(2024, 2, 15));
        godina1.setPocetakLetnjeg(LocalDate.of(2024, 2, 16));
        godina1.setKrajLetnjeg(LocalDate.of(2024, 9, 30));
        godina1.setAktivna(true);
        skolskaGodinaRepository.save(godina1);

        SkolskaGodina godina2 = new SkolskaGodina();
        godina2.setNaziv("2024/2025");
        godina2.setPocetakZimskog(LocalDate.of(2024, 10, 1));
        godina2.setKrajZimskog(LocalDate.of(2025, 2, 15));
        godina2.setPocetakLetnjeg(LocalDate.of(2025, 2, 16));
        godina2.setKrajLetnjeg(LocalDate.of(2025, 9, 30));
        godina2.setAktivna(false);
        skolskaGodinaRepository.save(godina2);

        // Predispitne obaveze
        List<Predmet> predmeti = new ArrayList<>();
        predmetRepository.findAll().forEach(predmeti::add);

        for (int i = 0; i < drziPredmetList.size(); i++) {

            DrziPredmet dp = drziPredmetList.get(i);

            PredispitneObaveze obaveza1 = new PredispitneObaveze();
            obaveza1.setDrziPredmet(dp);
            obaveza1.setSkolskaGodina(godina1);
            obaveza1.setVrsta("Kolokvijum 1");
            obaveza1.setMaksPoeni(30);
            predispitneObevezeRepository.save(obaveza1);

            PredispitneObaveze obaveza2 = new PredispitneObaveze();
            obaveza2.setDrziPredmet(dp);
            obaveza2.setSkolskaGodina(godina1);
            obaveza2.setVrsta("Kolokvijum 2");
            obaveza2.setMaksPoeni(30);
            predispitneObevezeRepository.save(obaveza2);

            PredispitneObaveze obaveza3 = new PredispitneObaveze();
            obaveza3.setDrziPredmet(dp);
            obaveza3.setSkolskaGodina(godina1);
            obaveza3.setVrsta("Projekat");
            obaveza3.setMaksPoeni(40);
            predispitneObevezeRepository.save(obaveza3);
        }



/*
        //  POLOŽENI PREDMETI - Prvi 3 studenta su položili neke predmete
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {  // Svaki student položio 2 predmeta
                PolozeniPredmeti pp = new PolozeniPredmeti();
                pp.setStudentIndeks(indeksList.get(i));
                pp.setPredmet(predmetList.get(j));
                pp.setOcena(8 + (i % 3));  // Ocene 8, 9, 10
                pp.setDatumPolaganja(LocalDate.of(2024, 6, 15 + i));
                polozeniPredmetiRepository.save(pp);
            }
        }*/

        //  UPLATE - Prvi 5 studenata je uplatilo različite iznose
        for (int i = 0; i < 5; i++) {
            Uplata uplata = new Uplata();
            uplata.setStudentPodaci(studentPodaciList.get(i));
            uplata.setDatumUplate(LocalDate.of(2024, 10, 1 + i));
            uplata.setIznosEur(500.0 + (i * 100));  // 500, 600, 700, 800, 900 EUR
            uplata.setSrednjiKurs(117.5);
            uplata.setIznosRsd(uplata.getIznosEur() * 117.5);
            uplataRepository.save(uplata);
        }

        //  UPIS GODINE - Prvi 3 studenta su upisali godinu
        for (int i = 0; i < 3; i++) {
            UpisGodine upisGodine = new UpisGodine();
            upisGodine.setStudentIndeks(indeksList.get(i));
            upisGodine.setSkolskaGodina(godina1);
            upisGodine.setGodinaStudija(1);
            upisGodine.setDatumUpisa(LocalDate.of(2023, 10, 1));
            upisGodine.setNapomena("Redovan upis");

            // Dodaj predmete koje student upisuje
            List<Predmet> upisaniPredmeti = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                upisaniPredmeti.add(predmetList.get(j));
            }
            upisGodine.setPredmeti(upisaniPredmeti);

            upisGodineRepository.save(upisGodine);
        }

        //  OBNOVA GODINE - Student 4 i 5 su obnovili godinu
        for (int i = 3; i < 5; i++) {
            ObnovaGodine obnovaGodine = new ObnovaGodine();
            obnovaGodine.setStudentIndeks(indeksList.get(i));
            obnovaGodine.setSkolskaGodina(godina1);
            obnovaGodine.setGodinaStudija(1);
            obnovaGodine.setDatumObnove(LocalDate.of(2023, 10, 1));
            obnovaGodine.setNapomena("Obnova zbog nepoloženih predmeta");

            // Dodaj predmete (max 60 ESPB)
            List<Predmet> obnovljeniPredmeti = new ArrayList<>();
            obnovljeniPredmeti.add(predmetList.get(0));  // 6 ESPB
            obnovljeniPredmeti.add(predmetList.get(1));  // 7 ESPB
            obnovljeniPredmeti.add(predmetList.get(2));  // 8 ESPB
            obnovaGodine.setPredmeti(obnovljeniPredmeti);


            obnovaGodineRepository.save(obnovaGodine);
        }



// ======== ISPITI, PRIJAVE ISPITA i IZLASCI STUDENATA NA ISPIT =========

// 1. KREIRAJ ISPITNE ROKOVE
        IspitniRok junskiRok = new IspitniRok();
        junskiRok.setNaziv("Jun 2024");
        junskiRok.setDatumPocetka(LocalDate.of(2024, 6, 1));
        junskiRok.setDatumZavrsetka(LocalDate.of(2024, 6, 30));
        junskiRok.setSkolskaGodina(godina1);
        junskiRok.setAktivan(true);
        junskiRok=ispitniRokRepository.save(junskiRok);

        IspitniRok septembarskiRok = new IspitniRok();
        septembarskiRok.setNaziv("Septembar 2024");
        septembarskiRok.setDatumPocetka(LocalDate.of(2024, 9, 1));
        septembarskiRok.setDatumZavrsetka(LocalDate.of(2024, 9, 15));
        septembarskiRok.setSkolskaGodina(godina1);
        septembarskiRok.setAktivan(false);
        septembarskiRok=ispitniRokRepository.save(septembarskiRok);


// 2. KREIRAJ ISPITE (5 ispita - 3 u junskom, 2 u septembarskom roku)
        List<Ispit> ispitiList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Ispit ispit = new Ispit();
            ispit.setPredmet(predmetList.get(i));
           // ispit.setIspitniRok(junskiRok);
            ispit.setDrziPredmet(drziPredmetList.get(i));
            ispit.setDatumOdrzavanja(LocalDate.of(2024, 6, 10 + i));
            ispit.setVremePocetka(null);
            ispit.setZakljucen(false);
            ispitiList.add(ispitRepository.save(ispit));
        }

        for (int i = 3; i < 5; i++) {
            Ispit ispit = new Ispit();
            ispit.setPredmet(predmetList.get(i));
            //ispit.setIspitniRok(septembarskiRok);
            ispit.setDrziPredmet(drziPredmetList.get(i));
            ispit.setDatumOdrzavanja(LocalDate.of(2024, 9, 5 + (i - 3)));
            ispit.setVremePocetka(null);
            ispit.setZakljucen(false);
            ispitiList.add(ispitRepository.save(ispit));
        }

// 3. DODAJ OSVOJENE POENE - JEDNOSTAVNO BEZ QUERY-ja

// Dohvati sve predispitne obaveze za prvi predmet u školskoj godini 1
        List<PredispitneObaveze> obavezePrviPredmet = new ArrayList<>();

        predispitneObevezeRepository.findAll().forEach(obaveza -> {
            if (
                    obaveza.getDrziPredmet() != null &&
                            obaveza.getDrziPredmet().getPredmet() != null &&
                            obaveza.getDrziPredmet().getPredmet().getId().equals(predmetList.get(0).getId()) &&
                            obaveza.getSkolskaGodina().getId().equals(godina1.getId())
            ) {
                obavezePrviPredmet.add(obaveza);
            }
        });


// Student 0 - osvojio 60% na svakoj obavezi
        for (PredispitneObaveze obaveza : obavezePrviPredmet) {
            PredispitniPoeni op = new PredispitniPoeni();
            op.setStudentIndeks(indeksList.get(0));
            op.setPredispitnaObaveza(obaveza);
            op.setPoeni((int)(obaveza.getMaksPoeni() * 0.6)); // 18, 18, 24 = 60 ukupno
            predispitniPoeniRepository.save(op);
        }

// Student 1 - osvojio 70% na svakoj obavezi
        for (PredispitneObaveze obaveza : obavezePrviPredmet) {
            PredispitniPoeni op = new PredispitniPoeni();
            op.setStudentIndeks(indeksList.get(1));
            op.setPredispitnaObaveza(obaveza);
            op.setPoeni((int)(obaveza.getMaksPoeni() * 0.7)); // 21, 21, 28 = 70 ukupno
            predispitniPoeniRepository.save(op);
        }

// Student 2 - osvojio 50% na svakoj obavezi
        for (PredispitneObaveze obaveza : obavezePrviPredmet) {
            PredispitniPoeni op = new PredispitniPoeni();
            op.setStudentIndeks(indeksList.get(2));
            op.setPredispitnaObaveza(obaveza);
            op.setPoeni((int)(obaveza.getMaksPoeni() * 0.5)); // 15, 15, 20 = 50 ukupno
            predispitniPoeniRepository.save(op);
        }

// Dohvati obaveze za drugi predmet
        List<PredispitneObaveze> obavezeDrugiPredmet = new ArrayList<>();

        predispitneObevezeRepository.findAll().forEach(obaveza -> {
            if (
                    obaveza.getDrziPredmet() != null &&
                            obaveza.getDrziPredmet().getPredmet() != null &&
                            obaveza.getDrziPredmet().getPredmet().getId().equals(predmetList.get(1).getId()) &&
                            obaveza.getSkolskaGodina().getId().equals(godina1.getId())
            ) {
                obavezeDrugiPredmet.add(obaveza);
            }
        });


// Student 0 i 1 imaju poene i na drugom predmetu
        for (int i = 0; i < 2; i++) {
            for (PredispitneObaveze obaveza : obavezeDrugiPredmet) {
                PredispitniPoeni op = new PredispitniPoeni();
                op.setStudentIndeks(indeksList.get(i));
                op.setPredispitnaObaveza(obaveza);
                op.setPoeni((int)(obaveza.getMaksPoeni() * 0.6));
                predispitniPoeniRepository.save(op);
            }
        }

// 4. PRIJAVE ISPITA
        List<PrijavaIspita> prijaveList = new ArrayList<>();

// Prijave za PRVI ispit (Ispit 0) - 5 studenata
        for (int i = 0; i < 5; i++) {
            PrijavaIspita prijava = new PrijavaIspita();
            prijava.setStudentIndeks(indeksList.get(i));
            prijava.setIspit(ispitiList.get(0));
            prijava.setDatumPrijave(LocalDate.of(2024, 6, 1 + i));
            prijava.setIzasao(i < 4);  // Prva 4 su izašla, 5. nije
            prijaveList.add(prijavaIspitaRepository.save(prijava));
        }

// Prijave za DRUGI ispit (Ispit 1) - 3 studenta
        for (int i = 0; i < 3; i++) {
            PrijavaIspita prijava = new PrijavaIspita();
            prijava.setStudentIndeks(indeksList.get(i));
            prijava.setIspit(ispitiList.get(1));
            prijava.setDatumPrijave(LocalDate.of(2024, 6, 2 + i));
            prijava.setIzasao(true);
            prijaveList.add(prijavaIspitaRepository.save(prijava));
        }

// Prijave za TREĆI ispit (Ispit 2) - 2 studenta
        for (int i = 0; i < 2; i++) {
            PrijavaIspita prijava = new PrijavaIspita();
            prijava.setStudentIndeks(indeksList.get(i + 5));  // Studenti 5 i 6
            prijava.setIspit(ispitiList.get(2));
            prijava.setDatumPrijave(LocalDate.of(2024, 6, 3));
            prijava.setIzasao(false);  // Nisu izašli
            prijaveList.add(prijavaIspitaRepository.save(prijava));
        }

// 5. IZLASCI NA ISPIT (samo za one koji su izašli)

// Izlasci za PRVI ispit
        for (int i = 0; i < 4; i++) {  // Prva 4 studenta su izašla
            PrijavaIspita prijava = prijaveList.get(i);

            IzlazakNaIspit izlazak = new IzlazakNaIspit();
            izlazak.setPrijavaIspita(prijava);

            // Različiti rezultati:
            if (i == 0) {
                // Student 0: položio sa odličnom ocenom
                izlazak.setPoeniPredispitne(55);
                izlazak.setPoeniIspit(40);  // Ukupno 95 -> ocena 10
                izlazak.setNapomena("Odličan rad!");
            } else if (i == 1) {
                // Student 1: položio sa dobrom ocenom
                izlazak.setPoeniPredispitne(58);
                izlazak.setPoeniIspit(25);  // Ukupno 83 -> ocena 9
                izlazak.setNapomena("Dobar rezultat");
            } else if (i == 2) {
                // Student 2: jedva položio
                izlazak.setPoeniPredispitne(61);
                izlazak.setPoeniIspit(1);   // Ukupno 62 -> ocena 7
                izlazak.setNapomena("Na granici");
            } else {
                // Student 3: pao
                izlazak.setPoeniPredispitne(64);
                izlazak.setPoeniIspit(0);   // Ukupno 64 -> ocena 7
                izlazak.setNapomena("Nedovoljno");
            }

            izlazak.setPonisteno(false);
            izlazakNaIspitRepository.save(izlazak);
        }

// Izlasci za DRUGI ispit
        for (int i = 5; i < 8; i++) {  // Prijave 5, 6, 7 (studenti 0, 1, 2 na drugom ispitu)
            PrijavaIspita prijava = prijaveList.get(i);

            IzlazakNaIspit izlazak = new IzlazakNaIspit();
            izlazak.setPrijavaIspita(prijava);
            izlazak.setPoeniPredispitne(50 + ((i - 5) * 5));
            izlazak.setPoeniIspit(30 + ((i - 5) * 5));
            izlazak.setNapomena("Rezultat ispita " + (i - 4));
            izlazak.setPonisteno(false);
            izlazakNaIspitRepository.save(izlazak);
        }

        System.out.println("Seeder completed successfully!");
        System.out.println("   - " + spList.size() + " Studijskih programa");
        System.out.println("   - " + predmetList.size() + " Predmeta");
        System.out.println("   - " + nastavnikList.size() + " Nastavnika");
        System.out.println("   - " + studentPodaciList.size() + " Studenata");
        System.out.println("   - " + srednjeSkolee.size() + " Srednjih skola");
        System.out.println("   - " + ispitiList.size() + " Ispita");
        System.out.println("   - " + prijaveList.size() + " Prijava ispita");
        System.out.println("   - Polozeni predmeti, Uplate, Upis i Obnova godine dodati");


    }

}