package com.club.control.dto;

import com.club.control.enums.TipoHabitacion;

import io.swagger.v3.oas.annotations.media.Schema;

public class HospedajeDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)//Esto oculta el id métodos POST y PUT para Swagger
	private Long id;
	private String codigoHabitacion;
	private double precio;
	private int capacidad;
	private String descripcion;
	private boolean disponible;
	private TipoHabitacion tipoHabitacion;
	
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
}
