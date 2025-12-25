package org.raflab.studsluzba.controllers.mappers;

import org.raflab.studsluzba.controllers.request.StudijskiProgramRequest;
import org.raflab.studsluzba.controllers.response.StudijskiProgramResponse;
import org.raflab.studsluzba.model.StudijskiProgram;
import org.raflab.studsluzba.model.VrstaStudija;

public class StudijskiProgramConverter {

    public static StudijskiProgram toEntity(
            StudijskiProgramRequest request,
            VrstaStudija vrstaStudija) {

        StudijskiProgram program = new StudijskiProgram();
        program.setOznaka(request.getOznaka());
        program.setNaziv(request.getNaziv());
        program.setGodinaAkreditacije(request.getGodinaAkreditacije());
        program.setZvanje(request.getZvanje());
        program.setTrajanjeGodina(request.getTrajanjeGodina());
        program.setTrajanjeSemestara(request.getTrajanjeSemestara());
        program.setUkupnoEspb(request.getUkupnoEspb());
        program.setVrstaStudija(vrstaStudija);

        return program;
    }

    public static StudijskiProgramResponse toResponse(StudijskiProgram program) {
        StudijskiProgramResponse response = new StudijskiProgramResponse();
        response.setId(program.getId());
        response.setOznaka(program.getOznaka());
        response.setNaziv(program.getNaziv());
        response.setGodinaAkreditacije(program.getGodinaAkreditacije());
        response.setZvanje(program.getZvanje());
        response.setTrajanjeGodina(program.getTrajanjeGodina());
        response.setTrajanjeSemestara(program.getTrajanjeSemestara());
        response.setUkupnoEspb(program.getUkupnoEspb());

        if (program.getVrstaStudija() != null) {
            response.setVrstaStudijaNaziv(program.getVrstaStudija().getPunNaziv());
        }

        return response;
    }
}

