package com.club.control.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

public class ClienteBungalowDTO {

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)//Esto oculta el id métodos POST y PUT para Swagger
	private Long id;
	private double montoTotal;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate fechaInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate fechaFin;
	
	private ClienteDTO cliente;
	private BungalowDTO bungalow;
	private MetodoPagoDTO metodoPago;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
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
	public ClienteDTO getCliente() {
		return cliente;
	}
	public void setCliente(ClienteDTO cliente) {
		this.cliente = cliente;
	}
	public BungalowDTO getBungalow() {
		return bungalow;
	}
	public void setBungalow(BungalowDTO bungalow) {
		this.bungalow = bungalow;
	}
	public MetodoPagoDTO getMetodoPago() {
		return metodoPago;
	}
	public void setMetodoPago(MetodoPagoDTO metodoPago) {
		this.metodoPago = metodoPago;
	}
	@Override
	public String toString() {
		return "ClienteBungalowDTO [id=" + id + ", montoTotal=" + montoTotal + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + ", cliente=" + cliente + ", bungalow=" + bungalow + ", metodoPago="
				+ metodoPago + "]";
	}
	
	
	
	
}
