package com.club.control.mapper;

import com.club.control.dto.BungalowDTO;
import com.club.control.entity.BungalowEntity;

public class BungalowMapper {

	public static BungalowEntity toEntity (BungalowDTO dto) {
		BungalowEntity entity = new BungalowEntity();
		entity.setId(dto.getId());
		entity.setCodigo(dto.getCodigo());
		entity.setCapacidad(dto.getCapacidad());
		entity.setPrecio(dto.getPrecio());
		entity.setDescripcion(dto.getDescripcion());
		entity.setDisponible(dto.isDisponible());
		return entity;
	}
	
	public static BungalowDTO toDto (BungalowEntity entity) {
		BungalowDTO dto = new BungalowDTO();
		dto.setId(entity.getId());
		dto.setCodigo(entity.getCodigo());
		dto.setCapacidad(entity.getCapacidad());
		dto.setPrecio(entity.getPrecio());
		dto.setDescripcion(entity.getDescripcion());
		dto.setDisponible(entity.isDisponible());
		return dto;
	}
}
