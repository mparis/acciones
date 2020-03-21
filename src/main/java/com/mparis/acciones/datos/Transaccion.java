package com.mparis.acciones.datos;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaccion implements Comparable<Transaccion> {

	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");

	public enum Tipo {
		COMPRA, VENTA;
	}

	private final Tipo tipo;
	private final boolean extranjero;
	private final Date fecha;
	private final String producto;
	private final String isin;
	private final int numTitulos;
	private final float precio;
	private final float total;

	public Tipo getTipo() {
		return tipo;
	}

	public boolean isExtranjero() {
		return extranjero;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getProducto() {
		return producto;
	}

	public String getIsin() {
		return isin;
	}

	public int getNumTitulos() {
		return numTitulos;
	}

	public float getPrecio() {
		return precio;
	}

	public float getTotal() {
		return total;
	}

	private Transaccion(Builder b) {
		this.tipo = b.tipo;
		this.extranjero = b.extranjero;
		this.fecha = b.fecha;
		this.producto = b.producto;
		this.isin = b.isin;
		this.numTitulos = b.numTitulos;
		this.precio = b.precio;
		this.total = b.total;
	}

	@Override
	public int compareTo(Transaccion other) {
		return this.fecha.compareTo(other.fecha);
	}

	@Override
	public String toString() {
		return "Transaccion [tipo=" + tipo + ", extranjero=" + extranjero + ", fecha=" + TIME_FORMAT.format(fecha)
				+ ", producto=" + producto + ", isin=" + isin + ", numTitulos=" + numTitulos + ", precio=" + precio
				+ ", total=" + total + "]";
	}

	public static class Builder {

		private Tipo tipo = Tipo.COMPRA;
		private boolean extranjero;
		private Date fecha;
		private String producto;
		private String isin;
		private int numTitulos;
		private float precio;
		private float total;

		public Builder() {
		}

		public Builder extranjero(boolean extranjero) {
			this.extranjero = extranjero;
			return this;
		}

		public Builder fecha(Date fecha) {
			this.fecha = fecha;
			return this;
		}

		public Builder producto(String producto) {
			this.producto = producto;
			return this;
		}

		public Builder isin(String isin) {
			this.isin = isin;
			return this;
		}

		public Builder numTitulos(int numTitulos) throws Exception {
			if (numTitulos == 0) {
				throw new Exception("Numero titulos no puede ser 0");
			}

			if (numTitulos < 0) {
				tipo = Tipo.VENTA;
			}

			this.numTitulos = numTitulos;
			return this;
		}

		public Builder precio(float precio) {
			this.precio = precio;
			return this;
		}

		public Builder total(float total) {
			this.total = total;
			return this;
		}

		public Transaccion build() {
			return new Transaccion(this);
		}

	}

}
