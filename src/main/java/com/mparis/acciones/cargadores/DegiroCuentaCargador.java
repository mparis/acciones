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
 * "fecha,hora,fecha_valor,producto,isin,descripcion,tipo,variacion,variacion_divisa,saldo,saldo_divisa,id_orden"
 * 
 * @author mparis
 *
 */
public class DegiroCuentaCargador {

	private static final Logger log = LoggerFactory.getLogger(DegiroCuentaCargador.class);

	public static void cargar(TransaccionesSet transaccionesSet, String filePath) throws Exception {
		FileReader reader = new FileReader(filePath);
		CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withCommentMarker('#')
				.withRecordSeparator(',').parse(reader);

		for (CSVRecord cuentaRecord : csvParser) {
			cargarMovimientoCuenta(cuentaRecord);
		}
	}

	private static void cargarMovimientoCuenta(CSVRecord cuentaRecord) throws Exception {
		String fecha_str = cuentaRecord.get("fecha");
		if (fecha_str.isEmpty()) {
			throw new Exception("Fecha no disponible");
		}

		String hora_str = cuentaRecord.get("hora");
		if (hora_str.isEmpty()) {
			throw new Exception("Hora no disponible");
		}

		Date fecha = Transaccion.TIME_FORMAT.parse(fecha_str + " " + hora_str);

//		String fecha_valor_str = record.get("fecha_valor"); /* no necesario */
		String producto = cuentaRecord.get("producto");
		String isin = cuentaRecord.get("isin");
		String descripcion = cuentaRecord.get("descripcion");
		String tipo = cuentaRecord.get("tipo");
		String variacion = cuentaRecord.get("variacion");
		String variacion_divisa = cuentaRecord.get("variacion_divisa");
		String saldo = cuentaRecord.get("saldo");
		String saldo_divisa = cuentaRecord.get("saldo_divisa");
		String id_orden = cuentaRecord.get("id_orden");

		log.trace("fecha: {}", Transaccion.TIME_FORMAT.format(fecha));
		log.trace("producto: {}", producto);
		log.trace("isin: {}", isin);
		log.trace("descripcion: {}", descripcion);
		log.trace("tipo: {}", tipo);
		log.trace("variacion: {}", variacion);
		log.trace("variacion_divisa: {}", variacion_divisa);
		log.trace("saldo: {}", saldo);
		log.trace("saldo_divisa: {}", saldo_divisa);
		log.trace("id_orden: {}", id_orden);
		log.trace("");

		/*
		 * TODO: cargar movimentos de cuenta y ajustar coste total de transacciones
		 * (ejemplo: Impuesto de transacción Frances)
		 */
	}
}
