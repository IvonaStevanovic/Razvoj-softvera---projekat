package org.raflab.studsluzbadesktopclient.dtos;

import java.time.LocalDate;

public class UplataRequest {
    private Double iznosRsd;
    private Double iznosEur;
    private String napomena; // Ovo je "Svrha uplate"
    private LocalDate datumUplate;
    private Long studentId;

    public UplataRequest() {}

    public Double getIznosRsd() { return iznosRsd; }
    public void setIznosRsd(Double iznosRsd) { this.iznosRsd = iznosRsd; }

    public Double getIznosEur() { return iznosEur; }
    public void setIznosEur(Double iznosEur) { this.iznosEur = iznosEur; }

    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }

    public LocalDate getDatumUplate() { return datumUplate; }
    public void setDatumUplate(LocalDate datumUplate) { this.datumUplate = datumUplate; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
}
