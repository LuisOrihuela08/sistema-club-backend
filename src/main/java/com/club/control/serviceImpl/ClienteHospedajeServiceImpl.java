package com.club.control.serviceimpl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.club.control.dto.ClienteHospedajeDTO;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.ClienteHospedajeEntity;
import com.club.control.entity.HospedajeEntity;
import com.club.control.entity.MetodoPagoEntity;
import com.club.control.error.ExportarExcelException;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteHospedajeMapper;
import com.club.control.repository.ClienteHospedajeRepository;
import com.club.control.repository.ClienteRepository;
import com.club.control.repository.HospedajeRepository;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.ClienteHospedajeService;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ClienteHospedajeServiceImpl implements ClienteHospedajeService {

	private static final Logger logger = LoggerFactory.getLogger(ClienteHospedajeServiceImpl.class);
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	private final ClienteHospedajeRepository clienteHospedajeRepository;
	private final ClienteRepository clienteRepository;
	private final HospedajeRepository hospedajeRepository;
	private final MetodoPagoRepository metodoPagoRepository;

	public ClienteHospedajeServiceImpl(ClienteHospedajeRepository clienteHospedajeRepository,
			ClienteRepository clienteRepository, HospedajeRepository hospedajeRepository,
			MetodoPagoRepository metodoPagoRepository) {
		this.clienteHospedajeRepository = clienteHospedajeRepository;
		this.clienteRepository = clienteRepository;
		this.hospedajeRepository = hospedajeRepository;
		this.metodoPagoRepository = metodoPagoRepository;
	}

	@Override
	public List<ClienteHospedajeDTO> listAll() {

		List<ClienteHospedajeEntity> list = clienteHospedajeRepository.findAll();
		logger.info("Lista de los servicios de hospedaje OK !");
		return list.stream().map(ClienteHospedajeMapper::toDto).toList();
	}

	@Override
	public ClienteHospedajeDTO saveClienteHospedaje(ClienteHospedajeDTO clienteHospedajeDTO) {

		if (clienteHospedajeDTO.getCliente() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al cliente");
		}

		if (clienteHospedajeDTO.getHospedaje() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al el cuarto disponible de hospedaje");
		}

		if (clienteHospedajeDTO.getMetodoPago() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir el método de pago");
		}

		ClienteEntity cliente = clienteRepository.findByDni(clienteHospedajeDTO.getCliente().getDni()).orElse(null);

		if (cliente == null) {
			cliente = new ClienteEntity();
			cliente.setName(clienteHospedajeDTO.getCliente().getName());
			cliente.setLastName(clienteHospedajeDTO.getCliente().getLastName());
			cliente.setDni(clienteHospedajeDTO.getCliente().getDni());
			cliente.setDistrict(clienteHospedajeDTO.getCliente().getDistrict());
			cliente.setTelephone(clienteHospedajeDTO.getCliente().getTelephone());
			cliente = clienteRepository.save(cliente);
		}

		HospedajeEntity hospedaje = hospedajeRepository.findById(clienteHospedajeDTO.getHospedaje().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException("Cuarto de hospedaje no encontrado con el id: "
							+ clienteHospedajeDTO.getHospedaje().getId());
				});

		// Aqui modifico la disponibilidad del hospedaje a false
		hospedaje.setDisponible(false);
		hospedaje = hospedajeRepository.save(hospedaje);

		MetodoPagoEntity metodoPago = metodoPagoRepository.findById(clienteHospedajeDTO.getMetodoPago().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException(
							"Método de pago no encontrado con el id: " + clienteHospedajeDTO.getMetodoPago().getId());
				});

		ClienteHospedajeEntity entity = ClienteHospedajeMapper.toEntity(clienteHospedajeDTO);
		entity.setFechaInicio(LocalDate.now());
		entity.setFechaFin(clienteHospedajeDTO.getFechaFin());
		entity.setMontoTotal(clienteHospedajeDTO.getMontoTotal());
		entity.setCliente(cliente);
		entity.setHospedaje(hospedaje);
		entity.setMetodoPago(metodoPago);

		ClienteHospedajeEntity saved = clienteHospedajeRepository.save(entity);

		logger.info("Nuevo servicio de hospedaje guardado: {}", saved);
		return ClienteHospedajeMapper.toDto(saved);
	}

	@Override
	public ClienteHospedajeDTO updateClienteHospedaje(Long id, ClienteHospedajeDTO clienteHospedajeDTO) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual que 0");
		}

		ClienteHospedajeEntity entity = clienteHospedajeRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("No se encontró un servicio de hospedaje con el id: " + id);
		});

		ClienteEntity cliente = entity.getCliente();

		if (cliente == null) {
			throw new RecursosNoEncontradosException("El servicio de hospedaje no tiene un cliente asignado");
		}

		cliente.setName(clienteHospedajeDTO.getCliente().getName());
		cliente.setLastName(clienteHospedajeDTO.getCliente().getLastName());
		cliente.setDni(clienteHospedajeDTO.getCliente().getDni());
		cliente.setDistrict(clienteHospedajeDTO.getCliente().getDistrict());
		cliente.setTelephone(clienteHospedajeDTO.getCliente().getTelephone());
		clienteRepository.save(cliente);

		HospedajeEntity hospedaje = hospedajeRepository.findById(clienteHospedajeDTO.getHospedaje().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException("Cuarto de hospedaje no encontrado con el id: "
							+ clienteHospedajeDTO.getHospedaje().getId());
				});

		MetodoPagoEntity metodoPago = metodoPagoRepository.findById(clienteHospedajeDTO.getMetodoPago().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException(
							"Método de pago no encontrado con el id: " + clienteHospedajeDTO.getMetodoPago().getId());
				});

		entity.setFechaInicio(clienteHospedajeDTO.getFechaInicio());
		entity.setFechaFin(clienteHospedajeDTO.getFechaFin());
		entity.setMontoTotal(clienteHospedajeDTO.getMontoTotal());
		entity.setCliente(cliente);
		entity.setHospedaje(hospedaje);
		entity.setMetodoPago(metodoPago);

		ClienteHospedajeEntity updated = clienteHospedajeRepository.save(entity);

		logger.info("Servicio de hospedaje actualizado: {}", updated);
		return ClienteHospedajeMapper.toDto(updated);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedaje(Pageable pageable) {
		logger.info("Listado por paginación del servicio de hospedaje OK !");
		return clienteHospedajeRepository.findAll(pageable).map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public List<ClienteHospedajeDTO> liberarHospedajeFinalizado(LocalDate fecha) {

		List<ClienteHospedajeEntity> serviciosFinalizados = clienteHospedajeRepository.findByFechaFinBefore(fecha);

		List<ClienteHospedajeDTO> liberados = new ArrayList<>();

		for (ClienteHospedajeEntity servicio : serviciosFinalizados) {
			HospedajeEntity hospedaje = servicio.getHospedaje();

			if (!hospedaje.isDisponible()) {
				hospedaje.setDisponible(true);
				hospedajeRepository.save(hospedaje);

				liberados.add(ClienteHospedajeMapper.toDto(servicio));
			}
		}
		return liberados;
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByFechaInicio(LocalDate fechaInicio, Pageable pageable) {

		if (fechaInicio == null) {
			throw new IllegalArgumentException("Ingresar una fecha para buscar");
		}

		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository.findByFechaInicio(fechaInicio, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException(
					"No se encontró servicios de hospedaje con la fecha ingresada: " + fechaInicio);
		}

		logger.info("Fecha Ingresada: {}", fechaInicio);
		logger.info("Búsqueda de servicios de hospedaje por fecha OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByFechasBetween(LocalDate desde, LocalDate hasta,
			Pageable pageable) {

		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Ambas fechas son requeridas para la búsqueda");
		}

		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository.findByFechaInicioBetween(desde, hasta,
				pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException(
					"No se encontró servicios de hospedaje entre la fecha: " + desde + " hasta " + hasta);
		}

		logger.info("Fechas ingresadas, desde: {} hasta: {}", desde, hasta);
		logger.info("Búsqueda de servicios de hospedaje entre fechas OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByClienteDni(String dni, Pageable pageable) {

		if (dni == null || dni.isEmpty()) {
			throw new IllegalArgumentException("En dni no puede ser nulo o vacío");
		}

		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository.findByClienteDni(dni, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No hay servicios de hospedaje con el DNI ingresado: " + dni);
		}

		logger.info("DNI ingresado: {}", dni);
		logger.info("Búsqueda de servicios de hospedaje con el DNI del cliente OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public Page<ClienteHospedajeDTO> pageClienteHospedajeByMetodoPagoAndFechaBetween(String nameMetodoPago,
			LocalDate desde, LocalDate hasta, Pageable pageable) {

		if (nameMetodoPago == null || nameMetodoPago.isEmpty()) {
			throw new IllegalArgumentException("El método de pago no debe ser nulo o vacío");
		}

		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Ambas fechas son obligatorio para el filtro");
		}

		Page<ClienteHospedajeEntity> result = clienteHospedajeRepository
				.findByMetodoPagoNameAndFechaInicioBetween(nameMetodoPago, desde, hasta, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de hospedaje con el método de pago: "
					+ nameMetodoPago + " y las fechas: " + desde + " hasta " + hasta);
		}

		logger.info("Método de pago ingresado: {}", nameMetodoPago);
		logger.info("Fechas ingresadas, desde: {} hasta: {}", desde, hasta);
		logger.info("La búsqueda de servicios de hospedaje por método de pago y entre fechas OK");
		return result.map(ClienteHospedajeMapper::toDto);
	}

	@Override
	public ClienteHospedajeDTO findClienteHospedajeById(Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual a 0");
		}

		ClienteHospedajeEntity result = clienteHospedajeRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Servicio de Hospedaje no encontrado con el id: " + id);
		});

		logger.info("ID ingresado: {}", id);
		logger.info("Servicio de Hospedaje encontrado: {}", result);
		return ClienteHospedajeMapper.toDto(result);
	}

	@Override
	public byte[] exportarPdfFiltradoClienteHospedaje(String dni, String metodoPago, LocalDate fechaInicio,
			LocalDate desde, LocalDate hasta) {

		List<ClienteHospedajeEntity> datos = obtenerDatosFiltradosSinPaginacion(dni, metodoPago, fechaInicio, desde,
				hasta);
		return generarPdf(datos);
	}

	// Esto es para generar PDF segun los filtros
	public List<ClienteHospedajeEntity> obtenerDatosFiltradosSinPaginacion(String dni, String metodoPago,
			LocalDate fechaInicio, LocalDate desde, LocalDate hasta) {
		if (dni != null && !dni.isEmpty()) {
			return clienteHospedajeRepository.findByClienteDni(dni);
		} else if (metodoPago != null && !metodoPago.isEmpty() && desde != null && hasta != null) {
			return clienteHospedajeRepository.findByMetodoPagoNameAndFechaInicioBetween(metodoPago, desde, hasta);
		} else if (desde != null && hasta != null) {
			return clienteHospedajeRepository.findByFechaInicioBetween(desde, hasta);
		} else if (fechaInicio != null) {
			return clienteHospedajeRepository.findByFechaInicio(fechaInicio);
		} else {
			return clienteHospedajeRepository.findAll();
		}
	}

	private byte[] generarPdf(List<ClienteHospedajeEntity> datos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);

		try {
			PdfWriter.getInstance(document, baos);
			document.open();

			// Títulos y Encabezados
			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE);
			Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
			Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
			Font contentFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

			// Encabezado Mejorado
			PdfPTable tableEncabezado = new PdfPTable(2);
			tableEncabezado.setWidthPercentage(100);
			tableEncabezado.setSpacingAfter(10f);
			tableEncabezado.setWidths(new float[] { 50, 50 });

			// Título centrado que ocupa las dos columnas
			PdfPCell header0 = new PdfPCell(new Paragraph("REPORTE DE SERVICIOS DE HOSPEDAJE", titleFont));
			header0.setColspan(2); // Ocupa las dos columnas
			header0.setBackgroundColor(new Color(41, 128, 185)); // 0, 102, 204 Azul oscuro
			header0.setHorizontalAlignment(Element.ALIGN_CENTER);
			header0.setVerticalAlignment(Element.ALIGN_MIDDLE);
			header0.setPadding(15f);
			tableEncabezado.addCell(header0);
			document.add(tableEncabezado);

			document.add(new Paragraph("Club Campestre Sol S.A.C", infoFont));
			document.add(new Paragraph("RUC: 4250125244", infoFont));
			document.add(new Paragraph("Mz L3 Lt35 Los Alamos", infoFont));
			document.add(new Paragraph("CHACLACAYO - LIMA", infoFont));
			document.add(new Paragraph("Fecha del reporte: " + LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)),
					infoFont));
			document.add(Chunk.NEWLINE);

			// Tabla con columnas personalizadas
			PdfPTable table = new PdfPTable(9); // 9 columnas
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

            // Headers
            table.addCell(createHeaderCell("Habitación", headerFont));
            table.addCell(createHeaderCell("Precio", headerFont));
            table.addCell(createHeaderCell("Cliente", headerFont));
            table.addCell(createHeaderCell("DNI", headerFont));
            table.addCell(createHeaderCell("Teléfono", headerFont));
            table.addCell(createHeaderCell("Fecha Inicio", headerFont));
            table.addCell(createHeaderCell("Fecha Fin", headerFont));
            table.addCell(createHeaderCell("Método Pago", headerFont));
            table.addCell(createHeaderCell("Total (S/)", headerFont));

			// Datos
			for (ClienteHospedajeEntity entity : datos) {
				table.addCell(createDataCell(entity.getHospedaje().getCodigoHabitacion(), contentFont));
				table.addCell(createDataCell(String.valueOf(entity.getHospedaje().getPrecio()), contentFont));
				table.addCell(createDataCell(
				    entity.getCliente().getName() + " " + entity.getCliente().getLastName(), contentFont));
				table.addCell(createDataCell(entity.getCliente().getDni(), contentFont));
				table.addCell(createDataCell(entity.getCliente().getTelephone(), contentFont));
				table.addCell(createDataCell(entity.getFechaInicio().toString(), contentFont));
				table.addCell(createDataCell(entity.getFechaFin().toString(), contentFont));
				table.addCell(createDataCell(entity.getMetodoPago().getName(), contentFont));
				table.addCell(createDataCell(entity.getMontoTotal().toString(), contentFont));
			}

			document.add(table);
			document.add(Chunk.NEWLINE);
			document.close();
		} catch (Exception e) {
			logger.error("Ocurrió un error al generar el PDF de reporte de servicios de hospedajes", e);
			throw new IllegalArgumentException("Ocurrió un error al generar el PDF de reporte de servicios de hospedajes");
		}

		logger.info("PDF generado Exitosamente !");
		return baos.toByteArray();
	}

	@Override
	public byte[] exportarPdfClienteHospedajeById(Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a 0");
		}

		ClienteHospedajeEntity entity = clienteHospedajeRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Servicio de Hospedaje no encontrado con el id: " + id);
		});

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);

		try {

			PdfWriter.getInstance(document, baos);
			document.open();

			// Títulos y Encabezados
			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE);
			Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
			Font headerFont = new Font(Font.HELVETICA, 10, Font.BOLD, Color.WHITE);
			Font contentFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

			// Encabezado Mejorado
			PdfPTable tableEncabezado = new PdfPTable(2);
			tableEncabezado.setWidthPercentage(100);
			tableEncabezado.setSpacingAfter(10f);
			tableEncabezado.setWidths(new float[] { 50, 50 });

			// Título centrado que ocupa las dos columnas
			PdfPCell header0 = new PdfPCell(new Paragraph("REPORTE DE SERVICIO DE HOSPEDAJE", titleFont));
			header0.setColspan(2); // Ocupa las dos columnas
			header0.setBackgroundColor(new Color(41, 128, 185)); // 0, 102, 204 Azul oscuro
			header0.setHorizontalAlignment(Element.ALIGN_CENTER);
			header0.setVerticalAlignment(Element.ALIGN_MIDDLE);
			header0.setPadding(15f);
			tableEncabezado.addCell(header0);
			document.add(tableEncabezado);

			document.add(new Paragraph("Club Campestre Sol S.A.C", infoFont));
			document.add(new Paragraph("RUC: 4250125244", infoFont));
			document.add(new Paragraph("Mz L3 Lt35 Los Alamos", infoFont));
			document.add(new Paragraph("CHACLACAYO - LIMA", infoFont));
			document.add(new Paragraph(
					"Fecha del reporte: " + LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)),
					infoFont));
			document.add(Chunk.NEWLINE);

			// Tabla con columnas personalizadas
			PdfPTable table = new PdfPTable(9);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

            // Headers
            table.addCell(createHeaderCell("Habitación", headerFont));
            table.addCell(createHeaderCell("Precio", headerFont));
            table.addCell(createHeaderCell("Cliente", headerFont));
            table.addCell(createHeaderCell("DNI", headerFont));
            table.addCell(createHeaderCell("Teléfono", headerFont));
            table.addCell(createHeaderCell("Fecha Inicio", headerFont));
            table.addCell(createHeaderCell("Fecha Fin", headerFont));
            table.addCell(createHeaderCell("Método Pago", headerFont));
            table.addCell(createHeaderCell("Total (S/)", headerFont));

			// Datos
			table.addCell(createDataCell(entity.getHospedaje().getCodigoHabitacion(), contentFont));
			table.addCell(createDataCell(String.valueOf(entity.getHospedaje().getPrecio()), contentFont));
			table.addCell(createDataCell(
			    entity.getCliente().getName() + " " + entity.getCliente().getLastName(), contentFont));
			table.addCell(createDataCell(entity.getCliente().getDni(), contentFont));
			table.addCell(createDataCell(entity.getCliente().getTelephone(), contentFont));
			table.addCell(createDataCell(entity.getFechaInicio().toString(), contentFont));
			table.addCell(createDataCell(entity.getFechaFin().toString(), contentFont));
			table.addCell(createDataCell(entity.getMetodoPago().getName(), contentFont));
			table.addCell(createDataCell(entity.getMontoTotal().toString(), contentFont));

			document.add(table);
			document.add(Chunk.NEWLINE);
			document.close();

		} catch (Exception e) {
			logger.error("Ocurrió un error al generar el PDF de reporte del servicio de hospedaje", e);
			throw new IllegalArgumentException("Ocurrió un error al generar el PDF de reporte del servicio de hospedaje");
		}
		logger.info("PDF generado Exitosamente del servicio de hospedaje con el id: {}", id);
		return baos.toByteArray();
	}

    // Metodo helper para el header PDF
    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setBackgroundColor(new Color(41, 128, 185));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(10f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidthBottom(1f);
        cell.setBorderColorBottom(new Color(200, 200, 200));
        return cell;
    }

    // Metodo helper para los datos PDF
    private PdfPCell createDataCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(text != null ? text : "", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(10f);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setBorderWidthBottom(0.5f);
        cell.setBorderColorBottom(new Color(220, 220, 220));
        return cell;
    }

	@Override
	public byte[] exportarExcelFiltradoClienteHospedaje(String dni, String metodoPago, LocalDate fechaInicio,
			LocalDate desde, LocalDate hasta) {
		List<ClienteHospedajeEntity> datos = obtenerDatosFiltradosSinPaginacion(dni, metodoPago, fechaInicio, desde, hasta);
		
		return generarExcel(datos);
	}
	
	private byte[] generarExcel(List<ClienteHospedajeEntity> datos) {
		
		try {
			
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Lista de Servicio de Hospedaje");
			
			Row headerRow = sheet.createRow(0);
			String [] columnas = {"Habitación", "Precio", "Cliente", "DNI", "Teléfono", "Fecha Ingreso", "Fecha Salida", "Método de pago", "Total"};

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			int rowNum = 1;
			for(ClienteHospedajeEntity entity: datos) {
				Row row = sheet.createRow(rowNum++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(entity.getHospedaje().getCodigoHabitacion());
                cell0.setCellStyle(dataStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(entity.getHospedaje().getPrecio());
                cell1.setCellStyle(dataStyle);

                Cell cell2 = row.createCell(2);
                cell2.setCellValue(entity.getCliente().getName() + " " + entity.getCliente().getLastName());
                cell2.setCellStyle(dataStyle);

                Cell cell3 = row.createCell(3);
                cell3.setCellValue(entity.getCliente().getDni());
                cell3.setCellStyle(dataStyle);

                Cell cell4 = row.createCell(4);
                cell4.setCellValue(entity.getCliente().getTelephone());
                cell4.setCellStyle(dataStyle);

                Cell cell5 = row.createCell(5);
                cell5.setCellValue(entity.getFechaInicio().format(formatter));
                cell5.setCellStyle(dataStyle);

                Cell cell6 = row.createCell(6);
                cell6.setCellValue(entity.getFechaFin().format(formatter));
                cell6.setCellStyle(dataStyle);

                Cell cell7 = row.createCell(7);
                cell7.setCellValue(entity.getMetodoPago().getName());
                cell7.setCellStyle(dataStyle);

                Cell cell8 = row.createCell(8);
                cell8.setCellValue(String.valueOf(entity.getMontoTotal()));
                cell8.setCellStyle(dataStyle);

                row.setHeightInPoints(25);

			}
			
			for (int i = 0; i < columnas.length; i++) {
				sheet.autoSizeColumn(i);
			}
			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			workbook.write(outputStream);
			workbook.close();
			
			logger.info("Excel de servicio de hospedaje se genero exitosamente");
			return outputStream.toByteArray();
			
		} catch (IOException e) {
			logger.error("Hubo un error al generar el excel de servicio de hospedaje");
			throw new ExportarExcelException("No se pudo generar el excel de servicios de hospedaje", e);
		}
	}

    //Estilo para el header
    private CellStyle createHeaderStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);

        XSSFCellStyle xssfCellStyle = (XSSFCellStyle) style;
        XSSFColor customColor = new XSSFColor(new byte[]{41, (byte)128, (byte)185}, null);
        xssfCellStyle.setFillForegroundColor(customColor);
        xssfCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_40_PERCENT.getIndex());

        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        org.apache.poi.ss.usermodel.Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 10);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());

        return style;

    }


}
