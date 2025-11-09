package org.raflab.studsluzba.controllers.request;


import lombok.Data;

@Data
public class SrednjaSkolaRequest {
    private String naziv;
    private String mesto;
    private String vrsta;
}