//    Openbravo POS is a point of sales application designed for touch screens.
//    Copyright (C) 2007 Openbravo, S.L.
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

package com.openbravo.pos.payment;

public class PaymentInfoValeCompass extends PaymentInfo {
    
    private double m_dTotal;
   
    /** Creates a new instance of PaymentInfoFree */
    public PaymentInfoValeCompass(double dTotal) {
        m_dTotal = dTotal;
    }
    
    public PaymentInfo copyPayment(){
        return new PaymentInfoValeCompass(m_dTotal);
    }    
    public String getName() {
        return "valecompass";
    }   
    public double getTotal() {
        return m_dTotal;
    }       
}
