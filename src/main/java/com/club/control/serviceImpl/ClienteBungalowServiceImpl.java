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

import com.club.control.dto.ClienteBungalowDTO;
import com.club.control.entity.BungalowEntity;
import com.club.control.entity.ClienteBungalowEntity;
import com.club.control.entity.ClienteEntity;
import com.club.control.entity.MetodoPagoEntity;
import com.club.control.error.ExportarExcelException;
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteBungalowMapper;
import com.club.control.repository.BungalowRepository;
import com.club.control.repository.ClienteBungalowRepository;
import com.club.control.repository.ClienteRepository;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.ClienteBungalowService;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class ClienteBungalowServiceImpl implements ClienteBungalowService {

	private static final Logger logger = LoggerFactory.getLogger(ClienteBungalowServiceImpl.class);
	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	private final ClienteBungalowRepository clienteBungalowRepository;
	private final ClienteRepository clienteRepository;
	private final MetodoPagoRepository metodoPagoRepository;
	private final BungalowRepository bungalowRepository;

	public ClienteBungalowServiceImpl(ClienteBungalowRepository clienteBungalowRepository,
			ClienteRepository clienteRepository, MetodoPagoRepository metodoPagoRepository,
			BungalowRepository bungalowRepository) {
		this.clienteBungalowRepository = clienteBungalowRepository;
		this.clienteRepository = clienteRepository;
		this.metodoPagoRepository = metodoPagoRepository;
		this.bungalowRepository = bungalowRepository;
	}

	@Override
	public List<ClienteBungalowDTO> listAll() {

		List<ClienteBungalowEntity> list = clienteBungalowRepository.findAll();
		logger.info("Listado del Servicios de Bungalows OK !");
		return list.stream().map(ClienteBungalowMapper::toDto).toList();
	}

	@Override
	public ClienteBungalowDTO createClienteBungalow(ClienteBungalowDTO clienteBungalowDTO) {

		if (clienteBungalowDTO.getCliente() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir al cliente");
		}
		if (clienteBungalowDTO.getMetodoPago() == null) {
			throw new IllegalArgumentException("Es obligatorio añadir el método de pago");
		}

		ClienteEntity cliente = clienteRepository.findByDni(clienteBungalowDTO.getCliente().getDni()).orElse(null);

		if (cliente == null) {
			cliente = new ClienteEntity();
			cliente.setName(clienteBungalowDTO.getCliente().getName());
			cliente.setLastName(clienteBungalowDTO.getCliente().getLastName());
			cliente.setDni(clienteBungalowDTO.getCliente().getDni());
			cliente.setDistrict(clienteBungalowDTO.getCliente().getDistrict());
			cliente.setTelephone(clienteBungalowDTO.getCliente().getTelephone());
			cliente = clienteRepository.save(cliente);
		}

		BungalowEntity bungalow = bungalowRepository.findById(clienteBungalowDTO.getBungalow().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException(
							"No se encontró el bungalow con el id: " + clienteBungalowDTO.getBungalow().getId());
				});

		// Y aqui cambiaria la disponibilidad a false del bungalow
		bungalow.setDisponible(false);
		bungalow = bungalowRepository.save(bungalow);

		MetodoPagoEntity metodo = metodoPagoRepository.findById(clienteBungalowDTO.getMetodoPago().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException("No se encontró el método de pago con el id: "
							+ clienteBungalowDTO.getMetodoPago().getId());
				});

		ClienteBungalowEntity entity = ClienteBungalowMapper.toEntity(clienteBungalowDTO);
		entity.setMontoTotal(clienteBungalowDTO.getMontoTotal());
		entity.setFechaInicio(LocalDate.now());
		entity.setFechaFin(clienteBungalowDTO.getFechaFin());
		entity.setCliente(cliente);
		entity.setBungalow(bungalow);
		entity.setMetodoPago(metodo);

		ClienteBungalowEntity saved = clienteBungalowRepository.save(entity);

		logger.info("Nuevo Servicio de Bungalow: {}", saved);
		return ClienteBungalowMapper.toDto(saved);
	}

	@Override
	public ClienteBungalowDTO updateClienteBungalow(Long id, ClienteBungalowDTO clienteBungalowDTO) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a 0");
		}

		ClienteBungalowEntity entity = clienteBungalowRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException(
					"No se encontró un servicio de bungalow registrado con el id: " + id);
		});

		ClienteEntity cliente = entity.getCliente();

		if (cliente == null) {
			throw new RecursosNoEncontradosException("El servicio de bungalow no tiene un cliente asignado");
		}

		cliente.setName(clienteBungalowDTO.getCliente().getName());
		cliente.setLastName(clienteBungalowDTO.getCliente().getLastName());
		cliente.setDni(clienteBungalowDTO.getCliente().getDni());
		cliente.setDistrict(clienteBungalowDTO.getCliente().getDistrict());
		cliente.setTelephone(clienteBungalowDTO.getCliente().getTelephone());
		clienteRepository.save(cliente);

		BungalowEntity bungalow = bungalowRepository.findById(clienteBungalowDTO.getBungalow().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException(
							"No se encontró el bungalow con el id: " + clienteBungalowDTO.getBungalow().getId());
				});

		MetodoPagoEntity metodo = metodoPagoRepository.findById(clienteBungalowDTO.getMetodoPago().getId())
				.orElseThrow(() -> {
					throw new RecursosNoEncontradosException("No se encontró el método de pago con el id: "
							+ clienteBungalowDTO.getMetodoPago().getId());
				});

		entity.setMontoTotal(clienteBungalowDTO.getMontoTotal());
		entity.setFechaInicio(clienteBungalowDTO.getFechaInicio());
		entity.setFechaFin(clienteBungalowDTO.getFechaFin());
		entity.setCliente(cliente);
		entity.setBungalow(bungalow);
		entity.setMetodoPago(metodo);

		ClienteBungalowEntity updated = clienteBungalowRepository.save(entity);
		logger.info("Servicio de bungalow actualizado: {}", updated);
		return ClienteBungalowMapper.toDto(updated);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalow(Pageable pageable) {
		logger.info("Listado por paginación del servicio de bungalow OK !");
		return clienteBungalowRepository.findAll(pageable).map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByFechaInicio(LocalDate fechaInicio, Pageable pageable) {

		if (fechaInicio == null) {
			throw new IllegalArgumentException("La fecha no puede ser nulo");
		}

		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByFechaInicio(fechaInicio, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException(
					"No se encontraron servicios de bungalows registrados con la fecha: " + fechaInicio);
		}

		logger.info("Fecha ingresada: {}", fechaInicio);
		logger.info("Búsqueda de servicios de bungalows por fecha diaria OK !");
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByFechasBetween(LocalDate desde, LocalDate hasta,
			Pageable pageable) {

		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Se debe ingresar ambas fechas");
		}

		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByFechaInicioBetween(desde, hasta, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException(
					"No se encontraron servicios de bungalows con las fechas ingresadas: " + desde + " y " + hasta);
		}

		logger.info("Fechas ingresadas, fecha inicio: {} fecha fin: {}", desde, hasta);
		logger.info("Búsqueda de servicios de bungalows entre fechas OK");
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByClienteDni(String dni, Pageable pageable) {

		if (dni == null || dni.isEmpty()) {
			throw new IllegalArgumentException("El dni no puede ser nulo o vacío");
		}

		Page<ClienteBungalowEntity> result = clienteBungalowRepository.findByClienteDni(dni, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException(
					"No se encontraron servicios de bungalows registrados con el dni: " + dni);
		}

		logger.info("DNI ingresado: {}", dni);
		logger.info("Servicios de bungalows encontrados: {}", result);
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public Page<ClienteBungalowDTO> pageClienteBungalowByMetodoPagoAndFechasBetween(String nameMetodoPago,
			LocalDate desde, LocalDate hasta, Pageable pageable) {

		if (desde == null || hasta == null) {
			throw new IllegalArgumentException("Se debe ingresar ambas fechas");
		}
		if (nameMetodoPago == null || nameMetodoPago.isEmpty()) {
			throw new IllegalArgumentException("El método de pago no puede ser nulo o vacío");
		}

		Page<ClienteBungalowEntity> result = clienteBungalowRepository
				.findByMetodoPagoNameAndFechaInicioBetween(nameMetodoPago, desde, hasta, pageable);

		if (result.isEmpty()) {
			throw new RecursosNoEncontradosException("No se encontraron servicios de bungalows con el método de pago: "
					+ nameMetodoPago + " entre las fechas ingresadas: " + desde + " y " + hasta);
		}

		logger.info("Método de pago ingresado: {}", nameMetodoPago);
		logger.info("Fechas ingresadas ingresadas, desde: {} hasta: {}", desde, hasta);
		logger.info("Búsqueda de servicios de bungalows por metodo de pago y entre fechas OK");
		return result.map(ClienteBungalowMapper::toDto);
	}

	@Override
	public List<ClienteBungalowDTO> liberarBungalowFinalizado(LocalDate fecha) {

		List<ClienteBungalowEntity> serviciosFinalizados = clienteBungalowRepository.findByFechaFinBefore(fecha);

		List<ClienteBungalowDTO> liberados = new ArrayList<>();

		for (ClienteBungalowEntity servicio : serviciosFinalizados) {
			BungalowEntity bungalow = servicio.getBungalow();

			if (!bungalow.isDisponible()) {
				bungalow.setDisponible(true);
				bungalowRepository.save(bungalow);

				liberados.add(ClienteBungalowMapper.toDto(servicio));
			}
		}
		return liberados;
	}

	@Override
	public byte[] exportarPdfFiltrado(String dni, String metodoPago, LocalDate fechaInicio, LocalDate desde,
			LocalDate hasta) {
		List<ClienteBungalowEntity> datos = obtenerDatosFiltradosSinPaginacion(dni, metodoPago, fechaInicio, desde,
				hasta);
		
		return generarPdf(datos);
	}

	// Esto es para generar PDF y Excel segun los filtros
	public List<ClienteBungalowEntity> obtenerDatosFiltradosSinPaginacion(String dni, String metodoPago,
			LocalDate fechaInicio, LocalDate desde, LocalDate hasta) {
		if (dni != null && !dni.isEmpty()) {
			return clienteBungalowRepository.findByClienteDni(dni);
		} else if (metodoPago != null && !metodoPago.isEmpty() && desde != null && hasta != null) {
			return clienteBungalowRepository.findByMetodoPagoNameAndFechaInicioBetween(metodoPago, desde, hasta);
		} else if (desde != null && hasta != null) {
			return clienteBungalowRepository.findByFechaInicioBetween(desde, hasta);
		} else if (fechaInicio != null) {
			return clienteBungalowRepository.findByFechaInicio(fechaInicio);
		} else {
			return clienteBungalowRepository.findAll();
		}
	}

	private byte[] generarPdf(List<ClienteBungalowEntity> datos) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30); // Horizontal para más espacio
        //Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, baos);
			document.open();

			// Títulos y Encabezados
			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE);
			Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
			Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
			Font contentFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

			// Encabezado Mejorado
			PdfPTable tableEncabezado = new PdfPTable(2);
			tableEncabezado.setWidthPercentage(100);
			tableEncabezado.setSpacingAfter(10f);
			tableEncabezado.setWidths(new float[] { 50, 50 });

			// Título centrado que ocupa las dos columnas
			PdfPCell header0 = new PdfPCell(new Paragraph("REPORTE DE SERVICIOS DE BUNGALOWS", titleFont));
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
					"Fecha del reporte: " + LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)), infoFont));
			document.add(Chunk.NEWLINE);

			// Tabla con columnas personalizadas
			PdfPTable table = new PdfPTable(9); // 9 columnas
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

            // Headers
            table.addCell(createHeaderCell("Bungalow", headerFont));
            table.addCell(createHeaderCell("Precio", headerFont));
            table.addCell(createHeaderCell("Cliente", headerFont));
            table.addCell(createHeaderCell("DNI", headerFont));
            table.addCell(createHeaderCell("Teléfono", headerFont));
            table.addCell(createHeaderCell("Fecha Ingreso", headerFont));
            table.addCell(createHeaderCell("Fecha Salida", headerFont));
            table.addCell(createHeaderCell("Método Pago", headerFont));
            table.addCell(createHeaderCell("Total (S/)", headerFont));

			// Datos
			for (ClienteBungalowEntity entity : datos) {
				table.addCell(createDataCell(entity.getBungalow().getCodigo(), contentFont));
				table.addCell(createDataCell(String.valueOf(entity.getBungalow().getPrecio()), contentFont));
				table.addCell(createDataCell(entity.getCliente().getName() + " " + entity.getCliente().getLastName(), contentFont));
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
			logger.error("Ocurrió un error al generar el PDF de reporte de servicios de bungalows", e);
			throw new IllegalArgumentException("Ocurrió un error al generar el PDF de reporte de servicios de bungalows");
		}
		
		logger.info("PDF generado Exitosamente !");
		return baos.toByteArray();
	}

	@Override
	public ClienteBungalowDTO findClienteBungalowById(Long id) {

		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo ó menor/igual a 0");
		}

		ClienteBungalowEntity result = clienteBungalowRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Servicio de Bungalow no encontrado con el id: " + id);
		});

		logger.info("ID ingresado: {}", id);
		logger.info("Servicio de bungalow encontrado: {}", result);
		return ClienteBungalowMapper.toDto(result);
	}

	@Override
	public byte[] exportarPdfClienteBungalowById(Long id) {
		
		if (id == null || id <= 0) {
			throw new IllegalArgumentException("El id no puede ser nulo o menor/igual a 0");
		}
		ClienteBungalowEntity entity = clienteBungalowRepository.findById(id).orElseThrow(() -> {
			throw new RecursosNoEncontradosException("Servicio de Bungalow no encontrado con el id: " + id);
		});
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30); // Horizontal para más espacio
		//Document document = new Document(PageSize.A4);
		
		try {
			
			PdfWriter.getInstance(document, baos);
			document.open();

			// Títulos y Encabezados
			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE);
			Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
			Font headerFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.WHITE);
			Font contentFont = new Font(Font.HELVETICA, 10, Font.NORMAL, Color.BLACK);

			// Encabezado Mejorado
			PdfPTable tableEncabezado = new PdfPTable(2);
			tableEncabezado.setWidthPercentage(100);
			tableEncabezado.setSpacingAfter(10f);
			tableEncabezado.setWidths(new float[] { 50, 50 });

			// Título centrado que ocupa las dos columnas
			PdfPCell header0 = new PdfPCell(new Paragraph("REPORTE DE SERVICIO DE BUNGALOW", titleFont));
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
			PdfPTable table = new PdfPTable(9);
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

            // Headers
            table.addCell(createHeaderCell("Bungalow", headerFont));
            table.addCell(createHeaderCell("Precio", headerFont));
            table.addCell(createHeaderCell("Cliente", headerFont));
            table.addCell(createHeaderCell("DNI", headerFont));
            table.addCell(createHeaderCell("Teléfono", headerFont));
            table.addCell(createHeaderCell("Fecha Ingreso", headerFont));
            table.addCell(createHeaderCell("Fecha Salida", headerFont));
            table.addCell(createHeaderCell("Método Pago", headerFont));
            table.addCell(createHeaderCell("Total (S/)", headerFont));

			// Datos del servicio de piscina requerido
			table.addCell(createDataCell(entity.getBungalow().getCodigo(), contentFont));
			table.addCell(createDataCell(String.valueOf(entity.getBungalow().getPrecio()), contentFont));
			table.addCell(createDataCell(entity.getCliente().getName() + " " + entity.getCliente().getLastName(), contentFont));
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
			logger.error("Ocurrió un error al generar el PDF de reporte del servicio de bungalow", e);
			throw new IllegalArgumentException("Ocurrió un error al generar el PDF de reporte del servicio de bungalow");
		}
		logger.info("PDF generado Exitosamente del servicio de bungalow con el id: {}", id);
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
	public byte[] exportarExcelFiltrado(String dni, String metodoPago, LocalDate fechaInicio, LocalDate desde,
			LocalDate hasta) {
		List<ClienteBungalowEntity> datos = obtenerDatosFiltradosSinPaginacion(dni, metodoPago, fechaInicio, desde, hasta);
						
		return generarExcel(datos);
	}
	
	private byte[] generarExcel(List<ClienteBungalowEntity> datos) {
		
		try {
			
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Lista de Servicio de Bungalows");
			
			Row headerRow = sheet.createRow(0);
			String [] columnas = {"Bungalow", "Precio", "Cliente", "DNI", "Teléfono", "Fecha Ingreso", "Fecha Salida", "Método de pago", "Total"};

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

			for (int i = 0; i < columnas.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnas[i]);
				cell.setCellStyle(headerStyle);
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			
			int rowNum = 1;
			for(ClienteBungalowEntity entity: datos) {
				Row row = sheet.createRow(rowNum++);

                Cell cell0 = row.createCell(0);
                cell0.setCellValue(entity.getBungalow().getCodigo());
                cell0.setCellStyle(dataStyle);

                Cell cell1 = row.createCell(1);
                cell1.setCellValue(entity.getBungalow().getPrecio());
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
			
			logger.info("Excel de servicio de bungalows generado exitosamente" );
			return outputStream.toByteArray();
			
		} catch (IOException e) {
			logger.error("Hubo un error al generar el excel de servicio de bungalows");
			throw new ExportarExcelException("No se pudo exportar el excel de servicio de bungalows", e);
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
