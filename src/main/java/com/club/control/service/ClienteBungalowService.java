package com.club.control.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.ClienteBungalowDTO;

public interface ClienteBungalowService {

	List<ClienteBungalowDTO> listAll();
	ClienteBungalowDTO findClienteBungalowById (Long id);
	Page<ClienteBungalowDTO> pageClienteBungalow (Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByFechaInicio (LocalDate fechaInicio, Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByFechasBetween (LocalDate desde, LocalDate hasta, Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByClienteDni (String dni, Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByMetodoPagoAndFechasBetween (String nameMetodoPago, LocalDate desde, LocalDate hasta, Pageable pageable);
	ClienteBungalowDTO createClienteBungalow (ClienteBungalowDTO clienteBungalowDTO);
	ClienteBungalowDTO updateClienteBungalow (Long id, ClienteBungalowDTO clienteBungalowDTO);
	List<ClienteBungalowDTO> liberarBungalowFinalizado (LocalDate fecha);
	
	//Esto es para generar PDF por filtros
	 byte[] exportarPdfFiltrado(String dni, String metodoPago, LocalDate fechaInicio, LocalDate desde, LocalDate hasta);

	 //Esto es para generar PDF por servicios de bungalow
	 byte[] exportarPdfClienteBungalowById (Long id);
}
