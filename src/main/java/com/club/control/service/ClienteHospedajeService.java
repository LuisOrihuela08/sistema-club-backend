package com.club.control.service;

import java.util.List;

import com.club.control.dto.ClienteHospedajeDTO;

public interface ClienteHospedajeService {

	List<ClienteHospedajeDTO> listAll();
	ClienteHospedajeDTO saveClienteHospedaje (ClienteHospedajeDTO clienteHospedajeDTO);
	ClienteHospedajeDTO updateClienteHospedaje (Long id, ClienteHospedajeDTO clienteHospedajeDTO);
}
