package com.club.control.controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.dto.BungalowDTO;
import com.club.control.service.BungalowService;

@RestController
@RequestMapping("/api/v1/bungalow")
public class BungalowController {

	private final BungalowService bungalowService;
	
	public BungalowController(BungalowService bungalowService) {
		this.bungalowService = bungalowService;
	}
	
	@GetMapping("/")
	public ResponseEntity<List<BungalowDTO>> listdAll (){
		return ResponseEntity.ok(bungalowService.findAll());
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<Page<BungalowDTO>> getPagination (@RequestParam ("page") int page,
											@RequestParam ("size") int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<BungalowDTO> pagination = bungalowService.pageBungalow(pageable);
		return ResponseEntity.ok(pagination);
	}
	
	@GetMapping("/codigo/{codigo}")
	public ResponseEntity<BungalowDTO> getBungalowByCodigo (@PathVariable String codigo){
		return ResponseEntity.ok(bungalowService.findBungalowByCodigo(codigo));
	}
	
	@GetMapping("/disponibles")
	public ResponseEntity<List<BungalowDTO>> getBungalowsDisponibles(){
		List<BungalowDTO> disponibles = bungalowService.findByDisponible(true);
		return ResponseEntity.ok(disponibles);
	}
	
	@GetMapping("/disponibilidad")
	public ResponseEntity<Page<BungalowDTO>> getBungalowsByDisponibilidad (@RequestParam ("page") int page,
													  @RequestParam ("size") int size,
													  @RequestParam ("disponible") boolean disponible){
		Pageable pageable = PageRequest.of(page, size);
		Page<BungalowDTO> result = bungalowService.pageBungalowDisponibilidad(disponible, pageable);
		return ResponseEntity.ok(result);
	}
	
	@PatchMapping("/{id}/disponibilidad")
	public ResponseEntity<BungalowDTO> updateDisponibility (@PathVariable Long id,
												  @RequestParam ("disponible") boolean disponible){
		BungalowDTO dto = bungalowService.updateDisponibility(id, disponible);
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping("/")
	public ResponseEntity<BungalowDTO> createBungalow (@RequestBody BungalowDTO bungalowDTO){
		BungalowDTO bungalowSaved = bungalowService.saveBungalow(bungalowDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(bungalowSaved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<BungalowDTO> updateBungalow (@PathVariable Long id,
											 @RequestBody BungalowDTO dto){
		
		BungalowDTO bungalowUpdated = bungalowService.updateBungalow(id, dto);
		return ResponseEntity.ok(bungalowUpdated);
	}
}
