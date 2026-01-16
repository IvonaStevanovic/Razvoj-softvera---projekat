package org.raflab.studsluzbadesktopclient;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientAppConfig {
	@Bean
    public RestTemplate getRestTemplate() {
       return new RestTemplate();
    }
    private static ConfigurableApplicationContext context;

    public static void setContext(ConfigurableApplicationContext context) {
        ClientAppConfig.context = context;
    }
    @Bean
    public WebClient getWebClient() {
        return WebClient.builder()
                .baseUrl(getBaseUrl())
                .build();
    }
    public static ConfigurableApplicationContext getContext() {
        return context;
    }
	@Bean
    public String getBaseUrl() {
       return "http://localhost:8090";
    }
}
