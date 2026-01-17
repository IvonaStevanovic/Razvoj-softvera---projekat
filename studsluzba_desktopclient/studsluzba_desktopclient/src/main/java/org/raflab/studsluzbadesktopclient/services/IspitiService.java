package org.raflab.studsluzbadesktopclient.services;

import javafx.application.Platform;
import org.raflab.studsluzbadesktopclient.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    public void prijaviStudenta(Long ispitId, String indeksString, Runnable success, Consumer<String> error) {
        PrijavaIspitaRequest req = new PrijavaIspitaRequest();
        req.setIspitId(ispitId);
        req.setStudentIndeksUnos(indeksString);
        req.setDatumPrijave(LocalDate.now());

        webClient.post().uri("/api/ispit/prijavi")
                .bodyValue(req)
                .retrieve()
                // Ako server vrati 4xx ili 5xx grešku
                .onStatus(status -> status.isError(), clientResponse ->
                        clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            // errorBody obično sadrži JSON, ali Spring često vrati čistu poruku
                            // Ovde pokušavamo da izvučemo "message" polje ili ceo tekst
                            return Mono.error(new RuntimeException(errorBody));
                        })
                )
                .toBodilessEntity()
                .subscribe(
                        res -> Platform.runLater(success),
                        err -> {
                            // Ovde parsiramo poruku da ne bi pisalo "java.lang.RuntimeException..."
                            String rawMessage = err.getMessage();
                            String cleanMessage = "Došlo je do greške na serveru.";

                            // Jednostavno čišćenje poruke ako server vrati JSON
                            if (rawMessage != null && rawMessage.contains("\"message\":\"")) {
                                cleanMessage = rawMessage.split("\"message\":\"")[1].split("\"")[0];
                            } else {
                                cleanMessage = rawMessage;
                            }

                            final String finalMsg = cleanMessage;
                            Platform.runLater(() -> error.accept(finalMsg));
                        }
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
    public void getDrziPredmetId(Long predmetId, Long nastavnikId, Consumer<Long> callback, Consumer<String> errorCallback) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/predmeti/drzi-predmet-id")
                        .queryParam("predmetId", predmetId)
                        .queryParam("nastavnikId", nastavnikId)
                        .build())
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), response ->
                        Mono.error(new RuntimeException("Izabrani nastavnik ne drzi izabrani predmet")))
                .bodyToMono(Long.class)
                .subscribe(
                        id -> Platform.runLater(() -> callback.accept(id)),
                        err -> Platform.runLater(() -> errorCallback.accept(err.getMessage()))
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
    // IspitiService.java (KLIJENT)
    public void getPrijavljeniZaIspit(Long ispitId, Consumer<List<StudentPodaciResponse>> callback) {
        // Mora biti /api/ispit/prijavljeni/ a NE /api/ispit/prijavi/
        webClient.get().uri("/api/ispit/prijavljeni/{ispitId}", ispitId)
                .retrieve()
                .bodyToFlux(StudentPodaciResponse.class)
                .collectList()
                .subscribe(
                        list -> Platform.runLater(() -> callback.accept(list)),
                        err -> System.err.println("Greška pri dohvatanju zapisnika: " + err.getMessage())
                );
    }

}
