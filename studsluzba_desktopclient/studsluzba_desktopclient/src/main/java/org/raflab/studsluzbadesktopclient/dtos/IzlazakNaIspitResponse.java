package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

@Data
public class IzlazakNaIspitResponse {
    private Long id;
    private Integer ostvarenoNaIspitu;
    private String napomena;
    private boolean ponistio;
    private boolean izasao;
    private Long studentIndeksId;
    private String studentImePrezime;
    private Long ispitId;
    private String predmetNaziv;
    private Integer poeniPredispitne;

    
    public int getUkupnoPoena() {
        return (ostvarenoNaIspitu != null ? ostvarenoNaIspitu : 0) +
                (poeniPredispitne != null ? poeniPredispitne : 0);
    }

}
