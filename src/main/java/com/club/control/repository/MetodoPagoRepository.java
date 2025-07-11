package com.club.control.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.MetodoPagoEntity;

public interface MetodoPagoRepository extends JpaRepository<MetodoPagoEntity, Long> {

	Optional<MetodoPagoEntity> findByName (String name);
}
