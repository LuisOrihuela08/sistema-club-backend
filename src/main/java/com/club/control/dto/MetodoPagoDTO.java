package com.club.control.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class MetodoPagoDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)//Esto oculta el id métodos POST y PUT
	private Long id;
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
