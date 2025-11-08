package org.raflab.studsluzba.controllers.response;

import lombok.Data;

import java.util.List;

@Data
public class GrupaResponse {
    private Long id;
    private Long studijskiProgramId;
    private String studijskiProgramNaziv;
    private List<String> predmetiNaziv;
}
