package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class VisokoskolskaUstanova {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    private String mesto;
    private String vrsta;

   /* @OneToMany(mappedBy = "visokoskolska_ustanova")
    private StudentPodaci  studentPodaci; */
}
