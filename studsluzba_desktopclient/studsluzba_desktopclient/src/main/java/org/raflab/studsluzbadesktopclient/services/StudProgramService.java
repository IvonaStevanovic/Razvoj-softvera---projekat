package org.raflab.studsluzbadesktopclient.services;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.raflab.studsluzbadesktopclient.dtos.StudijskiProgramResponse;
import org.raflab.studsluzbadesktopclient.dtos.PredmetResponse;
import org.raflab.studsluzbadesktopclient.dtos.PredmetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.function.Consumer;

@Service
public class StudProgramService {

    @Autowired
    private WebClient webClient;

    // Dobavljanje svih studijskih programa (za listu sa leve strane)
    public void getAllPrograms(Consumer<List<StudijskiProgramResponse>> callback) {
        webClient.get()
                .uri("/api/studijski-programi/all")
                .retrieve()
                .bodyToFlux(StudijskiProgramResponse.class)
                .collectList()
                .subscribe(list -> Platform.runLater(() -> callback.accept(list)));
    }

    // Dobavljanje predmeta za izabrani program
    public void getPredmetiByProgram(Long programId, Consumer<List<PredmetResponse>> callback) {
        webClient.get()
                .uri("/api/predmet/program/{id}", programId)
                .retrieve()
                .bodyToFlux(PredmetResponse.class)
                .collectList()
                .subscribe(list -> Platform.runLater(() -> callback.accept(list)));
    }

    // Dodavanje novog predmeta (Post zahtev)
    public void addPredmet(PredmetRequest request, Consumer<PredmetResponse> callback, Consumer<String> errorCallback) {
        webClient.post()
                .uri("/api/predmet/add")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(PredmetResponse.class)
                .subscribe(
                        res -> Platform.runLater(() -> callback.accept(res)),
                        err -> Platform.runLater(() -> errorCallback.accept(err.getMessage()))
                );
    }
    public void getProsekZaPredmet(Long predmetId, Integer odG, Integer doG, Consumer<Double> callback) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/predmet/{id}/prosek")
                        .queryParam("odGodina", odG)
                        .queryParam("doGodina", doG)
                        .build(predmetId))
                .retrieve()
                .bodyToMono(Double.class)
                .subscribe(prosek -> Platform.runLater(() -> callback.accept(prosek)));
    }
    public void deletePredmet(Long id, Runnable successCallback, Consumer<String> errorCallback) {
        webClient.delete()
                .uri("/api/predmet/{id}", id)
                .retrieve()
                .toBodilessEntity()
                .subscribe(
                        res -> Platform.runLater(successCallback),
                        err -> Platform.runLater(() -> errorCallback.accept("Ne možete obrisati predmet jer ga studenti slušaju."))
                );
    }
}