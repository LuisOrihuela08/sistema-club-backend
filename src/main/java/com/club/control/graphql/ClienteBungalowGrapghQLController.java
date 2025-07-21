package com.club.control.graphql;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.club.control.dto.ClienteBungalowDTO;
import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.dto.ClientePiscinaDTO;
import com.club.control.entity.ClienteBungalowEntity;
import com.club.control.entity.ClienteHospedajeEntity;
import com.club.control.entity.ClientePiscinaEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteBungalowMapper;
import com.club.control.mapper.ClienteHospedajeMapper;
import com.club.control.mapper.ClientePiscinaMapper;
import com.club.control.repository.ClienteBungalowRepository;
import com.club.control.repository.ClienteHospedajeRepository;
import com.club.control.repository.ClientePiscinaRepository;

@Controller
public class ClienteBungalowGrapghQLController {

	private static final String ID_INVALIDO = "El id no puede ser nulo ó menor/igual a 0";
	private static final Logger logger = LoggerFactory.getLogger(ClienteBungalowGrapghQLController.class);

	private final ClienteBungalowRepository clienteBungalowRepository;
	private final ClienteHospedajeRepository clienteHospedajeRepository;
	private final ClientePiscinaRepository clientePiscinaRepository;

	public ClienteBungalowGrapghQLController(ClienteBungalowRepository clienteBungalowRepository,
											 ClienteHospedajeRepository clienteHospedajeRepository,
											 ClientePiscinaRepository clientePiscinaRepository) {
		this.clienteBungalowRepository = clienteBungalowRepository;
		this.clienteHospedajeRepository = clienteHospedajeRepository;
		this.clientePiscinaRepository = clientePiscinaRepository;
	}

	@QueryMapping(name = "findClienteBungalowById")
	public ClienteBungalowDTO findClienteBungalowById(@Argument(name = "id") Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException(ID_INVALIDO);
		}

		try {
			ClienteBungalowEntity result = clienteBungalowRepository.findById(id).orElseThrow(() -> {
				throw new RecursosNoEncontradosException("No se encontro el servicio de bungalow con el id: " + id);
			});

			logger.info("GraphQL - Búsqueda de Servicio de Bungalow");
			logger.info("Servicio de bungalow encontrado con el id: {}", result);
			return ClienteBungalowMapper.toDto(result);
		} catch (Exception e) {
			logger.error("Se produjo un error inesperado al obtener el servicio ded bungalow con ID: "+ id, e);
			throw new IllegalArgumentException("Error inesperado al obtener el servicio de bungalow con ID: " +id ,e);
		}
	}

	@QueryMapping(name = "findClienteHospedajeById")
	public ClienteHospedajeDTO findClienteHospedajeById(@Argument(name = "id") Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException(ID_INVALIDO);
		}

		try {

			ClienteHospedajeEntity result = clienteHospedajeRepository.findById(id).orElseThrow(() -> {
				throw new RecursosNoEncontradosException("No se encontro el servicio de hospedaje con el id: " + id);
			});

			logger.info("GraphQL - Búsqueda de Servicio de Hospedaje");
			logger.info("Servicio de hospedaje encontrado con el id: {}", result);
			return ClienteHospedajeMapper.toDto(result);

		} catch (Exception e) {
			logger.error("Se produjo un error inesperado al obtener el servicio de hospedaje con ID: "+ id, e);
			throw new IllegalArgumentException("Error inesperado al obtener el servicio de hospedaje con ID: " + id , e);
		}
	}
	
	@QueryMapping(name = "findClientePiscinaById")
	public ClientePiscinaDTO findClientePiscinaById(@Argument(name = "id") Long id) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException(ID_INVALIDO);
		}
		
		try {
			
			ClientePiscinaEntity result = clientePiscinaRepository.findById(id).orElseThrow(() -> {
				throw new RecursosNoEncontradosException("No se encontró servicio de piscina con el id: " + id);
			});
			
			logger.info("GraphQL - Búsqueda de Servicio de Piscina");
			logger.info("Servicio de piscina encontrado con el id: {}", result);
			return ClientePiscinaMapper.toDto(result);
			
		} catch (Exception e) {
			e.getStackTrace();
			logger.error("Se produjo un error inesperado al obtener el servicio de piscina con ID: "+ id, e);
			throw new IllegalArgumentException ("Error al obtener el servicio de piscina con ID: " +id , e);
		}
	}
 }
