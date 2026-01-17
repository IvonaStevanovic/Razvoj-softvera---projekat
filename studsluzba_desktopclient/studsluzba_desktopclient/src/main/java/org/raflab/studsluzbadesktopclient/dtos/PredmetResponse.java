package org.raflab.studsluzbadesktopclient.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PredmetResponse {
    private Long id;
    private String sifra;
    private String naziv;
    private String opis;
    private Integer espb;
    private Long studijskiProgramId;
    private String studijskiProgramNaziv;
    private Boolean obavezan;
    private Integer fondPredavanja;
    private Integer fondVezbe;
    private int godina;

    // Defaultni konstruktor
    public PredmetResponse() { }

    // Konstruktor koji StudentProfileService koristi
    public PredmetResponse(Long id, String naziv, Integer espb) {
        this.id = id;
        this.naziv = naziv;
        this.espb = espb;
    }

    // Konstruktor sa svim parametrima
    public PredmetResponse(Long id, String sifra, String naziv, String opis, Integer espb,
                           Integer fondPredavanja, Integer fondVezbe, Boolean obavezan,
                           Long studijskiProgramId, String studijskiProgramNaziv) {
        this.id = id;
        this.sifra = sifra;
        this.naziv = naziv;
        this.opis = opis;
        this.espb = espb;
        this.fondPredavanja = fondPredavanja;
        this.fondVezbe = fondVezbe;
        this.obavezan = obavezan;
        this.studijskiProgramId = studijskiProgramId;
        this.studijskiProgramNaziv = studijskiProgramNaziv;
    }
}
