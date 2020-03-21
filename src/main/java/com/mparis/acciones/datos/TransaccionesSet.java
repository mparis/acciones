package com.mparis.acciones.datos;

import java.util.AbstractQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.mparis.acciones.datos.Transaccion.Tipo;

public class TransaccionesSet {
	private final Map<String, AbstractQueue<Transaccion>> transaccionesPorIsin = new HashMap<String, AbstractQueue<Transaccion>>();
	private final Map<String, AbstractQueue<Transaccion>> comprasPorIsin = new HashMap<String, AbstractQueue<Transaccion>>();
	private final Map<String, AbstractQueue<Transaccion>> ventasPorIsin = new HashMap<String, AbstractQueue<Transaccion>>();
	private final Map<String, String> isinProductoMap = new HashMap<String, String>();

	public TransaccionesSet() {

	}

	public void addTransaccion(Transaccion transaccion) throws Exception {
		String isin = transaccion.getIsin();

		AbstractQueue<Transaccion> transacciones = transaccionesPorIsin.get(isin);
		if (transacciones == null) {
			transacciones = new PriorityQueue<Transaccion>();
			transaccionesPorIsin.put(isin, transacciones);
		}
		transacciones.add(transaccion);

		if (transaccion.getTipo() == Tipo.COMPRA) {
			AbstractQueue<Transaccion> compras = comprasPorIsin.get(isin);
			if (compras == null) {
				compras = new PriorityQueue<Transaccion>();
				comprasPorIsin.put(isin, compras);
			}
			compras.add(transaccion);
		}

		if (transaccion.getTipo() == Tipo.VENTA) {
			AbstractQueue<Transaccion> ventas = ventasPorIsin.get(isin);
			if (ventas == null) {
				ventas = new PriorityQueue<Transaccion>();
				ventasPorIsin.put(isin, ventas);
			}
			ventas.add(transaccion);
		}

		String producto = transaccion.getProducto();
		String productoPrev = isinProductoMap.get(isin);
		if (productoPrev == null) {
			isinProductoMap.put(isin, producto);
		} else {
			if (!producto.equals(productoPrev)) {
				if (!containsIgnoreCase(producto, "non tradeable")
						&& !containsIgnoreCase(productoPrev, "non tradeable")) {
					throw new Exception("ISIN asociado al producto '" + productoPrev
							+ "' no coincide con un producto previo '" + producto + "'");
				}
			}
		}
	}

	public Map<String, AbstractQueue<Transaccion>> getTransaccionesPorIsin() {
		return transaccionesPorIsin;
	}

	public Map<String, AbstractQueue<Transaccion>> getComprasPorIsin() {
		return comprasPorIsin;
	}

	public Map<String, AbstractQueue<Transaccion>> getVentasPorIsin() {
		return ventasPorIsin;
	}

	public int getNumValores() {
		return transaccionesPorIsin.size();
	}

	public Set<String> getIsins() {
		return transaccionesPorIsin.keySet();
	}

	public Map<String, String> getIsinProductoMap() {
		return isinProductoMap;
	}

	private static boolean containsIgnoreCase(String str, String subString) {
		return str.toLowerCase().contains(subString.toLowerCase());
	}
}
