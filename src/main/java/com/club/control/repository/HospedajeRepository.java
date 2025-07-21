package com.club.control.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.HospedajeEntity;

public interface HospedajeRepository extends JpaRepository<HospedajeEntity, Long>{

	Optional<HospedajeEntity> findByCodigoHabitacion (String codigoHabitacion);
}
