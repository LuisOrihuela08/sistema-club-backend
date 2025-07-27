package com.club.control.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.service.ApiReniecService;

@RestController
@RequestMapping("/api/v1/reniec")
public class ApiReniecController {

	private final ApiReniecService apiReniecService;
	
	public ApiReniecController(ApiReniecService apiReniecService) {
		this.apiReniecService = apiReniecService;
	}
	
	@GetMapping("/{dni}")
	public ResponseEntity<Map<String, Object>> consulApiReniec (@PathVariable String dni){
		Map<String, Object> resultado = apiReniecService.consultarPersonaDni(dni);
		
		return ResponseEntity.ok(resultado);
	}
}
