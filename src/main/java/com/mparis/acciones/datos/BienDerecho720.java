package com.mparis.acciones.datos;

public class BienDerecho720 implements Comparable<BienDerecho720> {

	public enum Origen {
		A, M, C;
	}

	private final String isin;
	private final String producto;
	private final Origen origen;
	private final int numValores;
	private float valoracion = 0;

	public BienDerecho720(String isin, String producto, Origen origen, int numValores) {
		this.isin = isin;
		this.producto = producto;
		this.origen = origen;
		this.numValores = numValores;
	}

	public String getIsin() {
		return isin;
	}

	public String getProducto() {
		return producto;
	}

	public Origen getOrigen() {
		return origen;
	}

	public int getNumValores() {
		return numValores;
	}

	public float getValoracion() {
		return valoracion;
	}

	public void setValoracion(float valoracion) {
		this.valoracion = valoracion;
	}

	@Override
	public String toString() {
		return "BienDerecho720 [isin=" + isin + ", producto=" + producto + ", origen=" + origen + ", numValores="
				+ numValores + ", valoracion=" + valoracion + "]";
	}

	@Override
	public int compareTo(BienDerecho720 other) {
		return this.producto.compareTo(other.producto);
	}

}
