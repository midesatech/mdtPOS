package com.openbravo.pos.inventory;


/*17/12/2014 JDP*/

import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.SerializableWrite;
import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.IKeyed;

import java.io.Serializable;

public class TipoCuentaInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed {
	   
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		private String m_sID;
	    private String m_sName;

	    
	    /** Creates new CategoryInfo */
	    public TipoCuentaInfo() {
	        m_sID = null;
	        m_sName = null;

	    }
	    
	    public TipoCuentaInfo(String sID, String sName) {
	        m_sID = sID;
	        m_sName = sName;     

	    }
	    
	    public Object getKey() {
	        return m_sID;
	    }
	    public void readValues(DataRead dr) throws BasicException {
	        m_sID = dr.getString(1);
	        m_sName = dr.getString(2);

	    }
	    
	    public void writeValues(DataWrite dp) throws BasicException {
	        dp.setString(1, m_sID);
	        dp.setString(2, m_sName);  

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
	   
	    @Override
	    public String toString(){
	        return m_sName;
	    }

}
