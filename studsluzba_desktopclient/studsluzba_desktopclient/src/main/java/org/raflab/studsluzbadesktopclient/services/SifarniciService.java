package org.raflab.studsluzbadesktopclient.services;

import lombok.AllArgsConstructor;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaDTO;
import org.raflab.studsluzbadesktopclient.dtos.SrednjaSkolaResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Service
public class SifarniciService {

	private WebClient webClient;
	private String baseUrl="http://localhost:8090/api/student";

	private final String SKOLA_URL_PATH = "/srednja-skola";

    public SifarniciService(WebClient webClient) {
        this.webClient = webClient;
    }
	private String createURL(String pathEnd) {
		return baseUrl + SKOLA_URL_PATH + "/" + pathEnd;
	}


	public Integer saveSrednjaSkola(SrednjaSkolaDTO ss) {
		return webClient.post()
				.uri(createURL("add"))
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(ss)
				.retrieve()
				.bodyToMono(Integer.class)
				.block();
	}


    public List<SrednjaSkolaResponse> getSrednjeSkole() {
        return webClient.get()
                .uri(baseUrl + "/srednja-skola/all")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(SrednjaSkolaResponse.class) // Koristiš tvoj Response
                .collectList()
                .block();
    }
}