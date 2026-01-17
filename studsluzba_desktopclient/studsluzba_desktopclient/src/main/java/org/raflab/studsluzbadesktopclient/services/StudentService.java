package org.raflab.studsluzbadesktopclient.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.raflab.studsluzbadesktopclient.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders; // DODATO ZBOG JSON-a
import org.springframework.http.MediaType;   // DODATO ZBOG JSON-a
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


    public List<StudentPodaciResponse> searchStudents(String ime, String prezime, String indeks, String skola) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + STUDENT_URL_PATH + "/search")
                .queryParam("page", 0)
                .queryParam("size", 100);

        if (ime != null && !ime.trim().isEmpty()) builder.queryParam("ime", ime);
        if (prezime != null && !prezime.trim().isEmpty()) builder.queryParam("prezime", prezime);
        if (indeks != null && !indeks.trim().isEmpty()) builder.queryParam("indeks", indeks);

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return content.stream()
                    .map(map -> mapper.convertValue(map, StudentPodaciResponse.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

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

    public List<PolozeniPredmetiResponse> getPolozeniIspiti(Long studentId) {
        try {
            String url = baseUrl + "/api/student/" + studentId + "/polozeni-predmeti?page=0&size=100";
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.registerModule(new JavaTimeModule());

                List<PolozeniPredmetiResponse> rezultati = new ArrayList<>();
                for (Map<String, Object> obj : content) {
                    PolozeniPredmetiResponse dto = mapper.convertValue(obj, PolozeniPredmetiResponse.class);
                    if (dto.getNazivPredmeta() == null && obj.containsKey("predmetNaziv")) {
                        dto.setNazivPredmeta((String) obj.get("predmetNaziv"));
                    }
                    rezultati.add(dto);
                }
                return rezultati;
            }
        } catch (Exception e) {
            System.err.println("GRESKA u servisu: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // --- PROMENI IMPORT: import org.raflab.studsluzbadesktopclient.dtos.NepolozeniPredmetResponse; ---

    public List<NepolozeniPredmetResponse> getNepolozeniIspiti(Integer brojIndeksa) {
        String url = baseUrl + "/api/student/" + brojIndeksa + "/nepolozeni?page=0&size=1000";
        try {
            // Koristimo RestPageImpl<NepolozeniPredmetResponse>
            ParameterizedTypeReference<RestPageImpl<NepolozeniPredmetResponse>> responseType =
                    new ParameterizedTypeReference<RestPageImpl<NepolozeniPredmetResponse>>() {};

            ResponseEntity<RestPageImpl<NepolozeniPredmetResponse>> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    responseType
            );

            if (response.getBody() != null) {
                return response.getBody().getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
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

    public boolean dodajUplatu(UplataRequest request) {
        try {
            // Moramo eksplicitno postaviti Content-Type na JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Pakujemo request i headers u HttpEntity
            HttpEntity<UplataRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<Long> response = restTemplate.postForEntity(
                    baseUrl + "/api/student/uplata",
                    entity, // Šaljemo entity umesto samo request
                    Long.class
            );
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Greška pri slanju uplate: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // --- METODE ZA UPIS I OBNOVU GODINE ---

    public List<UpisGodineResponse> getUpisaneGodine(Long studentIndeksId) {
        String url = baseUrl + "/api/student/" + studentIndeksId + "/upisane-godine";
        try {
            ResponseEntity<List<UpisGodineResponse>> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<UpisGodineResponse>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<ObnovaGodineResponse> getObnovljeneGodine(Long studentIndeksId) {
        String url = baseUrl + "/api/student/" + studentIndeksId + "/obnovljene-godine";
        try {
            ResponseEntity<List<ObnovaGodineResponse>> response = restTemplate.exchange(
                    url,
                    org.springframework.http.HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<ObnovaGodineResponse>>() {}
            );
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    // --- METODA ZA UPIS GODINE (POPRAVLJENA) ---
    public boolean upisiGodinu(Long studentId, UpisGodineRequest request) {
        String url = baseUrl + "/api/student/" + studentId + "/upis-godine";
        try {
            // 1. Kreiramo zaglavlja (Headers) da kažemo serveru da šaljemo JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // 2. Pakujemo request objekat i zaglavlja u HttpEntity
            HttpEntity<UpisGodineRequest> entity = new HttpEntity<>(request, headers);

            // 3. Šaljemo POST zahtev
            ResponseEntity<UpisGodineResponse> response = restTemplate.postForEntity(url, entity, UpisGodineResponse.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

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
        return sviStudenti();
    }

    public List<StudentPodaciResponse> searchStudentsByStudProg(String studProg) {
        return sviStudenti();
    }

    public List<StudentPodaciResponse> getStudentiPoSrednjojSkoli(String nazivSkole) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/student/srednja-skola")
                        .queryParam("srednjaSkola", nazivSkole)
                        .build())
                .retrieve()
                .bodyToFlux(StudentPodaciResponse.class)
                .collectList()
                .block();
    }

    public StudentPodaciResponse getStudentById(Long id) {
        try {
            return restTemplate.getForObject(baseUrl + "/api/student/" + id, StudentPodaciResponse.class);
        } catch (Exception e) {
            System.err.println("Greska pri dohvatanju studenta sa servera: " + e.getMessage());
            return searchStudents(null, null, null, null).stream()
                    .filter(s -> s.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
    }

    private boolean matches(Object value, String query) {
        if (query == null || query.trim().isEmpty()) return true;
        if (value == null) return false;
        return value.toString().toLowerCase().contains(query.toLowerCase());
    }

    private String createURL(String pathEnd) {
        return baseUrl + STUDENT_URL_PATH + "/" + pathEnd;
    }
}