package com.club.control.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.dto.ClienteBungalowDTO;
import com.club.control.service.ClienteBungalowService;



@RestController
@RequestMapping("/api/v1/servicio-bungalow")
public class ClienteBungalowController {

	
	private final ClienteBungalowService clienteBungalowService;
	
	public ClienteBungalowController(ClienteBungalowService clienteBungalowService) {
		this.clienteBungalowService = clienteBungalowService;
	}
	
	@GetMapping("/")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(clienteBungalowService.listAll());
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<?> getPagination (@RequestParam ("page") int page,
											 @RequestParam ("size") int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteBungalowDTO> pagination = clienteBungalowService.pageClienteBungalow(pageable);
		return ResponseEntity.ok(pagination);
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createClienteBungalow (@RequestBody ClienteBungalowDTO dto){
		System.out.println("DTO recibido: " + dto);
	    System.out.println("Cliente: " + dto.getCliente());;
		
		ClienteBungalowDTO saved = clienteBungalowService.createClienteBungalow(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<?> updateClienteBungalow (@PathVariable Long id,
													@RequestBody ClienteBungalowDTO dto){
		ClienteBungalowDTO updated = clienteBungalowService.updateClienteBungalow(id, dto);
		return ResponseEntity.ok(updated);
	}
}
