package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
import org.raflab.studsluzba.controllers.request.StudentPodaciRequest;
import org.raflab.studsluzba.controllers.request.UpisGodineRequest;
import org.raflab.studsluzba.controllers.request.UplataRequest;
import org.raflab.studsluzba.controllers.response.*;
import org.raflab.studsluzba.model.*;
import org.raflab.studsluzba.model.dtos.StudentDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EntityMappers {

    public static StudentDTO fromStudentPodaciToDTO(StudentPodaci sp) {
        StudentDTO s = new StudentDTO();
        s.setIdStudentPodaci(sp.getId());
        s.setIme(sp.getIme());
        s.setPrezime(sp.getPrezime());
        return s;

    }
    // Dodaj ovo u EntityMappers.java na serveru

    public static StudentPodaci fromRequestToStudentPodaci(StudentPodaciRequest req) {
        StudentPodaci s = new StudentPodaci();

        // Osnovni podaci
        s.setIme(req.getIme());
        s.setPrezime(req.getPrezime());
        s.setSrednjeIme(req.getSrednjeIme());
        s.setJmbg(req.getJmbg());
        s.setDatumRodjenja(req.getDatumRodjenja());
        s.setPol(req.getPol());

        // Mesto i država
        s.setMestoRodjenja(req.getMestoRodjenja());
        s.setDrzavaRodjenja(req.getDrzavaRodjenja());
        s.setDrzavljanstvo(req.getDrzavljanstvo());
        s.setNacionalnost(req.getNacionalnost());

        // Kontakt i adresa
        s.setAdresa(req.getAdresa());
        //s.setMestoPrebivalista(req.getMestoPrebivalista());
        s.setMestoStanovanja(req.getMestoStanovanja());
        s.setAdresaStanovanja(req.getAdresaStanovanja());
        s.setBrojTelefonaMobilni(req.getBrojTelefonaMobilni());
        s.setBrojTelefonaFiksni(req.getBrojTelefonaFiksni());
        s.setEmailFakultet(req.getEmailFakultet());
        s.setEmailPrivatni(req.getEmailPrivatni());

        // Dokumenta
        s.setBrojLicneKarte(req.getBrojLicneKarte());
        s.setLicnuKartuIzdao(req.getLicnuKartuIzdao());

        // Srednja škola i uspeh (Ovo su polja u StudentPodaci entitetu, proveriti da li postoje)
        s.setUspehSrednjaSkola(req.getUspehSrednjaSkola());
        s.setUspehPrijemni(req.getUspehPrijemni());

        // Napomena
        //s.setNapomena(req.getNapomena());

        return s;
    }

    public static StudentDTO fromStudentIndeksToDTO(StudentIndeks si) {
        StudentDTO s = fromStudentPodaciToDTO(si.getStudent());
        s.setIdIndeks(si.getId());
        s.setGodinaUpisa(si.getGodina());
        s.setBroj(si.getBroj());
        s.setStudProgramOznaka(si.getStudProgramOznaka());
        s.setAktivanIndeks(si.isAktivan());
        return s;

    }

    public StudentIndeksResponse fromStudentIndexToResponse(StudentIndeks si) {
        if (si == null) {
            return null;
        }
        StudentIndeksResponse response = new StudentIndeksResponse();
        response.setId(si.getId());
        response.setBroj(si.getBroj());
        response.setGodina(si.getGodina());
        response.setStudProgramOznaka(si.getStudProgramOznaka());
        response.setNacinFinansiranja(si.getNacinFinansiranja());
        response.setAktivan(si.isAktivan());
        response.setVaziOd(si.getVaziOd());
        response.setOstvarenoEspb(si.getOstvarenoEspb());
        response.setStudijskiProgram(si.getStudijskiProgram());
        response.setStudent(si.getStudent());
        return response;
    }

    public StudentPodaciResponse fromStudentPodaciToResponse(StudentPodaci sp) {
        if (sp == null) {
            return null;
        }

        StudentPodaciResponse response = new StudentPodaciResponse();
        response.setId(sp.getId());
        response.setIme(sp.getIme());
        response.setPrezime(sp.getPrezime());
        response.setSrednjeIme(sp.getSrednjeIme());
        response.setJmbg(sp.getJmbg());
        response.setDatumRodjenja(sp.getDatumRodjenja());
        response.setMestoRodjenja(sp.getMestoRodjenja());
        ///response.setMestoStanovanja(sp.setMestoStanovanja());
        response.setDrzavaRodjenja(sp.getDrzavaRodjenja());
        response.setDrzavljanstvo(sp.getDrzavljanstvo());
        response.setNacionalnost(sp.getNacionalnost());
        response.setPol(sp.getPol());
        response.setAdresa(sp.getAdresa());
        response.setBrojTelefonaMobilni(sp.getBrojTelefonaMobilni());
        response.setBrojTelefonaFiksni(sp.getBrojTelefonaFiksni());
        response.setEmailFakultet(sp.getEmailFakultet());
        response.setEmailPrivatni(sp.getEmailPrivatni());
        response.setBrojLicneKarte(sp.getBrojLicneKarte());
        response.setLicnuKartuIzdao(sp.getLicnuKartuIzdao());
        response.setAdresaStanovanja(sp.getAdresaStanovanja());
        System.out.println("---- Mapiranje studenta: " + sp.getIme() + " ----");
        if (sp.getIndeksi() == null) {
            System.out.println("GREŠKA: Lista indeksa je NULL!");
        } else {
            System.out.println("Veličina liste indeksa: " + sp.getIndeksi().size());
            // Ispisujemo svaki indeks koji nađemo
            sp.getIndeksi().forEach(ind ->
                    System.out.println(" -> Indeks: " + ind.getBroj() + ", Godina: " + ind.getGodina() + ", Aktivan: " + ind.isAktivan())
            );
        }
        if (sp.getIndeksi() != null && !sp.getIndeksi().isEmpty()) {
            StudentIndeks odabraniIndeks = sp.getIndeksi().stream()
                    .filter(indeks -> indeks.isAktivan())
                    .findFirst()
                    .orElse(null);

            if (odabraniIndeks == null) {
                System.out.println("Nema aktivnog indeksa, uzimam prvi nasumični.");
                odabraniIndeks = sp.getIndeksi().iterator().next();
            }

            response.setBrojIndeksa(odabraniIndeks.getBroj());
            response.setGodinaUpisa(odabraniIndeks.getGodina());
        } else {
            System.out.println("Lista indeksa je PRAZNA, setujem 0.");
            response.setBrojIndeksa(0);
            response.setGodinaUpisa(0);
        }
        return response;
    }
}
