package com.club.control.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.ClienteHospedajeEntity;
import com.club.control.entity.HospedajeEntity;
import com.club.control.entity.MetodoPagoEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteHospedajeMapper;
import com.club.control.repository.ClienteHospedajeRepository;
import com.club.control.repository.ClienteRepository;
import com.club.control.repository.HospedajeRepository;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.ClienteHospedajeService;

@Service
public class ClienteHospedajeServiceImpl implements ClienteHospedajeService{

	private static final Logger logger = LoggerFactory.getLogger(ClienteHospedajeServiceImpl.class);
	
	private final ClienteHospedajeRepository clienteHospedajeRepository;
	private final ClienteRepository clienteRepository;
	private final HospedajeRepository hospedajeRepository;
	private final MetodoPagoRepository metodoPagoRepository;
	
	public ClienteHospedajeServiceImpl(ClienteHospedajeRepository clienteHospedajeRepository,
									   ClienteRepository clienteRepository,
									   HospedajeRepository hospedajeRepository,
									   MetodoPagoRepository metodoPagoRepository) {
		this.clienteHospedajeRepository = clienteHospedajeRepository;
		this.clienteRepository = clienteRepository;
		this.hospedajeRepository = hospedajeRepository;
		this.metodoPagoRepository = metodoPagoRepository;
	}
	
	@Override
	public List<ClienteHospedajeDTO> listAll() {
		
		List<ClienteHospedajeEntity> list = clienteHospedajeRepository.findAll();
		logger.info("Lista de los servicios de hospedaje OK !");
		return list.stream().map(ClienteHospedajeMapper::toDto)
							.collect(Collectors.toList());
	}

	@Override
	public ClienteHospedajeDTO saveClienteHospedaje(ClienteHospedajeDTO clienteHospedajeDTO) {
		
		if (clienteHospedajeDTO.getCliente() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al cliente");
		}
		
		if (clienteHospedajeDTO.getHospedaje() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al el cuarto disponible de hospedaje");
		}
		
		if (clienteHospedajeDTO.getMetodoPago() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir el método de pago");
		}
		
		ClienteEntity cliente = clienteRepository.findByDni(clienteHospedajeDTO.getCliente().getDni()).orElse(null);
		
		if (cliente == null) {
			cliente = new ClienteEntity();
			cliente.setName(clienteHospedajeDTO.getCliente().getName());
			cliente.setLastName(clienteHospedajeDTO.getCliente().getLastName());
			cliente.setDni(clienteHospedajeDTO.getCliente().getDni());
			cliente.setDistrict(clienteHospedajeDTO.getCliente().getDistrict());
			cliente.setTelephone(clienteHospedajeDTO.getCliente().getTelephone());
			cliente = clienteRepository.save(cliente);
		}
		
		HospedajeEntity hospedaje = hospedajeRepository.findById(clienteHospedajeDTO.getHospedaje().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Cuarto de hospedaje no encontrado con el id: " + clienteHospedajeDTO.getHospedaje().getId());
		});
		
		//Aqui modifico la disponibilidad del hospedaje a false
		hospedaje.setDisponible(false);
		hospedaje = hospedajeRepository.save(hospedaje);
		
