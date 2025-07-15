package com.club.control.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.club.control.dto.HospedajeDTO;

public interface HospedajeService {

	List<HospedajeDTO> listAll();
	Page<HospedajeDTO> pageHospedaje (Pageable pageable);
	HospedajeDTO saveHospedaje (HospedajeDTO hospedajeDTO);
	HospedajeDTO updateHospedaje (Long id, HospedajeDTO hospedajeDTO);
}
