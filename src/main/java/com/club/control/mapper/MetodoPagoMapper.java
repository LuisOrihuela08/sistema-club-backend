package com.club.control.mapper;

import com.club.control.dto.MetodoPagoDTO;
import com.club.control.entity.MetodoPagoEntity;

public class MetodoPagoMapper {

	public static MetodoPagoEntity toEntity (MetodoPagoDTO dto) {
		MetodoPagoEntity entity = new MetodoPagoEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		return entity;
	}
	
	public static MetodoPagoDTO toDto (MetodoPagoEntity entity) {
		MetodoPagoDTO dto = new MetodoPagoDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		return dto;
	}
}
