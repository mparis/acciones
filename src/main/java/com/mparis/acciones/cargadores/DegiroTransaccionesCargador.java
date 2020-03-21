package com.mparis.acciones.cargadores;

import java.io.FileReader;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.datos.Transaccion;
import com.mparis.acciones.datos.TransaccionesSet;

/**
 * 
 * Header:
 * "fecha,hora,producto,isin,bolsa,numero,precio_divisa,precio,valor_local_divisa,valor_local,valor_divisa,valor,tipo_cambio,tipo_cambio_divisa,comision,total_divisa,total,id_orden"
 * 
 * @author mparis
 *
 */
public class DegiroTransaccionesCargador {

	private static final Logger log = LoggerFactory.getLogger(DegiroTransaccionesCargador.class);

	public static void cargar(TransaccionesSet transaccionesSet, String filePath) throws Exception {
		FileReader reader = new FileReader(filePath);
		CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withCommentMarker('#')
				.withRecordSeparator(',').parse(reader);

		for (CSVRecord transaccionRecord : csvParser) {
			Transaccion transaccion = cargarTransaccion(transaccionRecord);
			log.trace("Add {}", transaccion);
			transaccionesSet.addTransaccion(transaccion);
		}
	}

	private static Transaccion cargarTransaccion(CSVRecord transaccionRecord) throws Exception {
		String fecha_str = transaccionRecord.get("fecha");
		if (fecha_str.isEmpty()) {
			throw new Exception("Fecha no disponible");
		}

		String hora_str = transaccionRecord.get("hora");
		if (hora_str.isEmpty()) {
			throw new Exception("Hora no disponible");
		}

		Date fecha = Transaccion.TIME_FORMAT.parse(fecha_str + " " + hora_str);

		String producto = transaccionRecord.get("producto");
		String isin = transaccionRecord.get("isin");
		if (hora_str.isEmpty()) {
			throw new Exception("ISIN no disponible");
		}

//		String bolsa = transaccionRecord.get("bolsa");

		String numero_str = transaccionRecord.get("numero");
		int numero = Integer.valueOf(numero_str);

//		String precio_divisa = transaccionRecord.get("precio_divisa");

		String precio_str = transaccionRecord.get("precio");
		float precio = Float.valueOf(precio_str);

//		String valor_local_divisa = transaccionRecord.get("valor_local_divisa");
//		String valor_local = transaccionRecord.get("valor_local");
//		String valor_divisa = transaccionRecord.get("valor_divisa");
//		String valor = transaccionRecord.get("valor");

//		String tipo_cambio_str = transaccionRecord.get("tipo_cambio");
//		float tipo_cambio = 1;
//		if (!tipo_cambio_str.isEmpty()) {
//			tipo_cambio = Float.valueOf(tipo_cambio_str);
//		}

//		String tipo_cambio_divisa = transaccionRecord.get("tipo_cambio_divisa");
//		String comision = transaccionRecord.get("comision");
//		String total_divisa = transaccionRecord.get("total_divisa");

		String total_str = transaccionRecord.get("total");
		float total = Float.valueOf(total_str);

//		String id_orden = transaccionRecord.get("id_orden");

//		log.debug("fecha: {}", TIME_FORMAT.format(fecha));
//		log.debug("producto: {}", producto);
//		log.debug("isin: {}", isin);
//		log.debug("bolsa: {}", bolsa);
//		log.debug("numero: {}", numero);
//		log.debug("precio_divisa: {}", precio_divisa);
//		log.debug("precio: {}", precio);
//		log.debug("valor_local_divisa: {}", valor_local_divisa);
//		log.debug("valor_local: {}", valor_local);
//		log.debug("valor_divisa: {}", valor_divisa);
//		log.debug("valor: {}", valor);
//		log.debug("tipo_cambio: {}", tipo_cambio);
//		log.debug("tipo_cambio_divisa: {}", tipo_cambio_divisa);
//		log.debug("comision: {}", comision);
//		log.debug("total_divisa: {}", total_divisa);
//		log.debug("total: {}", total);
//		log.debug("id_orden: {}", id_orden);
//		log.debug("");

		Transaccion.Builder b = new Transaccion.Builder();
		b.extranjero(true);
		b.fecha(fecha);
		b.producto(producto);
		b.isin(isin);
		b.numTitulos(numero);
		b.precio(precio);
		b.total(total);

		return b.build();
	}

}
