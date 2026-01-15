package org.raflab.studsluzbadesktopclient.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.raflab.studsluzbadesktopclient.dtos.NepolozeniPredmetDTO;
import org.raflab.studsluzbadesktopclient.dtos.PolozeniPredmetiResponse;
import org.raflab.studsluzbadesktopclient.dtos.StudentPodaciResponse;
import org.raflab.studsluzbadesktopclient.dtos.UplataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient webClient;

    private final String baseUrl = "http://localhost:8090";
    private final String STUDENT_URL_PATH = "/api/student";

    // --- GLAVNA PRETRAGA (Hibrid starog i novog) ---
    // Koristi tvoj stari način pozivanja servera (/api/student/search?ime=...),
    // ali dodaje filtriranje za prezime i školu na klijentu da bi ispunili KT2 specifikaciju

    public List<StudentPodaciResponse> searchStudents(String ime, String prezime, String indeks, String skola) {
        try {
            // 1. Pozivamo server koristeći tvoj stari endpoint koji radi
            // Ako je ime null, šaljemo prazan string da dobijemo sve studente
            String searchIme = (ime != null) ? ime : "";

            List<StudentPodaciResponse> studentiSaServera = searchStudentsInternal(searchIme);

            // 2. Dodatno filtriramo rezultate na klijentu za polja koja server možda još ne podržava (Prezime, Škola)
            return studentiSaServera.stream()
                    .filter(s -> matches(s.getPrezime(), prezime))
                    .filter(s -> matches(s.getSrednjaSkola(), skola))
                    // .filter(s -> matches(s.getBrojIndeksa(), indeks)) // Otkomentariši ako treba
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Tvoja originalna metoda za pretragu (malo refaktorisana da bude otpornija)
    private List<StudentPodaciResponse> searchStudentsInternal(String ime) {
        try {
            Map<String, Object> response = webClient
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/student/search")
                            .queryParam("ime", ime)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            if (response != null && response.get("content") != null) {
                List<Object> contentList = (List<Object>) response.get("content");

                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                return mapper.convertValue(contentList, new TypeReference<List<StudentPodaciResponse>>() {});
            }
        } catch (Exception e) {
            System.err.println("GRESKA pri komunikaciji sa serverom: " + e.getMessage());
            // Fallback: Pokušaj /api/student/all ako search ne radi
            return getAllStudentsFallback();
        }
        return new ArrayList<>();
    }

    private List<StudentPodaciResponse> getAllStudentsFallback() {
        try {
            return webClient.get()
                    .uri(baseUrl + "/api/student/all")
                    .retrieve()
                    .bodyToFlux(StudentPodaciResponse.class)
                    .collectList()
                    .block();
        } catch (Exception ex) {
            return new ArrayList<>();
        }
    }

    // --- METODE ZA PROFIL STUDENTA (Kopirane iz tvog starog koda) ---

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

    // --- SUPPORT ZA STARI KOD (Da ne puca) ---

    public List<StudentPodaciResponse> sviStudenti() {
        return searchStudents(null, null, null, null);
    }

    public List<StudentPodaciResponse> searchStudents(String ime) {
        return searchStudents(ime, null, null, null);
    }

    public List<StudentPodaciResponse> searchStudentsByGodinaUpisa(int godina) {
        return searchStudentsByGodinaUpisa(String.valueOf(godina));
    }

    public List<StudentPodaciResponse> searchStudentsByGodinaUpisa(String godina) {
        return sviStudenti(); // TODO: Implementirati filtriranje po godini ako treba
    }

    public List<StudentPodaciResponse> searchStudentsByStudProg(String studProg) {
        return sviStudenti();
    }

    public StudentPodaciResponse getStudentById(Long id) {
        // Improvisation: search all and find one, or add direct endpoint if exists
        return sviStudenti().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Pomoćna metoda za filtriranje
    private boolean matches(Object value, String query) {
        if (query == null || query.trim().isEmpty()) return true;
        if (value == null) return false;
        return value.toString().toLowerCase().contains(query.toLowerCase());
    }

    private String createURL(String pathEnd) {
        return baseUrl + STUDENT_URL_PATH + "/" + pathEnd;
    }
}