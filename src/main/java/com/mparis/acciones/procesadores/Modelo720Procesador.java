package com.mparis.acciones.procesadores;

import java.util.AbstractQueue;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.datos.BienDerecho720;
import com.mparis.acciones.datos.BienDerecho720.Origen;
import com.mparis.acciones.datos.Modelo720Resultado;
import com.mparis.acciones.datos.Transaccion;
import com.mparis.acciones.datos.TransaccionesSet;

public class Modelo720Procesador {

	private static final Logger log = LoggerFactory.getLogger(Modelo720Procesador.class);

	public static Modelo720Resultado procesar(TransaccionesSet transaccionesSet, int añoPrimeraDeclaracion,
			int añoFinal) throws Exception {
		if (añoPrimeraDeclaracion > añoFinal) {
			throw new Exception("añoPrimeraDeclaracion debe ser menor o igual que añoFinal");
		}

		Modelo720Resultado resultado = new Modelo720Resultado();

		Map<String, String> isinProductoMap = transaccionesSet.getIsinProductoMap();

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.DECEMBER);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);

		for (int año = añoPrimeraDeclaracion; año <= añoFinal; año++) {
			cal.set(Calendar.YEAR, año);
			Date fechaHasta = cal.getTime();

			for (Map.Entry<String, String> entry : isinProductoMap.entrySet()) {
				String isin = entry.getKey();
				String producto = entry.getValue();
				procesarProducto(resultado, transaccionesSet, isin, producto, fechaHasta);
			}
		}

		return resultado;
	}

	private static void procesarProducto(Modelo720Resultado resultado, TransaccionesSet transaccionesSet, String isin,
			String producto, Date fechaHasta) throws Exception {
		Map<String, AbstractQueue<Transaccion>> comprasPorIsin = transaccionesSet.getComprasPorIsin();
		Map<String, AbstractQueue<Transaccion>> ventasPorIsin = transaccionesSet.getVentasPorIsin();

		AbstractQueue<Transaccion> compras = comprasPorIsin.get(isin);
		if (compras == null) {
			throw new Exception("Al menos debe haber una compra");
		}

		Iterator<Transaccion> comprasIt = compras.iterator();

		AbstractQueue<Transaccion> ventas = ventasPorIsin.get(isin);
		Iterator<Transaccion> ventasIt = null;
		if (ventas != null) {
			ventasIt = ventas.iterator();
		}

		int titulosVentaExcedente = procesarTitulosAnteriores(resultado, isin, producto, fechaHasta, ventasIt);
		procesarTitulosNuevos(resultado, isin, producto, fechaHasta, comprasIt, ventasIt, titulosVentaExcedente);
	}

	private static int procesarTitulosAnteriores(Modelo720Resultado resultado, String isin, String producto,
			Date fechaHasta, Iterator<Transaccion> ventasIt) throws Exception {
		int año = getAño(fechaHasta);
		int titulosAnteriores = resultado.getNumTitulos(isin, año - 1);
		if (titulosAnteriores == 0) {
			return 0;
		}

		int titulosVentaExcedente = 0;
		int titulosM = titulosAnteriores;
		int titulosC = 0;
		float valoracionC = 0;

		if (ventasIt != null) {
			while (ventasIt.hasNext() && titulosM > 0) {
				Transaccion venta = ventasIt.next();
				Date fecha = venta.getFecha();
				if (fecha.compareTo(fechaHasta) > 0) {
					break;
				}

				int titulosVenta = venta.getNumTitulos();
				float total = venta.getTotal();

				if (titulosVenta > titulosM) {
					titulosC -= titulosM;
					valoracionC += total / titulosVenta * titulosM;
					titulosVentaExcedente += titulosVenta - titulosM;
					titulosM = 0;
				} else {
					titulosC -= titulosVenta;
					valoracionC += total;
					titulosM += titulosVenta;
				}

				ventasIt.remove();
			}
		}

		if (titulosM < 0) {
			throw new Exception("Numero de titulos M no puede ser negativo.");
		}
		if (titulosM > 0) {
			BienDerecho720 bienDerecho = new BienDerecho720(isin, producto, Origen.M, titulosM);
			resultado.addBienDerecho(bienDerecho, año);
		}

		if (titulosC < 0) {
			throw new Exception("Numero de titulos C no puede ser negativo.");
		}
		if (titulosC > 0) {
			BienDerecho720 bienDerecho = new BienDerecho720(isin, producto, Origen.C, titulosC);
			bienDerecho.setValoracion(valoracionC);
			resultado.addBienDerecho(bienDerecho, año);
		}

		return titulosVentaExcedente;
	}

	private static void procesarTitulosNuevos(Modelo720Resultado resultado, String isin, String producto,
			Date fechaHasta, Iterator<Transaccion> comprasIt, Iterator<Transaccion> ventasIt, int titulosVentaExcedente)
			throws Exception {
		int titulosCompraTotal = 0;

		while (comprasIt.hasNext()) {
			Transaccion compra = comprasIt.next();
			Date fecha = compra.getFecha();
			if (fecha.compareTo(fechaHasta) <= 0) {
				titulosCompraTotal += compra.getNumTitulos();
			} else {
				break;
			}

			comprasIt.remove();
		}

		int titulosVentaTotal = titulosVentaExcedente;
		if (ventasIt != null) {
			while (ventasIt.hasNext()) {
				Transaccion venta = ventasIt.next();
				Date fecha = venta.getFecha();
				if (fecha.compareTo(fechaHasta) <= 0) {
					titulosVentaTotal += venta.getNumTitulos();
				} else {
					break;
				}

				ventasIt.remove();
			}
		}

		int titulosTotal = titulosCompraTotal + titulosVentaTotal;
		if (titulosTotal < 0) {
			throw new Exception("No puede haber mas titulos vendidos que comprados.");
		}
		if (titulosTotal > 0) {
			BienDerecho720 bienDerecho = new BienDerecho720(isin, producto, Origen.A, titulosTotal);
			int año = getAño(fechaHasta);
			resultado.addBienDerecho(bienDerecho, año);
		}
	}

	private static int getAño(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);

		return calendar.get(Calendar.YEAR);
	}

}
