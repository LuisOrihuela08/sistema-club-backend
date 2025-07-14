package com.club.control.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicio_hospedaje")
public class ClienteHospedajeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fecha_inicio")
	private LocalDate fechaInicio;
	
	@Column(name = "fecha_fin")
	private LocalDate fechaFin;
	
	@Column(name = "monto_total", precision = 10, scale = 2)
	private BigDecimal montoTotal;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "hospedaje_id")
	private HospedajeEntity hospedaje;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "metodo_pago_id")
	private MetodoPagoEntity metodoPago;
	
	public ClienteHospedajeEntity() {
		super();
	}

	public ClienteHospedajeEntity(LocalDate fechaInicio, LocalDate fechaFin, BigDecimal montoTotal,
			ClienteEntity cliente, HospedajeEntity hospedaje, MetodoPagoEntity metodoPago) {
		super();
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.montoTotal = montoTotal;
		this.cliente = cliente;
		this.hospedaje = hospedaje;
		this.metodoPago = metodoPago;
	}

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

	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

	public HospedajeEntity getHospedaje() {
		return hospedaje;
	}

	public void setHospedaje(HospedajeEntity hospedaje) {
		this.hospedaje = hospedaje;
	}

	public MetodoPagoEntity getMetodoPago() {
		return metodoPago;
	}

	public void setMetodoPago(MetodoPagoEntity metodoPago) {
		this.metodoPago = metodoPago;
	}

	@Override
	public String toString() {
		return "ClienteHospedajeEntity [id=" + id + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin
				+ ", montoTotal=" + montoTotal + ", cliente=" + cliente + ", hospedaje=" + hospedaje + ", metodoPago="
				+ metodoPago + "]";
	}
	
}
