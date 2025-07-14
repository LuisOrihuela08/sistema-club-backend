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

import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.service.ClienteHospedajeService;
import com.club.control.service.ClienteService;

@RestController
@RequestMapping("/api/v1/servicio-hospedaje")
public class ClienteHospedajeController {

	private final ClienteHospedajeService clienteHospedajeService;
	
	public ClienteHospedajeController(ClienteHospedajeService clienteHospedajeService) {
		this.clienteHospedajeService = clienteHospedajeService;
	}
	
	@GetMapping("/")
	public ResponseEntity<?> findAll (){
		return ResponseEntity.ok(clienteHospedajeService.listAll());
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createClienteHospedaje (@RequestBody ClienteHospedajeDTO dto){
		ClienteHospedajeDTO created = clienteHospedajeService.saveClienteHospedaje(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<?> updateClienteHospedaje (@RequestBody ClienteHospedajeDTO dto,
													 @PathVariable Long id){
		ClienteHospedajeDTO updated = clienteHospedajeService.updateClienteHospedaje(id, dto);
		return ResponseEntity.ok(updated);
	}
}
