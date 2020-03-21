package com.mparis.acciones.cargadores;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.datos.TransaccionesSet;

public class DegiroCargador {

	private static final Logger log = LoggerFactory.getLogger(DegiroCargador.class);

	public static void cargar(TransaccionesSet transaccionesSet, String transaccionesFilePath, String cuentaFilePath)
			throws Exception {
		DegiroTransaccionesCargador.cargar(transaccionesSet, transaccionesFilePath);
		DegiroCuentaCargador.cargar(transaccionesSet, cuentaFilePath);
	}

}
