package com.club.control.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.ClienteHospedajeDTO;

public interface ClienteHospedajeService {

	List<ClienteHospedajeDTO> listAll();
	Page<ClienteHospedajeDTO> pageClienteHospedaje (Pageable pageable);
	ClienteHospedajeDTO saveClienteHospedaje (ClienteHospedajeDTO clienteHospedajeDTO);
	ClienteHospedajeDTO updateClienteHospedaje (Long id, ClienteHospedajeDTO clienteHospedajeDTO);
	List<ClienteHospedajeDTO> liberarHospedajeFinalizado (LocalDate fecha);
	Page<ClienteHospedajeDTO> pageClienteHospedajeByFechaInicio (LocalDate fechaInicio, Pageable pageable);
	Page<ClienteHospedajeDTO> pageClienteHospedajeByFechasBetween (LocalDate desde, LocalDate hasta, Pageable pageable);
	Page<ClienteHospedajeDTO> pageClienteHospedajeByClienteDni (String dni, Pageable pageable);
	Page<ClienteHospedajeDTO> pageClienteHospedajeByMetodoPagoAndFechaBetween (String nameMetodoPago, LocalDate desde, LocalDate hasta, Pageable pageable);
}
