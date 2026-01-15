package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UplataResponse {
    private LocalDate datumUplate;
    private Double iznosEur;
    private Double iznosRsd;
    private Double srednjiKurs;


    public UplataResponse() {
    }

    public UplataResponse(LocalDate datumUplate, Double iznosEur, Double iznosRsd, Double srednjiKurs) {
        this.datumUplate = datumUplate;
        this.iznosEur = iznosEur;
        this.iznosRsd = iznosRsd;
        this.srednjiKurs = srednjiKurs;
    }
}
