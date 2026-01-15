package org.raflab.studsluzbadesktopclient.dtos;

public class UpisGodineResponse {
    private Long id;
    private String datumUpisa;
    private String napomena;
    private int godinaKojaSeUpisuje;
    private String skolskaGodina; // e.g., "2023/2024"
    private boolean obnova; // true if renewal, false if regular enrollment

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatumUpisa() {
        return datumUpisa;
    }

    public void setDatumUpisa(String datumUpisa) {
        this.datumUpisa = datumUpisa;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public int getGodinaKojaSeUpisuje() {
        return godinaKojaSeUpisuje;
    }

    public void setGodinaKojaSeUpisuje(int godinaKojaSeUpisuje) {
        this.godinaKojaSeUpisuje = godinaKojaSeUpisuje;
    }

    public String getSkolskaGodina() {
        return skolskaGodina;
    }

    public void setSkolskaGodina(String skolskaGodina) {
        this.skolskaGodina = skolskaGodina;
    }

    public boolean isObnova() {
        return obnova;
    }

    public void setObnova(boolean obnova) {
        this.obnova = obnova;
    }
}