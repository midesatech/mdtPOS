package com.openbravo.pos.customers;

import java.io.Serializable;
import java.util.Date;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

public class RecargaCupoParamInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed{

	private String m_sID;
	private java.util.Date m_dDate= new Date();
    private Double m_dValor;
    private Boolean m_bActivo;
	
    public RecargaCupoParamInfo()
    {
    	this.setM_sID(null);
    	this.m_dDate= new Date();
    	this.setM_dValor(new Double(0.0));
	    this.setM_bActivo(false);
    }
    
    public RecargaCupoParamInfo(String sId, java.util.Date dDate, Double dValor, Boolean bActivo)
    {
    	this.setM_sID(sId);
    	this.m_dDate   = dDate;
    	this.setM_dValor(dValor);
	    this.setM_bActivo(bActivo);
    }
    
	@Override
	public Object getKey() {
		return this.getM_sID();
	}

	@Override
	public void writeValues(DataWrite dp) throws BasicException {
		dp.setString(1, this.getM_sID());
		dp.setTimestamp(2, this.m_dDate);
		dp.setDouble(3,  this.getM_dValor());
		dp.setBoolean(4, this.getM_bActivo());
	}

	@Override
	public void readValues(DataRead dr) throws BasicException {
       this.setM_sID(dr.getString(1));
       this.m_dDate = dr.getTimestamp(2);
       this.setM_dValor(dr.getDouble(3));
       this.setM_bActivo(dr.getBoolean(4));
	}
	
	
	@Override
    public String toString(){
		java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy");
        return df.format(m_dDate);
    }

	public String getM_sID() {
		return m_sID;
	}

	public void setM_sID(String m_sID) {
		this.m_sID = m_sID;
	}

	public Double getM_dValor() {
		return m_dValor;
	}

	public void setM_dValor(Double m_dValor) {
		this.m_dValor = m_dValor;
	}

	public Boolean getM_bActivo() {
		return m_bActivo;
	}

	public void setM_bActivo(Boolean m_bActivo) {
		this.m_bActivo = m_bActivo;
	}

}
