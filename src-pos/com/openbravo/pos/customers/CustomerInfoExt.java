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

import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.format.Formats;


import java.util.Date;

/**
 *
 * @author adrianromero
 */
public class CustomerInfoExt {
    
    private String id;
    private String taxid;
    private String name;
    
    private String card;
    private String address;
    private String notes;
    private Double maxdebt;
    private boolean visible;
    private Date curdate;
    private Double curdebt;
    
    private boolean sobrecupo; /*15/12/2014 JDP*/
	private String tipocuenta; /*17/12/2014 JDP*/
	
	private String corele;     /*18/12/2014 JDP*/
	private String celcon;
	private String nomcon;
	private Double cupartesp;
	private Double concupcli;
	private Double conartesp;
	
	private boolean aplicaiva;  /* 10/07/2017 JDP001 */
  

    
    /** Creates a new instance of UserInfoBasic */
    public CustomerInfoExt(String id, String taxid, String name) {
        this.id = id;
        this.taxid = taxid;
        this.name = name;    
    }
    
    public CustomerInfo getCustomerInfo() {
        return new CustomerInfo(id, taxid, name);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getMaxdebt() {
        return maxdebt;
    }

    public void setMaxdebt(Double maxdebt) {
        this.maxdebt = maxdebt;
    }

    public Date getCurdate() {
        return curdate;
    }

    public void setCurdate(Date curdate) {
        this.curdate = curdate;
    }

    public Double getCurdebt() {
        return curdebt;
    }

    public void setCurdebt(Double curdebt) {
        this.curdebt = curdebt;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public boolean isVisible() {
        return visible;
    }
    
    /*15/12/2014 JDP*/
    public boolean isSobreCupo() {
        return sobrecupo;
    }
    public void setSobreCupo(boolean sobrecupo) {
        this.sobrecupo = sobrecupo;
    }
	
	/*17/12/2014 JDP*/
    public String getTipoCuenta() {
        return tipocuenta;
    }

    public void setTipoCuenta(String tipocuenta) {
        this.tipocuenta = tipocuenta;
    }
	
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /*18/12/2014 JDP*/
    public String getCorEle() {
        return corele;
    }

    public void setCorEle(String corele) {
        this.corele = corele;
    }

    public String getCelCon() {
        return celcon;
    }

    public void setCelCon(String celcon) {
        this.celcon = celcon;
    }
    
    public String getNomCon() {
        return nomcon;
    }

    public void setNomCon(String nomcon) {
        this.nomcon = nomcon;
    }
    
    public Double getCupArtEsp() {
        return cupartesp;
    }

    public void setCupArtEsp(Double cupartesp) {
        this.cupartesp = cupartesp;
    }
    
    public Double getConCupCli() {
        return concupcli;
    }

    public void setConCupCli(Double concupcli) {
        this.concupcli = concupcli;
    }
    
    public Double getConArtEsp() {
        return conartesp;
    }

    public void setConArtEsp(Double conartesp) {
        this.conartesp = conartesp;
    }
    
   
    

    
    public String printCurDebt() {
        
        return Formats.CURRENCY.formatValue(curdebt == null ? new Double(0.0) : curdebt);
    }
    
    //JDP001
    public void setAplicaIva(boolean visible) {
        this.aplicaiva = visible;
    }
    
    public boolean isAplicaIva() {
        return this.aplicaiva;
    }
    
}
