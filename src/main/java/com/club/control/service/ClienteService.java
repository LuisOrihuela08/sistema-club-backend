package com.club.control.service;

import java.util.List;

import com.club.control.dto.ClienteDTO;

public interface ClienteService {
	
	ClienteDTO saveClient (ClienteDTO clienteDTO);
	ClienteDTO getClientById (Long id);
	ClienteDTO getClientByDni (String dni);
	List<ClienteDTO> listClients();
	void deleteClient (Long id);
}
