package org.raflab.studsluzbadesktopclient.dtos;

import lombok.Data;

@Data
public class StudentIspitRezultatiResponse {
    private Long studentIndeksId;
    private String ime;
    private String prezime;
    private String studijskiProgram;
    private int godinaUpisa;
    private String brojIndeksa;
    private int ukupnoPoena;
    
    public String getStudentImePrezime() {
        return ime + " " + prezime;
    }

    // 2. HELPER: Formatiran indeks (npr. 15/2023) za tabelu
    public String getFormatiranIndeks() {
        return brojIndeksa + "/" + godinaUpisa;
    }

    // 3. LOGIKA: Izračunavanje ocene na osnovu poena
    public int getOcena() {
        if (ukupnoPoena < 51) return 5;
        if (ukupnoPoena <= 60) return 6;
        if (ukupnoPoena <= 70) return 7;
        if (ukupnoPoena <= 80) return 8;
        if (ukupnoPoena <= 90) return 9;
        return 10;
    }
}

