package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import org.jetbrains.annotations.NotNull;


import java.time.LocalDate;

@Data
public class StudentPodaciRequest {

    @NotNull
    private String ime;
    @NotNull
    private String prezime;
    @NotNull
    private String srednjeIme;
    @NotNull
    private String jmbg;
    @NotNull
    private LocalDate datumRodjenja;
    private String mestoRodjenja;
    @NotNull
    private String mestoPrebivalista;
    private String drzavaRodjenja;
    @NotNull
    private String drzavljanstvo;
    private String nacionalnost;
    @NotNull
    private Character pol;
    @NotNull
    private String adresa;
    private String brojTelefonaMobilni;
    private String brojTelefonaFiksni;
    @NotNull
    private String emailFakultet;
    private String emailPrivatni;
    private String brojLicneKarte;
    private String licnuKartuIzdao;


    private Double uspehSrednjaSkola;
    private Double uspehPrijemni;


    @NotNull
    private Long srednjaSkolaId;

    @NotNull
    private Integer brojIndeksa;

    @NotNull
    private int godinaUpisa;

    @NotNull
    private String studProgramOznaka;

    private String nacinFinansiranja;
    private String mestoStanovanja;
    private String adresaStanovanja;
}