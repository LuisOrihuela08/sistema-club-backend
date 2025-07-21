package com.club.control.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.club.control.dto.MetodoPagoDTO;
import com.club.control.entity.MetodoPagoEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.MetodoPagoMapper;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.MetodoPagoService;

@Service
public class MetodoPagoServiceImpl implements MetodoPagoService {

	private static final Logger logger = LoggerFactory.getLogger(MetodoPagoServiceImpl.class);

	private final MetodoPagoRepository metodoPagoRepository;

	public MetodoPagoServiceImpl(MetodoPagoRepository metodoPagoRepository) {
		this.metodoPagoRepository = metodoPagoRepository;
	}

	@Override
	public List<MetodoPagoDTO> listAll() {
		List<MetodoPagoEntity> metodos = metodoPagoRepository.findAll();
		logger.info("Listado de Método de Pago OK");
		return metodos.stream().map(MetodoPagoMapper::toDto).toList();
	}

	@Override
	public MetodoPagoDTO findName(String name) {

		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
		}

		MetodoPagoEntity entity = metodoPagoRepository.findByName(name).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Método de pago no encontrado");
		});
		
		logger.info("Método de pago ingresado: {}", name);
		logger.info("Resultado de la búsqueda: {}", entity);
		return MetodoPagoMapper.toDto(entity);
	}

	@Override
	public MetodoPagoDTO saveMetodo(MetodoPagoDTO dto) {
		
		if (dto == null) {
			throw new IllegalArgumentException("El método de pago no puede ser nulo");
		}
		
		MetodoPagoEntity entity = MetodoPagoMapper.toEntity(dto);
		MetodoPagoEntity metodoSave = metodoPagoRepository.save(entity);
		
		logger.info("Método de pago registrado: {}", metodoSave);		
		return MetodoPagoMapper.toDto(metodoSave);
	}

	@Override
	public void deleteMetodo(Long id) {
				
		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo o 0");
		} else if (id <= 0) {
			throw new IllegalArgumentException("El id no puede ser igual o menor a 0");
		}
		
		MetodoPagoEntity entity = metodoPagoRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Método de pago no encontrado con el id: " + id);
		});
		
		metodoPagoRepository.delete(entity);
		logger.info("Método de Pago elimado exitosamente");
	}

	@Override
	public MetodoPagoDTO updateMetodo(Long id, MetodoPagoDTO dto) {
		
		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo o 0");
		} else if (id <= 0) {
			throw new IllegalArgumentException("El id no puede ser igual o menor a 0");
		}
		
		MetodoPagoEntity entity = metodoPagoRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Método de pago no encontrado con el id: " + id);
		});
		
		entity.setName(dto.getName());
		MetodoPagoEntity metodoUpdate = metodoPagoRepository.save(entity);
		
		logger.info("Método de Pago actualizado: {}", metodoUpdate);
		return MetodoPagoMapper.toDto(metodoUpdate);
	}

}
