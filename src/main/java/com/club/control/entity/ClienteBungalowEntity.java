package com.club.control.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.CascadeType;
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
@Table(name = "servicio_bungalow")
public class ClienteBungalowEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "monto_total")
	private double montoTotal;
	
	@Column(name = "fecha_inicio")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate fechaInicio;
	
	@Column(name = "fecha_fin")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate fechaFin;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "cliente_id")
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "bungalow_id")
	private BungalowEntity bungalow;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "metodo_pago_id")
	private MetodoPagoEntity metodoPago;
	
	public ClienteBungalowEntity() {
		super();
	}
	

	public ClienteBungalowEntity(double montoTotal, LocalDate fechaInicio, LocalDate fechaFin, ClienteEntity cliente,
			BungalowEntity bungalow, MetodoPagoEntity metodoPago) {
		super();
		this.montoTotal = montoTotal;
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.cliente = cliente;
		this.bungalow = bungalow;
		this.metodoPago = metodoPago;
	}

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

	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

	public BungalowEntity getBungalow() {
		return bungalow;
	}

	public void setBungalow(BungalowEntity bungalow) {
		this.bungalow = bungalow;
	}
	

	public MetodoPagoEntity getMetodoPago() {
		return metodoPago;
	}


	public void setMetodoPago(MetodoPagoEntity metodoPago) {
		this.metodoPago = metodoPago;
	}


	@Override
	public String toString() {
		return "ClienteBungalowEntity [id=" + id + ", montoTotal=" + montoTotal + ", fechaInicio=" + fechaInicio
				+ ", fechaFin=" + fechaFin + ", cliente=" + cliente + ", bungalow=" + bungalow + ", metodoPago="
				+ metodoPago + "]";
	}

}
