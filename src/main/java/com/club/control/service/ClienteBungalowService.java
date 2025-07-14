package com.club.control.service;

import java.util.List;

import com.club.control.dto.ClienteBungalowDTO;

public interface ClienteBungalowService {

	List<ClienteBungalowDTO> listAll();
	ClienteBungalowDTO createClienteBungalow (ClienteBungalowDTO clienteBungalowDTO);
	ClienteBungalowDTO updateClienteBungalow (Long id, ClienteBungalowDTO clienteBungalowDTO);
	
}
