package com.backend.elpepegamestop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class XanoApiService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public XanoApiService(
            @Value("${xano.api.base-url}") String baseUrl,
            RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    /**
     * Método genérico para hacer peticiones GET a Xano
     */
    public <T> T get(String endpoint, Class<T> responseType, Map<String, Object> params) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            UriComponentsBuilder builder = UriComponentsBuilder
                    .fromHttpUrl(baseUrl + endpoint);

            if (params != null) {
                params.forEach(builder::queryParam);
            }

            String url = builder.toUriString();
            log.debug("GET Request to Xano: {}", url);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, responseType);

            log.debug("Response from Xano: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Xano API: {}", e.getMessage());
            throw new RuntimeException("Error communicating with Xano API", e);
        }
    }

    /**
     * Método genérico para hacer peticiones POST a Xano
     */
    public <T> T post(String endpoint, Object body, Class<T> responseType) {
        try {
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            String url = baseUrl + endpoint;

            log.debug("POST Request to Xano: {}", url);
            log.debug("Request body: {}", body);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, responseType);

            log.debug("Response from Xano: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Xano API: {}", e.getMessage());
            throw new RuntimeException("Error communicating with Xano API", e);
        }
    }

    /**
     * Método genérico para hacer peticiones PUT a Xano
     */
    public <T> T put(String endpoint, Object body, Class<T> responseType) {
        try {
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            String url = baseUrl + endpoint;

            log.debug("PUT Request to Xano: {}", url);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.PUT, entity, responseType);

            log.debug("Response from Xano: {}", response.getStatusCode());
            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Xano API: {}", e.getMessage());
            throw new RuntimeException("Error communicating with Xano API", e);
        }
    }

    /**
     * Método genérico para hacer peticiones DELETE a Xano
     */
    public void delete(String endpoint) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);
            String url = baseUrl + endpoint;

            log.debug("DELETE Request to Xano: {}", url);

            restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error calling Xano API: {}", e.getMessage());
            throw new RuntimeException("Error communicating with Xano API", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Aquí puedes agregar headers de autenticación si Xano lo requiere
        // headers.set("Authorization", "Bearer " + token);
        return headers;
    }
}
