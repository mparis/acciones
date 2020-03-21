package com.mparis.acciones.datos;

import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mparis.acciones.datos.BienDerecho720.Origen;

public class Modelo720Resultado {

	private static final Logger log = LoggerFactory.getLogger(Modelo720Resultado.class);

	private final Map<Integer, Map<String, AbstractQueue<BienDerecho720>>> bienesDerechosPorAño = new HashMap<Integer, Map<String, AbstractQueue<BienDerecho720>>>();

	public void addBienDerecho(BienDerecho720 bienDerecho, int año) {
		Map<String, AbstractQueue<BienDerecho720>> bienesDerechosPorIsin = bienesDerechosPorAño.get(año);
		if (bienesDerechosPorIsin == null) {
			bienesDerechosPorIsin = new HashMap<String, AbstractQueue<BienDerecho720>>();
			bienesDerechosPorAño.put(año, bienesDerechosPorIsin);
		}

		String isin = bienDerecho.getIsin();
		AbstractQueue<BienDerecho720> bienesDerechos = bienesDerechosPorIsin.get(isin);
		if (bienesDerechos == null) {
			bienesDerechos = new PriorityQueue<BienDerecho720>();
			bienesDerechosPorIsin.put(isin, bienesDerechos);
		}
		bienesDerechos.add(bienDerecho);
	}

	public int getNumTitulos(String isin, int año) {
		Map<String, AbstractQueue<BienDerecho720>> bienesDerechosPorIsin = bienesDerechosPorAño.get(año);
		if (bienesDerechosPorIsin == null) {
			return 0;
		}

		AbstractQueue<BienDerecho720> bienesDerechos = bienesDerechosPorIsin.get(isin);
		if (bienesDerechos == null) {
			return 0;
		}

		int numTitulos = 0;

		for (BienDerecho720 bienDerecho : bienesDerechos) {
			Origen origen = bienDerecho.getOrigen();
			if (origen == Origen.C) {
				continue;
			}

			numTitulos += bienDerecho.getNumValores();
		}

		return numTitulos;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		for (Map.Entry<Integer, Map<String, AbstractQueue<BienDerecho720>>> bienesDerechosPorAñoEntry : bienesDerechosPorAño
				.entrySet()) {
			Integer año = bienesDerechosPorAñoEntry.getKey();
			Map<String, AbstractQueue<BienDerecho720>> bienesDerechosPorIsin = bienesDerechosPorAñoEntry.getValue();
			Map<String, AbstractQueue<BienDerecho720>> bienesDerechosPorIsinSorted = sortMapByValue(
					bienesDerechosPorIsin);

			builder.append(año).append("\n");
			for (Map.Entry<String, AbstractQueue<BienDerecho720>> bienesDerechosPorIsinEntry : bienesDerechosPorIsinSorted
					.entrySet()) {
				AbstractQueue<BienDerecho720> bienesDerechos = bienesDerechosPorIsinEntry.getValue();

				for (BienDerecho720 bienDerecho : bienesDerechos) {
					builder.append("  ").append(bienDerecho).append("\n");
				}
			}
		}

		return builder.toString();
	}

	private static TreeMap<String, AbstractQueue<BienDerecho720>> sortMapByValue(
			Map<String, AbstractQueue<BienDerecho720>> map) {
		Comparator<String> comparator = new KeyComparator(map);
		TreeMap<String, AbstractQueue<BienDerecho720>> result = new TreeMap<String, AbstractQueue<BienDerecho720>>(
				comparator);
		result.putAll(map);

		return result;
	}

	static class KeyComparator implements Comparator<String> {
		Map<String, AbstractQueue<BienDerecho720>> map = new HashMap<String, AbstractQueue<BienDerecho720>>();

		public KeyComparator(Map<String, AbstractQueue<BienDerecho720>> map) {
			this.map.putAll(map);
		}

		@Override
		public int compare(String s1, String s2) {
			String producto1 = map.get(s1).peek().getProducto();
			String producto2 = map.get(s2).peek().getProducto();

			return producto1.compareTo(producto2);
		}
	}

}
