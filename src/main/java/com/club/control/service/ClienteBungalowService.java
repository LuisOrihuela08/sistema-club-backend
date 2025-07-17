package com.club.control.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.ClienteBungalowDTO;

public interface ClienteBungalowService {

	List<ClienteBungalowDTO> listAll();
	Page<ClienteBungalowDTO> pageClienteBungalow (Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByFechaInicio (LocalDate fechaInicio, Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByFechasBetween (LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByClienteDni (String dni, Pageable pageable);
	Page<ClienteBungalowDTO> pageClienteBungalowByMetodoPagoAndFechasBetween (String nameMetodoPago, LocalDate desde, LocalDate hasta, Pageable pageable);
	ClienteBungalowDTO createClienteBungalow (ClienteBungalowDTO clienteBungalowDTO);
	ClienteBungalowDTO updateClienteBungalow (Long id, ClienteBungalowDTO clienteBungalowDTO);
	List<ClienteBungalowDTO> liberarBungalowFinalizado (LocalDate fecha);
}
