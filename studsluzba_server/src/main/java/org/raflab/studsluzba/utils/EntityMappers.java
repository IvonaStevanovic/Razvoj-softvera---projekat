package org.raflab.studsluzba.utils;

import org.raflab.studsluzba.controllers.request.SlusaPredmetRequest;
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
        if (si == null) return null;

        StudentIndeksResponse response = new StudentIndeksResponse();
        response.setId(si.getId());
        response.setBroj(si.getBroj());
        response.setGodina(si.getGodina());
        response.setStudProgramOznaka(si.getStudProgramOznaka());
        response.setNacinFinansiranja(si.getNacinFinansiranja());
        response.setAktivan(si.isAktivan());
        response.setVaziOd(si.getVaziOd());
        response.setOstvarenoEspb(si.getOstvarenoEspb());

        // Lagani DTO za StudijskiProgram
        if (si.getStudijskiProgram() != null) {
            StudijskiProgram sp = new StudijskiProgram();
            sp.setId(si.getStudijskiProgram().getId());
            sp.setOznaka(si.getStudijskiProgram().getOznaka());
            sp.setNaziv(si.getStudijskiProgram().getNaziv());
            response.setStudijskiProgram(sp);
        }

        // Lagani DTO za StudentPodaci
        if (si.getStudent() != null) {
            StudentPodaci student = new StudentPodaci();
            student.setId(si.getStudent().getId());
            student.setIme(si.getStudent().getIme());
            student.setPrezime(si.getStudent().getPrezime());
            student.setEmailFakultet(si.getStudent().getEmailFakultet());
            response.setStudent(student);
        }

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
        response.setEmailFakultet(sp.getEmailFakultet());
        response.setEmailPrivatni(sp.getEmailPrivatni());
        response.setBrojLicneKarte(sp.getBrojLicneKarte());
        response.setLicnuKartuIzdao(sp.getLicnuKartuIzdao());
        response.setMestoStanovanja(sp.getMestoStanovanja());
        response.setAdresaStanovanja(sp.getAdresaStanovanja());

        return response;
    }

    public static NastavnikResponse toNastavnikResponse(Nastavnik nastavnik) {
        if (nastavnik == null) return null;

        NastavnikResponse resp = new NastavnikResponse();
        resp.setId(nastavnik.getId());
        resp.setIme(nastavnik.getIme());
        resp.setPrezime(nastavnik.getPrezime());
        resp.setSrednjeIme(nastavnik.getSrednjeIme());
        resp.setEmail(nastavnik.getEmail());
        resp.setBrojTelefona(nastavnik.getBrojTelefona());
        resp.setAdresa(nastavnik.getAdresa());
        resp.setDatumRodjenja(nastavnik.getDatumRodjenja());
        resp.setPol(nastavnik.getPol());
        resp.setJmbg(nastavnik.getJmbg());

        if (nastavnik.getZvanja() != null) {
            resp.setZvanja(
                    nastavnik.getZvanja().stream()
                            .map(NastavnikZvanje::getZvanje)
                            .collect(Collectors.toSet())
            );
        }

        return resp;
    }



    public static List<NastavnikResponse> toNastavnikResponseList(Iterable<Nastavnik> nastavnici) {
        return ((List<Nastavnik>) nastavnici).stream()
                .map(EntityMappers::toNastavnikResponse)
                .collect(Collectors.toList());
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

    public SlusaPredmet fromSlusaPredmetRequestToEntity(SlusaPredmetRequest request, StudentIndeks studentIndeks, DrziPredmet drziPredmet, SkolskaGodina skolskaGodina) {
        SlusaPredmet sp = new SlusaPredmet();
        sp.setStudentIndeks(studentIndeks);
        sp.setDrziPredmet(drziPredmet);
        sp.setSkolskaGodina(skolskaGodina);
        return sp;
    }

    public SlusaPredmetResponse fromSlusaPredmetToResponse(SlusaPredmet sp) {
        SlusaPredmetResponse response = new SlusaPredmetResponse();
        response.setId(sp.getId());

        if (sp.getStudentIndeks() != null) {
            response.setStudentIndeksId(sp.getStudentIndeks().getId());
            if (sp.getStudentIndeks().getStudent() != null) {
                response.setStudentImePrezime(
                        sp.getStudentIndeks().getStudent().getIme() + " " +
                                sp.getStudentIndeks().getStudent().getPrezime()
                );
            }
        }

        if (sp.getDrziPredmet() != null && sp.getDrziPredmet().getPredmet() != null) {
            response.setDrziPredmetId(sp.getDrziPredmet().getPredmet().getId());
            response.setPredmetNaziv(sp.getDrziPredmet().getPredmet().getNaziv());
        }

        if (sp.getDrziPredmet() != null && sp.getDrziPredmet().getNastavnik() != null) {
            response.setDrziPredmetId(sp.getDrziPredmet().getNastavnik().getId());
            response.setNastavnikImePrezime(
                    sp.getDrziPredmet().getNastavnik().getIme() + " " +
                            sp.getDrziPredmet().getNastavnik().getPrezime()
            );
        }

        if (sp.getSkolskaGodina() != null) {
            response.setSkolskaGodinaId(sp.getSkolskaGodina().getId());
            response.setSkolskaGodinaNaziv(sp.getSkolskaGodina().getNaziv());
        }

        return response;
    }

    public List<SlusaPredmetResponse> fromSlusaPredmetListToResponseList(List<SlusaPredmet> list) {
        return list.stream()
                .map(this::fromSlusaPredmetToResponse)
                .collect(Collectors.toList());
    }


    public static UpisGodine toUpisGodine(UpisGodineRequest request,
                                          StudentIndeks student,
                                          Set<Predmet> prenetiPredmeti) {
        UpisGodine upis = new UpisGodine();
        upis.setGodinaStudija(request.getGodinaStudija());
        upis.setDatum(request.getDatum());
        upis.setNapomena(request.getNapomena());
        upis.setStudentIndeks(student);
        upis.setPrenetiPredmeti(prenetiPredmeti);
        return upis;
    }

    public static UpisGodineResponse toUpisGodineResponse(UpisGodine upis) {
        UpisGodineResponse response = new UpisGodineResponse();
        response.setId(upis.getId());
        response.setGodinaStudija(upis.getGodinaStudija());
        response.setDatum(upis.getDatum());
        response.setNapomena(upis.getNapomena());

        if (upis.getStudentIndeks() != null) {
            response.setStudentIndeksId(upis.getStudentIndeks().getId());
        } else {
            response.setStudentIndeksId(null);
        }

        if (upis.getPrenetiPredmeti() != null && !upis.getPrenetiPredmeti().isEmpty()) {
            response.setPrenetiPredmetiNazivi(
                    upis.getPrenetiPredmeti().stream()
                            .map(Predmet::getNaziv)
                            .collect(Collectors.toSet())
            );
        } else {
            response.setPrenetiPredmetiNazivi(Set.of());
        }

        return response;
    }

    public static Uplata fromUplataRequestToEntity(UplataRequest request, StudentIndeks studentIndeks, Double srednjiKurs) {
        if (request == null || studentIndeks == null) return null;

        Uplata u = new Uplata();
        u.setStudentIndeks(studentIndeks);
        u.setDatumUplate(request.getDatumUplate());
        u.setIznos(request.getIznos());
        u.setSrednjiKurs(srednjiKurs);
        return u;
    }

    public static UplataResponse fromUplataToResponse(Uplata uplata) {
        if (uplata == null) return null;

        UplataResponse resp = new UplataResponse();
        resp.setId(uplata.getId());
        resp.setDatumUplate(uplata.getDatumUplate());
        resp.setIznos(uplata.getIznos());
        resp.setSrednjiKurs(uplata.getSrednjiKurs());
        return resp;
    }

    public static List<UplataResponse> fromUplataListToResponseList(List<Uplata> list) {
        if (list == null) return List.of();
        return list.stream()
                .map(EntityMappers::fromUplataToResponse)
                .collect(Collectors.toList());
    }

    public static PreostaliIznosResponse calculatePreostaliIznos(List<Uplata> uplate, double ukupnoDin, double kurs) {
        double ukupnoUplacenoDin = uplate != null
                ? uplate.stream().mapToDouble(u -> u.getIznos() * (u.getSrednjiKurs() != null ? u.getSrednjiKurs() : kurs)).sum()
                : 0;

        double ukupnoUplacenoEur = uplate != null
                ? uplate.stream().mapToDouble(Uplata::getIznos).sum()
                : 0;

        PreostaliIznosResponse resp = new PreostaliIznosResponse();
        resp.setPreostaloDin(Math.max(0, ukupnoDin - ukupnoUplacenoDin));
        resp.setPreostaloEur(Math.max(0, ukupnoUplacenoEur - ukupnoDin / kurs)); // ili po potrebi
        return resp;
    }

}
