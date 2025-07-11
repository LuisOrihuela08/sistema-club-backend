package com.club.control.controller;

import java.util.List;
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

import com.club.control.dto.MetodoPagoDTO;
import com.club.control.service.MetodoPagoService;

@RestController
@RequestMapping("/api/v1/metodo-pago")
public class MetodoPagoController {

	public final MetodoPagoService metodoPagoService;
	
	public MetodoPagoController(MetodoPagoService metodoPagoService) {
		this.metodoPagoService = metodoPagoService;
	}
	
	@GetMapping("/")
	public ResponseEntity<?> findAllMetodosPago (){
		return ResponseEntity.ok(metodoPagoService.listAll());
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<?> findMetodoByName(@PathVariable String name){
		return ResponseEntity.ok(metodoPagoService.findName(name));
	}
	
	@PostMapping("/")
	public ResponseEntity<?> createMetodoPago (@RequestBody MetodoPagoDTO dto){
		MetodoPagoDTO metodoSave = metodoPagoService.saveMetodo(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(metodoSave);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateMetodo(@PathVariable Long id,
										  @RequestBody MetodoPagoDTO dto){
		MetodoPagoDTO metodoUpdate = metodoPagoService.updateMetodo(id, dto);
		return ResponseEntity.status(HttpStatus.OK).body(metodoUpdate);
	}
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<?> deleteMetodoPago (@PathVariable Long id){
		metodoPagoService.deleteMetodo(id);
		return new ResponseEntity<>(Map.of("message", "Método de pago eliminado exitosamente"), HttpStatus.OK);
	}
}
