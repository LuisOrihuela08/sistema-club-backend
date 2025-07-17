package com.club.control.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.ClienteHospedajeEntity;

public interface ClienteHospedajeRepository extends JpaRepository<ClienteHospedajeEntity, Long>{

	//Filtro para buscar servicios de hospedaje por dia
	Page<ClienteHospedajeEntity> findByFechaInicio (LocalDate fechaInicio, Pageable pageable);
	
	//Filtro para buscar servicios de bungalows entre fecha y para eso utilizare fechaInicio como intermediario -> desde - fechaInicio - hasta
	Page<ClienteHospedajeEntity> findByFechaInicioBetween (LocalDate desde, LocalDate hasta, Pageable pageable);
	
	//Filtro para buscar servicios de hospedaje por el DNI del cliente
	Page<ClienteHospedajeEntity> findByClienteDni (String dni, Pageable pageable);
	
	//Filtro para buscar servicios de hospedajes por el metodo de pago entre fechas
	Page<ClienteHospedajeEntity> findByMetodoPagoNameAndFechaInicioBetween (String name, LocalDate desde, LocalDate hasta, Pageable pageable);
	
	//Esto es para liberar el cuarto de hospedaje a activo luego de terminado el servicio seguna la fecha fin
	List<ClienteHospedajeEntity> findByFechaFinBefore (LocalDate fecha);
	
	// Versiones sin paginación para generar los PDF para cada filtro
	List<ClienteHospedajeEntity> findByFechaInicio (LocalDate fechaInicio);
	List<ClienteHospedajeEntity> findByFechaInicioBetween (LocalDate desde, LocalDate hasta);
	List<ClienteHospedajeEntity> findByClienteDni (String dni);
	List<ClienteHospedajeEntity> findByMetodoPagoNameAndFechaInicioBetween (String name, LocalDate desde, LocalDate hasta);
}
