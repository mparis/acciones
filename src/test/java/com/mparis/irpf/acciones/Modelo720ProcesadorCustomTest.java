package com.mparis.irpf.acciones;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.cargadores.DegiroCargador;
import com.mparis.acciones.cargadores.GenericoTransaccionesCargador;
import com.mparis.acciones.datos.Modelo720Resultado;
import com.mparis.acciones.datos.TransaccionesSet;
import com.mparis.acciones.procesadores.Modelo720Procesador;

public class Modelo720ProcesadorCustomTest {

	private static final Logger log = LoggerFactory.getLogger(Modelo720ProcesadorCustomTest.class);

	@Test
	public void test() throws Exception {
		final String degiroTransaccionesFilePath = "...";
		final String degiroCuentaFilePath = "...";
		final String transactinsEtradeFilePath = "...";

		TransaccionesSet transaccionesSet = new TransaccionesSet();
		DegiroCargador.cargar(transaccionesSet, degiroTransaccionesFilePath, degiroCuentaFilePath);
		GenericoTransaccionesCargador.cargar(transaccionesSet, transactinsEtradeFilePath);

		log.info("Num valores: {}", transaccionesSet.getNumValores());
		log.info("<ISIN, Producto>: {}", transaccionesSet.getIsinProductoMap());

		Modelo720Resultado resultado = Modelo720Procesador.procesar(transaccionesSet, 2018, 2019);
		log.info("MODELO 720:\n{}", resultado);
	}

}
