package com.club.control.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.ClientePiscinaEntity;

public interface ClientePiscinaRepository extends JpaRepository<ClientePiscinaEntity, Long>{

	//Filtro para buscar los servicios de piscina por dia
	Page<ClientePiscinaEntity> findByFecha (LocalDate fecha, Pageable pageable);
	
	//Filtro para buscar los servicios de piscina entre fechas
	Page<ClientePiscinaEntity> findByFechaBetween (LocalDate desde, LocalDate hasta, Pageable pageable);
	
	//Filtro del servicio de piscina por el DNI del cliente
	Page<ClientePiscinaEntity> findByClienteDni (String dni, Pageable pageable);
	
	//Filtro del servicio de piscina por el nombre del metodo de pago entre fechas
	Page<ClientePiscinaEntity> findByMetodoNameAndFechaBetween (String name, LocalDate desde, LocalDate hasta, Pageable pageable);

	//Versiones sin paginacion para generar los PDF para cada filtro
	List<ClientePiscinaEntity> findByFecha (LocalDate fecha);
	List<ClientePiscinaEntity> findByFechaBetween (LocalDate desde, LocalDate hasta);
	List<ClientePiscinaEntity> findByClienteDni (String dni);
	List<ClientePiscinaEntity> findByMetodoNameAndFechaBetween (String name, LocalDate desde, LocalDate hasta);

}
