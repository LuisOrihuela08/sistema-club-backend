package com.club.control.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
	
	
	public String consultarPersonaDni (String dni) {
		
		if (dni == null || !dni.matches("\\d{8}")) {
			logger.error("Dni inválido: {}", dni);
			throw new IllegalArgumentException("DNI inválido: " + dni);
		}
		
		try {
			
			//Aca construyo en cuerpo JSON requerido por la API
			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("token", apiToken);
			requestBody.put("type_document", "dni");
			requestBody.put("document_number", dni);
			
			//Encabezados
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			
			//Aca construyo la solicitud
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
			
			//Envio la solicitud
			ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, requestEntity, String.class);
			
			logger.info("Consulta exitosa con el dni: {}", dni);
			return response.getBody();
			
		} catch (HttpClientErrorException e) {
            logger.error("Error HTTP al consultar DNI {}: {}", dni, e.getResponseBodyAsString());
            throw new RuntimeException("Error HTTP al consultar DNI " + dni);
        } catch (Exception e) {
            logger.error("Error desconocido al consultar DNI {}: {}", dni, e.getMessage());
            throw new RuntimeException("Error general al consultar DNI " + dni);
        }
	}
}
