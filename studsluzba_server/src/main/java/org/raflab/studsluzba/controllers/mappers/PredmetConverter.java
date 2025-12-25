package org.raflab.studsluzba.controllers.mappers;

import org.raflab.studsluzba.controllers.request.PredmetRequest;
import org.raflab.studsluzba.controllers.response.PredmetResponse;
import org.raflab.studsluzba.model.Predmet;
import org.raflab.studsluzba.model.StudijskiProgram;

public class PredmetConverter {

    public static Predmet toEntity(PredmetRequest request, StudijskiProgram studijskiProgram) {
        Predmet predmet = new Predmet();
        predmet.setSifra(request.getSifra());
        predmet.setNaziv(request.getNaziv());
        predmet.setOpis(request.getOpis());
        predmet.setEspb(request.getEspb());
        predmet.setFondPredavanja(request.getFondPredavanja());
        predmet.setFondVezbe(request.getFondVezbe());
        predmet.setObavezan(request.getObavezan());
        predmet.setStudProgram(studijskiProgram);
        return predmet;
    }

    public static PredmetResponse toResponse(Predmet predmet) {
        PredmetResponse response = new PredmetResponse();
        response.setId(predmet.getId());
        response.setSifra(predmet.getSifra());
        response.setNaziv(predmet.getNaziv());
        response.setOpis(predmet.getOpis());
        response.setEspb(predmet.getEspb());
        response.setFondPredavanja(predmet.getFondPredavanja());
        response.setFondVezbe(predmet.getFondVezbe());
        response.setObavezan(predmet.isObavezan());

        if (predmet.getStudProgram() != null) {
            response.setStudijskiProgramId(predmet.getStudProgram().getId());
            response.setStudijskiProgramNaziv(predmet.getStudProgram().getNaziv());
        }

        return response;
    }
}

