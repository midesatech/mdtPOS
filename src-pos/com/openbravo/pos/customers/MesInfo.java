package com.openbravo.pos.customers;

public class MesInfo {

private int mes;
private String MES;
	
	public MesInfo(int mes)
	{
		this.setMes(mes);
		this.setMES(this.getMonthForInt(mes));
	}
	
	public MesInfo()
	{
		
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
	
	public String toString()
    {
        //return String.format("%02d",this.mes);
        return MES;
    }

	/**
	 * @return the mES
	 */
	public String getMES() {
		return MES;
	}

	/**
	 * @param mES the mES to set
	 */
	public void setMES(String mES) {
		MES = mES;
	}
	
	
	private String getMonthForInt(int num) {
        String month = "";
        java.text.DateFormatSymbols dfs = 
        		new java.text.DateFormatSymbols(new java.util.Locale("es" , "ES"));
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }
	
	
	
}
