package org.raflab.studsluzba;

import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@Transactional
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
    private SrednjaSkolaRepository srednjaSkolaRepository;
    @Autowired
    private VisokoskolskaUstanovaRepository visokoskolskaUstanovaRepository;
    @Autowired
    private IspitRepository ispitRepository;
    @Autowired
    private IspitniRokRepository ispitniRokRepository;
    @Autowired
    private SkolskaGodinaRepository skolskaGodinaRepository;
    @Autowired
    private PredispitneObavezeRepository predispitneObavezeRepository;
    @Autowired
    private VrstaStudijaRepository vrstaStudijaRepository;
    @Autowired
    private IzlazakNaIspitRepository izlazakNaIspitRepository;
    @Autowired
    private PrijavaIspitaRepository prijavaIspitaRepository;
    @Autowired
    private PredispitniPoeniRepository predispitniPoeniRepository;
    @Autowired
    private UpisGodineRepository upisGodineRepository;
    @Autowired
    private ObnovaGodineRepository obnovaGodineRepository;
    @Autowired
    private PolozeniPredmetiRepository polozeniPredmetiRepository;

    @Override
    public void run(String... args) throws Exception {
        List<StudijskiProgram> spList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudijskiProgram sp = new StudijskiProgram();
            sp.setOznaka("SP" + i);
            sp.setNaziv("Program " + i);
            sp.setGodinaAkreditacije(2020 + i);
            sp.setZvanje("Zvanje " + i);
            sp.setTrajanjeGodina(4);
            sp.setTrajanjeSemestara(8);
           /// sp.setVrstaStudija("OAS");
            sp.setUkupnoEspb(240);
            spList.add(studijskiProgramRepository.save(sp));
        }

        List<Predmet> predmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Predmet p = new Predmet();
            p.setSifra("PR" + i);
            p.setNaziv("Predmet " + i);
            p.setOpis("Opis predmeta " + i);
            p.setEspb(6 + i);
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

        List<StudentPodaci> studentPodaciList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentPodaci s = new StudentPodaci();
            s.setIme("Student" + i);
            s.setPrezime("Prezime" + i);
            s.setSrednjeIme("Srednje" + i);
            s.setJmbg("00101012345" + i);
            s.setDatumRodjenja(LocalDate.of(2000 + i, i, i));
            s.setMestoRodjenja("Mesto" + i);
            s.setMestoPrebivalista("Prebivaliste" + i);
            s.setDrzavaRodjenja("Srbija");
            s.setDrzavljanstvo("Srbija");
            s.setNacionalnost("Srpska");
            s.setPol(i % 2 == 0 ? 'F' : 'M');
            s.setAdresa("Adresa " + i);
            s.setBrojTelefonaMobilni("06123456" + i);
            s.setEmailFakultet("student" + i + "@example.com");
            s.setEmailPrivatni("student" + i + "@example.com");
            studentPodaciList.add(studentPodaciRepository.save(s));
        }

        List<StudentIndeks> indeksList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            StudentIndeks si = new StudentIndeks();
            si.setBroj(i);
            si.setGodina(2023);
            si.setStudProgramOznaka(spList.get(i - 1).getOznaka());
            si.setNacinFinansiranja(i % 2 == 0 ? "Budzet" : "Samofinansiranje");
            si.setAktivan(true);
            si.setVaziOd(LocalDate.of(2023, 10, i));
            si.setStudent(studentPodaciList.get(i - 1));
            si.setStudijskiProgram(spList.get(i - 1));
            si.setOstvarenoEspb(0);
            indeksList.add(studentIndeksRepository.save(si));
        }

        List<DrziPredmet> drziPredmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            DrziPredmet dp = new DrziPredmet();
            dp.setNastavnik(nastavnikList.get(i - 1));
            dp.setPredmet(predmetList.get(i - 1));
            drziPredmetList.add(drziPredmetRepository.save(dp));
        }

        List<SlusaPredmet> slusaPredmetList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SlusaPredmet sl = new SlusaPredmet();
            sl.setStudentIndeks(indeksList.get(i - 1));
            sl.setDrziPredmet(drziPredmetList.get(i - 1));
            slusaPredmetList.add(slusaPredmetRepository.save(sl));
        }

        for (int i = 1; i <= 5; i++) {
            Grupa g = new Grupa();
            g.setStudijskiProgram(spList.get(i - 1));
            g.setPredmeti(Collections.singletonList(predmetList.get(i - 1)));
            grupaRepository.save(g);
        }

        List<SrednjaSkola> srednjaSkolaList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SrednjaSkola ss = new SrednjaSkola();
            ss.setNaziv("Srednja Skola " + i);
            ss.setMesto("Mesto " + i);
            ss.setVrsta("Gimnazija");
            srednjaSkolaList.add(srednjaSkolaRepository.save(ss));
        }

        List<VisokoskolskaUstanova> ustanovaList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            VisokoskolskaUstanova vu = new VisokoskolskaUstanova();
            vu.setNaziv("Ustanova " + i);
            vu.setMesto("Grad " + i);
            ustanovaList.add(visokoskolskaUstanovaRepository.save(vu));
        }

        List<SkolskaGodina> skolskaGodinaList = new ArrayList<>();
        for (int i = 2022; i <= 2026; i++) {
            SkolskaGodina sg = new SkolskaGodina();
            sg.setNaziv(i + "/" + (i + 1));
            sg.setAktivna(i == 2025);
            skolskaGodinaList.add(skolskaGodinaRepository.save(sg));
        }

        List<IspitniRok> ispitniRokList = new ArrayList<>();
        for (SkolskaGodina sg : skolskaGodinaList) {
            IspitniRok ir = new IspitniRok();
            ir.setDatumPocetka(LocalDate.of(2025, 6, 1));
            ir.setDatumZavrsetka(LocalDate.of(2025, 6, 15));
            ir.setSkolskaGodina(sg);

            if (sg.getIspitniRokovi() == null) {
                sg.setIspitniRokovi(new HashSet<>());
            }
            sg.getIspitniRokovi().add(ir);

            ispitniRokList.add(ispitniRokRepository.save(ir));
        }



        List<Ispit> ispitList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Ispit ispit = new Ispit();
            ispit.setDatumOdrzavanja(LocalDate.of(2025, 6, i + 1));
            ispit.setVremePocetka(LocalTime.of(10 + i, 0));
            ispit.setPredmet(predmetList.get(i));
            ispit.setNastavnik(nastavnikList.get(i));
            ispit.setZakljucen(i % 2 == 0);

            ispit.setIspitniRok(ispitniRokList.get(i % ispitniRokList.size()));

            ispitList.add(ispitRepository.save(ispit));
        }

        List<PredispitneObaveze> obavezeList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            PredispitneObaveze po = new PredispitneObaveze();
            po.setVrsta(i % 2 == 0 ? "Test" : "Kolokvijum");
            po.setMaksPoeni(10 + i * 5);
            po.setDrziPredmet(drziPredmetList.get(i - 1));
            po.setSkolskaGodina(skolskaGodinaList.get(i % skolskaGodinaList.size()));
            obavezeList.add(predispitneObavezeRepository.save(po));
        }

        List<VrstaStudija> vrstaStudijaList = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            VrstaStudija vs = new VrstaStudija();
            vs.setOznaka("OAS" + i);
            vs.setPunNaziv("Osnovne akademske studije " + i);
            vrstaStudijaList.add(vrstaStudijaRepository.save(vs));
        }

        List<IzlazakNaIspit> izlazakList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            IzlazakNaIspit izlazak = new IzlazakNaIspit();
            izlazak.setOstvarenoNaIspitu(50 + i * 10);
            izlazak.setNapomena("Napomena " + (i + 1));
            izlazak.setPonistio(i % 2 == 0);
            izlazak.setIzasao(true);
            izlazak.setStudentIndeks(indeksList.get(i));
            izlazak.setIspit(ispitList.get(i));
            izlazak.setSlusaPredmet(slusaPredmetList.get(i));
            izlazakList.add(izlazakNaIspitRepository.save(izlazak));
        }

        List<PrijavaIspita> prijavaList = new ArrayList<>();
        for (int i = 0; i < indeksList.size(); i++) {
            PrijavaIspita pi = new PrijavaIspita();
            pi.setStudentIndeks(indeksList.get(i));
            pi.setIspit(ispitList.get(i % ispitList.size()));
            pi.setDatumPrijave(LocalDate.now());
            prijavaList.add(prijavaIspitaRepository.save(pi));
        }

        List<PredispitniPoeni> poeniList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PredispitniPoeni poeni = new PredispitniPoeni();
            poeni.setPoeni(5 + i * 2);
            poeni.setStudentIndeks(indeksList.get(i));
            poeni.setPredispitnaObaveza(obavezeList.get(i));
            poeni.setSlusaPredmet(slusaPredmetList.get(i));
            poeni.setSkolskaGodina(skolskaGodinaList.get(i));
            poeniList.add(predispitniPoeniRepository.save(poeni));
        }

        List<UpisGodine> upisGodineList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UpisGodine upis = new UpisGodine();
            upis.setGodinaStudija(i + 1);
            upis.setDatum(LocalDate.of(2023, 10, i + 1));
            upis.setNapomena("Napomena " + (i + 1));
            upis.setStudentIndeks(indeksList.get(i));

            Set<Predmet> preneti = new HashSet<>();
            preneti.add(predmetList.get(0));
            preneti.add(predmetList.get(1));
            upis.setPrenetiPredmeti(preneti);

            upisGodineList.add(upisGodineRepository.save(upis));
        }

        List<ObnovaGodine> obnovaGodineList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ObnovaGodine obnova = new ObnovaGodine();
            obnova.setGodinaStudija(i + 1);
            obnova.setDatum(LocalDate.of(2023, 10, i + 1));
            obnova.setNapomena("Napomena obnove " + (i + 1));
            obnova.setStudentIndeks(indeksList.get(i));

            Set<Predmet> predmeti = new HashSet<>();
            predmeti.add(predmetList.get(0));
            predmeti.add(predmetList.get(1));
            obnova.setPredmetiKojeUpisuje(predmeti);

            obnovaGodineList.add(obnovaGodineRepository.save(obnova));
        }

        List<PolozeniPredmeti> polozeniPredmetiList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PolozeniPredmeti pp = new PolozeniPredmeti();
            pp.setStudentIndeks(indeksList.get(i));
            pp.setPredmet(predmetList.get(i));
            pp.setOcena(6 + i % 5);
            pp.setPriznat(i % 2 == 0);
            pp.setIzlazakNaIspit(izlazakList.get(i));

            polozeniPredmetiList.add(polozeniPredmetiRepository.save(pp));
        }


        }

}
