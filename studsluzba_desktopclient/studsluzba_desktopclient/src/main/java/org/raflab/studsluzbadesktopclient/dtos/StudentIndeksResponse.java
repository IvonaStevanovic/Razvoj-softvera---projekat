package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentIndeksResponse {
    private Long id;
    private int broj;
    private int godina;
    private String studProgramOznaka;
    private String nacinFinansiranja;
    private boolean aktivan;
    private LocalDate vaziOd;
    private StudentPodaciResponse student;
    private StudijskiProgramResponse studijskiProgram;   // na koji studijski program je upisan
    private Integer ostvarenoEspb;
}
