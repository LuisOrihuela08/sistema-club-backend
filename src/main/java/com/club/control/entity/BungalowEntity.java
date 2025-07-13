package com.club.control.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bungalows")
public class BungalowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String codigo;
	private int capacidad;
	private double precio;
	private String descripcion;
	private boolean disponible;
		
	public BungalowEntity() {
		super();
	}
	
	public BungalowEntity(String codigo, int capacidad, double precio, String descripcion, boolean disponible) {
		super();
		this.codigo = codigo;
		this.capacidad = capacidad;
		this.precio = precio;
		this.descripcion = descripcion;
		this.disponible = disponible;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}

	@Override
	public String toString() {
		return "BungalowEntity [id=" + id + ", codigo=" + codigo + ", capacidad=" + capacidad + ", precio=" + precio
				+ ", descripcion=" + descripcion + ", disponible=" + disponible + "]";
	}
	
	
}
