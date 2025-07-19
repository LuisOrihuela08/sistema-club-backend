package com.club.control.graphql;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.club.control.dto.ClienteBungalowDTO;
import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.entity.ClienteBungalowEntity;
import com.club.control.entity.ClienteHospedajeEntity;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteBungalowMapper;
import com.club.control.mapper.ClienteHospedajeMapper;
import com.club.control.repository.ClienteBungalowRepository;
import com.club.control.repository.ClienteHospedajeRepository;

@Controller
public class ClienteBungalowGrapghQLController {

	private static final Logger logger = LoggerFactory.getLogger(ClienteBungalowGrapghQLController.class);

	private final ClienteBungalowRepository clienteBungalowRepository;
	private final ClienteHospedajeRepository clienteHospedajeRepository;

	public ClienteBungalowGrapghQLController(ClienteBungalowRepository clienteBungalowRepository,
			ClienteHospedajeRepository clienteHospedajeRepository) {
		this.clienteBungalowRepository = clienteBungalowRepository;
		this.clienteHospedajeRepository = clienteHospedajeRepository;
	}

	@QueryMapping(name = "findClienteBungalowById")
	public ClienteBungalowDTO findClienteBungalowById(@Argument(name = "id") Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual a 0");
		}

		try {
			ClienteBungalowEntity result = clienteBungalowRepository.findById(id).orElseThrow(() -> {
				throw new RecursosNoEncontradosException("No se encontro el servicio de bungalow con el id: " + id);
			});

			logger.info("GraphQL - Búsqueda de Servicio de Bungalow");
			logger.info("Servicio de bungalow encontrado con el id: {}", result);
			return ClienteBungalowMapper.toDto(result);
		} catch (Exception e) {
			e.getStackTrace();
			logger.error("Error al encontrar el servicio de Bungalow: {}", e);
			throw new RuntimeException("Error al encontrar el servicio de Bungalow: " + e.getMessage());
		}
	}

	@QueryMapping(name = "findClienteHospedajeById")
	public ClienteHospedajeDTO findClienteHospedajeById(@Argument(name = "id") Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual a 0");
		}

		try {

			ClienteHospedajeEntity result = clienteHospedajeRepository.findById(id).orElseThrow(() -> {
				throw new RecursosNoEncontradosException("No se encontro el servicio de hospedaje con el id: " + id);
			});

			logger.info("GraphQL - Búsqueda de Servicio de Hospedaje");
			logger.info("Servicio de hospedaje encontrado con el id: {}", result);
			return ClienteHospedajeMapper.toDto(result);

		} catch (Exception e) {
			e.getStackTrace();
			logger.error("Error al encontrar el servicio de Bungalow: {}", e);
			throw new RuntimeException("Error al encontrar el servicio de Bungalow: " + e.getMessage());
		}
	}
}
