package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class PredispitneObaveze {
    @Id
    private Long id;
    private String vrsta;
    private Integer maksPoeni;

   /* @ManyToOne
    private SlusaPredmet slusaPredmet;*/

}
