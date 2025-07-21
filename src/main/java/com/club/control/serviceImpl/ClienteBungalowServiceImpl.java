package com.club.control.serviceimpl;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import com.club.control.error.RecursosNoEncontradosException;
import com.club.control.mapper.ClienteBungalowMapper;
import com.club.control.repository.BungalowRepository;
import com.club.control.repository.ClienteBungalowRepository;
import com.club.control.repository.ClienteRepository;
import com.club.control.repository.MetodoPagoRepository;
import com.club.control.service.ClienteBungalowService;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
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

	// Esto es para generar PDF segun los filtros
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

		Document document = new Document(PageSize.A4);
		try {
			PdfWriter.getInstance(document, baos);
			document.open();

			// Títulos y Encabezados
			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE);
			Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
			Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE);

			// Encabezado Mejorado
			PdfPTable tableEncabezado = new PdfPTable(2);
			tableEncabezado.setWidthPercentage(100);
			tableEncabezado.setSpacingAfter(10f);
			tableEncabezado.setWidths(new float[] { 50, 50 });

			// Título centrado que ocupa las dos columnas
			PdfPCell header0 = new PdfPCell(new Paragraph("REPORTE DE SERVICIOS DE BUNGALOWS", titleFont));
			header0.setColspan(2); // Ocupa las dos columnas
			header0.setBackgroundColor(new Color(63, 169, 219)); // 0, 102, 204 Azul oscuro
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
					"Fecha: " + LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN)), infoFont));
			document.add(Chunk.NEWLINE);

			// Tabla con columnas personalizadas
			PdfPTable table = new PdfPTable(7); // 6 columnas
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			PdfPCell header1 = new PdfPCell(new Phrase("Bungalow", headerFont));
			header1.setBackgroundColor(new Color(63, 169, 219));
			header1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header1);

			PdfPCell header2 = new PdfPCell(new Phrase("Cliente", headerFont));
			header2.setBackgroundColor(new Color(63, 169, 219));
			header2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header2);

			PdfPCell header3 = new PdfPCell(new Phrase("DNI", headerFont));
			header3.setBackgroundColor(new Color(63, 169, 219));
			header3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header3);

			PdfPCell header4 = new PdfPCell(new Phrase("Fecha Ingreso", headerFont));
			header4.setBackgroundColor(new Color(63, 169, 219));
			header4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header4);
			
			PdfPCell header5 = new PdfPCell(new Phrase("Fecha Salida", headerFont));
			header5.setBackgroundColor(new Color(63, 169, 219));
			header5.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header5);
			
			PdfPCell header6 = new PdfPCell(new Phrase("M.Pago", headerFont));
			header6.setBackgroundColor(new Color(63, 169, 219));
			header6.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header6);
			
			PdfPCell header7 = new PdfPCell(new Phrase("Total (S/)", headerFont));
			header7.setBackgroundColor(new Color(63, 169, 219));
			header7.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header7);

			// Datos
			for (ClienteBungalowEntity entity : datos) {
				table.addCell(entity.getBungalow().getCodigo());
				table.addCell(entity.getCliente().getName() + " " + entity.getCliente().getLastName());
				table.addCell(entity.getCliente().getDni());
				table.addCell(entity.getFechaInicio().toString());
				table.addCell(entity.getFechaFin().toString());
				table.addCell(entity.getMetodoPago().getName());
				table.addCell(entity.getMontoTotal().toString());
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
		Document document = new Document(PageSize.A4);
		
		try {
			
			PdfWriter.getInstance(document, baos);
			document.open();

			// Títulos y Encabezados
			Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.WHITE);
			Font infoFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.DARK_GRAY);
			Font headerFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.WHITE);

			// Encabezado Mejorado
			PdfPTable tableEncabezado = new PdfPTable(2);
			tableEncabezado.setWidthPercentage(100);
			tableEncabezado.setSpacingAfter(10f);
			tableEncabezado.setWidths(new float[] { 50, 50 });

			// Título centrado que ocupa las dos columnas
			PdfPCell header0 = new PdfPCell(new Paragraph("REPORTE DE SERVICIO DE BUNGALOW", titleFont));
			header0.setColspan(2); // Ocupa las dos columnas
			header0.setBackgroundColor(new Color(63, 169, 219)); // 0, 102, 204 Azul oscuro
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
			PdfPTable table = new PdfPTable(7); // 7 columnas
			table.setWidthPercentage(100);
			table.setSpacingBefore(10f);
			table.setSpacingAfter(10f);

			PdfPCell header1 = new PdfPCell(new Phrase("Bungalow", headerFont));
			header1.setBackgroundColor(new Color(63, 169, 219));
			header1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header1);

			PdfPCell header2 = new PdfPCell(new Phrase("Cliente", headerFont));
			header2.setBackgroundColor(new Color(63, 169, 219));
			header2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header2);

			PdfPCell header3 = new PdfPCell(new Phrase("DNI", headerFont));
			header3.setBackgroundColor(new Color(63, 169, 219));
			header3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header3);

			PdfPCell header4 = new PdfPCell(new Phrase("Fecha Ingreso", headerFont));
			header4.setBackgroundColor(new Color(63, 169, 219));
			header4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header4);
			
			PdfPCell header5 = new PdfPCell(new Phrase("Fecha Salida", headerFont));
			header5.setBackgroundColor(new Color(63, 169, 219));
			header5.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header5);
			
			PdfPCell header6 = new PdfPCell(new Phrase("M.Pago", headerFont));
			header6.setBackgroundColor(new Color(63, 169, 219));
			header6.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header6);
			
			PdfPCell header7 = new PdfPCell(new Phrase("Total (S/)", headerFont));
			header7.setBackgroundColor(new Color(63, 169, 219));
			header7.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(header7);

			// Datos del servicio de piscina requerido
			table.addCell(entity.getBungalow().getCodigo());
			table.addCell(entity.getCliente().getName() + " " + entity.getCliente().getLastName());
			table.addCell(entity.getCliente().getDni());
			table.addCell(entity.getFechaInicio().toString());
			table.addCell(entity.getFechaFin().toString());
			table.addCell(entity.getMetodoPago().getName());
			table.addCell(entity.getMontoTotal().toString());

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

}
