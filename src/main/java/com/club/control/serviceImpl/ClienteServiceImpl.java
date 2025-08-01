package com.club.control.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
		return clientes.stream().map(ClienteMapper::toDto).toList();
	}

	@Override
	public ClienteDTO updateClient(Long id, ClienteDTO clienteDTO) {
		
		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo");
		} else if (id <= 0) {
			throw new IllegalArgumentException("El id no puede ser igual o menor a 0");
		}
		
		ClienteEntity entity = clienteRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontro el cliente con el id: " + id);
		});
		
		entity.setName(clienteDTO.getName());
		entity.setLastName(clienteDTO.getLastName());
		entity.setTelephone(clienteDTO.getTelephone());
		entity.setDistrict(clienteDTO.getDistrict());
		entity.setDni(clienteDTO.getDni());
		
		ClienteEntity clienteSaved = clienteRepository.save(entity);
		logger.info("Cliente actualizado: {}", clienteSaved);
		return ClienteMapper.toDto(clienteSaved);
	}

	@Override
	public Page<ClienteDTO> pageClients(Pageable pageable) {
		
		if (pageable.getPageSize() <= 0 )  {
			throw new IllegalArgumentException("El tamaño no puede ser menor/igual a 0");
		} else if (pageable.getPageNumber() < 0) {
			throw new IllegalArgumentException("La página no puede ser menor a 0");
		}
		logger.info("Listado de paginación de clientes OK !");
		return clienteRepository.findAll(pageable)
								.map(ClienteMapper::toDto);
	}
}
