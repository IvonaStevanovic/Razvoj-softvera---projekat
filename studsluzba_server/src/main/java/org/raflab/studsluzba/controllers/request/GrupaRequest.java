package org.raflab.studsluzba.controllers.request;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class GrupaRequest {
    @NonNull
    private Long studijskiProgramId;
    @NonNull
    private List<Long> predmetiId;
}
