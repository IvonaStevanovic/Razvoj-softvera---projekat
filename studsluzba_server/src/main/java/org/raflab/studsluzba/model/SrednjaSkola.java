package org.raflab.studsluzba.model;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class SrednjaSkola {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String naziv;
    private String mesto;
    private String vrsta;

    @OneToMany(mappedBy = "srednjaSkola")
    private Set<StudentPodaci> studentPodaci;
}
