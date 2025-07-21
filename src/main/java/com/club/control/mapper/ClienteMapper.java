package com.club.control.mapper;

import com.club.control.dto.ClienteDTO;
import com.club.control.entity.ClienteEntity;

public class ClienteMapper {

	 // Constructor privado para evitar instanciación
    private ClienteMapper() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada");
    }
	
	public static ClienteEntity toEntity (ClienteDTO dto) {
		ClienteEntity entity = new ClienteEntity();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		entity.setLastName(dto.getLastName());
		entity.setDni(dto.getDni());
		entity.setDistrict(dto.getDistrict());
		entity.setTelephone(dto.getTelephone());
		return entity;
	}
	
	public static ClienteDTO toDto(ClienteEntity entity) {
		ClienteDTO dto = new ClienteDTO();
		dto.setId(entity.getId());
		dto.setName(entity.getName());
		dto.setLastName(entity.getLastName());
		dto.setDni(entity.getDni());
		dto.setDistrict(entity.getDistrict());
		dto.setTelephone(entity.getTelephone());
		return dto;
		
	}
}
