package com.club.control.mapper;

import com.club.control.dto.ClienteDTO;
import com.club.control.dto.ClientePiscinaDTO;
import com.club.control.dto.MetodoPagoDTO;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.ClientePiscinaEntity;
import com.club.control.entity.MetodoPagoEntity;

public class ClientePiscinaMapper {

	public static ClientePiscinaEntity toEntity(ClientePiscinaDTO dto) {

		ClientePiscinaEntity entity = new ClientePiscinaEntity();
		entity.setId(dto.getId());
		entity.setPrecioUnitario(dto.getPrecioUnitario());
		entity.setCantidadPersonas(dto.getCantidadPersonas());
		entity.setMontoTotal(dto.getMontoTotal());
		entity.setFecha(dto.getFecha());

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
		
		if (dto.getMetodoPago() != null && dto.getMetodoPago().getId() != null) {
			// Para el método de pago
			MetodoPagoEntity metodo = new MetodoPagoEntity();
			metodo.setId(dto.getMetodoPago().getId());
			entity.setMetodo(metodo);
		}
		return entity;
	}

	public static ClientePiscinaDTO toDto(ClientePiscinaEntity entity) {

		ClientePiscinaDTO dto = new ClientePiscinaDTO();
		dto.setId(entity.getId());
		dto.setPrecioUnitario(entity.getPrecioUnitario());
		dto.setCantidadPersonas(entity.getCantidadPersonas());
		dto.setMontoTotal(entity.getMontoTotal());
		dto.setFecha(entity.getFecha());

		// Para cliente
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

		if (entity.getMetodo() != null) {
			MetodoPagoDTO metodoDTO = new MetodoPagoDTO();
			metodoDTO.setId(entity.getMetodo().getId());
			metodoDTO.setName(entity.getMetodo().getName());
			dto.setMetodoPago(metodoDTO);
		}

		return dto;
	}
}
