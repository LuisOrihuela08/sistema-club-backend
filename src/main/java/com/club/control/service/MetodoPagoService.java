package com.club.control.service;

import java.util.List;

import com.club.control.dto.MetodoPagoDTO;

public interface MetodoPagoService {

	List<MetodoPagoDTO> listAll();
	MetodoPagoDTO findName (String name);
	MetodoPagoDTO saveMetodo (MetodoPagoDTO dto);
	MetodoPagoDTO updateMetodo(Long id, MetodoPagoDTO dto);
	void deleteMetodo (Long id);
}
