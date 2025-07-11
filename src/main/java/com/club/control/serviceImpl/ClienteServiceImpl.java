package com.club.control.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.club.control.dto.ClienteDTO;
import com.club.control.entity.ClienteEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteMapper;
import com.club.control.repository.ClienteRepository;
import com.club.control.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService {

	private static Logger logger = LoggerFactory.getLogger(ClienteServiceImpl.class);

	private final ClienteRepository clienteRepository;

	public ClienteServiceImpl(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	@Override
	public ClienteDTO saveClient(ClienteDTO clienteDTO) {

		if (clienteDTO == null) {
			throw new IllegalArgumentException("El cliente no puede ser nulo");
		}

		ClienteEntity entity = ClienteMapper.toEntity(clienteDTO);
		ClienteEntity clientSave = clienteRepository.save(entity);
		logger.info("Cliente agregado: {}", clientSave);
		return ClienteMapper.toDto(clientSave);
	}

	@Override
	public ClienteDTO getClientById(Long id) {

		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo");
		} else if (id <= 0) {
			throw new IllegalArgumentException("El id no puede ser menor ó igual a 0");
		}

		ClienteEntity entity = clienteRepository.findById(id).orElseThrow(() -> {
			return new RecursosNoEncontradosException("No se encontró cliente con el ID: " + id);
		});

		logger.info("Cliente encontrado: {}", id);
		return ClienteMapper.toDto(entity);
	}

	@Override
	public ClienteDTO getClientByDni(String dni) {

		if (dni == null || dni.isBlank()) {
			throw new IllegalArgumentException("El dni no puede ser nulo o vacío");
		} else if (!dni.matches("\\d{8}")) {
			throw new IllegalArgumentException("El dni debe tener 8 dígitos");
		}

		ClienteEntity entity = clienteRepository.findByDni(dni).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró al cliente con el dni: " + dni);
		});

		logger.info("DNI ingresado: {}, Cliente encontrado: {}", dni, entity);
		return ClienteMapper.toDto(entity);
	}

	@Override
	public List<ClienteDTO> listClients() {
		
		List<ClienteEntity> clientes = clienteRepository.findAll();
		logger.info("Listado de clientes OK");
		return clientes.stream().map(ClienteMapper::toDto)
								.collect(Collectors.toList());
	}

	@Override
	public void deleteClient(Long id) {
		// TODO Auto-generated method stub

	}
}
