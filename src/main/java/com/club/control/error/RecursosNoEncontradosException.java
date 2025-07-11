package com.club.control.error;

public class RecursosNoEncontradosException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public RecursosNoEncontradosException(String mensaje) {
		super(mensaje);
	}
}
