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

package com.openbravo.pos.ticket;

import com.openbravo.pos.util.StringUtils;
import java.io.Serializable;

/**
 *
 * @author adrianromero
 */
public class TicketProductInfo implements Serializable {
    
    private String id;
    private String name;
    private boolean com;
    private boolean artesp;   /* 18/12/2014 JDP */
    private boolean nomosiva; /* 16/08/2017 JDP */
    
    public TicketProductInfo(String id, String name, boolean com, boolean artesp, boolean noMosIva) {
        this.id = id;
        this.name = name;
        this.com = com;
        this.artesp = artesp;
        this.nomosiva = noMosIva;
    } 
    
    public TicketProductInfo(String name) {
        this(null, name, false, false, false);
    }
    
    public TicketProductInfo() {
        this(null, null, false, false, false);
    }
    
    public TicketProductInfo(ProductInfoExt product) {
        this(product.getID(), product.getName(), product.isCom(), product.isArtEsp(), product.noMosIva());
    }
    
    public TicketProductInfo copyTicketProduct() {
        TicketProductInfo p = new TicketProductInfo();
        p.id = id;
        p.name = name;
        p.com = com;
        p.artesp = artesp;
        p.nomosiva = nomosiva;
        return p;        
    }
    
    public String getId() {
        return id;
    }    
    
    public String getName() {
        return name;
    }     
    
    public void setName(String value) {
        if (id == null) {
            name = value;
        }
    }
 
    public boolean isCom() {
        return com;
    }
    
    public void setCom(boolean value) {
        if (id == null) {
            com = value;
        }
    }    

    /*18/12/2014 JDP*/
    public boolean isArtEsp() {
        return artesp;
    }
    
    public void setArtEsp(boolean value) {
        if (id != null) {
            artesp = value;
        }
    }    
    
    public String printName() {
         return name == null ? "" : StringUtils.encodeXML(name);
    }  
    
    /* 16/08/2017 JDP*/
    public boolean noMosIva() {
        return nomosiva;
    }
    
    public void setNoMosIva(boolean value) {
        if (id != null) {
            nomosiva = value;
        }
    }    

}
