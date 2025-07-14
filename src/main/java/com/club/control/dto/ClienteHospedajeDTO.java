package com.club.control.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

public class ClienteHospedajeDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)//Esto oculta el id métodos POST y PUT para Swagger
	private Long id;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private BigDecimal montoTotal;
	
	private ClienteDTO cliente;
	private HospedajeDTO hospedaje;
	private MetodoPagoDTO metodoPago;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public LocalDate getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}
	public BigDecimal getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(BigDecimal montoTotal) {
		this.montoTotal = montoTotal;
	}
	public ClienteDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}
	public HospedajeDTO getHospedaje() {
		return hospedaje;
	}
	public void setHospedaje(HospedajeDTO hospedaje) {
		this.hospedaje = hospedaje;
	}
	public MetodoPagoDTO getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(MetodoPagoDTO metodoPago) {
		this.metodoPago = metodoPago;
	}
	
	
}
