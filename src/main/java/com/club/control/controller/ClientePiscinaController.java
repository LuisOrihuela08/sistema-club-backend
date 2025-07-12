package com.club.control.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.dto.ClientePiscinaDTO;
import com.club.control.service.ClientePiscinaService;

@RestController
@RequestMapping("/api/v1/servicio-piscina")
public class ClientePiscinaController {

	private final ClientePiscinaService clientePiscinaService;
	
	public ClientePiscinaController(ClientePiscinaService clientePiscinaService) {
		this.clientePiscinaService = clientePiscinaService;
	}
	
	@GetMapping("/")
	public ResponseEntity<?> findAll (){
		return ResponseEntity.ok(clientePiscinaService.listAll());
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createServicePiscina (@RequestBody ClientePiscinaDTO dto){
		ClientePiscinaDTO saved = clientePiscinaService.saveClientePiscina(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<?> update (@PathVariable Long id,
									 @RequestBody ClientePiscinaDTO dto){
		
		ClientePiscinaDTO updated = clientePiscinaService.updateClientePiscina(id, dto);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<?> delete (@PathVariable Long id){
		clientePiscinaService.deleteClientePiscina(id);
		return new ResponseEntity<>(Map.of("mensaje", "Servicio de piscina eliminado exitosamente"),
										 HttpStatus.OK);
	}
}
