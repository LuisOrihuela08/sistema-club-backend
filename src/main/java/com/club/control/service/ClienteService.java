package com.club.control.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.ClienteDTO;

public interface ClienteService {
	
	ClienteDTO saveClient (ClienteDTO clienteDTO);
	ClienteDTO updateClient (Long id, ClienteDTO clienteDTO);
	ClienteDTO getClientById (Long id);
	ClienteDTO getClientByDni (String dni);
	List<ClienteDTO> listClients();
	Page<ClienteDTO> pageClients(Pageable pageable);
	
}
