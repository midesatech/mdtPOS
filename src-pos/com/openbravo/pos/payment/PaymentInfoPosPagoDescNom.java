package com.openbravo.pos.payment;

public class PaymentInfoPosPagoDescNom extends PaymentInfo{
    
    private double m_dTotal;
   
    /** Creates a new instance of PaymentInfoFree */
    public PaymentInfoPosPagoDescNom(double dTotal) {
        m_dTotal = dTotal;
    }
    
    public PaymentInfo copyPayment(){
        return new PaymentInfoPosPagoDescNom(m_dTotal);
    }    
    public String getName() {
        return "postpagodesnom";
    }   
    public double getTotal() {
        return m_dTotal;
    }       
}
