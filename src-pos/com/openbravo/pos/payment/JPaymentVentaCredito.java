/* **************************************************************************************************
 * DATE        | TAG     | AUTHOR            | DESCRIPTION
 * **************************************************************************************************
 * 07/07/2016    D001      JOSE DE PAZ         Creation
 * 
 * 
 * 
 */


package com.openbravo.pos.payment;

import java.awt.Component;

import com.openbravo.pos.customers.CustomerInfoExt;
import com.openbravo.pos.forms.AppLocal;

public class JPaymentVentaCredito extends javax.swing.JPanel implements JPaymentInterface {
	
    private double m_dTotal;
    private JPaymentNotifier m_notifier;

	/**
	 * 
	 */
	private static final long serialVersionUID = -728278370482710074L;

	
	public JPaymentVentaCredito(JPaymentNotifier notifier) {
        this.m_notifier = notifier;
        initComponents();
    }
	
	private void initComponents() {
        jLabel1 = new javax.swing.JLabel();

        jLabel1.setText(AppLocal.getIntString("message.paymentventacredito"));
        add(jLabel1);

    }
	
	@Override
	public void activate(CustomerInfoExt customerext, double dTotal) {
		this.m_dTotal = dTotal;
		this.m_notifier.setStatus(true, true);
		
	}

	@Override
	public PaymentInfo executePayment() {
		return new PaymentInfoVentaCredito(m_dTotal);
	}

	@Override
	public Component getComponent() {
		 return this;
	}

	@Override
	public Boolean getAlcanza() {
		
		return true;
	}

	@Override
	public double getSaldoCupo() {
		
		return 0.0;
	}

	private javax.swing.JLabel jLabel1;
}
