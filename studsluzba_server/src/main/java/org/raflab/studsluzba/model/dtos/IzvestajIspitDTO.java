package org.raflab.studsluzba.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IzvestajIspitDTO {
    private String nazivPredmeta;
    private Integer ocena;
    private Integer espb;
    private String datumPolaganja;
    private Integer godinaStudija;
}
