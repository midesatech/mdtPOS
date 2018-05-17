/* **************************************************************************************************
 * DATE        | TAG     | AUTHOR            | DESCRIPTION
 * **************************************************************************************************
 * 07/07/2016    D001      JOSE DE PAZ         Creation
 * 
 * 
 * 
 */

package com.openbravo.pos.payment;

public class PaymentInfoVentaCredito extends PaymentInfo {

	private double m_dTotal;

	public PaymentInfoVentaCredito(double dTotal) {
		this.m_dTotal = dTotal;
	}

	@Override
	public String getName() {
		return "ventacredito";
	}

	@Override
	public double getTotal() {
		  return this.m_dTotal;
	}

	@Override
	public PaymentInfo copyPayment() {
		return new PaymentInfoVentaCredito(m_dTotal);
	}

}
