package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.*;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.repositories.IspitniRokRepository;
import org.raflab.studsluzba.repositories.NastavnikRepository;
import org.raflab.studsluzba.repositories.PredispitniPoeniRepository;
import org.raflab.studsluzba.repositories.PredmetRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.*;
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
        studentPodaci.setMestoStanovanja(request.getMestoPrebivalista());
        studentPodaci.setDrzavaRodjenja(request.getDrzavaRodjenja());
        studentPodaci.setDrzavljanstvo(request.getDrzavljanstvo());
        studentPodaci.setNacionalnost(request.getNacionalnost());
        studentPodaci.setPol(request.getPol());
        studentPodaci.setAdresa(request.getAdresa());
        studentPodaci.setBrojTelefonaMobilni(request.getBrojTelefonaMobilni());
        studentPodaci.setBrojTelefonaFiksni(request.getBrojTelefonaFiksni());
        studentPodaci.setEmailFakultet(request.getEmailFakultet());
        studentPodaci.setEmailPrivatni(request.getEmailPrivatni());
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

    // SkolskaGodina converters
    public static SkolskaGodina toSkolskaGodina(SkolskaGodinaRequest request) {
        SkolskaGodina godina = new SkolskaGodina();
        godina.setNaziv(request.getNaziv());
        godina.setPocetakZimskog(request.getPocetakZimskog());
        godina.setKrajZimskog(request.getKrajZimskog());
        godina.setPocetakLetnjeg(request.getPocetakLetnjeg());
        godina.setKrajLetnjeg(request.getKrajLetnjeg());
        godina.setAktivna(request.getAktivna() != null ? request.getAktivna() : false);
        return godina;
    }

    public static SkolskaGodinaResponse toSkolskaGodinaResponse(SkolskaGodina godina) {
        SkolskaGodinaResponse response = new SkolskaGodinaResponse();
        response.setId(godina.getId());
        response.setNaziv(godina.getNaziv());
        response.setPocetakZimskog(godina.getPocetakZimskog());
        response.setKrajZimskog(godina.getKrajZimskog());
        response.setPocetakLetnjeg(godina.getPocetakLetnjeg());
        response.setKrajLetnjeg(godina.getKrajLetnjeg());
        //response.setAktivna(godina.getAktivna());
        return response;
    }

    public static List<SkolskaGodinaResponse> toSkolskaGodinaResponseList(Iterable<SkolskaGodina> godine) {
        List<SkolskaGodinaResponse> responses = new ArrayList<>();
        godine.forEach(godina -> responses.add(toSkolskaGodinaResponse(godina)));
        return responses;
    }

    // PredispitnaObaveza converters
    public static PredispitneObaveze toPredispitnaObaveza(
            PredispitneObavezeRequest request,
            DrziPredmet drziPredmet,
            SkolskaGodina godina
    ) {
        PredispitneObaveze obaveza = new PredispitneObaveze();
        obaveza.setDrziPredmet(drziPredmet);
        obaveza.setSkolskaGodina(godina);
        obaveza.setVrsta(request.getVrsta());
        obaveza.setMaksPoeni(request.getMaksPoeni());
        return obaveza;
    }


    public static PredispitneObavezeResponse toPredispitnaObavezaResponse(PredispitneObaveze obaveza) {
        PredispitneObavezeResponse response = new PredispitneObavezeResponse();
        response.setId(obaveza.getId());

        response.setPredmetId(obaveza.getDrziPredmet().getPredmet().getId());
        response.setPredmetNaziv(obaveza.getDrziPredmet().getPredmet().getNaziv());

        response.setSkolskaGodinaId(obaveza.getSkolskaGodina().getId());
        response.setSkolskaGodinaNaziv(obaveza.getSkolskaGodina().getNaziv());
        response.setVrsta(obaveza.getVrsta());
        response.setMaksPoeni(obaveza.getMaksPoeni());
        return response;
    }


    public static List<PredispitneObavezeResponse> toPredispitnaObavezaResponseList(Iterable<PredispitneObaveze> obaveze) {
        List<PredispitneObavezeResponse> responses = new ArrayList<>();
        obaveze.forEach(obaveza -> responses.add(toPredispitnaObavezaResponse(obaveza)));
        return responses;
    }

    // OsvojeniPoeni converters
    public static PredispitniPoeni toOsvojeniPoeni(PredispitniPoeniRequest request,
                                                   StudentIndeks indeks,
                                                   PredispitneObaveze obaveza) {
        PredispitniPoeni poeni = new PredispitniPoeni();
        poeni.setStudentIndeks(indeks);
        poeni.setPredispitnaObaveza(obaveza);
        poeni.setPoeni(request.getPoeni());
        return poeni;
    }

    public static PredispitniPoeniResponse toOsvojeniPoeniResponse(PredispitniPoeni osvojeniPoeni) {
        PredispitniPoeniResponse response = new PredispitniPoeniResponse();
        response.setId(osvojeniPoeni.getId());
        response.setPoeni(osvojeniPoeni.getPoeni());

        // Student podaci
        if (osvojeniPoeni.getStudentIndeks() != null) {
            response.setStudentIndeksId(osvojeniPoeni.getStudentIndeks().getId());
            response.setStudentBrojIndeksa(osvojeniPoeni.getStudentIndeks().getBroj());
            response.setStudentGodinaIndeksa(osvojeniPoeni.getStudentIndeks().getGodina());

            if (osvojeniPoeni.getStudentIndeks().getStudent() != null) {
                response.setStudentIme(osvojeniPoeni.getStudentIndeks().getStudent().getIme());
                response.setStudentPrezime(osvojeniPoeni.getStudentIndeks().getStudent().getPrezime());
            }
        }

        // Predispitna obaveza
        if (osvojeniPoeni.getPredispitnaObaveza() != null) {
            response.setPredispitnaObavezaId(osvojeniPoeni.getPredispitnaObaveza().getId());
            response.setObavezaVrsta(osvojeniPoeni.getPredispitnaObaveza().getVrsta());
            response.setPoeni(osvojeniPoeni.getPoeni());

            // Predmet podaci
            if (osvojeniPoeni.getPredispitnaObaveza().getDrziPredmet() != null &&
                    osvojeniPoeni.getPredispitnaObaveza().getDrziPredmet().getPredmet() != null) {

                Predmet predmet = osvojeniPoeni.getPredispitnaObaveza().getDrziPredmet().getPredmet();

                response.setPredmetId(predmet.getId());
                response.setPredmetSifra(predmet.getSifra());
                response.setPredmetNaziv(predmet.getNaziv());
            }


            // Školska godina
            if (osvojeniPoeni.getPredispitnaObaveza().getSkolskaGodina() != null) {
                response.setSkolskaGodinaId(osvojeniPoeni.getPredispitnaObaveza().getSkolskaGodina().getId());
                response.setSkolskaGodinaNaziv(osvojeniPoeni.getPredispitnaObaveza().getSkolskaGodina().getNaziv());
            }
        }

        return response;
    }

    // IspitniRok converters
    public static IspitniRok toIspitniRok(IspitniRokRequest request, SkolskaGodina godina) {
        IspitniRok rok = new IspitniRok();
        rok.setNaziv(request.getNaziv());
        rok.setSkolskaGodina(godina);
        rok.setDatumPocetka(request.getPocetak());
        rok.setDatumZavrsetka(request.getKraj());
        rok.setAktivan(request.getAktivan() != null ? request.getAktivan() : false);
        return rok;
    }

    public static IspitniRokResponse toIspitniRokResponse(IspitniRok rok) {
        IspitniRokResponse response = new IspitniRokResponse();
        response.setId(rok.getId());
        response.setNaziv(rok.getNaziv());
        response.setPocetak(rok.getDatumPocetka());
        response.setKraj(rok.getDatumZavrsetka());
        response.setAktivan(rok.getAktivan());

        if (rok.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(rok.getSkolskaGodina().getId());
            response.setSkolskaGodinaNaziv(rok.getSkolskaGodina().getNaziv());
        }

        return response;
    }

    public static IspitResponse toIspitResponse(Ispit ispit) {
        IspitResponse response = new IspitResponse();
        response.setId(ispit.getId());

        if (ispit.getPredmet() != null) {
            response.setPredmetId(ispit.getPredmet().getId());
            response.setPredmetSifra(ispit.getPredmet().getSifra());
            response.setPredmetNaziv(ispit.getPredmet().getNaziv());
        }

        if (ispit.getIspitniRok() != null) {
            response.setIspitniRokId(ispit.getIspitniRok().getId());
            response.setIspitniRokNaziv(ispit.getIspitniRok().getNaziv());
            response.setIspitniRokPocetak(ispit.getIspitniRok().getDatumPocetka());
            response.setIspitniRokKraj(ispit.getIspitniRok().getDatumZavrsetka());

            if (ispit.getIspitniRok().getSkolskaGodina() != null) {
                response.setSkolskaGodinaId(ispit.getIspitniRok().getSkolskaGodina().getId());
                response.setSkolskaGodinaNaziv(ispit.getIspitniRok().getSkolskaGodina().getNaziv());
            }
        }

        if (ispit.getDrziPredmet() != null && ispit.getDrziPredmet().getNastavnik() != null) {
            response.setNastavnikId(ispit.getDrziPredmet().getNastavnik().getId());
            response.setNastavnikIme(ispit.getDrziPredmet().getNastavnik().getIme());
            response.setNastavnikPrezime(ispit.getDrziPredmet().getNastavnik().getPrezime());
        }

        response.setDatumOdrzavanja(ispit.getDatumOdrzavanja());
        response.setVremePocetka(ispit.getVremePocetka());
        response.setZakljucen(ispit.isZakljucen());
        response.setNapomena(ispit.getNapomena());

        return response;
    }



    // Predmet converters
    public static Predmet toPredmet(PredmetRequest request, StudijskiProgram program) {
        Predmet predmet = new Predmet();
        predmet.setSifra(request.getSifra());
        predmet.setNaziv(request.getNaziv());
        predmet.setOpis(request.getOpis());
        predmet.setEspb(request.getEspb());
        predmet.setStudProgram(program);
        predmet.setObavezan(request.getObavezan());
        return predmet;
    }

    public static PredmetResponse toPredmetResponse(Predmet predmet) {
        PredmetResponse response = new PredmetResponse();
        response.setId(predmet.getId());
        response.setSifra(predmet.getSifra());
        response.setNaziv(predmet.getNaziv());
        response.setOpis(predmet.getOpis());
        response.setEspb(predmet.getEspb());
        response.setObavezan(predmet.isObavezan());

        if (predmet.getStudProgram() != null) {
            response.setStudijskiProgramId(predmet.getStudProgram().getId());
            response.setStudijskiProgramNaziv(predmet.getStudProgram().getNaziv());
        }

        return response;
    }
}