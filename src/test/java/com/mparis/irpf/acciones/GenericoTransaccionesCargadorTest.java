package com.mparis.irpf.acciones;

import static org.junit.Assert.assertEquals;

import java.util.AbstractQueue;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.cargadores.GenericoTransaccionesCargador;
import com.mparis.acciones.datos.Transaccion;
import com.mparis.acciones.datos.TransaccionesSet;

public class GenericoTransaccionesCargadorTest {

	private static final Logger log = LoggerFactory.getLogger(GenericoTransaccionesCargadorTest.class);

	@Test
	public void test1Empresa() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		String transaccionesPath = classLoader.getResource("transacciones-genericas-001.csv").getPath();
		TransaccionesSet transaccionesSet = new TransaccionesSet();
		GenericoTransaccionesCargador.cargar(transaccionesSet, transaccionesPath);

		assertEquals(1, transaccionesSet.getNumValores());

		Map<String, String> isinProductoMap = transaccionesSet.getIsinProductoMap();
		log.debug("isinProductoMap: {}", isinProductoMap);
		assertEquals(1, isinProductoMap.size());
		String empresa = isinProductoMap.get("ISIN-A");
		assertEquals("EMPRESA A", empresa);

		Map<String, AbstractQueue<Transaccion>> comprasPorIsin = transaccionesSet.getComprasPorIsin();
		log.debug("comprasPorIsin: {}", comprasPorIsin);
		assertEquals(1, comprasPorIsin.size());
		AbstractQueue<Transaccion> compras = comprasPorIsin.get("ISIN-A");
		log.debug("compras: {}", compras);
		assertEquals(4, compras.size());

		Map<String, AbstractQueue<Transaccion>> ventasPorIsin = transaccionesSet.getVentasPorIsin();
		log.debug("ventasPorIsin: {}", ventasPorIsin);
		assertEquals(1, ventasPorIsin.size());
		AbstractQueue<Transaccion> ventas = ventasPorIsin.get("ISIN-A");
		log.debug("ventas: {}", ventas);
		assertEquals(4, ventas.size());
	}

	@Test
	public void test2Empresas() throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		String transaccionesPath = classLoader.getResource("transacciones-genericas-002.csv").getPath();
		TransaccionesSet transaccionesSet = new TransaccionesSet();
		GenericoTransaccionesCargador.cargar(transaccionesSet, transaccionesPath);

		assertEquals(2, transaccionesSet.getNumValores());

		Map<String, String> isinProductoMap = transaccionesSet.getIsinProductoMap();
		assertEquals(2, isinProductoMap.size());
		String empresa = isinProductoMap.get("ISIN-A");
		assertEquals("EMPRESA A", empresa);
		empresa = isinProductoMap.get("ISIN-B");
		assertEquals("EMPRESA B", empresa);

		Map<String, AbstractQueue<Transaccion>> comprasPorIsin = transaccionesSet.getComprasPorIsin();
		assertEquals(2, comprasPorIsin.size());
		AbstractQueue<Transaccion> compras = comprasPorIsin.get("ISIN-A");
		assertEquals(4, compras.size());
		compras = comprasPorIsin.get("ISIN-B");
		assertEquals(1, compras.size());

		Map<String, AbstractQueue<Transaccion>> ventasPorIsin = transaccionesSet.getVentasPorIsin();
		assertEquals(1, ventasPorIsin.size());
		AbstractQueue<Transaccion> ventas = ventasPorIsin.get("ISIN-A");
		assertEquals(4, ventas.size());
	}

}
