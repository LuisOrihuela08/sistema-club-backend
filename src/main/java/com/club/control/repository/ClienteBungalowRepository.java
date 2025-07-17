package com.club.control.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.ClienteBungalowEntity;

public interface ClienteBungalowRepository extends JpaRepository<ClienteBungalowEntity, Long>{

	//Filtro para buscar servicios de bungalows por dia
	Page<ClienteBungalowEntity> findByFechaInicio (LocalDate desde, Pageable hasta);
	
	//Filtro para buscar servicios de bungalows entre fechas y para eso utilizare fechaInicio como intermediario -> desde - fechaInicio - hasta
	Page<ClienteBungalowEntity> findByFechaInicioBetween (LocalDate desde, LocalDate hasta, Pageable pageable);
	
	//Filtro para buscar servicios de bungalows por el DNI del cliente
	Page<ClienteBungalowEntity> findByClienteDni (String dni, Pageable pageable);
	
	//Filtro para buscar servicios de bungalows por el metodo de pago entre fechas
	Page<ClienteBungalowEntity> findByMetodoPagoNameAndFechaInicioBetween ( String name, LocalDate desde, LocalDate hasta, Pageable pageable);

	//Esto es para liberar un bungalow a activo luego de terminado el servicio seguna la fecha fin
	List<ClienteBungalowEntity> findByFechaFinBefore (LocalDate fecha);
	
	// Versiones sin paginación para generar los PDF para cada filtro
	List<ClienteBungalowEntity> findByClienteDni(String dni);
	List<ClienteBungalowEntity> findByFechaInicio (LocalDate fechaInicio);
	List<ClienteBungalowEntity> findByFechaInicioBetween(LocalDate desde, LocalDate hasta);
	List<ClienteBungalowEntity> findByMetodoPagoNameAndFechaInicioBetween(String metodo, LocalDate desde, LocalDate hasta);

}