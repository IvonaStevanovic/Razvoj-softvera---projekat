package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NepolozeniPredmetResponse {
    private Long id;
    private Long predmetId;
    private String sifraPredmeta;
    private String nazivPredmeta;
    private int espb;
}
