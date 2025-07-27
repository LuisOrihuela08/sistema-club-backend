package com.club.control.service;


import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApiReniecService {

	private static final Logger logger = LoggerFactory.getLogger(ApiReniecService.class);
	
	private final RestTemplate restTemplate;
	
	public ApiReniecService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Value("${api.reniec.url}")
	private String apiUrl;

	@Value("${api.reniec.token}")
	private String apiToken;
	
	
	public Map<String, Object> consultarPersonaDni (String dni) {
		
		if (dni == null || !dni.matches("\\d{8}")) {
			logger.error("Dni inválido: {}", dni);
			throw new IllegalArgumentException("DNI inválido: " + dni);
		}
		
		try {
			
			String url = apiUrl + "/" + dni;
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + apiToken);
			
			HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
			
			ResponseEntity<String> response = restTemplate.exchange(
	                url,
	                HttpMethod.GET,
	                requestEntity,
	                String.class
	            );
			
			logger.info("Consulta exitosa con el dni: {}", dni);
			
			 // Convertir el JSON en un Map
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonMap = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

            return jsonMap;
			
		} catch (HttpClientErrorException e) {
            logger.error("Error HTTP al consultar DNI {}: {}", dni, e.getResponseBodyAsString());
            throw new RuntimeException("Error HTTP al consultar DNI " + dni);
        } catch (Exception e) {
            logger.error("Error desconocido al consultar DNI {}: {}", dni, e.getMessage());
            throw new RuntimeException("Error general al consultar DNI " + dni);
        }
	}
}
