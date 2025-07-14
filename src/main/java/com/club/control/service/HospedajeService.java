package com.club.control.service;

import java.util.List;

import com.club.control.dto.HospedajeDTO;

public interface HospedajeService {

	List<HospedajeDTO> listAll();
	HospedajeDTO saveHospedaje (HospedajeDTO hospedajeDTO);
	HospedajeDTO updateHospedaje (Long id, HospedajeDTO hospedajeDTO);
}
