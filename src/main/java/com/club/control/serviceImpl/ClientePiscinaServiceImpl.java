package com.club.control.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.club.control.dto.ClientePiscinaDTO;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.ClientePiscinaEntity;
import com.club.control.entity.MetodoPagoEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClientePiscinaMapper;
import com.club.control.repository.ClientePiscinaRepository;
import com.club.control.repository.ClienteRepository;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.ClientePiscinaService;

@Service
public class ClientePiscinaServiceImpl implements ClientePiscinaService{
	
	private static final Logger logger = LoggerFactory.getLogger(ClientePiscinaServiceImpl.class);
	
	private final ClienteRepository clienteRepository;
	
	private final MetodoPagoRepository metodoPagoRepository;
	
	private final ClientePiscinaRepository clientePiscinaRepository;
	
	public ClientePiscinaServiceImpl(ClienteRepository clienteRepository,
								     MetodoPagoRepository metodoPagoRepository,
								     ClientePiscinaRepository clientePiscinaRepository) {
		this.clienteRepository = clienteRepository;
		this.metodoPagoRepository = metodoPagoRepository;
		this.clientePiscinaRepository = clientePiscinaRepository;
	}

	@Override
	public List<ClientePiscinaDTO> listAll() {
		
		List<ClientePiscinaEntity> list = clientePiscinaRepository.findAll();
		logger.info("Listado del Servicio de piscina OK");
		return list.stream().map(ClientePiscinaMapper::toDto)
							.collect(Collectors.toList());
	}

	@Override
	public ClientePiscinaDTO saveClientePiscina(ClientePiscinaDTO clientePiscinaDTO) {
		
		if(clientePiscinaDTO.getCliente() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al cliente");
		}
		if(clientePiscinaDTO.getMetodoPago() == null) {
			throw new IllegalArgumentException("Es obligatorio asignar un método de Pago");
		}
		
		//Ahora Voy a verificar si el cliente existe por el DNI
		ClienteEntity cliente = clienteRepository.findByDni(clientePiscinaDTO.getCliente().getDni()).orElse(null);
		
		if (cliente == null) {
			
			cliente = new ClienteEntity();
			cliente.setName(clientePiscinaDTO.getCliente().getName());
			cliente.setLastName(clientePiscinaDTO.getCliente().getLastName());
			cliente.setDni(clientePiscinaDTO.getCliente().getDni());
			cliente.setDistrict(clientePiscinaDTO.getCliente().getDistrict());
			cliente.setTelephone(clientePiscinaDTO.getCliente().getTelephone());
			cliente = clienteRepository.save(cliente);
		}
		
		//Voy a verificar el método de pago
		MetodoPagoEntity metodo = metodoPagoRepository.findById(clientePiscinaDTO.getMetodoPago().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Método de pago no encontrado con el id: " + clientePiscinaDTO.getMetodoPago().getId());
		});
		
		ClientePiscinaEntity entity = ClientePiscinaMapper.toEntity(clientePiscinaDTO);
		
		//Aca hago el calculo automatico del monto total, utilizando BigDecimal porque es el tipo del atributo montoTotal
		BigDecimal montoTotal = BigDecimal.valueOf(clientePiscinaDTO.getPrecioUnitario())
										  .multiply(BigDecimal.valueOf(clientePiscinaDTO.getCantidadPersonas()));
		
		entity.setMontoTotal(montoTotal);		
		entity.setFecha(LocalDate.now());
		//Seteo cliente y el método de pago
		entity.setCliente(cliente);
		entity.setMetodo(metodo);
		
