package com.club.control.error;

public class ExportarExcelException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ExportarExcelException(String mensaje) {
		super(mensaje);
	}
	
	public ExportarExcelException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
