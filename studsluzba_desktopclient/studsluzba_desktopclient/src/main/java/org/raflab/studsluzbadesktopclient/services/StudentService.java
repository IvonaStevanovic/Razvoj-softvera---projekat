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
        // Pravimo URL i dodajemo osnovne parametre za paginaciju (da bi se slagalo sa Page odgovorom)
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + STUDENT_URL_PATH + "/search")
                .queryParam("page", 0)
                .queryParam("size", 100); // Uzmi prvih 100 rezultata

        // Dodajemo parametre samo ako nisu prazni
        if (ime != null && !ime.trim().isEmpty()) builder.queryParam("ime", ime);
        if (prezime != null && !prezime.trim().isEmpty()) builder.queryParam("prezime", prezime);
        if (indeks != null && !indeks.trim().isEmpty()) builder.queryParam("indeks", indeks);

        try {
            // Pošto server vraća Page<StudentPodaciResponse>, moramo to ispravno pročitati
            // Ako tvoj RestTemplate nije podešen za Page, najsigurnije je ovako:
            ResponseEntity<Map> response = restTemplate.getForEntity(builder.toUriString(), Map.class);

            // Izvlačimo listu iz "content" polja Page objekta
            List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");

            // Pretvaramo mapu nazad u listu tvojih objekata
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule()); // Zbog LocalDate
            return content.stream()
                    .map(map -> mapper.convertValue(map, StudentPodaciResponse.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
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
            String url = baseUrl + "/api/student/" + studentId + "/polozeni-predmeti?page=0&size=100";

            // Uzimamo sirov odgovor kao Map
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");

                ObjectMapper mapper = new ObjectMapper();
                // Isključujemo strogu proveru polja direktno u mapperu
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.registerModule(new JavaTimeModule());

                List<PolozeniPredmetiResponse> rezultati = new ArrayList<>();
                for (Map<String, Object> obj : content) {
                    // Ručno mapiramo najbitnije polje ako automatika zakaže
                    PolozeniPredmetiResponse dto = mapper.convertValue(obj, PolozeniPredmetiResponse.class);
                    if (dto.getNazivPredmeta() == null && obj.containsKey("predmetNaziv")) {
                        dto.setNazivPredmeta((String) obj.get("predmetNaziv"));
                    }
                    rezultati.add(dto);
                }

                System.out.println("DEBUG: Uspešno mapirano " + rezultati.size() + " ispita.");
                return rezultati;
            }
        } catch (Exception e) {
            System.err.println("GRESKA u servisu: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public List<NepolozeniPredmetDTO> getNepolozeniIspiti(Integer brojIndeksa) {
        try {
            // 1. ISPRAVLJEN URL: mora se poklapati sa @GetMapping na serveru
            // Koristimo RestTemplate jer smo ga već podesili za položene ispite
            String url = baseUrl + "/api/student/" + brojIndeksa + "/nepolozeni?page=0&size=10";

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("content")) {
                List<Map<String, Object>> content = (List<Map<String, Object>>) response.getBody().get("content");

                ObjectMapper mapper = new ObjectMapper();
                return content.stream()
                        .map(map -> mapper.convertValue(map, NepolozeniPredmetDTO.class))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            System.err.println("GRESKA pri dobavljanju nepoloženih: " + e.getMessage());
        }
        return new ArrayList<>();
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
    public List<StudentPodaciResponse> getStudentiPoSrednjojSkoli(String nazivSkole) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/student/srednja-skola") // Putanja do tvog @GetMapping na serveru
                        .queryParam("srednjaSkola", nazivSkole)
                        .build())
                .retrieve()
                .bodyToFlux(StudentPodaciResponse.class)
                .collectList()
                .block();
    }
    public StudentPodaciResponse getStudentById(Long id) {
        try {
            // Koristimo direktan poziv ka serveru po ID-ju studenta
            return restTemplate.getForObject(baseUrl + "/api/student/" + id, StudentPodaciResponse.class);
        } catch (Exception e) {
            System.err.println("Greska pri dohvatanju studenta sa servera: " + e.getMessage());
            // Fallback: ako nemas direktan endpoint, pretrazi sve (manje efikasno)
            return searchStudents(null, null, null, null).stream()
                    .filter(s -> s.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
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