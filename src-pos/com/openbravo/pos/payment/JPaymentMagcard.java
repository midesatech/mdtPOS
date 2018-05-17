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

package com.openbravo.pos.payment;

import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.*;

/**
 *
 * @author  adrianromero
 */
public class JPaymentMagcard extends javax.swing.JPanel implements JPaymentInterface {
    
    private PaymentPanel m_cardpanel;
    private PaymentGateway m_paymentgateway;
    private JPaymentNotifier m_notifier;
    private String transaction;
    
    /* 12-09-2014 */
	public Boolean getAlcanza() {
		return true;
	}
	
	/* 16-09-2014 */
	public double getSaldoCupo()
	{
	  return 0.0;
	}
    
    
    
    /** Creates new form JPaymentMagcard */
    public JPaymentMagcard(AppView app, JPaymentNotifier notifier) {
        
        initComponents();   
        
        m_notifier = notifier;
        
        m_paymentgateway = PaymentGatewayFac.getPaymentGateway(app.getProperties());
        
        if (m_paymentgateway == null) {
            jlblMessage.setText(AppLocal.getIntString("message.nopaymentgateway"));            
        } else {           
            // Se van a poder efectuar pagos con tarjeta
            m_cardpanel = PaymentPanelFac.getPaymentPanel(app.getProperties().getProperty("payment.magcardreader"), notifier);
            add(m_cardpanel.getComponent(), BorderLayout.CENTER);
            jlblMessage.setText(null);
            // jlblMessage.setText(AppLocal.getIntString("message.nocardreader"));
        }
    }
    
    public void activate(CustomerInfoExt customerext, double dTotal) {
        
        transaction = StringUtils.getCardNumber(); // Integer.toString(m_ticket.getId());   

        if (m_cardpanel == null) {
            jlblMessage.setText(AppLocal.getIntString("message.nopaymentgateway"));  
            m_notifier.setStatus(false, false);
        } else {
            jlblMessage.setText(null);
            m_cardpanel.activate(transaction, dTotal); 
            // The cardpanel sets the status
        }
    }
    public PaymentInfo executePayment() {
        
        jlblMessage.setText(null);

        PaymentInfoMagcard payinfo = m_cardpanel.getPaymentInfoMagcard();
        
        m_paymentgateway.execute(payinfo);
        if (payinfo.isPaymentOK()) {
            return payinfo;
        } else {
            jlblMessage.setText(payinfo.getMessage());
            return null;
        }
    }  
    public Component getComponent() {
        return this;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jlblMessage = new javax.swing.JTextArea();

        setLayout(new java.awt.BorderLayout());

        jlblMessage.setBackground(javax.swing.UIManager.getDefaults().getColor("Label.background"));
        jlblMessage.setEditable(false);
        jlblMessage.setLineWrap(true);
        jlblMessage.setWrapStyleWord(true);
        jlblMessage.setFocusable(false);
        jlblMessage.setPreferredSize(new java.awt.Dimension(300, 72));
        jlblMessage.setRequestFocusEnabled(false);
        jPanel1.add(jlblMessage);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea jlblMessage;
    // End of variables declaration//GEN-END:variables
    
}
