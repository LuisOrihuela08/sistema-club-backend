package com.club.control.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.service.ClienteHospedajeService;

@RestController
@RequestMapping("/api/v1/servicio-hospedaje")
public class ClienteHospedajeController {

	private final ClienteHospedajeService clienteHospedajeService;

	public ClienteHospedajeController(ClienteHospedajeService clienteHospedajeService) {
		this.clienteHospedajeService = clienteHospedajeService;
	}

	@GetMapping("/")
	public ResponseEntity<List<ClienteHospedajeDTO>> findAll() {
		return ResponseEntity.ok(clienteHospedajeService.listAll());
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<ClienteHospedajeDTO> getClienteHospedajeById(@PathVariable Long id) {
		return ResponseEntity.ok(clienteHospedajeService.findClienteHospedajeById(id));
	}

	@GetMapping("/pagination")
	public ResponseEntity<Page<ClienteHospedajeDTO>> getPagination(@RequestParam("page") int page, @RequestParam("size") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteHospedajeDTO> pagination = clienteHospedajeService.pageClienteHospedaje(pageable);
		return ResponseEntity.ok(pagination);
	}

	@GetMapping("/pagination/fecha")
	public ResponseEntity<Page<ClienteHospedajeDTO>> getPaginationByFechaInicio(@RequestParam("page") int page, @RequestParam("size") int size,
			@RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteHospedajeDTO> result = clienteHospedajeService.pageClienteHospedajeByFechaInicio(fechaInicio,
				pageable);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pagination/fecha-between")
	public ResponseEntity<Page<ClienteHospedajeDTO>> getPaginationByFechaBetween(@RequestParam("page") int page, @RequestParam("size") int size,
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteHospedajeDTO> result = clienteHospedajeService.pageClienteHospedajeByFechasBetween(desde, hasta,
				pageable);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pagination/dni")
	public ResponseEntity<Page<ClienteHospedajeDTO>> getPaginationByClienteDni(@RequestParam("page") int page, @RequestParam("size") int size,
			@RequestParam("dni") String dni) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteHospedajeDTO> result = clienteHospedajeService.pageClienteHospedajeByClienteDni(dni, pageable);
		return ResponseEntity.ok(result);
	}

	@GetMapping("/pagination/metodoPago/fecha-between")
	public ResponseEntity<Page<ClienteHospedajeDTO>> getPaginationByMetodoPagoAndFechasBetween(@RequestParam("page") int page,
			@RequestParam("size") int size, @RequestParam("nameMetodoPago") String nameMetodoPago,
			@RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
		Pageable pageable = PageRequest.of(page, size);
		Page<ClienteHospedajeDTO> result = clienteHospedajeService
				.pageClienteHospedajeByMetodoPagoAndFechaBetween(nameMetodoPago, desde, hasta, pageable);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/")
	public ResponseEntity<ClienteHospedajeDTO> createClienteHospedaje(@RequestBody ClienteHospedajeDTO dto) {
		ClienteHospedajeDTO created = clienteHospedajeService.saveClienteHospedaje(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/id/{id}")
	public ResponseEntity<ClienteHospedajeDTO> updateClienteHospedaje(@RequestBody ClienteHospedajeDTO dto, @PathVariable Long id) {
		ClienteHospedajeDTO updated = clienteHospedajeService.updateClienteHospedaje(id, dto);
		return ResponseEntity.ok(updated);
	}

	@GetMapping("/exportar-pdf")
	public ResponseEntity<byte[]> exportarPdf(@RequestParam(required = false) String dni,
			@RequestParam(required = false) String metodoPago,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

		byte[] pdf = clienteHospedajeService.exportarPdfFiltradoClienteHospedaje(dni, metodoPago, fechaInicio, desde,
				hasta);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "reporte.pdf");

		return ResponseEntity.ok().headers(headers).body(pdf);
	}

	@GetMapping("/exportar-pdf/id/{id}")
	public ResponseEntity<byte[]> exportarPdfClienteHospedajeById(@PathVariable Long id) {
		byte[] pdf = clienteHospedajeService.exportarPdfClienteHospedajeById(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("attachment", "reporte.pdf");
		
		return ResponseEntity.ok().headers(headers).body(pdf);
	}
}
