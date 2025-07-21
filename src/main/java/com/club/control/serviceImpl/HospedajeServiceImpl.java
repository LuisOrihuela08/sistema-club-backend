package com.club.control.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.club.control.dto.HospedajeDTO;
import com.club.control.entity.HospedajeEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.HospedajeMapper;
import com.club.control.repository.HospedajeRepository;
import com.club.control.service.HospedajeService;

@Service
public class HospedajeServiceImpl implements HospedajeService{

	private static final Logger logger = LoggerFactory.getLogger(HospedajeServiceImpl.class);
	
	private final HospedajeRepository hospedajeRepository;
	
	public HospedajeServiceImpl(HospedajeRepository hospedajeRepository) {
		this.hospedajeRepository = hospedajeRepository;
	}
	
	@Override
	public List<HospedajeDTO> listAll() {
		
		List<HospedajeEntity> list = hospedajeRepository.findAll();
		logger.info("Listado de hospedajes OK");
		return list.stream().map(HospedajeMapper::toDto).toList();
	}

	@Override
	public HospedajeDTO saveHospedaje(HospedajeDTO hospedajeDTO) {

		if (hospedajeDTO == null) {
			throw new IllegalArgumentException("El hospedaje no puede ser nulo");
		}
		
		HospedajeEntity entity = HospedajeMapper.toEntity(hospedajeDTO);		
		HospedajeEntity saved = hospedajeRepository.save(entity);
		
		logger.info("Hospedaje registrado: {}", saved);
		return HospedajeMapper.toDto(saved);
	}

	@Override
	public HospedajeDTO updateHospedaje(Long id, HospedajeDTO hospedajeDTO) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a 0");
		}
		
		HospedajeEntity entity = hospedajeRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Hospedaje no encontrado con el id: " + id);
		});
		
		entity.setCodigoHabitacion(hospedajeDTO.getCodigoHabitacion());
		entity.setCapacidad(hospedajeDTO.getCapacidad());
		entity.setPrecio(hospedajeDTO.getPrecio());
		entity.setDescripcion(hospedajeDTO.getDescripcion());
		entity.setDisponible(hospedajeDTO.isDisponible());
		entity.setTipoHabitacion(hospedajeDTO.getTipoHabitacion());
		
		HospedajeEntity saved = hospedajeRepository.save(entity);
		
		logger.info("Hospedaje actualizado: {}", saved);
		return HospedajeMapper.toDto(saved);
	}

	@Override
	public Page<HospedajeDTO> pageHospedaje(Pageable pageable) {
		logger.info("Listado por paginación de Hospedaje OK !");
		return hospedajeRepository.findAll(pageable)
								  .map(HospedajeMapper::toDto);
	}

}
