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
@Table(name = "servicio_piscina")
public class ClientePiscinaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "precio_unitario")
	private double precioUnitario;
	
	@Column(name = "cantidad_personas")
	private int cantidadPersonas;
	
	@Column(name = "monto_total")
	private double montoTotal;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate fecha;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "cliente_id")
	private ClienteEntity cliente;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "metodo_pago_id")
	private MetodoPagoEntity metodo;

	public ClientePiscinaEntity() {
		super();
	}

	public ClientePiscinaEntity(double precioUnitario, int cantidadPersonas, double montoTotal, LocalDate fecha,
			ClienteEntity cliente, MetodoPagoEntity metodo) {
		super();
		this.precioUnitario = precioUnitario;
		this.cantidadPersonas = cantidadPersonas;
		this.montoTotal = montoTotal;
		this.fecha = fecha;
		this.cliente = cliente;
		this.metodo = metodo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public double getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public int getCantidadPersonas() {
		return cantidadPersonas;
	}

	public void setCantidadPersonas(int cantidadPersonas) {
		this.cantidadPersonas = cantidadPersonas;
	}

	public double getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(double montoTotal) {
		this.montoTotal = montoTotal;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public ClienteEntity getCliente() {
		return cliente;
	}

	public void setCliente(ClienteEntity cliente) {
		this.cliente = cliente;
	}

	public MetodoPagoEntity getMetodo() {
		return metodo;
	}

	public void setMetodo(MetodoPagoEntity metodo) {
		this.metodo = metodo;
	}

	@Override
	public String toString() {
		return "ClientePiscina [id=" + id + ", precioUnitario=" + precioUnitario + ", cantidadPersonas="
				+ cantidadPersonas + ", montoTotal=" + montoTotal + ", fecha=" + fecha + ", cliente=" + cliente
				+ ", metodo=" + metodo + "]";
	}
	
	
}
