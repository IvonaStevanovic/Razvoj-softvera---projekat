package org.raflab.studsluzba.utils;

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
        response.setMestoPrebivalista(sp.getMestoPrebivalista());
        response.setDrzavaRodjenja(sp.getDrzavaRodjenja());
        response.setDrzavljanstvo(sp.getDrzavljanstvo());
        response.setNacionalnost(sp.getNacionalnost());
        response.setPol(sp.getPol());
        response.setAdresa(sp.getAdresa());
        response.setBrojTelefonaMobilni(sp.getBrojTelefonaMobilni());
        response.setBrojTelefonaFiksni(sp.getBrojTelefonaFiksni());
        response.setEmail(sp.getEmailPrivatni());
        response.setEmail(sp.getEmailFakultet());
        response.setBrojLicneKarte(sp.getBrojLicneKarte());
        response.setLicnuKartuIzdao(sp.getLicnuKartuIzdao());
        response.setMestoStanovanja(sp.getMestoStanovanja());
        response.setAdresaStanovanja(sp.getAdresaStanovanja());

        return response;
    }
    public NastavnikResponse fromNastavnikToResponse(Nastavnik entity) {
        if (entity == null) return null;
        NastavnikResponse resp = new NastavnikResponse();
        resp.setId(entity.getId());
        resp.setIme(entity.getIme());
        resp.setPrezime(entity.getPrezime());
        resp.setEmail(entity.getEmail());
        resp.setBrojTelefona(entity.getBrojTelefona());
        return resp;
    }

    public IspitResponse fromIspitToResponse(Ispit entity) {
        if (entity == null) return null;

        IspitResponse resp = new IspitResponse();
        resp.setId(entity.getId());
        resp.setDatumOdrzavanja(entity.getDatumOdrzavanja());
        resp.setVremePocetka(entity.getVremePocetka());
        resp.setZakljucen(entity.isZakljucen());

        resp.setPredmetId(entity.getPredmet() != null ? entity.getPredmet().getId() : null);
        resp.setPredmetNaziv(entity.getPredmet() != null ? entity.getPredmet().getNaziv() : null);
        resp.setNastavnikId(entity.getNastavnik() != null ? entity.getNastavnik().getId() : null);
        resp.setIspitniRokId(entity.getIspitniRok() != null ? entity.getIspitniRok().getId() : null);

        return resp;
    }

    public IspitniRokResponse fromIspitniRokToResponse(IspitniRok entity) {
        if (entity == null) return null;
        IspitniRokResponse resp = new IspitniRokResponse();
        resp.setId(entity.getId());
        resp.setDatumPocetka(entity.getDatumPocetka());
        resp.setDatumZavrsetka(entity.getDatumZavrsetka());
        return resp;
    }

    public PredmetResponse fromPredmetToResponse(Predmet entity) {
        if (entity == null) return null;
        PredmetResponse resp = new PredmetResponse();
        resp.setId(entity.getId());
        resp.setNaziv(entity.getNaziv());
        resp.setSifra(entity.getSifra());
        resp.setEspb(entity.getEspb());
        return resp;
    }

    public DrziPredmetResponse fromDrziPredmetToResponse(DrziPredmet entity) {
        if (entity == null) return null;
        DrziPredmetResponse resp = new DrziPredmetResponse();
        resp.setId(entity.getId());
        resp.setPredmetId(entity.getPredmet() != null ? entity.getPredmet().getId() : null);
        resp.setNastavnikId(entity.getNastavnik() != null ? entity.getNastavnik().getId() : null);
        return resp;
    }

    public GrupaResponse fromGrupaToResponse(Grupa entity) {
        if (entity == null) return null;

        GrupaResponse resp = new GrupaResponse();
        resp.setId(entity.getId());

        if (entity.getStudijskiProgram() != null) {
            resp.setStudijskiProgramId(entity.getStudijskiProgram().getId());
            resp.setStudijskiProgramNaziv(entity.getStudijskiProgram().getNaziv());
        }

        if (entity.getPredmeti() != null) {
            List<String> list = new ArrayList<>();
            for (Predmet predmet : entity.getPredmeti()) {
                String naziv = predmet.getNaziv();
                list.add(naziv);
            }
            resp.setPredmetiNaziv(Collections.unmodifiableList(list));
        } else {
            resp.setPredmetiNaziv(List.of());
        }

        return resp;
    }
    public IzlazakNaIspitResponse fromIzlazakNaIspitToResponse(IzlazakNaIspit entity) {
        if (entity == null) return null;
        IzlazakNaIspitResponse resp = new IzlazakNaIspitResponse();
        resp.setId(entity.getId());
        resp.setOstvarenoNaIspitu(entity.getOstvarenoNaIspitu());
        resp.setNapomena(entity.getNapomena());
        resp.setPonistio(entity.isPonistio());
        resp.setIzasao(entity.isIzasao());
        resp.setStudentIndeksId(entity.getStudentIndeks() != null ? entity.getStudentIndeks().getId() : null);
        resp.setIspitId(entity.getIspit() != null ? entity.getIspit().getId() : null);
        return resp;
    }

    public NastavnikZvanjeResponse fromNastavnikZvanjeToResponse(NastavnikZvanje entity) {
        if (entity == null) return null;
        NastavnikZvanjeResponse resp = new NastavnikZvanjeResponse();
        resp.setId(entity.getId());
        resp.setDatumIzbora(entity.getDatumIzbora());
        resp.setNaucnaOblast(entity.getNaucnaOblast());
        resp.setUzaNaucnaOblast(entity.getUzaNaucnaOblast());
        resp.setZvanje(entity.getZvanje());
        resp.setAktivno(entity.isAktivno());
        resp.setNastavnikId(entity.getNastavnik() != null ? entity.getNastavnik().getId() : null);
        return resp;
    }
    public ObnovaGodineResponse fromObnovaGodineToResponse(ObnovaGodine entity) {
        if (entity == null) return null;

        ObnovaGodineResponse resp = new ObnovaGodineResponse();
        resp.setId(entity.getId());
        resp.setGodinaStudija(entity.getGodinaStudija());
        resp.setDatum(entity.getDatum());
        resp.setNapomena(entity.getNapomena());

        if (entity.getStudentIndeks() != null && entity.getStudentIndeks().getStudent() != null) {
            resp.setStudentIndeksId(entity.getStudentIndeks().getId());
            resp.setStudentImePrezime(entity.getStudentIndeks().getStudent().getIme() + " " +
                    entity.getStudentIndeks().getStudent().getPrezime());
        }

        if (entity.getPredmetiKojeUpisuje() != null) {
            resp.setPredmetiNazivi(
                    entity.getPredmetiKojeUpisuje()
                            .stream()
                            .map(Predmet::getNaziv)
                            .collect(Collectors.toSet())
            );
        } else {
            resp.setPredmetiNazivi(Set.of());
        }

        return resp;
    }


    public PolozeniPredmetiResponse fromPolozeniPredmetiToResponse(PolozeniPredmeti entity) {
        PolozeniPredmetiResponse resp = new PolozeniPredmetiResponse();
        resp.setId(entity.getId());
        resp.setStudentIndeksId(entity.getStudentIndeks().getId());
        resp.setStudentImePrezime(entity.getStudentIndeks().getStudent().getIme() + " " +
                entity.getStudentIndeks().getStudent().getPrezime());
        resp.setPredmetId(entity.getPredmet().getId());
        resp.setPredmetNaziv(entity.getPredmet().getNaziv());
        resp.setOcena(entity.getOcena());
        resp.setPriznat(entity.isPriznat());
        resp.setIzlazakNaIspitId(entity.getIzlazakNaIspit() != null ? entity.getIzlazakNaIspit().getId() : null);
        return resp;
    }

    public StudijskiProgramResponse fromStudijskiProgramToResponse(StudijskiProgram sp) {
        if (sp == null) return null;

        StudijskiProgramResponse response = new StudijskiProgramResponse();
        response.setId(sp.getId());
        response.setOznaka(sp.getOznaka());
        response.setNaziv(sp.getNaziv());
        response.setGodinaAkreditacije(sp.getGodinaAkreditacije());
        response.setZvanje(sp.getZvanje());
        response.setTrajanjeGodina(sp.getTrajanjeGodina());
        response.setTrajanjeSemestara(sp.getTrajanjeSemestara());
        response.setUkupnoEspb(sp.getUkupnoEspb());
        response.setVrstaStudijaNaziv(sp.getVrstaStudija() != null ? sp.getVrstaStudija().getPunNaziv() : null);

        return response;
    }

}
