package org.raflab.studsluzbadesktopclient.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudijskiProgramRequest {

    @NotNull
    private String oznaka;

    @NotNull
    private String naziv;

    @NotNull
    private Integer godinaAkreditacije;

    private String zvanje;

    private Integer trajanjeGodina;

    private Integer trajanjeSemestara;

    private Integer ukupnoEspb;

    private Long vrstaStudijaId;
}
