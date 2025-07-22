package com.club.control.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
	public ResponseEntity<List<ClientePiscinaDTO>> findAll (){
		return ResponseEntity.ok(clientePiscinaService.listAll());
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<ClientePiscinaDTO> getClientePiscinaById(@PathVariable Long id){
		return ResponseEntity.ok(clientePiscinaService.findClientePiscinaById(id));
	}
	
	@GetMapping("/pagination")
	public ResponseEntity<Page<ClientePiscinaDTO>> getPagination (@RequestParam ("page") int page,
			 								 @RequestParam ("size") int size){
		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));//Esto listara de manera descendente para el id
		Page<ClientePiscinaDTO> pagination = clientePiscinaService.pageClientsPiscina(pageable);
		return ResponseEntity.ok(pagination);
	}
	
	@GetMapping("/pagination/fecha")
	public ResponseEntity<Page<ClientePiscinaDTO>> getPaginationByFecha (@RequestParam ("page") int page,
			 										@RequestParam ("size") int size,
			 										@RequestParam ("fecha")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClientePiscinaDTO> pageResult = clientePiscinaService.pageClientePiscinaByFecha(fecha, pageable);
		return ResponseEntity.ok(pageResult);
	}
	
	@GetMapping("/pagination/fecha-between")
	public ResponseEntity<Page<ClientePiscinaDTO>> getPaginationByFechaMonth (@RequestParam ("page") int page,
														 @RequestParam ("size") int size,
														 @RequestParam ("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
					 									 @RequestParam ("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(clientePiscinaService.pageClientePiscinaByFechaBetween(desde, hasta, pageable));
	}
	
	@GetMapping("/pagination/dni")
	public ResponseEntity<Page<ClientePiscinaDTO>> getByPaginationByClienteDni (@RequestParam("dni") String dni,
		    											   @RequestParam("page") int page,
		    											   @RequestParam("size") int size){
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(clientePiscinaService.pageClientePiscinaByClienteDni(dni, pageable));
	}
	
	@GetMapping("/pagination/metodo-pago-fecha")
	public ResponseEntity<Page<ClientePiscinaDTO>> getByPaginationMetodoPagoAndFecha (@RequestParam("metodoPago") String metodoPago,
		    													 @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
		    													 @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
		    													 @RequestParam("page") int page,
		    													 @RequestParam("size") int size){
		Pageable pageable = PageRequest.of(page, size);
		Page<ClientePiscinaDTO> result = clientePiscinaService.pageClientePiscinaMetodoPagoNombreAndFecha(metodoPago, inicio, fin, pageable);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/")
	public ResponseEntity<ClientePiscinaDTO> createServicePiscina (@RequestBody ClientePiscinaDTO dto){
		ClientePiscinaDTO saved = clientePiscinaService.saveClientePiscina(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	
	@PutMapping("/id/{id}")
	public ResponseEntity<ClientePiscinaDTO> update (@PathVariable Long id,
									 @RequestBody ClientePiscinaDTO dto){
		
		ClientePiscinaDTO updated = clientePiscinaService.updateClientePiscina(id, dto);
		return new ResponseEntity<>(updated, HttpStatus.OK);
	}
	
	@DeleteMapping("/id/{id}")
	public ResponseEntity<Map<String, String>> delete (@PathVariable Long id){
		clientePiscinaService.deleteClientePiscina(id);
		return new ResponseEntity<>(Map.of("mensaje", "Servicio de piscina eliminado exitosamente"),
										 HttpStatus.OK);
	}
	
	@GetMapping("/exportar-pdf")
	public ResponseEntity<byte[]> exportarPdf(
	    @RequestParam(required = false) String dni,
	    @RequestParam(required = false) String metodoPago,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
	    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

	    byte[] pdf = clientePiscinaService.exportarPdfFiltradoClientePiscina(dni, metodoPago, fechaInicio, desde, hasta);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "reporte.pdf");

	    return ResponseEntity.ok().headers(headers).body(pdf);
	}
	
	@GetMapping("/exportar-pdf/id/{id}")
	public ResponseEntity<byte[]> exportarPdfById(@PathVariable Long id){
		byte[] pdf = clientePiscinaService.exportarPdfClientePiscinaById(id);
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("attachment", "reporte.pdf");
	    
	    return ResponseEntity.ok().headers(headers).body(pdf);
	}
}
