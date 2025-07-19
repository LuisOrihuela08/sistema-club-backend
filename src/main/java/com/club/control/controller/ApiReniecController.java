package com.club.control.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/dni/{dni}")
	public ResponseEntity<?> consulApiReniec (@PathVariable String dni){
		return ResponseEntity.ok(apiReniecService.consultarPersonaDni(dni));
	}
}
