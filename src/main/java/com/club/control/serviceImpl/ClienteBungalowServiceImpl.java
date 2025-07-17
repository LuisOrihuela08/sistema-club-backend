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

import com.club.control.dto.ClienteBungalowDTO;
import com.club.control.entity.BungalowEntity;
import com.club.control.entity.ClienteBungalowEntity;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.MetodoPagoEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteBungalowMapper;
import com.club.control.repository.BungalowRepository;
import com.club.control.repository.ClienteBungalowRepository;
import com.club.control.repository.ClienteRepository;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.ClienteBungalowService;

@Service
public class ClienteBungalowServiceImpl implements ClienteBungalowService{

	private static final Logger logger = LoggerFactory.getLogger(ClienteBungalowServiceImpl.class);
	
	private final ClienteBungalowRepository clienteBungalowRepository;
	private final ClienteRepository clienteRepository;
	private final MetodoPagoRepository metodoPagoRepository;
	private final BungalowRepository bungalowRepository;
	
	public ClienteBungalowServiceImpl(ClienteBungalowRepository clienteBungalowRepository,
									  ClienteRepository clienteRepository,
									  MetodoPagoRepository metodoPagoRepository,
									  BungalowRepository bungalowRepository) {
		this.clienteBungalowRepository = clienteBungalowRepository;
		this.clienteRepository = clienteRepository;
		this.metodoPagoRepository = metodoPagoRepository;
		this.bungalowRepository = bungalowRepository;
	}
	
	@Override
	public List<ClienteBungalowDTO> listAll() {
		
		List<ClienteBungalowEntity> list = clienteBungalowRepository.findAll();
		logger.info("Listado del Servicios de Bungalows OK !");
		return list.stream().map(ClienteBungalowMapper::toDto)
							.collect(Collectors.toList());
	}

	@Override
	public ClienteBungalowDTO createClienteBungalow(ClienteBungalowDTO clienteBungalowDTO) {
		
		if (clienteBungalowDTO.getCliente() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al cliente");
		}
		if (clienteBungalowDTO.getMetodoPago() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir el método de pago");
		}
		
		ClienteEntity cliente = clienteRepository.findByDni(clienteBungalowDTO.getCliente().getDni()).orElse(null);
		
		if (cliente == null) {
			cliente = new ClienteEntity();
			cliente.setName(clienteBungalowDTO.getCliente().getName());
			cliente.setLastName(clienteBungalowDTO.getCliente().getLastName());
			cliente.setDni(clienteBungalowDTO.getCliente().getDni());
			cliente.setDistrict(clienteBungalowDTO.getCliente().getDistrict());
			cliente.setTelephone(clienteBungalowDTO.getCliente().getTelephone());
			cliente = clienteRepository.save(cliente);
		}
		
		BungalowEntity bungalow = bungalowRepository.findById(clienteBungalowDTO.getBungalow().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró el bungalow con el id: " + clienteBungalowDTO.getBungalow().getId());
		});
		
		//Y aqui cambiaria la disponibilidad a false del bungalow
		bungalow.setDisponible(false);
		bungalow = bungalowRepository.save(bungalow);
			
