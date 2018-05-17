package com.openbravo.pos.customers;

import java.io.Serializable;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

public class CustomerInfoList implements SerializableRead, SerializableWrite, Serializable, IKeyed {
    private String m_sId;
    private String m_sTaxId;
    private String m_sName;
    private String m_sCard;
    
    /** Creates new CategoryInfo */
    public CustomerInfoList() {
        m_sId = null;
        m_sName = null;
        m_sTaxId = null;
        m_sCard = null;
    }
    
    public CustomerInfoList(String sId, String sTaxId, String sName/*, String sCard*/) {
        m_sId = sId;
        m_sName = sName;     
        m_sTaxId = sTaxId;   
       // m_sCard = sCard;
    }
    
    public Object getKey() {
        return m_sId;
    }
    public void readValues(DataRead dr) throws BasicException {
        m_sId = dr.getString(1);
        m_sName = dr.getString(3);
        m_sTaxId =dr.getString(2);
       // m_sCard =dr.getString(4);
    }
    
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, m_sId);
        dp.setString(2, m_sTaxId);  
        dp.setString(3, m_sName);
       // dp.setString(4, m_sCard);
    }
    
    public void setId(String sID) {
        m_sId = sID;
    }
    
    public String getID() {
        return m_sId;
    }

    public String getName() {
        return m_sName;
    }
    
    public void setName(String sName) {
        m_sName = sName;
    }
    
    public void setTaxId(String sTaxId){
    	m_sTaxId=sTaxId;
    }
    
    public String getTaxId()
    {
    	return m_sTaxId;
    }
    
    public String getCard() {
        return m_sCard;
    }
    
    public void setCard(String sCard) {
    	m_sCard = sCard;
    }
   
   
    @Override
    public String toString(){
        return m_sName;
    }
}
