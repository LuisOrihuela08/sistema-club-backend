package com.club.control.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class ClienteDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)//Esto oculta el id métodos POST y PUT para Swagger
	private Long id;
	private String name;
	private String lastName;
	private String dni;
	private String district;
	private String telephone;
		
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
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	
}
