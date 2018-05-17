package com.openbravo.pos.inventory;


import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;

import java.io.Serializable;

public class CentroCostoInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed {
	   
	    private String m_sID;
	    private String m_sName;
	    private Boolean m_sEstado;
	    
	    /** Creates new CategoryInfo */
	    public CentroCostoInfo() {
	        m_sID = null;
	        m_sName = null;
	        m_sEstado = false;
	    }
	    
	    public CentroCostoInfo(String sID, String sName, Boolean sEstado) {
	        m_sID = sID;
	        m_sName = sName;     
	        m_sEstado = sEstado;
	    }
	    
	    public Object getKey() {
	        return m_sID;
	    }
	    public void readValues(DataRead dr) throws BasicException {
	        m_sID = dr.getString(1);
	        m_sName = dr.getString(2);
	        m_sEstado = dr.getBoolean(3);
	    }
	    
	    public void writeValues(DataWrite dp) throws BasicException {
	        dp.setString(1, m_sID);
	        dp.setString(2, m_sName);  
	        dp.setBoolean(3, m_sEstado);
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
	   
	   
	    @Override
	    public String toString(){
	        return m_sName;
	    }
	

}