		ClientePiscinaEntity saved = clientePiscinaRepository.save(entity);
		logger.info("Nuevo Servicio de piscina guardado: {}", saved);
		return ClientePiscinaMapper.toDto(saved);
	}

	@Override
	public ClientePiscinaDTO updateClientePiscina(Long id, ClientePiscinaDTO clientePiscinaDTO) {
		
		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo o 0");
		} else if (id <= 0) {
			throw new IllegalArgumentException("El id no puede ser igual o menor a 0");
		}
		
		ClientePiscinaEntity entity = clientePiscinaRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró un servicio de piscina registrado con el id: "+ id);
		});
		
		//Verificando la existencia y actualizacion del cliente
		ClienteEntity cliente = entity.getCliente();
		
		if (cliente == null) {
			throw new RecursosNoEncontradosException("El servicio de piscina no tiene cliente asignado.");
		}
		
		cliente.setName(clientePiscinaDTO.getCliente().getName());
		cliente.setLastName(clientePiscinaDTO.getCliente().getLastName());
		cliente.setDni(clientePiscinaDTO.getCliente().getDni());
		cliente.setDistrict(clientePiscinaDTO.getCliente().getDistrict());
		cliente.setTelephone(clientePiscinaDTO.getCliente().getTelephone());
		clienteRepository.save(cliente);
		
		//Verificando la existencia y actualizacion del metodo de pago
		
		MetodoPagoEntity metodo = metodoPagoRepository.findById(clientePiscinaDTO.getMetodoPago().getId()).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró el método de pago con el id: "+ clientePiscinaDTO.getMetodoPago().getId());
		});
		
		//Actualizamos el servicio de piscina
		entity.setPrecioUnitario(clientePiscinaDTO.getPrecioUnitario());
		entity.setCantidadPersonas(clientePiscinaDTO.getCantidadPersonas());
		
		//Calculando el monto total
		BigDecimal montoTotal = BigDecimal.valueOf(clientePiscinaDTO.getPrecioUnitario())
										  .multiply(BigDecimal.valueOf(clientePiscinaDTO.getCantidadPersonas()));
		entity.setMontoTotal(montoTotal);
		entity.setFecha(clientePiscinaDTO.getFecha());
		entity.setCliente(cliente);
		entity.setMetodo(metodo);
		
		ClientePiscinaEntity updated = clientePiscinaRepository.save(entity);
		logger.info("Servicio de piscina actualizado: {}", updated);
		return ClientePiscinaMapper.toDto(updated);
	}

	@Override
	public void deleteClientePiscina(Long id) {
		
		if (id == null) {
			throw new IllegalArgumentException("El id no puede ser nulo");
		} else if (id <= 0) {
			throw new IllegalArgumentException("El id no puede ser menor o igual a 0");
		}
		
		ClientePiscinaEntity entity = clientePiscinaRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontro un servicio de piscina con el id: " + id);
		});
		
		clientePiscinaRepository.delete(entity);
		logger.info("Servicio de piscina eliminado exitosamente");
		
	}

	@Override
	public Page<ClientePiscinaDTO> pageClientsPiscina(Pageable pageable) {
		logger.info("Listado de servicio de piscina con paginación OK !");
		return clientePiscinaRepository.findAll(pageable)
									   .map(ClientePiscinaMapper::toDto);
	}

	@Override
	public Page<ClientePiscinaDTO> pageClientePiscinaByFecha(LocalDate fecha, Pageable pageable) {
		
		if (fecha == null) {
			throw new IllegalArgumentException("La fecha no puede ser nulo");
		}
		
		Page<ClientePiscinaEntity> pageEntity = clientePiscinaRepository.findByFecha(fecha, pageable);
		
		if (pageEntity.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron registros en la fecha " + fecha);
		}
		logger.info("Fecha ingresada: {}", fecha);
		logger.info("Listado por paginación por fecha diaria");
		return pageEntity.map(ClientePiscinaMapper::toDto);
	}

	@Override
	public Page<ClientePiscinaDTO> pageClientePiscinaByFechaBetween(LocalDate desde, LocalDate hasta, Pageable pageable) {
		
		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Se debe proporcionar ambas fechas");
		}
		
		Page<ClientePiscinaEntity> pageEntity = clientePiscinaRepository.findByFechaBetween(desde, hasta, pageable);
		
		if (pageEntity.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron registros en la fecha: " + desde + " hasta " + hasta);
		}
		
		logger.info("Fechas ingresadas, desde: {} hasta: {}", desde, hasta);
		logger.info("Búsqueda de servicios de piscina por fechas OK");
		return pageEntity.map(ClientePiscinaMapper::toDto);
	}

	@Override
	public Page<ClientePiscinaDTO> pageClientePiscinaByClienteDni(String dni, Pageable pageable) {
		
		if (dni == null || dni.isEmpty()) {
			throw new IllegalArgumentException("El dni no puede ser nulo o vacío");
		}
		
		Page<ClientePiscinaEntity> pageEntity = clientePiscinaRepository.findByClienteDni(dni, pageable);
		
		if (pageEntity.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de piscina con el dni: " + dni);
		}
		
		logger.info("DNI ingresado: {}", dni);
		logger.info("Servicio de piscina encontrado con el dni: {}", pageEntity);
		return pageEntity.map(ClientePiscinaMapper::toDto);
	}

	@Override
	public Page<ClientePiscinaDTO> pageClientePiscinaMetodoPagoNombreAndFecha(String nombreMetodoPago, LocalDate desde, LocalDate hasta, Pageable pageable) {
		
		if (nombreMetodoPago == null || nombreMetodoPago.isEmpty()) {
			throw new IllegalArgumentException("El método de pago no puede ser nulo o vacío");
		}
		
		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Se debe proporcionar ambas fechas");
		}
		
		if (desde.isAfter(hasta)) {
			throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin");
		}
		
		Page<ClientePiscinaEntity> pageEntity = clientePiscinaRepository.findByMetodoNameAndFechaBetween(nombreMetodoPago, desde, hasta, pageable);
		
		if (pageEntity.isEmpty()) {
			throw new RecursosNoEncontradosException(
					"No se encontraron servicios de piscina con el método de pago '" +
					nombreMetodoPago + "' entre " + desde + " y " + hasta);
		}
		
		logger.info("Búsqueda de servicios de piscina por método de pago y entre meses");
		return pageEntity.map(ClientePiscinaMapper::toDto);
	}

	@Override
	public ClientePiscinaDTO findClientePiscinaById(Long id) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual a 0");
		}
		
		ClientePiscinaEntity result = clientePiscinaRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Servicio de Piscina no encontrado con el id: " + id);
		});
		
		logger.info("ID ingresado: {}", id);
		logger.info("Servicio de piscina encontrado: {}", result);
		return ClientePiscinaMapper.toDto(result);
	}

	@Override
	public byte[] exportarPdfFiltradoClientePiscina(String dni, String nombreMetodoPago, LocalDate fecha,
			LocalDate desde, LocalDate hasta) {
		// TODO Auto-generated method stub
		return null;
	}	
}
