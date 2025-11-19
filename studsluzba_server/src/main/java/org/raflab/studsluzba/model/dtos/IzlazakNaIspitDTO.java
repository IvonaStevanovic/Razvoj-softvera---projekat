package org.raflab.studsluzba.model.dtos;

import lombok.Data;
import org.raflab.studsluzba.model.Ispit;
import org.raflab.studsluzba.model.SlusaPredmet;
import org.raflab.studsluzba.model.StudentIndeks;

@Data
public class IzlazakNaIspitDTO {
    private StudentIndeks student;
    private Ispit ispit;
    private SlusaPredmet slusaPredmet;
    private int ukupnoPoeni;
}
