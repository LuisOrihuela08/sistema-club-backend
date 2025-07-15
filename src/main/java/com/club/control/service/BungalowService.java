package com.club.control.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.BungalowDTO;

public interface BungalowService {

	List<BungalowDTO> findAll();
	Page<BungalowDTO> pageBungalow (Pageable pageable);
	BungalowDTO saveBungalow (BungalowDTO bungalowDTO);
	BungalowDTO updateBungalow (Long id, BungalowDTO bungalowDTO);
	BungalowDTO findBungalowByCodigo (String codigo);
	Page<BungalowDTO> pageBungalowDisponibilidad (boolean disponible, Pageable pageable);
	BungalowDTO updateDisponibility (Long id, boolean disponible);
}
