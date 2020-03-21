package com.mparis.irpf.acciones;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.cargadores.GenericoTransaccionesCargador;
import com.mparis.acciones.datos.Modelo720Resultado;
import com.mparis.acciones.datos.TransaccionesSet;
import com.mparis.acciones.procesadores.Modelo720Procesador;

public class Modelo720ProcesadorTest {

	private static final Logger log = LoggerFactory.getLogger(Modelo720ProcesadorTest.class);

	@Test
	public void simpleTest() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		String transaccionesPath = classLoader.getResource("transacciones-genericas-001.csv").getPath();
		TransaccionesSet transaccionesSet = new TransaccionesSet();
		GenericoTransaccionesCargador.cargar(transaccionesSet, transaccionesPath);

		Modelo720Resultado resultado = Modelo720Procesador.procesar(transaccionesSet, 2018, 2022);
		log.debug("MODELO 720:\n{}", resultado);
	}

}
