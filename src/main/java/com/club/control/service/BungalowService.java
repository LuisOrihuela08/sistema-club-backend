package com.club.control.service;

import java.util.List;

import com.club.control.dto.BungalowDTO;

public interface BungalowService {

	List<BungalowDTO> findAll();
	BungalowDTO saveBungalow (BungalowDTO bungalowDTO);
	BungalowDTO updateBungalow (Long id, BungalowDTO bungalowDTO);
	BungalowDTO findBungalowByCodigo (String codigo);
	BungalowDTO updateDisponibility (Long id, boolean disponible);
}
