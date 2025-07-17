package com.club.control.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.ClientePiscinaDTO;

public interface ClientePiscinaService {

	ClientePiscinaDTO findClientePiscinaById (Long id);
	List<ClientePiscinaDTO> listAll();
	Page<ClientePiscinaDTO> pageClientsPiscina (Pageable pageable);
	Page<ClientePiscinaDTO> pageClientePiscinaByFecha (LocalDate fecha, Pageable pageable);
	Page<ClientePiscinaDTO> pageClientePiscinaByFechaMonth (int anio, int mes, Pageable pageable);
	Page<ClientePiscinaDTO> pageClientePiscinaByClienteDni (String dni, Pageable pageable);
	Page<ClientePiscinaDTO> pageClientePiscinaMetodoPagoNombreAndFecha (String nombreMetodoPago, LocalDate inicio, LocalDate fin, Pageable pageable);
	ClientePiscinaDTO saveClientePiscina (ClientePiscinaDTO clientePiscinaDTO);
	ClientePiscinaDTO updateClientePiscina (Long id, ClientePiscinaDTO clientePiscinaDTO);
	void deleteClientePiscina (Long id);
	
}
