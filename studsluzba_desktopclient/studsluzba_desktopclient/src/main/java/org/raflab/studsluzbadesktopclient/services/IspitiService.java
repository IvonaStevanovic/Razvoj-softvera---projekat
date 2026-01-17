package org.raflab.studsluzbadesktopclient.services;

import javafx.application.Platform;
import org.raflab.studsluzbadesktopclient.dtos.*;
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
        webClient.get().uri("/api/ispit/{id}/rezultati", ispitId)
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

        webClient.post().uri("/api/ispit/prijavi")
                .bodyValue(req)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        res -> Platform.runLater(success),
                        err -> Platform.runLater(() -> error.accept("Student ne sluša predmet ili je već položio."))
                );
    }
    public void getAllPredmeti(Consumer<List<PredmetResponse>> callback) {
        webClient.get().uri("/api/predmeti/all")
                .retrieve()
                .bodyToFlux(PredmetResponse.class)
                .collectList()
                .subscribe(list -> Platform.runLater(() -> callback.accept(list)));
    }

    public void getAllNastavnici(Consumer<List<NastavnikResponse>> callback) {
        webClient.get().uri("/api/nastavnici/all")
                .retrieve()
                .bodyToFlux(NastavnikResponse.class)
                .collectList()
                .subscribe(list -> Platform.runLater(() -> callback.accept(list)));
    }

    // I metoda za čuvanje ispita koju će pozvati "Sačuvaj" dugme
    public void saveIspit(IspitRequest request, Runnable onSuccess) {
        if (request.getDrziPredmetId() == null) {
            System.err.println("Greška: drziPredmetId je NULL. Ne šaljem zahtev.");
            return;
        }

        webClient.post()
                .uri("/api/ispit")
                .bodyValue(request) // Ovde Jackson puca ako je unutra null
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        response -> Platform.runLater(onSuccess),
                        error -> System.err.println("Greška pri čuvanju: " + error.getMessage())
                );
    }
    public void getDrziPredmetId(Long predmetId, Long nastavnikId, Consumer<Long> callback) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/predmeti/drzi-predmet-id") // Mora se poklapati sa serverom
                        .queryParam("predmetId", predmetId)
                        .queryParam("nastavnikId", nastavnikId)
                        .build())
                .retrieve()
                .bodyToMono(Long.class)
                .subscribe(
                        id -> Platform.runLater(() -> callback.accept(id)),
                        err -> System.err.println("Greška na serveru: " + err.getMessage())
                );
    }
    public void saveIspitniRok(IspitniRokRequest request, Runnable onSuccess) {
        webClient.post()
                .uri("/api/ispitni-rokovi")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .subscribe(res -> Platform.runLater(onSuccess));
    }
}
