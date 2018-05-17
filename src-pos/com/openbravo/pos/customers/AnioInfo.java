package com.openbravo.pos.customers;


public class AnioInfo {

	private int anio;
	
	public AnioInfo(int anio)
	{
		this.setAnio(anio);
	}
	
	public AnioInfo()
	{
		
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
	
	public String toString()
    {
        return String.format("%04d",this.anio);
    }
	
	
	
}
