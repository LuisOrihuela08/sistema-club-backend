package com.club.control.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	@Column(name = "last_name")
	private String lastName;
	private String dni;
	private String district;
	private String telephone;
	
	@OneToMany(mappedBy = "cliente")
	@JsonIgnore
	private List<ClientePiscinaEntity> clientePiscina;
	
	@OneToMany(mappedBy = "cliente")
	@JsonIgnore
	private List<ClienteBungalowEntity> clienteBungalow;
	
	public ClienteEntity() {
		super();
	}
	
	
	public ClienteEntity(String name, String lastName, String dni, String district, String telephone) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.dni = dni;
		this.district = district;
		this.telephone = telephone;
	}

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


	@Override
	public String toString() {
		return "ClienteEntity [id=" + id + ", name=" + name + ", lastName=" + lastName + ", dni=" + dni + ", district="
				+ district + ", telephone=" + telephone + "]";
	}
	
	
	
	
	
}
