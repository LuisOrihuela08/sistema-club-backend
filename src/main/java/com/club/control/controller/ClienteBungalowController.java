package com.club.control.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	public ResponseEntity<List<ClienteBungalowDTO>> getAll(){
		return ResponseEntity.ok(clienteBungalowService.listAll());
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ClienteBungalowDTO> getClienteBungalowById (@PathVariable Long id){
		return ResponseEntity.ok(clienteBungalowService.findClienteBungalowById(id));
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<Page<ClienteBungalowDTO>> getPagination (@RequestParam ("page") int page,
											@RequestParam ("size") int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
		Page<ClienteBungalowDTO> pagination = clienteBungalowService.pageClienteBungalow(pageable);
		return ResponseEntity.ok(pagination);
	}
		
	@GetMapping("/pagination/fecha")
	public ResponseEntity<Page<ClienteBungalowDTO>> getPaginationByFechaInicio (@RequestParam ("page") int page,
														 @RequestParam ("size") int size,
														 @RequestParam ("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteBungalowDTO> result = clienteBungalowService.pageClienteBungalowByFechaInicio(fechaInicio, pageable);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pagination/fecha-between")
	public ResponseEntity<Page<ClienteBungalowDTO>> getPaginationByFechaBetween (@RequestParam ("page") int page,
			 											  @RequestParam ("size") int size,
			 											  @RequestParam ("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			 											  @RequestParam ("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteBungalowDTO> result = clienteBungalowService.pageClienteBungalowByFechasBetween(desde, hasta, pageable);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pagination/cliente-dni")
	public ResponseEntity<Page<ClienteBungalowDTO>> getPaginationByClienteDni (@RequestParam ("page") int page,
			  								   		    @RequestParam ("size") int size,
			  								   		    @RequestParam ("dni") String dni){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteBungalowDTO> result = clienteBungalowService.pageClienteBungalowByClienteDni(dni, pageable);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/pagination/metodoPago/fecha-between")
	public ResponseEntity<Page<ClienteBungalowDTO>> getPaginationByMetodoPagoAndFechaBetween (@RequestParam ("page") int page,
	   		    													   @RequestParam ("size") int size,
	   		    													   @RequestParam ("nameMetodoPago") String nameMetodoPago,
	   		    													   @RequestParam ("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
	   		    													   @RequestParam ("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteBungalowDTO> result = clienteBungalowService.pageClienteBungalowByMetodoPagoAndFechasBetween(nameMetodoPago, desde, hasta, pageable);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/")
	public ResponseEntity<ClienteBungalowDTO> createClienteBungalow (@RequestBody ClienteBungalowDTO dto){	
		ClienteBungalowDTO saved = clienteBungalowService.createClienteBungalow(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<ClienteBungalowDTO> updateClienteBungalow (@PathVariable Long id,
													@RequestBody ClienteBungalowDTO dto){
		ClienteBungalowDTO updated = clienteBungalowService.updateClienteBungalow(id, dto);
		return ResponseEntity.ok(updated);
	}
	
	@GetMapping("/exportar-pdf")
	public ResponseEntity<byte[]> exportarPdf(
	    @RequestParam(required = false) String dni,
	    @RequestParam(required = false) String metodoPago,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

	    byte[] pdf = clienteBungalowService.exportarPdfFiltrado(dni, metodoPago, fechaInicio, desde, hasta);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "reporte.pdf");

	    return ResponseEntity.ok().headers(headers).body(pdf);
	}
	
	@GetMapping("/exportar-pdf/id/{id}")
	public ResponseEntity<byte[]> exportarPdfClienteBungalowById (@PathVariable Long id){
		byte[] pdf = clienteBungalowService.exportarPdfClienteBungalowById(id);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "reporte.pdf");
	    
	    return ResponseEntity.ok().headers(headers).body(pdf);
	}
	
	@GetMapping("/exportar-excel")
	public ResponseEntity<byte[]> exportarExcel(
	    @RequestParam(required = false) String dni,
	    @RequestParam(required = false) String metodoPago,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
		
		byte[] excel = clienteBungalowService.exportarExcelFiltrado(dni, metodoPago, fechaInicio, desde, hasta);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		headers.setContentDisposition(ContentDisposition.attachment().filename("servicio-bungalow.xlsx").build());
		return ResponseEntity.ok().headers(headers).body(excel);
		
	}
}
