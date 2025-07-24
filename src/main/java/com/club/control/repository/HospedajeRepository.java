package com.club.control.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.HospedajeEntity;

public interface HospedajeRepository extends JpaRepository<HospedajeEntity, Long>{

	Optional<HospedajeEntity> findByCodigoHabitacion (String codigoHabitacion);
	
	//Esto es para filtrar los hospedajes por disponibilidad
	Page<HospedajeEntity> findByDisponible (boolean disponible, Pageable pageable);
}
