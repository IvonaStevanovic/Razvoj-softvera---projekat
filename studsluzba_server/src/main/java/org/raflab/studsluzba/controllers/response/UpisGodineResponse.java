package org.raflab.studsluzba.controllers.response;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UpisGodineResponse {
    private Long id;
    private Integer godinaStudija;
    private LocalDate datumUpisa;
    private String napomena;
    private String skolskaGodina;
    private List<PredmetResponse> predmeti;

    // Getteri i setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getGodinaStudija() { return godinaStudija; }
    public void setGodinaStudija(Integer godinaStudija) { this.godinaStudija = godinaStudija; }

    public LocalDate getDatumUpisa() { return datumUpisa; }
    public void setDatumUpisa(LocalDate datumUpisa) { this.datumUpisa = datumUpisa; }

    public String getNapomena() { return napomena; }
    public void setNapomena(String napomena) { this.napomena = napomena; }

    public String getSkolskaGodina() { return skolskaGodina; }
    public void setSkolskaGodina(String skolskaGodina) { this.skolskaGodina = skolskaGodina; }

    public List<PredmetResponse> getPredmeti() { return predmeti; }
    public void setPredmeti(List<PredmetResponse> predmeti) { this.predmeti = predmeti; }
}
