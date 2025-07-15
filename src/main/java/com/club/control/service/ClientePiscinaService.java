package com.club.control.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.ClientePiscinaDTO;

public interface ClientePiscinaService {

	List<ClientePiscinaDTO> listAll();
	Page<ClientePiscinaDTO> pageClientsPiscina (Pageable pageable);
	ClientePiscinaDTO getClientePiscinaId (Long id);
	ClientePiscinaDTO saveClientePiscina (ClientePiscinaDTO clientePiscinaDTO);
	ClientePiscinaDTO updateClientePiscina (Long id, ClientePiscinaDTO clientePiscinaDTO);
	void deleteClientePiscina (Long id);
}
