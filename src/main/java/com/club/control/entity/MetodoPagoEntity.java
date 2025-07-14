package com.club.control.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "metodos_pago")
public class MetodoPagoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "metodo")
	private List<ClientePiscinaEntity> clientePiscina;
	
	@OneToMany(mappedBy = "metodoPago")
	private List<ClienteBungalowEntity> clienteBungalow;
	
	public MetodoPagoEntity() {
		super();
	}
		
	public MetodoPagoEntity(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "MetodoPagoEntity [id=" + id + ", name=" + name + "]";
	}	
	
}
