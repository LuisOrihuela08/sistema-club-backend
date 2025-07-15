package com.club.control.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.ClienteEntity;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long>{

	Optional<ClienteEntity> findByDni (String dni);
	
}
