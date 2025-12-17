package org.raflab.studsluzba.controllers;

import lombok.RequiredArgsConstructor;
import org.raflab.studsluzba.controllers.request.ObnovaGodineRequest;
import org.raflab.studsluzba.controllers.response.ObnovaGodineResponse;
import org.raflab.studsluzba.model.ObnovaGodine;
import org.raflab.studsluzba.repositories.ObnovaGodineRepository;
import org.raflab.studsluzba.services.ObnovaGodineService;
import org.raflab.studsluzba.utils.Converters;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/obnova-godine")
@RequiredArgsConstructor
public class ObnovaGodineController {
/*
    private final ObnovaGodineService service;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody ObnovaGodineRequest request){
        try {
            ObnovaGodine obnova = service.save(request);
            return ResponseEntity.ok(Converters.toObnovaGodineResponse(obnova));
        } catch (RuntimeException e) {
            if(e.getMessage().contains("već postoji")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/all")
    public List<ObnovaGodineResponse> getAll(){
        return service.findAll()
                .stream()
                .map(Converters::toObnovaGodineResponse)
                .collect(Collectors.toList());
    }


    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        service.delete(id);
        return "Obrisana obnova godine sa ID " + id;
    }

 */
}
