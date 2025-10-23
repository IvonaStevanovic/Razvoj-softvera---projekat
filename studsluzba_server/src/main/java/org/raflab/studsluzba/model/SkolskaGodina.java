package org.raflab.studsluzba.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class SkolskaGodina {
    @Id
    private Long id;
    private boolean aktivna;

    @OneToMany(mappedBy = "skolskaGodina")
    private List<DrziPredmet> drziPredmetList;
}
