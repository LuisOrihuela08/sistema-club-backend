package com.club.control.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.dto.HospedajeDTO;
import com.club.control.service.HospedajeService;

@RestController
@RequestMapping("/api/v1/hospedaje")
public class HospedajeController {

	private final HospedajeService hospedajeService;
	
	public HospedajeController(HospedajeService hospedajeService) {
		this.hospedajeService = hospedajeService;
	}
	
	@GetMapping("/")
	public ResponseEntity<?> findAll (){
		return ResponseEntity.ok(hospedajeService.listAll());
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createHospedaje (@RequestBody HospedajeDTO hospedajeDTO){
		HospedajeDTO saved = hospedajeService.saveHospedaje(hospedajeDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<?> updateHospedaje (@PathVariable Long id,
											  @RequestBody HospedajeDTO hospedajeDTO){
		HospedajeDTO updated = hospedajeService.updateHospedaje(id, hospedajeDTO);
		return ResponseEntity.ok(updated);
	}
}
