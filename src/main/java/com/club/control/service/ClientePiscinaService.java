package com.club.control.service;

import java.util.List;

import com.club.control.dto.ClientePiscinaDTO;

public interface ClientePiscinaService {

	List<ClientePiscinaDTO> listAll();
	ClientePiscinaDTO getClientePiscinaId (Long id);
	ClientePiscinaDTO saveClientePiscina (ClientePiscinaDTO clientePiscinaDTO);
	ClientePiscinaDTO updateClientePiscina (Long id, ClientePiscinaDTO clientePiscinaDTO);
	void deleteClientePiscina (Long id);
}
