package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Converters {

    public static Nastavnik toNastavnik(NastavnikRequest nastavnikRequest) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme(nastavnikRequest.getIme());
        nastavnik.setPrezime(nastavnikRequest.getPrezime());
        nastavnik.setSrednjeIme(nastavnikRequest.getSrednjeIme());
        nastavnik.setEmail(nastavnikRequest.getEmail());
        nastavnik.setBrojTelefona(nastavnikRequest.getBrojTelefona());
        nastavnik.setAdresa(nastavnikRequest.getAdresa());
        nastavnik.setZvanja(nastavnikRequest.getZvanja());
        nastavnik.setDatumRodjenja(nastavnikRequest.getDatumRodjenja());
        nastavnik.setPol(nastavnikRequest.getPol());
        nastavnik.setJmbg(nastavnikRequest.getJmbg());
        return nastavnik;
    }

    public static NastavnikResponse toNastavnikResponse(Nastavnik nastavnik) {
        NastavnikResponse response = new NastavnikResponse();
        response.setId(nastavnik.getId());
        response.setIme(nastavnik.getIme());
        response.setPrezime(nastavnik.getPrezime());
        response.setSrednjeIme(nastavnik.getSrednjeIme());
        response.setEmail(nastavnik.getEmail());
        response.setBrojTelefona(nastavnik.getBrojTelefona());
        response.setAdresa(nastavnik.getAdresa());
        response.setZvanja(nastavnik.getZvanja());

        response.setDatumRodjenja(nastavnik.getDatumRodjenja());
        response.setPol(nastavnik.getPol());
        response.setJmbg(nastavnik.getJmbg());
        return response;
    }

    public static List<NastavnikResponse> toNastavnikResponseList(Iterable<Nastavnik> nastavnikIterable) {
        List<NastavnikResponse> nastavnikResponses = new ArrayList<>();

        nastavnikIterable.forEach((nastavnik) -> {
            nastavnikResponses.add(toNastavnikResponse(nastavnik));
        });
        return nastavnikResponses;
    }

    public static StudentPodaci toStudentPodaci(StudentPodaciRequest request) {
        StudentPodaci studentPodaci = new StudentPodaci();
        studentPodaci.setIme(request.getIme());
        studentPodaci.setPrezime(request.getPrezime());
        studentPodaci.setSrednjeIme(request.getSrednjeIme());
        studentPodaci.setJmbg(request.getJmbg());
        studentPodaci.setDatumRodjenja(request.getDatumRodjenja());
        studentPodaci.setMestoRodjenja(request.getMestoRodjenja());
        studentPodaci.setMestoPrebivalista(request.getMestoPrebivalista());
        studentPodaci.setDrzavaRodjenja(request.getDrzavaRodjenja());
        studentPodaci.setDrzavljanstvo(request.getDrzavljanstvo());
        studentPodaci.setNacionalnost(request.getNacionalnost());
        studentPodaci.setPol(request.getPol());
        studentPodaci.setAdresa(request.getAdresa());
        studentPodaci.setBrojTelefonaMobilni(request.getBrojTelefonaMobilni());
        studentPodaci.setBrojTelefonaFiksni(request.getBrojTelefonaFiksni());
        studentPodaci.setEmailPrivatni(request.getEmail());
        studentPodaci.setEmailFakultet(request.getEmail());
        studentPodaci.setBrojLicneKarte(request.getBrojLicneKarte());
        studentPodaci.setLicnuKartuIzdao(request.getLicnuKartuIzdao());
        studentPodaci.setMestoStanovanja(request.getMestoStanovanja());
        studentPodaci.setAdresaStanovanja(request.getAdresaStanovanja());
        return studentPodaci;
    }

    public static StudentIndeks toStudentIndeks(StudentIndeksRequest studentIndeksRequest) {
        StudentIndeks studentIndeks = new StudentIndeks();
        studentIndeks.setGodina(studentIndeksRequest.getGodina());
        studentIndeks.setStudProgramOznaka(studentIndeksRequest.getStudProgramOznaka());
        studentIndeks.setNacinFinansiranja(studentIndeksRequest.getNacinFinansiranja());
        studentIndeks.setAktivan(studentIndeksRequest.isAktivan());
        studentIndeks.setVaziOd(studentIndeksRequest.getVaziOd());
        return studentIndeks;
    }

    public static Ispit toIspit(IspitRequest request) {
        Ispit ispit = new Ispit();
        ispit.setDatumOdrzavanja(request.getDatumOdrzavanja());
        ispit.setVremePocetka(request.getVremePocetka());
        ispit.setZakljucen(request.isZakljucen());

        if (request.getPredmetId() != null) {
            Predmet predmet = new Predmet();
            predmet.setId(request.getPredmetId());
            ispit.setPredmet(predmet);
        }

        if (request.getNastavnikId() != null) {
            Nastavnik nastavnik = new Nastavnik();
            nastavnik.setId(request.getNastavnikId());
            ispit.setNastavnik(nastavnik);
        }

        if (request.getIspitniRokId() != null) {
            IspitniRok rok = new IspitniRok();
            rok.setId(request.getIspitniRokId());
            ispit.setIspitniRok(rok);
        }
        return ispit;
    }

    public static IspitResponse toIspitResponse(Ispit ispit) {
        IspitResponse response = new IspitResponse();
        response.setId(ispit.getId());
        response.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        response.setVremePocetka(ispit.getVremePocetka());
        response.setZakljucen(ispit.isZakljucen());

        if (ispit.getPredmet() != null) {
            response.setPredmetId(ispit.getPredmet().getId());
            response.setPredmetNaziv(ispit.getPredmet().getNaziv());
        }

        if (ispit.getNastavnik() != null) {
            response.setNastavnikId(ispit.getNastavnik().getId());
            response.setNastavnikImePrezime(
                    ispit.getNastavnik().getIme() + " " + ispit.getNastavnik().getPrezime()
            );
        }

        if (ispit.getIspitniRok() != null) {
            response.setIspitniRokId(ispit.getIspitniRok().getId());
        }
        return response;
    }

    public static List<IspitResponse> toIspitResponseList(Iterable<Ispit> ispiti) {
        List<IspitResponse> responses = new ArrayList<>();
        ispiti.forEach(ispit -> responses.add(toIspitResponse(ispit)));
        return responses;
    }

    public static IspitniRok toIspitniRok(IspitniRokRequest request) {
        IspitniRok rok = new IspitniRok();
        rok.setDatumPocetka(request.getDatumPocetka());
        rok.setDatumZavrsetka(request.getDatumZavrsetka());

        if (request.getSkolskaGodinaId() != null) {
            SkolskaGodina godina = new SkolskaGodina();
            godina.setId(request.getSkolskaGodinaId());
            rok.setSkolskaGodina(godina);
        }

        return rok;
    }

    public static IspitniRokResponse toIspitniRokResponse(IspitniRok rok) {
        IspitniRokResponse response = new IspitniRokResponse();
        response.setId(rok.getId());
        response.setDatumPocetka(rok.getDatumPocetka());
        response.setDatumZavrsetka(rok.getDatumZavrsetka());

        if (rok.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(rok.getSkolskaGodina().getId());
            // response.setSkolskaGodinaNaziv(rok.getSkolskaGodina().getNaziv()); // opciono
        }

        return response;
    }

    public static List<IspitniRokResponse> toIspitniRokResponseList(Iterable<IspitniRok> rokovi) {
        List<IspitniRokResponse> responses = new ArrayList<>();
        rokovi.forEach(rok -> responses.add(toIspitniRokResponse(rok)));
        return responses;
    }

    // Convert DrziPredmetRequest + entitet u DrziPredmet
    public static DrziPredmet toDrziPredmet(DrziPredmetNewRequest request, Predmet predmet, Nastavnik nastavnik) {
        DrziPredmet drziPredmet = new DrziPredmet();
        drziPredmet.setPredmet(predmet);
        drziPredmet.setNastavnik(nastavnik);
        return drziPredmet;
    }

    public static DrziPredmetResponse toDrziPredmetResponse(DrziPredmet drziPredmet) {
        DrziPredmetResponse response = new DrziPredmetResponse();
        response.setId(drziPredmet.getId());
        response.setPredmetId(drziPredmet.getPredmet().getId());
        response.setPredmetNaziv(drziPredmet.getPredmet().getNaziv());
        response.setNastavnikId(drziPredmet.getNastavnik().getId());
        response.setNastavnikImePrezime(drziPredmet.getNastavnik().getIme() + " " + drziPredmet.getNastavnik().getPrezime());
        return response;
    }

    public static List<DrziPredmetResponse> toDrziPredmetResponseList(List<DrziPredmet> drziPredmeti) {
        return drziPredmeti.stream()
                .map(Converters::toDrziPredmetResponse)
                .collect(Collectors.toList());
    }


    public static Grupa toGrupa(GrupaRequest request, StudijskiProgram studijskiProgram, List<Predmet> predmeti) {
        Grupa grupa = new Grupa();
        grupa.setStudijskiProgram(studijskiProgram);
        grupa.setPredmeti(predmeti);
        return grupa;
    }

    public static GrupaResponse toGrupaResponse(Grupa grupa) {
        GrupaResponse response = new GrupaResponse();
        response.setId(grupa.getId());
        response.setStudijskiProgramId(grupa.getStudijskiProgram().getId());
        response.setStudijskiProgramNaziv(grupa.getStudijskiProgram().getNaziv());
        response.setPredmetiNaziv(
                grupa.getPredmeti().stream()
                        .map(Predmet::getNaziv)
                        .collect(Collectors.toList())
        );
        return response;
    }

    public static List<GrupaResponse> toGrupaResponseList(List<Grupa> grupe) {
        return grupe.stream()
                .map(Converters::toGrupaResponse)
                .collect(Collectors.toList());
    }

    public static IzlazakNaIspit toIzlazakNaIspit(IzlazakNaIspitRequest request,
                                                  StudentIndeks studentIndeks,
                                                  Ispit ispit,
                                                  SlusaPredmet slusaPredmet) {
        IzlazakNaIspit izlazak = new IzlazakNaIspit();
        izlazak.setStudentIndeks(studentIndeks);
        izlazak.setIspit(ispit);
        izlazak.setSlusaPredmet(slusaPredmet);
        izlazak.setOstvarenoNaIspitu(request.getOstvarenoNaIspitu());
        izlazak.setNapomena(request.getNapomena());
        izlazak.setPonistio(request.isPonistio());
        izlazak.setIzasao(request.isIzasao());
        return izlazak;
    }

    public static IzlazakNaIspitResponse toIzlazakNaIspitResponse(IzlazakNaIspit izlazak) {
        IzlazakNaIspitResponse response = new IzlazakNaIspitResponse();

        response.setId(izlazak.getId());
        response.setOstvarenoNaIspitu(izlazak.getOstvarenoNaIspitu());
        response.setNapomena(izlazak.getNapomena());
        response.setPonistio(izlazak.isPonistio());
        response.setIzasao(izlazak.isIzasao());

        // Student
        if (izlazak.getStudentIndeks() != null) {
            response.setStudentIndeksId(izlazak.getStudentIndeks().getId());

            if (izlazak.getStudentIndeks().getStudent() != null) {
                response.setStudentImePrezime(
                        izlazak.getStudentIndeks().getStudent().getIme() + " " +
                                izlazak.getStudentIndeks().getStudent().getPrezime()
                );
            } else {
                response.setStudentImePrezime(null);
            }
        }

        // Ispit
        if (izlazak.getIspit() != null) {
            response.setIspitId(izlazak.getIspit().getId());
        }

        // Predmet
        if (izlazak.getSlusaPredmet() != null &&
                izlazak.getSlusaPredmet().getDrziPredmet() != null &&
                izlazak.getSlusaPredmet().getDrziPredmet().getPredmet() != null) {
            response.setPredmetNaziv(
                    izlazak.getSlusaPredmet().getDrziPredmet().getPredmet().getNaziv()
            );
        }

        return response;
    }

    public static List<IzlazakNaIspitResponse> toIzlazakNaIspitResponseList(List<IzlazakNaIspit> list) {
        return list.stream()
                .map(Converters::toIzlazakNaIspitResponse)
                .collect(Collectors.toList());

    }

    public static NastavnikZvanjeResponse toNastavnikZvanjeResponse(NastavnikZvanje nz) {
        NastavnikZvanjeResponse response = new NastavnikZvanjeResponse();
        response.setId(nz.getId());
        response.setDatumIzbora(nz.getDatumIzbora());
        response.setNaucnaOblast(nz.getNaucnaOblast());
        response.setUzaNaucnaOblast(nz.getUzaNaucnaOblast());
        response.setZvanje(nz.getZvanje());
        response.setAktivno(nz.isAktivno());
        response.setNastavnikId(nz.getNastavnik().getId());
        response.setNastavnikImePrezime(nz.getNastavnik().getIme() + " " + nz.getNastavnik().getPrezime());
        return response;
    }

    public static List<NastavnikZvanjeResponse> toNastavnikZvanjeResponseList(List<NastavnikZvanje> list) {
        List<NastavnikZvanjeResponse> responses = new ArrayList<>();
        for (NastavnikZvanje nz : list) {
            responses.add(toNastavnikZvanjeResponse(nz));
        }
        return responses;
    }

    public static ObnovaGodineResponse toObnovaGodineResponse(ObnovaGodine obnova) {
            ObnovaGodineResponse response = new ObnovaGodineResponse();
            response.setId(obnova.getId());
            response.setGodinaStudija(obnova.getGodinaStudija());
            response.setDatum(obnova.getDatum());
            response.setNapomena(obnova.getNapomena());
            if (obnova.getStudentIndeks() != null) {
                response.setStudentIndeksId(obnova.getStudentIndeks().getId());
                response.setStudentImePrezime(
                        obnova.getStudentIndeks().getStudent().getIme() + " " +
                                obnova.getStudentIndeks().getStudent().getPrezime()
                );
            }
            if (obnova.getPredmetiKojeUpisuje() != null) {
                response.setPredmetiNazivi(
                        obnova.getPredmetiKojeUpisuje()
                                .stream()
                                .map(p -> p.getNaziv())
                                .collect(Collectors.toSet())
                );
            }
            return response;
        }
    public static SlusaPredmet toSlusaPredmet(SlusaPredmetRequest request,
                                              StudentIndeks studentIndeks,
                                              DrziPredmet drziPredmet,
                                              SkolskaGodina skolskaGodina) {
        SlusaPredmet sp = new SlusaPredmet();
        sp.setStudentIndeks(studentIndeks);
        sp.setDrziPredmet(drziPredmet);
        sp.setSkolskaGodina(skolskaGodina);
        return sp;
    }

    public static SlusaPredmetResponse toSlusaPredmetResponse(SlusaPredmet sp) {
        SlusaPredmetResponse response = new SlusaPredmetResponse();
        response.setId(sp.getId());
        if (sp.getStudentIndeks() != null) {
            response.setStudentIndeksId(sp.getStudentIndeks().getId());
            response.setStudentImePrezime(sp.getStudentIndeks().getStudent().getIme() + " " +
                    sp.getStudentIndeks().getStudent().getPrezime());
        }
        if (sp.getDrziPredmet() != null && sp.getDrziPredmet().getPredmet() != null) {
            response.setDrziPredmetId(sp.getDrziPredmet().getPredmet().getId());
            response.setPredmetNaziv(sp.getDrziPredmet().getPredmet().getNaziv());
        }
        if (sp.getDrziPredmet() != null && sp.getDrziPredmet().getNastavnik() != null) {
            response.setDrziPredmetId(sp.getDrziPredmet().getNastavnik().getId());
            response.setNastavnikImePrezime(sp.getDrziPredmet().getNastavnik().getIme() + " " +
                    sp.getDrziPredmet().getNastavnik().getPrezime());
        }
        if (sp.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(sp.getSkolskaGodina().getId());
            response.setSkolskaGodinaNaziv(sp.getSkolskaGodina().getNaziv());
        }
        return response;
    }

    public static List<SlusaPredmetResponse> toSlusaPredmetResponseList(List<SlusaPredmet> list) {
        return list.stream().map(Converters::toSlusaPredmetResponse).collect(Collectors.toList());
    }

    // u Converters.java dodati
    public static SrednjaSkola toSrednjaSkola(SrednjaSkolaRequest request) {
        SrednjaSkola skola = new SrednjaSkola();
        skola.setNaziv(request.getNaziv());
        skola.setMesto(request.getMesto());
        skola.setVrsta(request.getVrsta());
        return skola;
    }

    public static SrednjaSkolaResponse toSrednjaSkolaResponse(SrednjaSkola skola) {
        SrednjaSkolaResponse response = new SrednjaSkolaResponse();
        response.setId(skola.getId());
        response.setNaziv(skola.getNaziv());
        response.setMesto(skola.getMesto());
        response.setVrsta(skola.getVrsta());
        return response;
    }
    public static VisokoskolskaUstanova toVisokoskolskaUstanova(VisokoskolskaUstanovaRequest request) {
        VisokoskolskaUstanova ustanova = new VisokoskolskaUstanova();
        ustanova.setNaziv(request.getNaziv());
        ustanova.setMesto(request.getMesto());
        ustanova.setVrsta(request.getVrsta());
        return ustanova;
    }

    public static VisokoskolskaUstanovaResponse toVisokoskolskaUstanovaResponse(VisokoskolskaUstanova ustanova) {
        VisokoskolskaUstanovaResponse response = new VisokoskolskaUstanovaResponse();
        response.setId(ustanova.getId());
        response.setNaziv(ustanova.getNaziv());
        response.setMesto(ustanova.getMesto());
        response.setVrsta(ustanova.getVrsta());
        return response;
    }

    public static List<VisokoskolskaUstanovaResponse> toVisokoskolskaUstanovaResponseList(List<VisokoskolskaUstanova> list) {
        return list.stream().map(Converters::toVisokoskolskaUstanovaResponse).collect(Collectors.toList());
    }

    public static SkolskaGodina toSkolskaGodina(SkolskaGodinaRequest request) {
        SkolskaGodina godina = new SkolskaGodina();
        godina.setNaziv(request.getNaziv());
        godina.setAktivna(request.isAktivna());
        return godina;
    }

    public static SkolskaGodinaResponse toSkolskaGodinaResponse(SkolskaGodina godina) {
        SkolskaGodinaResponse response = new SkolskaGodinaResponse();
        response.setId(godina.getId());
        response.setNaziv(godina.getNaziv());
        response.setAktivna(godina.isAktivna());
        return response;
    }

    public static List<SkolskaGodinaResponse> toSkolskaGodinaResponseList(List<SkolskaGodina> list) {
        return list.stream().map(Converters::toSkolskaGodinaResponse).collect(Collectors.toList());
    }

    public static PredispitneObaveze toPredispitneObaveze(PredispitneObavezeRequest request) {
        PredispitneObaveze obaveza = new PredispitneObaveze();
        obaveza.setVrsta(request.getVrsta());
        obaveza.setMaksPoeni(request.getMaksPoeni());

        if (request.getDrziPredmetId() != null) {
            DrziPredmet dp = new DrziPredmet();
            dp.setId(request.getDrziPredmetId());
            obaveza.setDrziPredmet(dp);
        }

        if (request.getSkolskaGodinaId() != null) {
            SkolskaGodina godina = new SkolskaGodina();
            godina.setId(request.getSkolskaGodinaId());
            obaveza.setSkolskaGodina(godina);
        }

        return obaveza;
    }

    public static PredispitneObavezeResponse toPredispitneObavezeResponse(PredispitneObaveze obaveza) {
        PredispitneObavezeResponse response = new PredispitneObavezeResponse();
        response.setId(obaveza.getId());
        response.setVrsta(obaveza.getVrsta());
        response.setMaksPoeni(obaveza.getMaksPoeni());
        if (obaveza.getDrziPredmet() != null) response.setDrziPredmetId(obaveza.getDrziPredmet().getId());
        if (obaveza.getSkolskaGodina() != null) response.setSkolskaGodinaId(obaveza.getSkolskaGodina().getId());
        return response;
    }

    public static List<PredispitneObavezeResponse> toPredispitneObavezeResponseList(List<PredispitneObaveze> list) {
        return list.stream().map(Converters::toPredispitneObavezeResponse).collect(Collectors.toList());
    }
    public static VrstaStudija toVrstaStudija(VrstaStudijaRequest request) {
        VrstaStudija vrsta = new VrstaStudija();
        vrsta.setOznaka(request.getOznaka());
        vrsta.setPunNaziv(request.getPunNaziv());
        return vrsta;
    }

    public static VrstaStudijaResponse toVrstaStudijaResponse(VrstaStudija vrsta) {
        VrstaStudijaResponse response = new VrstaStudijaResponse();
        response.setId(vrsta.getId());
        response.setOznaka(vrsta.getOznaka());
        response.setPunNaziv(vrsta.getPunNaziv());
        return response;
    }

    public static List<VrstaStudijaResponse> toVrstaStudijaResponseList(List<VrstaStudija> list) {
        return list.stream().map(Converters::toVrstaStudijaResponse).collect(Collectors.toList());
    }

}