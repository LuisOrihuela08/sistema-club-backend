package com.club.control.mapper;

import com.club.control.dto.BungalowDTO;
import com.club.control.dto.ClienteBungalowDTO;
import com.club.control.dto.ClienteDTO;
import com.club.control.dto.MetodoPagoDTO;
import com.club.control.entity.BungalowEntity;
import com.club.control.entity.ClienteBungalowEntity;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.MetodoPagoEntity;

public class ClienteBungalowMapper {

	public static ClienteBungalowEntity toEntity (ClienteBungalowDTO dto) {
		
		ClienteBungalowEntity entity = new ClienteBungalowEntity();
		entity.setId(dto.getId());
		entity.setMontoTotal(dto.getMontoTotal());
		entity.setFechaInicio(dto.getFechaInicio());
		entity.setFechaFin(dto.getFechaFin());
		
		if (dto.getCliente() != null) {
			ClienteEntity cliente = new ClienteEntity();
			cliente.setId(dto.getCliente().getId());
			cliente.setName(dto.getCliente().getName());
			cliente.setLastName(dto.getCliente().getLastName());
			cliente.setDni(dto.getCliente().getDni());
			cliente.setDistrict(dto.getCliente().getDistrict());
			cliente.setTelephone(dto.getCliente().getTelephone());
			entity.setCliente(cliente);
		}
		
		if (dto.getBungalow() != null) {
			BungalowEntity bungalow = new BungalowEntity();
			bungalow.setId(dto.getBungalow().getId());
			bungalow.setCodigo(dto.getBungalow().getCodigo());
			bungalow.setCapacidad(dto.getBungalow().getCapacidad());
			bungalow.setDescripcion(dto.getBungalow().getDescripcion());
			bungalow.setPrecio(dto.getBungalow().getPrecio());
			bungalow.setDisponible(dto.getBungalow().isDisponible());
			entity.setBungalow(bungalow);
		}
		
		if (dto.getMetodoPago() !=null) {
			MetodoPagoEntity metodoPago = new MetodoPagoEntity();
			metodoPago.setId(dto.getMetodoPago().getId());
			metodoPago.setName(dto.getMetodoPago().getName());
			entity.setMetodoPago(metodoPago);
		}
		return entity;
	}
	
	public static ClienteBungalowDTO toDto(ClienteBungalowEntity entity) {
		
		ClienteBungalowDTO dto = new ClienteBungalowDTO();
		dto.setId(entity.getId());
		dto.setMontoTotal(entity.getMontoTotal());
		dto.setFechaInicio(entity.getFechaInicio());
		dto.setFechaFin(entity.getFechaFin());
		
		if (entity.getCliente() != null) {
			ClienteDTO clienteDTO = new ClienteDTO();
			clienteDTO.setId(entity.getCliente().getId());
			clienteDTO.setName(entity.getCliente().getName());
			clienteDTO.setLastName(entity.getCliente().getLastName());
			clienteDTO.setDni(entity.getCliente().getDni());
			clienteDTO.setDistrict(entity.getCliente().getDistrict());
			clienteDTO.setTelephone(entity.getCliente().getTelephone());
			dto.setCliente(clienteDTO);
		}
		
		if (entity.getBungalow() != null) {
			BungalowDTO bungalowDTO = new BungalowDTO();
			bungalowDTO.setId(entity.getBungalow().getId());
			bungalowDTO.setCodigo(entity.getBungalow().getCodigo());
			bungalowDTO.setCapacidad(entity.getBungalow().getCapacidad());
			bungalowDTO.setDescripcion(entity.getBungalow().getDescripcion());
			bungalowDTO.setPrecio(entity.getBungalow().getPrecio());
			bungalowDTO.setDisponible(entity.getBungalow().isDisponible());
			dto.setBungalow(bungalowDTO);
		}
		
		if (entity.getMetodoPago() != null) {
			MetodoPagoDTO metodoDTO = new MetodoPagoDTO();
			metodoDTO.setId(entity.getMetodoPago().getId());
			metodoDTO.setName(entity.getMetodoPago().getName());
			dto.setMetodoPago(metodoDTO);
		}
		return dto;
	}
}