		MetodoPagoEntity metodo = metodoPagoRepository.findById(clienteBungalowDTO.getMetodoPago().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró el método de pago con el id: " + clienteBungalowDTO.getMetodoPago().getId());
		});
		
		ClienteBungalowEntity entity = ClienteBungalowMapper.toEntity(clienteBungalowDTO);
		entity.setMontoTotal(clienteBungalowDTO.getMontoTotal());
		entity.setFechaInicio(LocalDate.now());
		entity.setFechaFin(clienteBungalowDTO.getFechaFin());
		entity.setCliente(cliente);
		entity.setBungalow(bungalow);
		entity.setMetodoPago(metodo);
		
		ClienteBungalowEntity saved = clienteBungalowRepository.save(entity);
		
		logger.info("Nuevo Servicio de Bungalow: {}", saved);	
		return ClienteBungalowMapper.toDto(saved);
	}

	@Override
	public ClienteBungalowDTO updateClienteBungalow(Long id, ClienteBungalowDTO clienteBungalowDTO) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a 0");
		}
		
		ClienteBungalowEntity entity = clienteBungalowRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró un servicio de bungalow registrado con el id: " + id);
		});
		
		ClienteEntity cliente = entity.getCliente();
		
		if (cliente == null) {
			throw new RecursosNoEncontradosException("El servicio de bungalow no tiene un cliente asignado");
		}
		
		cliente.setName(clienteBungalowDTO.getCliente().getName());
		cliente.setLastName(clienteBungalowDTO.getCliente().getLastName());
		cliente.setDni(clienteBungalowDTO.getCliente().getDni());
		cliente.setDistrict(clienteBungalowDTO.getCliente().getDistrict());
		cliente.setTelephone(clienteBungalowDTO.getCliente().getTelephone());
		clienteRepository.save(cliente);
		
		BungalowEntity bungalow = bungalowRepository.findById(clienteBungalowDTO.getBungalow().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró el bungalow con el id: " + clienteBungalowDTO.getBungalow().getId());
		});
			
		MetodoPagoEntity metodo = metodoPagoRepository.findById(clienteBungalowDTO.getMetodoPago().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró el método de pago con el id: " + clienteBungalowDTO.getMetodoPago().getId());
		});
		
		entity.setMontoTotal(clienteBungalowDTO.getMontoTotal());
		entity.setFechaInicio(clienteBungalowDTO.getFechaInicio());
		entity.setFechaFin(clienteBungalowDTO.getFechaFin());
		entity.setCliente(cliente);
		entity.setBungalow(bungalow);
		entity.setMetodoPago(metodo);
		
		ClienteBungalowEntity updated = clienteBungalowRepository.save(entity);
		logger.info("Servicio de bungalow actualizado: "+ updated);	
		return ClienteBungalowMapper.toDto(updated);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalow(Pageable pageable) {
		logger.info("Listado por paginación del servicio de bungalow OK !");
		return clienteBungalowRepository.findAll(pageable)
										.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByFechaInicio(LocalDate fechaInicio, Pageable pageable) {
		
		if (fechaInicio == null) {
			throw new IllegalArgumentException("La fecha no puede ser nulo");
		}
		
		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByFechaInicio(fechaInicio, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de bungalows registrados con la fecha: " + fechaInicio);
		}
		
		logger.info("Fecha ingresada: {}", fechaInicio);
		logger.info("Búsqueda de servicios de bungalows por fecha diaria OK !");
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByFechasBetween(LocalDate desde, LocalDate hasta,
			Pageable pageable) {
		
		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Se debe ingresar ambas fechas");
		}
		
		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByFechaInicioBetween(desde, hasta, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de bungalows con las fechas ingresadas: " + desde + " y " + hasta);
		}
		
		logger.info("Fechas ingresadas, fecha inicio: {} fecha fin: {}", desde, hasta);
		logger.info("Búsqueda de servicios de bungalows entre fechas OK");
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByClienteDni(String dni, Pageable pageable) {
		
		if (dni == null || dni.isEmpty()) {
			throw new IllegalArgumentException("El dni no puede ser nulo o vacío");
		}
		
		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByClienteDni(dni, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de bungalows registrados con el dni: " + dni);
		}
		
		logger.info("DNI ingresado: {}", dni);
		logger.info("Servicios de bungalows encontrados: {}", result);
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByMetodoPagoAndFechasBetween(String nameMetodoPago, LocalDate desde, LocalDate hasta, Pageable pageable) {
		
		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Se debe ingresar ambas fechas");
		}
		if (nameMetodoPago == null || nameMetodoPago.isEmpty()) {
			throw new IllegalArgumentException("El método de pago no puede ser nulo o vacío");
		}
		
		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByMetodoPagoNameAndFechaInicioBetween(nameMetodoPago, desde, hasta, pageable);
		
		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de bungalows con el método de pago: " + nameMetodoPago + " entre las fechas ingresadas: " + desde + " y " + hasta);
		}
		
		logger.info("Método de pago ingresado: {}", nameMetodoPago);
		logger.info("Fechas ingresadas ingresadas, desde: {} hasta: {}", desde, hasta);
		logger.info("Búsqueda de servicios de bungalows por metodo de pago y entre fechas OK");
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public List<ClienteBungalowDTO> liberarBungalowFinalizado(LocalDate fecha) {
		
		List<ClienteBungalowEntity> serviciosFinalizados = clienteBungalowRepository.findByFechaFinBefore(fecha);
		
		List<ClienteBungalowDTO> liberados = new ArrayList<>();
		
		for(ClienteBungalowEntity servicio: serviciosFinalizados) {
			BungalowEntity bungalow = servicio.getBungalow();
			
			if (!bungalow.isDisponible()) {
				bungalow.setDisponible(true);
				bungalowRepository.save(bungalow);
				
				liberados.add(ClienteBungalowMapper.toDto(servicio));
			}
		}
		return liberados;
	}

}
