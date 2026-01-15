package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzbadesktopclient.dtos.RestPageImpl;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8090";

    private final String STUDENT_URL_PATH = "/api/student";

    private final ParameterizedTypeReference<RestPageImpl<StudentPodaciResponse>> pageResponseType =
            new ParameterizedTypeReference<>() {};

    private String createURL(String pathEnd) {
        return baseUrl + STUDENT_URL_PATH + "/" + pathEnd;
    }

    // --- METODE ZA PRETRAGU (Usklađene sa /search i Page odgovorom) ---

    // U StudentService.java
    public List<StudentPodaciResponse> searchStudents(String ime) {
        try {
            RestPageImpl<StudentPodaciResponse> response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/student/search")
                            .queryParam("ime", ime)
                            .build())
                    .retrieve()
                    .bodyToMono(pageResponseType) // Ovo mora biti ParameterizedTypeReference
                    .block();

            if (response != null && response.getContent() != null) {
                System.out.println("DEBUG: Server vratio " + response.getContent().size() + " studenata.");
                return response.getContent();
            }
        } catch (Exception e) {
            System.out.println("GRESKA U SERVISU: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public Flux<StudentPodaciResponse> searchStudentsAsync(String ime) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/student/search")
                        .queryParam("ime", ime)
                        .build())
                .retrieve()
                .bodyToMono(pageResponseType)
                .flatMapMany(page -> Flux.fromIterable(page.getContent()));
    }

    public List<StudentPodaciResponse> sviStudenti() {
        return searchStudents(""); // Server /search bez parametara tretira kao "svi"
    }

    // --- METODA ZA ČUVANJE (Usklađena sa @PostMapping("/dodaj")) ---

    public StudentPodaciResponse saveStudent(StudentPodaciResponse student) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("dodaj"));
        ResponseEntity<StudentPodaciResponse> response = restTemplate.postForEntity(
                builder.toUriString(),
                new HttpEntity<>(student),
                StudentPodaciResponse.class);
        return response.getBody();
    }

    // --- OSTALE METODE IZ TVOG ORIGINALNOG KODA (Usklađene putanje) ---

    public List<StudentPodaciResponse> searchStudent(String ime) {
        String url = UriComponentsBuilder.fromHttpUrl(createURL("search"))
                .queryParam("ime", ime)
                .toUriString();

        ResponseEntity<RestPageImpl<StudentPodaciResponse>> response = restTemplate.exchange(
                url, HttpMethod.GET, null, pageResponseType);

        return response.getBody() != null ? response.getBody().getContent() : null;
    }

    public List<StudentPodaciResponse> searchStudentsByGodinaUpisa(Integer godinaUpisa) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(STUDENT_URL_PATH + "/godina-upisa") // Proveri da li na serveru postoji ova ruta
                        .queryParam("godinaUpisa", godinaUpisa)
                        .build())
                .retrieve()
                .bodyToFlux(StudentPodaciResponse.class)
                .collectList().block();
    }

    public List<StudentPodaciResponse> searchStudentsByStudProg(String studProg) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(STUDENT_URL_PATH + "/studprogram") // Proveri da li na serveru postoji ova ruta
                        .queryParam("studProg", studProg)
                        .build())
                .retrieve()
                .bodyToFlux(StudentPodaciResponse.class)
                .collectList().block();
    }
}