package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.response.UplataResponse;
import org.raflab.studsluzba.services.StudentProfileService; // BITNO: Koristimo ovaj servis
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/uplate")
@RequiredArgsConstructor
public class UplataController {

    private final StudentProfileService studentProfileService;

    // Endpoint koji povezuje tabelu sa logikom iz StudentProfileService
    @GetMapping("/student/{studentId}")
    public List<UplataResponse> getUplateZaStudenta(@PathVariable Long studentId) {
        return studentProfileService.getSveUplate(studentId);
    }
}