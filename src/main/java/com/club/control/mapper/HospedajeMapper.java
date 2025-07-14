package com.club.control.mapper;

import com.club.control.dto.HospedajeDTO;
import com.club.control.entity.HospedajeEntity;

public class HospedajeMapper {

	public static HospedajeEntity toEntity (HospedajeDTO dto) {
		
		HospedajeEntity entity = new HospedajeEntity();
		entity.setId(dto.getId());
		entity.setCodigoHabitacion(dto.getCodigoHabitacion());
		entity.setCapacidad(dto.getCapacidad());
		entity.setPrecio(dto.getPrecio());
		entity.setDescripcion(dto.getDescripcion());
		entity.setDisponible(dto.isDisponible());
		entity.setTipoHabitacion(dto.getTipoHabitacion());
		return entity;
	}
	
	public static HospedajeDTO toDto (HospedajeEntity entity) {
		
		HospedajeDTO dto = new HospedajeDTO();
		dto.setId(entity.getId());
		dto.setCodigoHabitacion(entity.getCodigoHabitacion());
		dto.setCapacidad(entity.getCapacidad());
		dto.setPrecio(entity.getPrecio());
		dto.setDescripcion(entity.getDescripcion());
		dto.setDisponible(entity.isDisponible());
		dto.setTipoHabitacion(entity.getTipoHabitacion());
		return dto;
	}
}
