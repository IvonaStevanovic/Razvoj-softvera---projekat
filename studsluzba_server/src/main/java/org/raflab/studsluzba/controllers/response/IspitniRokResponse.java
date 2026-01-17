package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class IspitniRokResponse {
    private Long id;
    private String naziv;
    private Long skolskaGodinaId;
    private String skolskaGodinaNaziv;
    private LocalDate pocetak;
    private LocalDate kraj;
    private Boolean aktivan;

    public String getNaziv() {
        return naziv;
    }

    public Long getSkolskaGodinaId() {
        return skolskaGodinaId;
    }

    public String getSkolskaGodinaNaziv() {
        return skolskaGodinaNaziv;
    }

    public LocalDate getPocetak() {
        return pocetak;
    }

    public LocalDate getKraj() {
        return kraj;
    }

    public Boolean getAktivan() {
        return aktivan;
    }
}
