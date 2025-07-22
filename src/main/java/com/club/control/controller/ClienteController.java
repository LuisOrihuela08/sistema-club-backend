package com.club.control.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.dto.ClienteDTO;
import com.club.control.service.ClienteService;

@RestController
@RequestMapping("/api/v1/cliente")
public class ClienteController {
	
	private final ClienteService clienteService;
	
	public ClienteController (ClienteService clienteService) {
		this.clienteService = clienteService;
	}

	@GetMapping("/")
	public ResponseEntity<List<ClienteDTO>> getAllClients (){
		return ResponseEntity.ok(clienteService.listClients());
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<Page<ClienteDTO>> getPageClients(@RequestParam ("page") int page,
											 @RequestParam ("size") int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<ClienteDTO> pageResult = clienteService.pageClients(pageable);
		return ResponseEntity.ok(pageResult);
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.getClientById(id));
	}
	
	@GetMapping("/dni/{dni}")
	public ResponseEntity<ClienteDTO> getClienteByDni(@PathVariable String dni){
		return ResponseEntity.ok(clienteService.getClientByDni(dni));
	}
	
	@PostMapping("/")
	public ResponseEntity<ClienteDTO> createClient (@RequestBody ClienteDTO clienteDTO){
		ClienteDTO clienteSaved = clienteService.saveClient(clienteDTO);		
		return ResponseEntity.status(HttpStatus.CREATED).body(clienteSaved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<ClienteDTO> updateCliente (@PathVariable Long id,
											@RequestBody ClienteDTO dto){
		ClienteDTO clienteUpdate = clienteService.updateClient(id, dto);
		return ResponseEntity.ok(clienteUpdate);
		
	}
}
