package com.openbravo.pos.customers;

public class DiaInfo {
    private int dia;
	
	public DiaInfo(int dia)
	{
		this.setdia(dia);
	}
	
	public DiaInfo()
	{
		
	}

	/**
	 * @return the dia
	 */
	public int getdia() {
		return dia;
	}

	/**
	 * @param dia the dia to set
	 */
	public void setdia(int dia) {
		this.dia = dia;
	}
	
	public String toString()
    {
        return String.format("%02d",this.dia);
    }
}
