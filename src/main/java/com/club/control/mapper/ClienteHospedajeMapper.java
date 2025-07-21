package com.club.control.mapper;

import com.club.control.dto.ClienteDTO;
import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.dto.HospedajeDTO;
import com.club.control.dto.MetodoPagoDTO;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.ClienteHospedajeEntity;
import com.club.control.entity.HospedajeEntity;
import com.club.control.entity.MetodoPagoEntity;

public class ClienteHospedajeMapper {

	 // Constructor privado para evitar instanciación
    private ClienteHospedajeMapper() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no debe ser instanciada");
    }
	
	public static ClienteHospedajeEntity toEntity (ClienteHospedajeDTO dto) {
		
		ClienteHospedajeEntity entity = new ClienteHospedajeEntity();
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
		
		if (dto.getHospedaje() != null) {
			HospedajeEntity hospedaje = new HospedajeEntity();
			hospedaje.setId(dto.getHospedaje().getId());
			hospedaje.setCodigoHabitacion(dto.getHospedaje().getCodigoHabitacion());
			hospedaje.setPrecio(dto.getHospedaje().getPrecio());
			hospedaje.setCapacidad(dto.getHospedaje().getCapacidad());
			hospedaje.setDescripcion(dto.getHospedaje().getDescripcion());
			hospedaje.setDisponible(dto.getHospedaje().isDisponible());
			hospedaje.setTipoHabitacion(dto.getHospedaje().getTipoHabitacion());
			entity.setHospedaje(hospedaje);
		}
		
		if (dto.getMetodoPago() != null) {
			MetodoPagoEntity metodoPago = new MetodoPagoEntity();
			metodoPago.setId(dto.getMetodoPago().getId());
			metodoPago.setName(dto.getMetodoPago().getName());
			entity.setMetodoPago(metodoPago);
		}
		return entity;
	}
	
	public static ClienteHospedajeDTO toDto (ClienteHospedajeEntity entity) {
		
		ClienteHospedajeDTO dto = new ClienteHospedajeDTO();
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
		
		if (entity.getHospedaje() != null) {
			HospedajeDTO hospedajeDTO = new HospedajeDTO();
			hospedajeDTO.setId(entity.getHospedaje().getId());
			hospedajeDTO.setCodigoHabitacion(entity.getHospedaje().getCodigoHabitacion());
			hospedajeDTO.setPrecio(entity.getHospedaje().getPrecio());
			hospedajeDTO.setCapacidad(entity.getHospedaje().getCapacidad());
			hospedajeDTO.setDescripcion(entity.getHospedaje().getDescripcion());
			hospedajeDTO.setDisponible(entity.getHospedaje().isDisponible());
			hospedajeDTO.setTipoHabitacion(entity.getHospedaje().getTipoHabitacion());
			dto.setHospedaje(hospedajeDTO);
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