		MetodoPagoEntity metodoPago = metodoPagoRepository.findById(clienteHospedajeDTO.getMetodoPago().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Método de pago no encontrado con el id: " + clienteHospedajeDTO.getMetodoPago().getId());
		});
		
		ClienteHospedajeEntity entity = ClienteHospedajeMapper.toEntity(clienteHospedajeDTO);
		entity.setFechaInicio(LocalDate.now());
		entity.setFechaFin(clienteHospedajeDTO.getFechaFin());
		entity.setMontoTotal(clienteHospedajeDTO.getMontoTotal());
		entity.setCliente(cliente);
		entity.setHospedaje(hospedaje);
		entity.setMetodoPago(metodoPago);
				
		ClienteHospedajeEntity saved = clienteHospedajeRepository.save(entity);
		
		logger.info("Nuevo servicio de hospedaje guardado: {}", saved);
		return ClienteHospedajeMapper.toDto(saved);
	}

	@Override
	public ClienteHospedajeDTO updateClienteHospedaje(Long id, ClienteHospedajeDTO clienteHospedajeDTO) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual que 0");
		}
		
		ClienteHospedajeEntity entity = clienteHospedajeRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró un servicio de hospedaje con el id: " + id);
		});
		
		ClienteEntity cliente = entity.getCliente();
		
		if (cliente == null) {
			throw new RecursosNoEncontradosException("El servicio de hospedaje no tiene un cliente asignado");
		}
		
		cliente.setName(clienteHospedajeDTO.getCliente().getName());
		cliente.setLastName(clienteHospedajeDTO.getCliente().getLastName());
		cliente.setDni(clienteHospedajeDTO.getCliente().getDni());
		cliente.setDistrict(clienteHospedajeDTO.getCliente().getDistrict());
		cliente.setTelephone(clienteHospedajeDTO.getCliente().getTelephone());
		clienteRepository.save(cliente);
		
		HospedajeEntity hospedaje = hospedajeRepository.findById(clienteHospedajeDTO.getHospedaje().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Cuarto de hospedaje no encontrado con el id: " + clienteHospedajeDTO.getHospedaje().getId());
		});
		
		MetodoPagoEntity metodoPago = metodoPagoRepository.findById(clienteHospedajeDTO.getMetodoPago().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Método de pago no encontrado con el id: " + clienteHospedajeDTO.getMetodoPago().getId());
		});
		
		entity.setFechaInicio(clienteHospedajeDTO.getFechaInicio());
		entity.setFechaFin(clienteHospedajeDTO.getFechaFin());
		entity.setMontoTotal(clienteHospedajeDTO.getMontoTotal());
		entity.setCliente(cliente);
		entity.setHospedaje(hospedaje);
		entity.setMetodoPago(metodoPago);
		
		ClienteHospedajeEntity updated = clienteHospedajeRepository.save(entity);
		
		logger.info("Servicio de hospedaje actualizado: {}", updated);
		return ClienteHospedajeMapper.toDto(updated);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedaje(Pageable pageable) {
		logger.info("Listado por paginación del servicio de hospedaje OK !");
		return clienteHospedajeRepository.findAll(pageable)
										 .map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public List<ClienteHospedajeDTO> liberarHospedajeFinalizado(LocalDate fecha) {
		
		List<ClienteHospedajeEntity> serviciosFinalizados = clienteHospedajeRepository.findByFechaFinBefore(fecha);
		
		List<ClienteHospedajeDTO> liberados = new ArrayList<>();
		
		for(ClienteHospedajeEntity servicio: serviciosFinalizados) {
			HospedajeEntity hospedaje = servicio.getHospedaje();
			
			if (!hospedaje.isDisponible()) {
				hospedaje.setDisponible(true);
				hospedajeRepository.save(hospedaje);
				
				liberados.add(ClienteHospedajeMapper.toDto(servicio));
			}
		}
		return liberados;
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByFechaInicio(LocalDate fechaInicio, Pageable pageable) {
		
		if (fechaInicio == null) {
			throw new IllegalArgumentException("Ingresar una fecha para buscar");
		}
		
		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository.findByFechaInicio(fechaInicio, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontró servicios de hospedaje con la fecha ingresada: " + fechaInicio);
		}
		
		logger.info("Fecha Ingresada: {}", fechaInicio);
		logger.info("Búsqueda de servicios de hospedaje por fecha OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByFechasBetween(LocalDate desde, LocalDate hasta,
			Pageable pageable) {
		
		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Ambas fechas son requeridas para la búsqueda");
		}
		
		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository.findByFechaInicioBetween(desde, hasta, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontró servicios de hospedaje entre la fecha: " + desde + " hasta " + hasta);
		}
		
		logger.info("Fechas ingresadas, desde: {} hasta: {}", desde, hasta);
		logger.info("Búsqueda de servicios de hospedaje entre fechas OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByClienteDni(String dni, Pageable pageable) {
		
		if (dni == null || dni.isEmpty()) {
			throw new IllegalArgumentException("En dni no puede ser nulo o vacío");
		}
		
		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository.findByClienteDni(dni, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No hay servicios de hospedaje con el DNI ingresado: " + dni);
		}
		
		logger.info("DNI ingresado: {}", dni);
		logger.info("Búsqueda de servicios de hospedaje con el DNI del cliente OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

}
