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
	
	//Esto es para liberar el cuarto de hospedaje a activo luego de terminado el servicio seguna la fecha fin
	List<ClienteHospedajeEntity> findByFechaFinBefore (LocalDate fecha);
}
