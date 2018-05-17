package com.openbravo.pos.inventory;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;

public class TipoPagoRecargaInfo implements SerializableRead, IKeyed {
private String m_sID;
private String m_sName;

/** Creates a new instance of FloorsInfo */
public TipoPagoRecargaInfo() {
    m_sID = null;
    m_sName = null;
}

public Object getKey() {
    return m_sID;
}

public void readValues(DataRead dr) throws BasicException {
    m_sID = dr.getString(1);
    m_sName = dr.getString(2);
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

public String toString(){
    return m_sName;
}       
}