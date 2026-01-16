package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

@Data
public class SrednjaSkolaResponse {
    private Long id;
    private String naziv;
    private String mesto;
    private String vrsta;
}