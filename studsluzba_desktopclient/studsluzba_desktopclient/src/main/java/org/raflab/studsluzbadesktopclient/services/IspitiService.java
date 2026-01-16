package org.raflab.studsluzbadesktopclient.services;

import javafx.application.Platform;
import org.raflab.studsluzbadesktopclient.dtos.IspitResponse;
import org.raflab.studsluzbadesktopclient.dtos.IspitniRokResponse;
import org.raflab.studsluzbadesktopclient.dtos.PrijavaIspitaRequest;
import org.raflab.studsluzbadesktopclient.dtos.StudentIspitRezultatiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
@Service
public class IspitiService {
    @Autowired
    private WebClient webClient;

    public void getAllRokovi(Consumer<List<IspitniRokResponse>> callback) {
        webClient.get().uri("/api/ispitni-rokovi/all")
                .retrieve().bodyToFlux(IspitniRokResponse.class).collectList()
                .subscribe(list -> Platform.runLater(() -> callback.accept(list)));
    }

    public void getIspitiByRok(Long rokId, Consumer<List<IspitResponse>> callback) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/ispit/search/rok")
                        .queryParam("ispitniRokId", rokId) // Mora odgovarati @RequestParam na serveru
                        .build())
                .retrieve()
                .bodyToFlux(IspitResponse.class)
                .collectList()
                .subscribe(
                        list -> Platform.runLater(() -> callback.accept(list)),
                        err -> System.err.println("Greška: " + err.getMessage())
                );
    }

    public void getRezultati(Long ispitId, Consumer<List<StudentIspitRezultatiResponse>> callback) {
        webClient.get().uri("/api/ispiti/{id}/rezultati", ispitId)
                .retrieve()
                .bodyToFlux(StudentIspitRezultatiResponse.class) // KORISTI NOVU KLASU
                .collectList()
                .subscribe(list -> Platform.runLater(() -> callback.accept(list)));
    }

    public void prijaviStudenta(Long ispitId, Long studentIndeksId, Runnable success, Consumer<String> error) {
        // Kreiramo request DTO koji tvoj server očekuje
        PrijavaIspitaRequest req = new PrijavaIspitaRequest();
        req.setIspitId(ispitId);
        req.setStudentIndeksId(studentIndeksId);
        req.setDatumPrijave(LocalDate.now());

        webClient.post().uri("/api/ispiti/prijavi")
                .bodyValue(req)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        res -> Platform.runLater(success),
                        err -> Platform.runLater(() -> error.accept("Student ne sluša predmet ili je već položio."))
                );
    }
}
