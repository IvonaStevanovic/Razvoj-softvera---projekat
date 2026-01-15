package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzbadesktopclient.controllers.RestPageImpl;
import org.raflab.studsluzbadesktopclient.dtos.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
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

    // --- PRETRAGA (Sekcija 1 klijentske specifikacije) ---

    public List<StudentPodaciResponse> searchStudents(String ime) {
        try {
            // 1. Prvo dovlačimo sirove podatke kao Mapu (ključ-vrednost)
            java.util.Map<String, Object> response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/student/search")
                            .queryParam("ime", ime)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<java.util.Map<String, Object>>() {})
                    .block();

            if (response != null && response.get("content") != null) {
                // 2. Izvlačimo listu iz "content" polja
                List<Object> contentList = (List<Object>) response.get("content");

                // 3. Koristimo Jackson da pretvorimo tu listu mapa u listu tvojih DTO objekata
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule()); // Za datume
                mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                return mapper.convertValue(contentList,
                        new com.fasterxml.jackson.core.type.TypeReference<List<StudentPodaciResponse>>() {});
            }
        } catch (Exception e) {
            System.err.println("Problem sa deserijalizacijom: " + e.getMessage());
            // e.printStackTrace(); // Zakomentarisano da ti ne puni konzolu ako ne mora
        }
        return new java.util.ArrayList<>();
    }
    // Novo: Pretraga po srednjoj školi (Zahtev iz specifikacije)
    public List<StudentPodaciResponse> searchStudentsBySkola(int skolaId, String ime) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/student/search-by-skola") // Proveri rutu na serveru
                            .queryParam("skolaId", skolaId)
                            .queryParam("ime", ime)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<StudentPodaciResponse>>() {})
                    .block();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- DETALJI PROFILA (Ispiti, Uplate) ---

    // Dohvatanje položenih ispita (za tabelu i prosek)
    public List<PolozeniPredmetiResponse> getPolozeniIspiti(Long studentId) {
        try {
            return webClient.get()
                    .uri(baseUrl + "/api/polozeni/student/" + studentId)
                    .retrieve()
                    .bodyToFlux(PolozeniPredmetiResponse.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Dohvatanje nepoloženih ispita
    public List<NepolozeniPredmetDTO> getNepolozeniIspiti(Long studentId) {
        try {
            return webClient.get()
                    .uri(baseUrl + "/api/ispiti/nepolozeni/" + studentId)
                    .retrieve()
                    .bodyToFlux(NepolozeniPredmetDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // Dohvatanje uplata (Sekcija 1 - Finansije)
    public List<UplataResponse> getUplate(Long studentId) {
        try {
            return webClient.get()
                    .uri(baseUrl + "/api/uplate/student/" + studentId)
                    .retrieve()
                    .bodyToFlux(UplataResponse.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    // --- ČUVANJE I OSTALO ---

    public StudentPodaciResponse saveStudent(StudentPodaciResponse student) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(createURL("dodaj"));
        ResponseEntity<StudentPodaciResponse> response = restTemplate.postForEntity(
                builder.toUriString(),
                new HttpEntity<>(student),
                StudentPodaciResponse.class);
        return response.getBody();
    }

    public List<StudentPodaciResponse> sviStudenti() {
        return searchStudents("");
    }
    public List<StudentPodaciResponse> searchStudentsByGodinaUpisa(Integer godinaUpisa) {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/student/godina-upisa")
                            .queryParam("godinaUpisa", godinaUpisa)
                            .build())
                    .retrieve()
                    .bodyToFlux(StudentPodaciResponse.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Greška pri pretrazi po godini upisa: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    public List<StudentPodaciResponse> searchStudentsByStudProg(String studProg) {
        try {
            return webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/student/studprogram") // Proveri da li je ovo tačna putanja na tvom serveru
                            .queryParam("studProg", studProg)
                            .build())
                    .retrieve()
                    .bodyToFlux(StudentPodaciResponse.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.err.println("Greška pri pretrazi po studijskom programu: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}