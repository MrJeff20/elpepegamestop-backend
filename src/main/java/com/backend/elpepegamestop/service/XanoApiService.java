package com.backend.elpepegamestop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class XanoApiService {

    private final RestTemplate restTemplate;

    @Value("${xano.api.base-url}")
    private String baseUrl;

    public <T> T get(String endpoint, Class<T> responseType) {
        return get(endpoint, responseType, null);
    }

    public <T> T get(String endpoint, Class<T> responseType, Map<String, Object> queryParams) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);

            String url = baseUrl + endpoint;
            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

            if (queryParams != null) {
                queryParams.forEach(builder::queryParam);
            }

            String finalUrl = builder.toUriString();
            log.debug("GET Request to Xano: {}", finalUrl);

            ResponseEntity<T> response = restTemplate.exchange(
                    finalUrl, HttpMethod.GET, entity, responseType);

            return response.getBody();

        } catch (Exception e) {
            log.error("Error calling Xano API GET {}: {}", endpoint, e.getMessage(), e);
            throw new RuntimeException("Error communicating with Xano API: " + e.getMessage(), e);
        }
    }

    public <T> T post(String endpoint, Object body, Class<T> responseType) {
        try {
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            String url = baseUrl + endpoint;

            log.debug("POST Request to Xano: {}", url);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.POST, entity, responseType);

            return response.getBody();

        } catch (Exception e) {
            log.error("Error calling Xano API POST {}: {}", endpoint, e.getMessage(), e);
            throw new RuntimeException("Error communicating with Xano API: " + e.getMessage(), e);
        }
    }

    public <T> T put(String endpoint, Object body, Class<T> responseType) {
        try {
            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            String url = baseUrl + endpoint;

            log.debug("PUT Request to Xano: {}", url);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.PUT, entity, responseType);

            return response.getBody();

        } catch (Exception e) {
            log.error("Error calling Xano API PUT {}: {}", endpoint, e.getMessage(), e);
            throw new RuntimeException("Error communicating with Xano API: " + e.getMessage(), e);
        }
    }

    public <T> T delete(String endpoint, Class<T> responseType) {
        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<?> entity = new HttpEntity<>(headers);
            String url = baseUrl + endpoint;

            log.debug("DELETE Request to Xano: {}", url);

            ResponseEntity<T> response = restTemplate.exchange(
                    url, HttpMethod.DELETE, entity, responseType);

            return response.getBody();

        } catch (Exception e) {
            log.error("Error calling Xano API DELETE {}: {}", endpoint, e.getMessage(), e);
            throw new RuntimeException("Error communicating with Xano API: " + e.getMessage(), e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        // Agregar headers de autenticación si son necesarios
        return headers;
    }
}
