package com.club.control.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.club.control.dto.BungalowDTO;
import com.club.control.entity.BungalowEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.BungalowMapper;
import com.club.control.repository.BungalowRepository;
import com.club.control.service.BungalowService;

@Service
public class BungalowServiceImpl implements BungalowService{

	private static final Logger logger = LoggerFactory.getLogger(BungalowServiceImpl.class);
	
	private final BungalowRepository bungalowRepository;
	
	public BungalowServiceImpl(BungalowRepository bungalowRepository) {
		this.bungalowRepository = bungalowRepository;
	}
	
	@Override
	public List<BungalowDTO> findAll() {
		
		List<BungalowEntity> bungalows = bungalowRepository.findAll();		
		logger.info("Listado de bungalows OK");
		return bungalows.stream().map(BungalowMapper::toDto).toList();
	}

	@Override
	public BungalowDTO saveBungalow(BungalowDTO bungalowDTO) {

		if (bungalowDTO == null) {
			throw new IllegalArgumentException("El bungalow no puede ser nulo");
		}
		
		BungalowEntity entity = BungalowMapper.toEntity(bungalowDTO);
		BungalowEntity bungalowSaved = bungalowRepository.save(entity);
		
		logger.info("Bugalow registrado: {}", bungalowSaved);
		return BungalowMapper.toDto(bungalowSaved);
	}

	@Override
	public BungalowDTO updateBungalow(Long id, BungalowDTO bungalowDTO) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual a 0");
		}
		
		BungalowEntity entity = bungalowRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Bungalow no encontrado con el id: " + id);
		});
		
		entity.setCodigo(bungalowDTO.getCodigo());
		entity.setCapacidad(bungalowDTO.getCapacidad());
		entity.setPrecio(bungalowDTO.getPrecio());
		entity.setDescripcion(bungalowDTO.getDescripcion());
		entity.setDisponible(bungalowDTO.isDisponible());
		
		BungalowEntity bungalowSaved = bungalowRepository.save(entity);
		
		logger.info("Bungalow actualizado: {}", bungalowSaved);
		return BungalowMapper.toDto(bungalowSaved);
	}

	@Override
	public BungalowDTO findBungalowByCodigo(String codigo) {

		if (codigo == null || codigo.isBlank()) {
			throw new IllegalArgumentException("El código no puede ser nulo o vacío");
		}
		
		BungalowEntity entity = bungalowRepository.findByCodigo(codigo).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró el bungalow con el codigo: " + codigo);
		});
		logger.info("Código ingresado: {}", codigo);
		logger.info("Bungalow encontrado: {}", entity);
		return BungalowMapper.toDto(entity);
	}

	@Override
	public BungalowDTO updateDisponibility(Long id, boolean disponible) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a 0");
		}
		
		BungalowEntity entity = bungalowRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Se encontró bungalow con el id: " + id);
		});
		
		entity.setDisponible(disponible);
		BungalowEntity updateDisponibility = bungalowRepository.save(entity);
		
		logger.info("Se cambió el estado de la disponibilidad del Bungalow: {}", updateDisponibility);
		return BungalowMapper.toDto(updateDisponibility);
	}

	@Override
	public Page<BungalowDTO> pageBungalow(Pageable pageable) {
		logger.info("Listado por paginación de Bungalows OK !");
		return bungalowRepository.findAll(pageable)
								 .map(BungalowMapper::toDto);
	}

	@Override
	public Page<BungalowDTO> pageBungalowDisponibilidad(boolean disponible, Pageable pageable) {
		
		Page<BungalowEntity> entities = bungalowRepository.findByDisponible(disponible, pageable);
		
		if (entities.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron bungalows con la disponiblidad: " + disponible);
		}
		
		logger.info("Búsqueda de la disponibilidad de los bungalows OK !");
		return entities.map(BungalowMapper::toDto);
	}

}
