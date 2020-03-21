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
 * Header: "fecha,hora,producto,isin,numero,precio,total"
 * 
 * @author mparis
 *
 */
public class GenericoTransaccionesCargador {

	private static final Logger log = LoggerFactory.getLogger(GenericoTransaccionesCargador.class);

	public static void cargar(TransaccionesSet transaccionesSet, String filePath) throws Exception {
		FileReader reader = new FileReader(filePath);
		CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withCommentMarker('#')
				.withRecordSeparator(',').parse(reader);

		for (CSVRecord transaccionRecord : csvParser) {
			Transaccion transaccion = cargarTransaccion(transaccionRecord);
			log.trace("Añadir {}", transaccion);
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

		String numero_str = transaccionRecord.get("numero");
		int numero = Integer.valueOf(numero_str);

		String precio_str = transaccionRecord.get("precio");
		float precio = Float.valueOf(precio_str);

		String total_str = transaccionRecord.get("total");
		float total = Float.valueOf(total_str);

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
