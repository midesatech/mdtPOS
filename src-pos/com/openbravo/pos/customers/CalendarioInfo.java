package com.openbravo.pos.customers;

public class CalendarioInfo {

	private int anio;
	private int mes;
	private int dia;
	private double valor;
	private java.util.Date fecha;
	
	public CalendarioInfo(int anio, int mes, int dia, double valor)
	{
		this.setAnio(anio);
		this.setMes(mes);
		this.setDia(dia);
		this.setValor(valor);
		this.generaFecha();
	}
	
	public CalendarioInfo()
	{
		
	}
	
	public String toString()
    {
        return String.format("%02d",this.dia) + "/" 
               + String.format("%02d",this.mes) + "/" 
        	   + String.format("%04d",this.anio);
    }

	/**
	 * @return the anio
	 */
	public int getAnio() {
		return anio;
	}

	/**
	 * @param anio the anio to set
	 */
	public void setAnio(int anio) {
		this.anio = anio;
	}

	/**
	 * @return the mes
	 */
	public int getMes() {
		return mes;
	}

	/**
	 * @param mes the mes to set
	 */
	public void setMes(int mes) {
		this.mes = mes;
	}

	/**
	 * @return the dia
	 */
	public int getDia() {
		return dia;
	}

	/**
	 * @param dia the dia to set
	 */
	public void setDia(int dia) {
		this.dia = dia;
	}

	/**
	 * @return the valor
	 */
	public double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}

	/**
	 * @return the fecha
	 */
	public java.util.Date getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}
	
	
	protected void generaFecha()
	{
		java.util.Calendar calendario = java.util.Calendar.getInstance();
		calendario.set(anio, mes, dia,0,0,0);
		this.setFecha(calendario.getTime());		
		
		
	}
	
	
	
}
