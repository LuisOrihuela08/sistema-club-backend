package com.club.control.entity;

import com.club.control.enums.TipoHabitacion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hospedajes")
public class HospedajeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "codigo_habitacion")
	private String codigoHabitacion;
	private double precio;
	private int capacidad;
	private String descripcion;
	private boolean disponible;
	
	@Enumerated(EnumType.STRING)//Esto guarda como String los tipos en la bd y no los guarda enumerados
	@Column(name = "tipo_habitacion")
	private TipoHabitacion tipoHabitacion;
	
	
	public HospedajeEntity() {
		super();
	}
		
	public HospedajeEntity(String codigoHabitacion, double precio, int capacidad, String descripcion,
			boolean disponible, TipoHabitacion tipoHabitacion) {
		super();
		this.codigoHabitacion = codigoHabitacion;
		this.precio = precio;
		this.capacidad = capacidad;
		this.descripcion = descripcion;
		this.disponible = disponible;
		this.tipoHabitacion = tipoHabitacion;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigoHabitacion() {
		return codigoHabitacion;
	}
	public void setCodigoHabitacion(String codigoHabitacion) {
		this.codigoHabitacion = codigoHabitacion;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
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

	public TipoHabitacion getTipoHabitacion() {
		return tipoHabitacion;
	}

	public void setTipoHabitacion(TipoHabitacion tipoHabitacion) {
		this.tipoHabitacion = tipoHabitacion;
	}

	@Override
	public String toString() {
		return "HospedajeEntity [id=" + id + ", codigoHabitacion=" + codigoHabitacion + ", precio=" + precio
				+ ", capacidad=" + capacidad + ", descripcion=" + descripcion + ", disponible=" + disponible
				+ ", tipoHabitacion=" + tipoHabitacion + "]";
	}

	
}
