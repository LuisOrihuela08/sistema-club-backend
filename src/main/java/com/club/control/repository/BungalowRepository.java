package com.club.control.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.club.control.entity.BungalowEntity;

public interface BungalowRepository extends JpaRepository<BungalowEntity, Long>{

	Optional<BungalowEntity> findByCodigo (String codigo);
}
