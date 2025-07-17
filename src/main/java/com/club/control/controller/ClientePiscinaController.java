package com.club.control.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getClientePiscinaById(@PathVariable Long id){
		return ResponseEntity.ok(clientePiscinaService.findClientePiscinaById(id));
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<?> getPagination (@RequestParam ("page") int page,
			 								 @RequestParam ("size") int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<ClientePiscinaDTO> pagination = clientePiscinaService.pageClientsPiscina(pageable);
		return ResponseEntity.ok(pagination);
	}
	
	@GetMapping("/pagination/fecha")
	public ResponseEntity<?> getPaginationByFecha (@RequestParam ("page") int page,
			 										@RequestParam ("size") int size,
			 										@RequestParam ("fecha")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClientePiscinaDTO> pageResult = clientePiscinaService.pageClientePiscinaByFecha(fecha, pageable);
		return ResponseEntity.ok(pageResult);
	}
	
	@GetMapping("/pagination/mes")
	public ResponseEntity<?> getPaginationByFechaMonth (@RequestParam ("page") int page,
														 @RequestParam ("size") int size,
														 @RequestParam ("mes") int mes,
					 									 @RequestParam ("anio") int anio){
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(clientePiscinaService.pageClientePiscinaByFechaMonth(anio, mes, pageable));
	}
	
	@GetMapping("/pagination/dni")
	public ResponseEntity<?> getByPaginationByClienteDni (@RequestParam("dni") String dni,
		    											   @RequestParam("page") int page,
		    											   @RequestParam("size") int size){
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(clientePiscinaService.pageClientePiscinaByClienteDni(dni, pageable));
	}
	
	@GetMapping("/pagination/metodo-pago-fecha")
	public ResponseEntity<?> getByPaginationMetodoPagoAndFecha (@RequestParam("metodoPago") String metodoPago,
		    													 @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
		    													 @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
		    													 @RequestParam("page") int page,
		    													 @RequestParam("size") int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClientePiscinaDTO> result = clientePiscinaService.pageClientePiscinaMetodoPagoNombreAndFecha(metodoPago, inicio, fin, pageable);
		return ResponseEntity.ok(result);
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
