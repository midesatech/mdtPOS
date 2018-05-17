package com.openbravo.pos.inventory;

import java.io.Serializable;

import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;

public class SubCentroCostoInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed {
	    private String m_sID;
	    private String m_sName;
	    private Boolean m_sEstado;
	    private String m_IdCentroCosto;
	    
	    /** Creates new CategoryInfo */
	    public SubCentroCostoInfo() {
	        m_sID = null;
	        m_sName = null;
	        m_sEstado = false;
	        m_IdCentroCosto = null;
	    }
	    
	    public SubCentroCostoInfo(String sID, String sName, Boolean sEstado, String sCentroCosto) {
	        m_sID = sID;
	        m_sName = sName;     
	        m_sEstado = sEstado;
	        m_IdCentroCosto = sCentroCosto;
	    }
	    
	    public Object getKey() {
	        return m_sID;
	    }
	    public void readValues(DataRead dr) throws BasicException {
	        m_sID = dr.getString(1);
	        m_sName = dr.getString(2);
	        m_sEstado = dr.getBoolean(3);
	        m_IdCentroCosto =dr.getString(4);
	    }
	    
	    public void writeValues(DataWrite dp) throws BasicException {
	        dp.setString(1, m_sID);
	        dp.setString(2, m_sName);  
	        dp.setBoolean(3, m_sEstado);
	        dp.setString(4, m_IdCentroCosto);
	    }
	    
	    public void setID(String sID) {
	        m_sID = sID;
	    }
	    
	    public String getID() {
	        return m_sID;
	    }

	    public String getName() {
	        return m_sName;
	    }
	    
	    public void setName(String sName) {
	        m_sName = sName;
	    }
	    
	    public void setEstado(Boolean sEstado){
	    	 m_sEstado=sEstado;
	    }
	    
	    public Boolean getEstado()
	    {
	    	return m_sEstado;
	    }
	    
	    public String getIdCentroCosto() {
	        return m_IdCentroCosto;
	    }
	    
	    public void setIdCentroCosto(String sName) {
	    	m_IdCentroCosto = sName;
	    }
	   
	   
	    @Override
	    public String toString(){
	        return m_sName;
	    }
}
