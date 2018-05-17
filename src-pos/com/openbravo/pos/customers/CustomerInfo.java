//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007-2008 Openbravo, S.L.
//    http://sourceforge.net/projects/openbravopos
//
//    This program is free software; you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation; either version 2 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package com.openbravo.pos.customers;

import java.io.Serializable;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

/**
 *
 * @author adrianromero
 */
public class CustomerInfo implements Serializable, SerializableRead, SerializableWrite, IKeyed {
    
    private String id;
    private String taxid;
    private String name;
    
    /** Creates a new instance of UserInfoBasic */
    public CustomerInfo(String id, String taxid, String name) {
        this.id = id;
        this.taxid = taxid;
        this.name = name;    
    }
    
    public CustomerInfo() {
        this.id = null;
        this.taxid = null;
        this.name = null;    
    }
    
    public void readValues(DataRead dr) throws BasicException {
        id = dr.getString(1);
        name = dr.getString(3);
        taxid =dr.getString(2);
       // m_sCard =dr.getString(4);
    }
    
    public void writeValues(DataWrite dp) throws BasicException {
        dp.setString(1, id);
        dp.setString(2, taxid);  
        dp.setString(3, name);
       // dp.setString(4, m_sCard);
    }
    
    public String getId() {
        return id;
    }    
    
    public String getTaxid() {
        return taxid;
    }    
    
    public String getName() {
        return name;
    }   
    
    @Override
    public String toString() {
        return name;
    }
    
    public Object getKey() {
        return id;
    }
}

