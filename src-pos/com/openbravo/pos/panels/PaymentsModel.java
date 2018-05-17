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

/*  12/08/2015 */
//   Se modifica select para que sea solamente por app.getActiveCashIndex en el
//   RECEIPT.ID in

/*  01/10/2015 */
//   Se corrige para solucionar el problema de printCategoria.
// 

/*  15/06/2016 */
//   Ordenamiento de detalle de VENTA e IVA para reporte X y reporte Zeta.
//

/*  09/07/2016 JDP */
//   .- Grouping for VENTA CREDITO payments.
//   .- Show details (only units and total) VENTA CREDITO in new section 
//      CLOSED CASH report.     

/*  19/12/2016 JDP */
//   .- Solutions a bugs in details VENTAS DEFINITIVAS and DEVOLUCIONES APLICADAS.

/*  17/07/2017 JDP */
//   .- VENTAS CREDITO and VENTAS PREPAGO without VENTA NETA ant TOTAL IVA


/*  21/09/2017 JDP */
//   .- Impuesto Bolsa and refactoring loadInstance method



package com.openbravo.pos.panels;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.table.AbstractTableModel;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.*;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.util.StringUtils;

/**
 *
 * @author adrianromero
 */
public class PaymentsModel {

	private String m_sHost;
	private Date m_dDateStart;
	private Date m_dDateEnd;
	private Integer m_dConsecutivo; /* 20/06/2014 */
	/* 05/12/2014 JDP INI */
	private String m_sTipoCierre;
	private Integer m_iFctInicial;
	private Integer m_iFctFinal;
	private Double m_dPagoEfectivo;
	private java.util.List<ReporteXZ> m_LineasReporteXZ;
	private java.util.List<GrupoReporteXZ> m_ReporteXZ;
	private Double m_dSumTotalCat;
	private Double m_dSumTotalCatDev;
	/* 05/12/2014 JDP FIN */

	private Integer m_iPayments;
	private Double m_dPaymentsTotal;
	private java.util.List<PaymentsLine> m_lpayments;

	private final static String[] PAYMENTHEADERS = { "Label.Payment", "label.totalcash" };

	private Integer m_iSales;
	private Integer m_iCantFact;
	private Double m_dSalesSubtotal;
	private Double m_dSalesTotal;

	private java.util.List<SalesLine> m_lsales;

	private final static String[] SALEHEADERS = { "label.taxcash", "label.subtotalcash", "label.totalcash" };
	DecimalFormat df = new DecimalFormat("#.##");

	/* 09/07/2016 */
	private java.util.List<PaymentsLine> m_lpaymentsReportes;
	private Double m_dSalesSubtotalSVC;
	private Double m_dSalesSubtotalSVP;
	private Double m_dSalesTotalSVC;
	private Double m_dSalesTotalSVP;
	private Double m_dSalesTotalUnidadesVC;
	private Double m_dSalesTotalUnidadesVP;
	private Double m_dVentaCredito;
	private Double m_dVentaPrepago;
	private java.util.List<SalesLine> m_lsalesReportes;
	private java.util.List<SalesLine> m_lsalesSoloVentaCredito;
	private java.util.List<SalesLine> m_lsalesSoloVentaPrepago;
	
	/* 21/09/2017 */
	private Double m_dSalesSubtotalImpBolsa;
	private Double m_dSalesSubtotalImpBolsaNeg;
	private Double m_dSalesSubtotalImpBolsaVC;
	private Double m_dSalesSubtotalImpBolsaVP;
	

	private PaymentsModel() {

	}

	public static PaymentsModel emptyInstance() {

		PaymentsModel p = new PaymentsModel();

		p.m_iPayments = new Integer(0);
		p.m_dPaymentsTotal = new Double(0.000);
		p.m_lpayments = new ArrayList<PaymentsLine>();

		p.m_iSales = new Integer(0);
		p.m_dSalesSubtotal = new Double(0.000);
		p.m_dSalesTotal = new Double(0.000);
		p.m_lsales = new ArrayList<SalesLine>();

		/* 05/12/2014 JDP INI */
		p.m_iFctInicial = new Integer(0);
		p.m_iFctFinal = new Integer(0);
		p.m_dPagoEfectivo = new Double(0.000);
		p.m_LineasReporteXZ = new ArrayList<ReporteXZ>();
		p.m_ReporteXZ = new ArrayList<GrupoReporteXZ>();
		p.m_dSumTotalCat = new Double(0.000);
		p.m_dSumTotalCatDev = new Double(0.000);
		p.m_iCantFact = new Integer(0);
		/* 05/12/2014 JDP FIN */

		/* 09/07/2016 */
		p.m_dSalesSubtotalSVC = new Double(0.000);
		p.m_dSalesSubtotalSVP = new Double(0.000);
		p.m_dSalesTotalSVC = new Double(0.000);
		p.m_dSalesTotalSVP = new Double(0.000);
		p.m_dSalesTotalUnidadesVC = new Double(0.000);
		p.m_dSalesTotalUnidadesVP = new Double(0.000);
		p.m_dVentaCredito = new Double(0.000);
		p.m_dVentaPrepago = new Double(0.000);
		p.m_lpaymentsReportes = new ArrayList<PaymentsLine>();
		p.m_lsalesReportes = new ArrayList<SalesLine>();
		p.m_lsalesSoloVentaCredito = new ArrayList<SalesLine>();
		p.m_lsalesSoloVentaPrepago = new ArrayList<SalesLine>();

		/* 21/09/2017 */
		p.m_dSalesSubtotalImpBolsa = new Double(0.000);
		p.m_dSalesSubtotalImpBolsaNeg = new Double(0.000);
		p.m_dSalesSubtotalImpBolsaVC = new Double(0.000);
		p.m_dSalesSubtotalImpBolsaVP = new Double(0.000);
		
		return p;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static PaymentsModel loadInstance(AppView app) throws BasicException {
		DecimalFormat df = new DecimalFormat("#.##");
		PaymentsModel p = new PaymentsModel();

		// Propiedades globales
		p.m_sHost = app.getProperties().getHost();
		p.m_dDateStart = app.getActiveCashDateStart();
		p.m_dDateEnd = null;
		p.m_dConsecutivo = null; /* 20/06/2014 */
		
		EntidadApp appE= new EntidadApp();		
		appE.setActiveCashDateStart(app.getActiveCashDateStart());
		appE.setActiveCashDateEnd(null);
		appE.setSession(app.getSession());
		appE.setHost(app.getProperties().getHost());
		appE.setActiveCashIndex(app.getActiveCashIndex());
		
		
		return loadInstanace(p, appE);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static PaymentsModel loadInstance(EntidadApp app, int iConsecutivo) throws BasicException {
		DecimalFormat df = new DecimalFormat("#.##");
		PaymentsModel p = new PaymentsModel();

		// Propiedades globales
		p.m_sHost = app.getHost();
		p.m_dDateStart = app.getActiveCashDateStart();
		p.m_dDateEnd = app.getActiveCashDateEnd();
		p.m_dConsecutivo = iConsecutivo; /* 20/06/2014 */
		
		return loadInstanace(p, app);		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static PaymentsModel loadInstanace(PaymentsModel p, EntidadApp app) throws BasicException
	{
		p.m_ReporteXZ = new ArrayList<GrupoReporteXZ>();
		p.m_dSumTotalCat = new Double(0.000);
		p.m_dSumTotalCatDev = new Double(0.000);
		p.m_dPagoEfectivo = new Double(0.000); /* 25/02/2015 */

		/* 09/07/2016 */
		p.m_dVentaCredito = new Double(0.000);
		p.m_dVentaPrepago = new Double(0.000);
		p.m_dSalesSubtotalSVC = new Double(0.000);
		p.m_dSalesSubtotalSVP = new Double(0.000);
		p.m_dSalesTotalSVC = new Double(0.000);
		p.m_dSalesTotalSVP = new Double(0.000);
		p.m_dSalesTotalUnidadesVC = new Double(0.000);
		p.m_dSalesTotalUnidadesVP = new Double(0.000);
		
		/* 21/09/2017 */
		p.m_dSalesSubtotalImpBolsa = new Double(0.000);
		p.m_dSalesSubtotalImpBolsaNeg = new Double(0.000);
		p.m_dSalesSubtotalImpBolsaVC = new Double(0.000);
		p.m_dSalesSubtotalImpBolsaVP = new Double(0.000);

		// Pagos
		Object[] valtickets = (Object[]) new StaticSentence(app.getSession(),
				"SELECT COUNT(*), SUM(PAYMENTS.TOTAL) " + "FROM PAYMENTS, RECEIPTS "
						+ "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? ",
				SerializerWriteString.INSTANCE, new SerializerReadBasic(new Datas[] { Datas.INT, Datas.DOUBLE }))
						.find(app.getActiveCashIndex());

		if (valtickets == null) {
			p.m_iPayments = new Integer(0);
			p.m_dPaymentsTotal = new Double(0.000);
		} else {
			p.m_iPayments = (Integer) valtickets[0];
			p.m_dPaymentsTotal = (Double) valtickets[1];
		}

		List l = new StaticSentence(app.getSession(),
				"SELECT PAYMENTS.PAYMENT, SUM(PAYMENTS.TOTAL) " + "FROM PAYMENTS, RECEIPTS "
						+ "WHERE PAYMENTS.RECEIPT = RECEIPTS.ID AND RECEIPTS.MONEY = ? " + "GROUP BY PAYMENTS.PAYMENT",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.PaymentsLine.class))
						.list(app.getActiveCashIndex());

		if (l == null) {
			p.m_lpayments = new ArrayList();
			p.m_lpaymentsReportes = new ArrayList(); /* 09/07/2016 */
		} else {

			/* 05/12/2014 JDP */
			// p.m_lpayments = l;
			/* 09/07/2016 */
			p.m_lpayments = new ArrayList();
			PaymentsLine oPaymentLineAux = new PaymentsLine();
			PaymentsLine oPaymentLineAuxVP = new PaymentsLine();
			Iterator it = l.iterator();
			while (it.hasNext()) {
				PaymentsLine linea = (PaymentsLine) it.next();
				p.m_lpayments.add(linea);
				if (linea.m_PaymentType.trim().equals("cash")) {
					p.m_dPagoEfectivo = p.m_dPagoEfectivo + linea.m_PaymentValue;
				} else /* 25/02/2015 */
				{
					if (linea.m_PaymentType.trim().equals("cashrefund"))
						p.m_dPagoEfectivo = p.m_dPagoEfectivo + linea.m_PaymentValue;
					else {
						/* 09/07/2016 */
						if (linea.m_PaymentType.trim().equals("ventacredito")) {
							p.m_dVentaCredito = p.m_dVentaCredito + linea.m_PaymentValue;
							/* delete after to the list */
							oPaymentLineAux = linea;
						}
						else
						{
							if (linea.m_PaymentType.trim().equals("prepago")) {
							    p.m_dVentaPrepago = p.m_dVentaPrepago + linea.m_PaymentValue;
							    oPaymentLineAuxVP = linea;
							}
						}
					}
				}
			}
			/* 09/07/2016 */
			l.remove(oPaymentLineAux);
			l.remove(oPaymentLineAuxVP);
			p.m_lpaymentsReportes = l;

			/* 05/12/2014 JDP */
		}

		/// count(if(ccc_news_comments.id = 'approved', ccc_news_comments.id,
		/// NULL))
		// Ventas
		/* 12/08/2015 */
		Object[] recsales = (Object[]) new StaticSentence(app.getSession(),
				"SELECT COUNT(DISTINCT TICKETS.ID), " + "SUM(UNITS * PRICE), "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE)), "
						+ "MIN(if(TICKETS.TICKETID>0,if(TICKETS.ISDIAN=1,TICKETS.TICKETID,NULL),NULL)), MAX(if(TICKETS.TICKETID>0,if(TICKETS.ISDIAN=1,TICKETS.TICKETID,NULL),NULL)), "
						+ "COUNT(if(TICKETS.TICKETID>0,TICKETS.TICKETID,NULL)) "
						+ "FROM RECEIPTS, TICKETS, TICKETLINES, TAXES WHERE RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND TICKETLINES.TAXID = TAXES.ID "
						+ "AND RECEIPTS.MONEY = ? AND RECEIPTS.ID IN "
						+ "(SELECT PAYMENTS.RECEIPT FROM PAYMENTS LEFT JOIN RECEIPTS ON PAYMENTS.RECEIPT=RECEIPTS.ID WHERE RECEIPTS.MONEY = '"
						+ app.getActiveCashIndex() + "' )",
				SerializerWriteString.INSTANCE,
				new SerializerReadBasic(
						new Datas[] { Datas.INT, Datas.DOUBLE, Datas.DOUBLE, Datas.INT, Datas.INT, Datas.INT }))
								.find(app.getActiveCashIndex());
		if (recsales == null) {
			p.m_iSales = new Integer(0);
			p.m_dSalesSubtotal = new Double(0.000);
			p.m_dSalesTotal = new Double(0.000);
			p.m_iFctInicial = new Integer(0);
			p.m_iFctFinal = new Integer(0);
			p.m_iCantFact = new Integer(0);
		} else {
			p.m_iSales = (Integer) recsales[0];
			p.m_dSalesSubtotal = (Double) recsales[1];
			p.m_dSalesTotal = (Double) recsales[2];
			p.m_iFctInicial = (Integer) recsales[3];
			p.m_iFctFinal = (Integer) recsales[4];

			if ((p.m_iFctFinal != null) && (p.m_iFctInicial != null))
				p.m_iCantFact = (p.m_iFctFinal - p.m_iFctInicial) + 1;
			else
				p.m_iCantFact = 0;

		}

		/*---------------- VENTAS CREDITO ------------------*/

		List<ReporteXZ> LineasReporteXZNegVC = new StaticSentence(app.getSession(),
				"SELECT " + "CATEGORIES.NAME AS CATEGORIA, " 
		                + " TICKETLINES.NAME AS PRODUCTO, "
						+ "sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS,0)) AS UNIDAD,"
						+ "(TICKETLINES.PRICE * (1 + TAXES.RATE)) AS PRECIO, "
						+ "sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE),0)) AS TOTAL, "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "TICKETLINES.ATTRIBUTES "
						+ "FROM TICKETLINES LEFT JOIN PRODUCTS ON TICKETLINES.PRODUCT=PRODUCTS.ID "
						+ "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID "
						+ "LEFT JOIN CATEGORIES ON PRODUCTS.CATEGORY=CATEGORIES.ID " + "WHERE "
						+ "TICKET IN (SELECT ID FROM TICKETS WHERE ID IN "
						+ "(SELECT RECEIPTS.ID FROM RECEIPTS LEFT JOIN PAYMENTS " + "ON RECEIPTS.ID = PAYMENTS.RECEIPT "
						+ "WHERE PAYMENTS.PAYMENT = 'ventacredito' AND RECEIPTS.MONEY=?)) "
						+ "group by TICKETLINES.PRODUCT, PRECIO having UNIDAD<>0 ",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.ReporteXZ.class))
						.list(app.getActiveCashIndex());

		List<ReporteXZ> LineasReporteXZVC = new StaticSentence(app.getSession(),
				"SELECT " + "CATEGORIES.NAME AS CATEGORIA, "
		                + "TICKETLINES.NAME AS PRODUCTO, "
						+ "(sum(if(TICKETLINES.UNITS>0,TICKETLINES.UNITS,0))) AS UNIDAD, "
						+ "(TICKETLINES.PRICE * (1 + TAXES.RATE)) AS PRECIO, "
						+ "sum(if(TICKETLINES.UNITS>0,TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE),0)) AS TOTAL, "
						+ "sum(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "TICKETLINES.ATTRIBUTES "
						+ "FROM TICKETLINES LEFT JOIN PRODUCTS ON TICKETLINES.PRODUCT=PRODUCTS.ID "
						+ "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID "
						+ "LEFT JOIN CATEGORIES ON PRODUCTS.CATEGORY=CATEGORIES.ID " + "WHERE "
						+ "TICKET IN (SELECT ID FROM TICKETS WHERE ID IN "
						+ "(SELECT RECEIPTS.ID FROM RECEIPTS LEFT JOIN PAYMENTS " + "ON RECEIPTS.ID = PAYMENTS.RECEIPT "
						+ "WHERE PAYMENTS.PAYMENT = 'ventacredito' AND RECEIPTS.MONEY=?)) "
						+ "group by TICKETLINES.PRODUCT, PRECIO having UNIDAD<>0",

				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.ReporteXZ.class))
						.list(app.getActiveCashIndex());
		
		
		if (LineasReporteXZNegVC != null) {
			LineasReporteXZVC.addAll(LineasReporteXZNegVC);
		}

		List<SalesLine> asalesVC = new StaticSentence(app.getSession(),
				"SELECT TAXES.NAME, " + "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE)), " + "PAYMENTS.PAYMENT, "
						+ "SUM(TICKETLINES.UNITS), TICKETS.TICKETID, TICKETLINES.NAME "
						+ "FROM RECEIPTS, TICKETS, TICKETLINES, TAXES, PAYMENTS "
						+ "WHERE RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND TICKETLINES.TAXID = TAXES.ID "
						+ "AND PAYMENTS.RECEIPT = RECEIPTS.ID " + "AND RECEIPTS.MONEY = ? AND RECEIPTS.ID IN "
						+ "(SELECT PAYMENTS.RECEIPT FROM PAYMENTS LEFT JOIN RECEIPTS ON PAYMENTS.RECEIPT=RECEIPTS.ID "
						+ "WHERE PAYMENTS.PAYMENT = 'ventacredito' AND RECEIPTS.MONEY = '" + app.getActiveCashIndex()
						+ "' GROUP BY PAYMENTS.RECEIPT) "
						+ "GROUP BY TAXES.ID, TAXES.NAME, PAYMENTS.PAYMENT, TICKETLINES.TICKET, TICKETLINES.PRODUCT, TICKETS.TICKETID ORDER BY PAYMENTS.PAYMENT ASC",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.SalesLine.class))
						.list(app.getActiveCashIndex());

		/* 09/07/2016 */
		p.m_lsalesReportes = new ArrayList<SalesLine>();
		p.m_lsales = new ArrayList<SalesLine>();
		p.m_lsalesSoloVentaCredito = new ArrayList<SalesLine>();
		p.m_lsalesSoloVentaPrepago = new ArrayList<SalesLine>();
		
		if (asalesVC != null) {
			int iExisteAuxVC = 0;
			List<SalesLine> asalesAuxVC = new ArrayList<SalesLine>();
			for (SalesLine saleLine : asalesVC) {
				for (SalesLine saleLineAux : asalesAuxVC) {
					if (saleLine.getTicketId().equals(saleLineAux.getTicketId())) {
						iExisteAuxVC = 1;
						break;
					} else {
						if (saleLine.getTypePay().equals("ventacredito"))
							iExisteAuxVC = 0;
						else {
							iExisteAuxVC = 1;
							break;
						}
					}
				}
				if ((iExisteAuxVC == 0) && (saleLine.getTypePay().equals("ventacredito"))) {
					asalesAuxVC.add(saleLine);
				}
				iExisteAuxVC = 0;
			}
			
			SalesLine itemSaleLineVC = new SalesLine();			
			for(ReporteXZ lineaVC: LineasReporteXZVC)
			{
				
				/*if (lineaVC.getUnidad() < 0) {
					p.m_dSumTotalCatDev += (lineaVC.getTotal() * -1);
					lineaVC.m_Unidad *= -1;
					lineaVC.m_Total *= -1;
					lineaGrupoXZ1.m_LineasReporteXZDev.add(lineaXZ);
				} else {

					lineaGrupoXZ1.m_LineasReporteXZ.add(lineaXZ);
				}*/
				
				
				itemSaleLineVC = new SalesLine();
				itemSaleLineVC.m_SalesPayType = "";
				itemSaleLineVC.m_SalesSubtotal = lineaVC.getPrecio();
				itemSaleLineVC.m_SalesTaxes = "";
				itemSaleLineVC.m_SalesTotal = lineaVC.getTotal();
				itemSaleLineVC.m_Unidades = lineaVC.getUnidad();
                itemSaleLineVC.m_Producto = lineaVC.getProducto();
				p.m_lsalesSoloVentaCredito.add(itemSaleLineVC);
				p.m_dSalesSubtotalSVC += lineaVC.getSubTotal();
				p.m_dSalesTotalSVC += itemSaleLineVC.m_SalesTotal;
				p.m_dSalesTotalUnidadesVC += itemSaleLineVC.m_Unidades;
				
				
				
				
				
				/* Totalizar si es IMPUESTO */
				if (lineaVC.getCategoria().equals("IMPUESTO"))
				{
					p.m_dSalesSubtotalImpBolsaVC += lineaVC.getTotal();
				}
			}
		}
		
		

		/*---------------- FIN VENTAS CREDITO --------------*/
		
		
		/*---------------- VENTAS PREPAGO ------------------*/
		
		List<ReporteXZ> LineasReporteXZNegVP = new StaticSentence(app.getSession(),
				"SELECT " + "CATEGORIES.NAME AS CATEGORIA, " 
		                + " TICKETLINES.NAME AS PRODUCTO, "
						+ "sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS,0)) AS UNIDAD,"
						+ "(TICKETLINES.PRICE * (1 + TAXES.RATE)) AS PRECIO, "
						+ "sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE),0)) AS TOTAL, "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "TICKETLINES.ATTRIBUTES "
						+ "FROM TICKETLINES LEFT JOIN PRODUCTS ON TICKETLINES.PRODUCT=PRODUCTS.ID "
						+ "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID "
						+ "LEFT JOIN CATEGORIES ON PRODUCTS.CATEGORY=CATEGORIES.ID " + "WHERE "
						+ "TICKET IN (SELECT ID FROM TICKETS WHERE ID IN "
						+ "(SELECT RECEIPTS.ID FROM RECEIPTS LEFT JOIN PAYMENTS " + "ON RECEIPTS.ID = PAYMENTS.RECEIPT "
						+ "WHERE PAYMENTS.PAYMENT = 'prepago' AND RECEIPTS.MONEY=?)) "
						+ "group by TICKETLINES.PRODUCT, PRECIO having UNIDAD<>0 ",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.ReporteXZ.class))
						.list(app.getActiveCashIndex());
		
		List<SalesLine> asalesVP = new StaticSentence(app.getSession(),
				"SELECT TAXES.NAME, " + "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE)), " + "PAYMENTS.PAYMENT, "
						+ "SUM(TICKETLINES.UNITS), TICKETS.TICKETID, TICKETLINES.NAME "
						+ "FROM RECEIPTS, TICKETS, TICKETLINES, TAXES, PAYMENTS "
						+ "WHERE RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND TICKETLINES.TAXID = TAXES.ID "
						+ "AND PAYMENTS.RECEIPT = RECEIPTS.ID " + "AND RECEIPTS.MONEY = ? AND RECEIPTS.ID IN "
						+ "(SELECT PAYMENTS.RECEIPT FROM PAYMENTS LEFT JOIN RECEIPTS ON PAYMENTS.RECEIPT=RECEIPTS.ID "
						+ "WHERE PAYMENTS.PAYMENT = 'prepago' AND RECEIPTS.MONEY = '" + app.getActiveCashIndex()
						+ "' GROUP BY PAYMENTS.RECEIPT) "
						+ "GROUP BY TAXES.ID, TAXES.NAME, PAYMENTS.PAYMENT, TICKETLINES.TICKET, TICKETLINES.PRODUCT, TICKETS.TICKETID ORDER BY PAYMENTS.PAYMENT ASC",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.SalesLine.class))
						.list(app.getActiveCashIndex());
		
		List<ReporteXZ> LineasReporteXZVP = new StaticSentence(app.getSession(),
				"SELECT " + "CATEGORIES.NAME AS CATEGORIA, "
		                + "TICKETLINES.NAME AS PRODUCTO, "
						+ "(sum(if(TICKETLINES.UNITS>0,TICKETLINES.UNITS,0))) AS UNIDAD, "
						+ "(TICKETLINES.PRICE * (1 + TAXES.RATE)) AS PRECIO, "
						+ "sum(if(TICKETLINES.UNITS>0,TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE),0)) AS TOTAL, "
						+ "sum(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "TICKETLINES.ATTRIBUTES "
						+ "FROM TICKETLINES LEFT JOIN PRODUCTS ON TICKETLINES.PRODUCT=PRODUCTS.ID "
						+ "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID "
						+ "LEFT JOIN CATEGORIES ON PRODUCTS.CATEGORY=CATEGORIES.ID " + "WHERE "
						+ "TICKET IN (SELECT ID FROM TICKETS WHERE ID IN "
						+ "(SELECT RECEIPTS.ID FROM RECEIPTS LEFT JOIN PAYMENTS " + "ON RECEIPTS.ID = PAYMENTS.RECEIPT "
						+ "WHERE PAYMENTS.PAYMENT = 'prepago' AND RECEIPTS.MONEY=?)) "
						+ "group by TICKETLINES.PRODUCT, PRECIO having UNIDAD<>0",

				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.ReporteXZ.class))
						.list(app.getActiveCashIndex());
		
		
		if (LineasReporteXZNegVP != null) {
			LineasReporteXZVP.addAll(LineasReporteXZNegVP);
		}
		
		if (asalesVP != null) {
			int iExisteAuxVP = 0;
			List<SalesLine> asalesAuxVP = new ArrayList<SalesLine>();
			for (SalesLine saleLine : asalesVP) {
				for (SalesLine saleLineAux : asalesAuxVP) {
					if (saleLine.getTicketId().equals(saleLineAux.getTicketId())) {
						iExisteAuxVP = 1;
						break;
					} else {
						if (saleLine.getTypePay().equals("prepago"))
							iExisteAuxVP = 0;
						else {
							iExisteAuxVP = 1;
							break;
						}
					}
				}
				if ((iExisteAuxVP == 0) && (saleLine.getTypePay().equals("prepago"))) {
					asalesAuxVP.add(saleLine);
				}
				iExisteAuxVP = 0;
			}
			
			SalesLine itemSaleLineVP = new SalesLine();
			
			for(ReporteXZ lineaVP: LineasReporteXZVP)
			{
				itemSaleLineVP = new SalesLine();
				itemSaleLineVP.m_SalesPayType = "";
				itemSaleLineVP.m_SalesSubtotal = lineaVP.getPrecio();
				itemSaleLineVP.m_SalesTaxes = "";
				itemSaleLineVP.m_SalesTotal = lineaVP.getTotal();
				itemSaleLineVP.m_Unidades = lineaVP.getUnidad();
                itemSaleLineVP.m_Producto = lineaVP.getProducto();
				p.m_lsalesSoloVentaPrepago.add(itemSaleLineVP);
				p.m_dSalesSubtotalSVP += lineaVP.getSubTotal();
				p.m_dSalesTotalSVP += itemSaleLineVP.m_SalesTotal;
				p.m_dSalesTotalUnidadesVP += itemSaleLineVP.m_Unidades;
				
				/* Totalizar si es IMPUESTO */
				if (lineaVP.getCategoria().equals("IMPUESTO"))
				{
					p.m_dSalesSubtotalImpBolsaVP += lineaVP.getTotal();
				}
			}
		}

		
		/*---------------- FIN VENTAS PREPAGO --------------*/
				
		

		/* 12/08/2015 */
		List<SalesLine> asales = new StaticSentence(app.getSession(),
				"SELECT TAXES.NAME, " + "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE)), " + "PAYMENTS.PAYMENT, "
						+ "SUM(TICKETLINES.UNITS), TICKETS.TICKETID, TICKETLINES.NAME "
						+ "FROM RECEIPTS, TICKETS, TICKETLINES, TAXES, PAYMENTS "
						+ "WHERE RECEIPTS.ID = TICKETS.ID AND TICKETS.ID = TICKETLINES.TICKET AND TICKETLINES.TAXID = TAXES.ID "
						+ "AND PAYMENTS.RECEIPT = RECEIPTS.ID " + "AND RECEIPTS.MONEY = ? AND RECEIPTS.ID IN "
						+ "(SELECT PAYMENTS.RECEIPT FROM PAYMENTS LEFT JOIN RECEIPTS ON PAYMENTS.RECEIPT=RECEIPTS.ID "
						+ "WHERE RECEIPTS.MONEY = '" + app.getActiveCashIndex() + "' GROUP BY PAYMENTS.RECEIPT) "
						+ "GROUP BY TAXES.ID, TAXES.NAME, PAYMENTS.PAYMENT, TICKETLINES.TICKET, TICKETS.TICKETID "
						+ "ORDER BY PAYMENTS.PAYMENT ASC, TAXES.RATE DESC, TAXES.NAME ASC",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.SalesLine.class))
						.list(app.getActiveCashIndex());
		if (asales == null) {
			p.m_lsales = new ArrayList<SalesLine>();
			p.m_lsalesReportes = new ArrayList<SalesLine>();
			p.m_lsalesSoloVentaCredito = new ArrayList<SalesLine>();
			p.m_lsalesSoloVentaPrepago = new ArrayList<SalesLine>();
		} else {

			int iExisteAux = 0;
			List<SalesLine> asalesAux = new ArrayList<SalesLine>();
			for (SalesLine saleLine : asales) {
				for (SalesLine saleLineAux : asalesAux) {
					if (saleLine.getTicketId().equals(saleLineAux.getTicketId())) {
						iExisteAux = 1;
						break;
					} else {
						iExisteAux = 0;
					}
				}
				if (iExisteAux == 0) {
					asalesAux.add(saleLine);
				}
				iExisteAux = 0;
			}



			int iExiste0 = 0;
			int iExiste1 = 0;
			int iPos0 = 0;
			int iPos1 = 0;
			
			for (SalesLine saleLine : asalesAux) {
				if ((!saleLine.printTypePay().equals("ventacredito")) && (!saleLine.printTypePay().equals("prepago"))) {
					for (SalesLine itemLine : p.m_lsalesReportes) {
						if (itemLine.getTax().equals(saleLine.getTax())) {
							iExiste0 = 1;
							break;
						} else {
							iExiste0 = 0;
						}
						iPos0++;
					}
					if (iExiste0 == 0) {
						// add
						p.m_lsalesReportes.add(saleLine);
					} else {
						// sum to existing
						p.m_lsalesReportes.get(iPos0).m_SalesSubtotal += saleLine.m_SalesSubtotal;
						p.m_lsalesReportes.get(iPos0).m_SalesTotal += saleLine.m_SalesTotal;
					}
					iPos0 = 0;
				} else {
					
				}

				// grouping all
				iExiste1 = 0;
				for (SalesLine itemLine1 : p.m_lsales) {
					if (itemLine1.getTax().equals(saleLine.getTax())) {
						iExiste1 = 1;						
						break;
					} else {
						iExiste1 = 0;						
					}
					iPos1++;
				}

				if (iExiste1 == 0) {
					// add					
					SalesLine itemSaleLine = new SalesLine();
					itemSaleLine.m_SalesPayType = "";
					itemSaleLine.m_SalesSubtotal = saleLine.m_SalesSubtotal;
					itemSaleLine.m_SalesTaxes = saleLine.m_SalesTaxes;
					itemSaleLine.m_SalesTotal = saleLine.m_SalesTotal;
					p.m_lsales.add(itemSaleLine);					

				} else {					
					p.m_lsales.get(iPos1).m_SalesSubtotal += saleLine.m_SalesSubtotal;
					p.m_lsales.get(iPos1).m_SalesTotal += saleLine.m_SalesTotal;					
				}
				iPos1 = 0;

			} /* end main for */

			
		}

		List<ReporteXZ> LineasReporteXZNeg = new StaticSentence(app.getSession(),
				"SELECT " + "CATEGORIES.NAME AS CATEGORIA, "
		                + "TICKETLINES.NAME AS PRODUCTO, "
						+ "sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS,0)) AS UNIDAD,"
						+ "(TICKETLINES.PRICE * (1 + TAXES.RATE)) AS PRECIO, "
						+ "sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE),0)) AS TOTAL, "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "TICKETLINES.ATTRIBUTES "
						+ "FROM TICKETLINES LEFT JOIN PRODUCTS ON TICKETLINES.PRODUCT=PRODUCTS.ID "
						+ "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID "
						+ "LEFT JOIN CATEGORIES ON PRODUCTS.CATEGORY=CATEGORIES.ID " + "WHERE "
						+ "TICKET IN (SELECT ID FROM TICKETS WHERE ID IN "
						+ "(SELECT RECEIPTS.ID FROM RECEIPTS LEFT JOIN PAYMENTS " + "ON RECEIPTS.ID = PAYMENTS.RECEIPT "
						+ "WHERE PAYMENTS.PAYMENT <> 'ventacredito' AND PAYMENTS.PAYMENT <> 'prepago' AND RECEIPTS.MONEY=?)) "
						+ "group by TICKETLINES.PRODUCT having UNIDAD<>0 ",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.ReporteXZ.class))
						.list(app.getActiveCashIndex());

		List<ReporteXZ> LineasReporteXZ = new StaticSentence(app.getSession(),
				"SELECT " + "CATEGORIES.NAME AS CATEGORIA, "
		                + "TICKETLINES.NAME AS PRODUCTO, "
						+ "(sum(if(TICKETLINES.UNITS>0,TICKETLINES.UNITS,0)) + sum(if(TICKETLINES.UNITS<0,TICKETLINES.UNITS,0))) AS UNIDAD, "
						+ "(TICKETLINES.PRICE * (1 + TAXES.RATE)) AS PRECIO, "
						+ "sum(TICKETLINES.UNITS * TICKETLINES.PRICE * (1 + TAXES.RATE)) AS TOTAL, "
						+ "SUM(TICKETLINES.UNITS * TICKETLINES.PRICE), "
						+ "TICKETLINES.ATTRIBUTES "
						+ "FROM TICKETLINES LEFT JOIN PRODUCTS ON TICKETLINES.PRODUCT=PRODUCTS.ID "
						+ "LEFT JOIN TAXES ON TICKETLINES.TAXID = TAXES.ID "
						+ "LEFT JOIN CATEGORIES ON PRODUCTS.CATEGORY=CATEGORIES.ID " + "WHERE "
						+ "TICKET IN (SELECT ID FROM TICKETS WHERE ID IN "
						+ "(SELECT RECEIPTS.ID FROM RECEIPTS LEFT JOIN PAYMENTS " + "ON RECEIPTS.ID = PAYMENTS.RECEIPT "
						+ "WHERE PAYMENTS.PAYMENT <> 'ventacredito' AND PAYMENTS.PAYMENT <> 'prepago' AND RECEIPTS.MONEY=?)) "
						+ "group by TICKETLINES.PRODUCT having UNIDAD<>0",
				SerializerWriteString.INSTANCE, new SerializerReadClass(PaymentsModel.ReporteXZ.class))
						.list(app.getActiveCashIndex());
		if (LineasReporteXZ == null) {
			p.m_LineasReporteXZ = new ArrayList<ReporteXZ>();
		} else {
			if (LineasReporteXZNeg != null) {
				LineasReporteXZ.addAll(LineasReporteXZNeg);
			}

			Boolean encontro = false;
			ReporteXZ lineaXZ;
			GrupoReporteXZ lineaGrupoXZ;
			int indice = 0;
			Iterator<ReporteXZ> itReporteXZ = LineasReporteXZ.iterator();
			while (itReporteXZ.hasNext()) {

				lineaXZ = itReporteXZ.next();
				// lineaXZ.setTotal(lineaXZ.getUnidad() * lineaXZ.getPrecio());
				Iterator<GrupoReporteXZ> itGrupo = p.m_ReporteXZ.iterator();
				encontro = false;							
				
				while (itGrupo.hasNext()) {
					lineaGrupoXZ = itGrupo.next();
					if ((lineaGrupoXZ.m_Categoria != null) && (lineaGrupoXZ.m_Categoria.equals(lineaXZ.m_Categoria))) {
						encontro = true;

						indice = p.m_ReporteXZ.indexOf(lineaGrupoXZ);
						GrupoReporteXZ lineaGrupoXZ1 = p.m_ReporteXZ.get(indice);

						lineaGrupoXZ1.addCount(lineaXZ.getUnidad());
						lineaGrupoXZ1.addTotalCat(lineaXZ.getTotal());
						if (lineaXZ.getUnidad() < 0) {
							
							if (lineaXZ.getCategoria()!=null){
							if (lineaXZ.getCategoria().equals("IMPUESTO"))
							{								
								p.m_dSalesSubtotalImpBolsaNeg += (lineaXZ.getTotal() * -1);								
							}	
							}
							
							p.m_dSumTotalCatDev += (lineaXZ.getTotal() * -1);
							lineaXZ.m_Unidad *= -1;
							lineaXZ.m_Total *= -1;
							lineaGrupoXZ1.m_LineasReporteXZDev.add(lineaXZ);							
							
						} else {		
							
							if (lineaXZ.getCategoria()!=null){
								if (lineaXZ.getCategoria().equals("IMPUESTO"))
							    {								
								  p.m_dSalesSubtotalImpBolsa += (lineaXZ.getTotal() );								
							    }	
							}
							
							lineaGrupoXZ1.m_LineasReporteXZ.add(lineaXZ);							
						}
						p.m_ReporteXZ.set(indice, lineaGrupoXZ1);					
						break;
					}
				}
				
				if (lineaXZ.getUnidad() < 0) {		
					p.m_dSumTotalCatDev += (lineaXZ.getTotal() * -1);
					
				} else {
					p.m_dSumTotalCat += lineaXZ.getTotal();					
				}
				
				if (encontro == false) {
					p.m_ReporteXZ.add(new GrupoReporteXZ(lineaXZ.m_Categoria, lineaXZ));
					
					if (lineaXZ.getUnidad() < 0) {	
						if (lineaXZ.getCategoria()!=null){
						if (lineaXZ.getCategoria().equals("IMPUESTO"))
						{							
							p.m_dSalesSubtotalImpBolsaNeg += (lineaXZ.getTotal() * -1);							
						}		
						}
					} else {	
						if (lineaXZ.getCategoria()!=null){
						if (lineaXZ.getCategoria().equals("IMPUESTO"))
						{							
							p.m_dSalesSubtotalImpBolsa += (lineaXZ.getTotal());							
						}
						}
					}					
				} 								
				
			}
			p.m_LineasReporteXZ = LineasReporteXZ;
		}

		return p;
	}
	
	public int getPayments() {
		return m_iPayments.intValue();
	}

	public double getTotal() {
		return m_dPaymentsTotal.doubleValue();
	}

	public String getHost() {
		return m_sHost;
	}

	public Date getDateStart() {
		return m_dDateStart;
	}

	public void setDateEnd(Date dValue) {
		m_dDateEnd = dValue;
	}

	public Date getDateEnd() {
		return m_dDateEnd;
	}

	/* 20/06/2014 */
	public void setConsecutivo(Integer iValue) {
		m_dConsecutivo = iValue;
	}

	/* 05/12/2014 */
	public void setTipoCierre(String iValue) {
		m_sTipoCierre = iValue;
	}

	public Integer getConsecutivo() {
		return m_dConsecutivo;
	}

	public String printHost() {
		return m_sHost;
	}

	public String printDateStart() {
		return Formats.TIMESTAMP.formatValue(m_dDateStart);
	}

	public String printDateEnd() {
		return Formats.TIMESTAMP.formatValue(m_dDateEnd);
	}

	/* 20/06/2014 */
	public String printConsecutivo() {
		return Formats.INT.formatValue(m_dConsecutivo);
	}

	/* 05/12/2014 INI */
	public String printTipoCierre() {
		return m_sTipoCierre;
	}

	public String printTotalIva() {
		return Formats.DOUBLE.formatValue(m_dSalesTotal - m_dSalesSubtotal);
	}

	public String printFctInicial() {
		return Formats.INT.formatValue(m_iFctInicial);
	}

	public String printFctFinal() {
		return Formats.INT.formatValue(m_iFctFinal);
	}

	public String printPagoEfectivo() {
		return Formats.DOUBLE.formatValue(m_dPagoEfectivo);
	}

	public List<GrupoReporteXZ> getReporteXZLines() {
		return m_ReporteXZ;
	}

	public String printSumTotalCat() {
		//return Formats.DOUBLE.formatValue(m_dSumTotalCat - getSumTotalCatDev());
		return Formats.DOUBLE.formatValue(m_dSumTotalCat);
	}

	public String printSumTotalCatDev() {
		return Formats.DOUBLE.formatValue(m_dSumTotalCatDev);
	}

	public Double getSumTotalCatDev() {
		return m_dSumTotalCatDev.doubleValue();
	}

	/* 05/12/2014 FIN */

	public String printPayments() {
		return Formats.INT.formatValue(m_iPayments);
	}

	public String printPaymentsTotal() {
		return Formats.DOUBLE.formatValue(m_dPaymentsTotal - m_dVentaCredito - m_dVentaPrepago);
	}

	public List<PaymentsLine> getPaymentLines() {
		return m_lpaymentsReportes; /* 09/07/2016 */
	}

	public int getSales() {
		return m_iSales.intValue();
	}

	public String printSales() {
		return Formats.INT.formatValue(m_iSales);
	}

	public int getCantFact() {
		return m_iCantFact.intValue();
	}

	public String printCantFact() {
		return Formats.INT.formatValue(m_iCantFact);
	}

	public String printSalesSubtotal() {
		return Formats.DOUBLE.formatValue(m_dSalesSubtotal);
	}

	public String printSalesTotal() {
		return Formats.DOUBLE.formatValue(m_dSalesTotal);
	}

	/* 23/09/2017 JDP */
	public String printPaymentsTotalSVC() {
		return Formats.DOUBLE.formatValue(m_dPaymentsTotal - m_dVentaCredito - m_dVentaPrepago - getImpBolsaTotalSVCVP());
	}

	public String printSalesSubtotalSVC() {
		Double res = new Double(0.000);
		res = m_dSalesSubtotal - m_dSalesSubtotalSVC - m_dSalesSubtotalSVP - getImpBolsaTotalSVCVP();	
		return Formats.DOUBLE.formatValue(res);
	}	

	public String printSalesSubtotalVC() {
		return Formats.DOUBLE.formatValue(m_dSalesSubtotalSVC);
	}
	
	public String printSalesSubtotalVP() {
		return Formats.DOUBLE.formatValue(m_dSalesSubtotalSVP);
	}

	public String printSalesTotalSVC() {
		return Formats.DOUBLE.formatValue(m_dSalesTotal - m_dSalesTotalSVC - m_dSalesTotalSVP);
	}

	public String printSalesTotalVC() {
		return Formats.DOUBLE.formatValue(m_dSalesTotalSVC);
	}
	
	public String printSalesTotalVP(){
		return Formats.DOUBLE.formatValue(m_dSalesTotalSVP);
	}

	public String printSalesTotalUnidadesVC() {
		return Formats.DOUBLE.formatValue(m_dSalesTotalUnidadesVC);
	}
	
	public String printSalesTotalUnidadesVP(){
		return Formats.DOUBLE.formatValue(m_dSalesTotalUnidadesVP);
	}

	/* 15/06/2016 JDP */
	/* 09/07/2016 JDP NEW SALES WITHOUT VENTA CREDITO */
	public List<SalesLine> getSalesLines() {
		Collections.sort(m_lsalesReportes, new TaxComparator());
		return m_lsalesReportes;
	}

	/* 09/07/2016 JDP */
	public List<SalesLine> getSalesLinesVC() {
		return m_lsalesSoloVentaCredito;
	}
	
	/* 12/07/2017 JDP */
	public List<SalesLine> getSalesLinesVP(){
		return m_lsalesSoloVentaPrepago;
	}
	
	public Double getImpBolsaTotalSVCVP()
	{
		Double ret=new Double(0.000);
		ret = m_dSalesSubtotalImpBolsa;
		return ret;
	}
	
	public String printPaymentsTotalImpB()
	{
		//return Formats.DOUBLE.formatValue(getImpBolsaTotal());
		return Formats.DOUBLE.formatValue(m_dSalesSubtotalImpBolsa);
	}
	
	/* 21/09/2017 INI */
	public Double getImpBolsaTotal()
	{
		Double ret=new Double(0.000);
		ret = m_dSalesSubtotalImpBolsa + m_dSalesSubtotalImpBolsaVC + m_dSalesSubtotalImpBolsaVP;
		return ret;
	}

	public String printPaymentsTotalImpBSVCVP()
	{
		return Formats.DOUBLE.formatValue(getImpBolsaTotalSVCVP());
	}
	
	
	public Double getSalesTotalSVCSVP() {
		Double res = new Double(0.000);
		res = m_dPaymentsTotal - m_dVentaCredito - m_dVentaPrepago;
		return res;
	}
	
	public String printPaymentsTotalSVCImpB()
	{
		return Formats.DOUBLE.formatValue(getSalesTotalSVCSVP());
	}

	/* 21/09/2017 FIN */

	public AbstractTableModel getPaymentsModel() {
		return new AbstractTableModel() {
			public String getColumnName(int column) {
				return AppLocal.getIntString(PAYMENTHEADERS[column]);
			}

			public int getRowCount() {
				return m_lpayments.size();
			}

			public int getColumnCount() {
				return PAYMENTHEADERS.length;
			}

			public Object getValueAt(int row, int column) {
				PaymentsLine l = m_lpayments.get(row);
				switch (column) {
				case 0:
					return l.getType();
				case 1:
					return l.getValue();
				default:
					return null;
				}
			}
		};
	}

	/* 15/06/2016 JDP */
	public class TaxComparator implements Comparator<SalesLine> {
		public int compare(SalesLine p1, SalesLine p2) {
			return p1.getTax().compareTo(p2.getTax());
		}
	}

	/* 09/07/2016 JDP */
	public static class SalesLine implements SerializableRead {

		private String m_SalesTaxes;
		private Double m_SalesSubtotal = 0.0;
		private Double m_SalesTotal = 0.0;
		private String m_SalesPayType;
		private Double m_Unidades = 0.0;
		private Integer m_TicketId = 0;
		private String m_Producto;

		public void readValues(DataRead dr) throws BasicException {
			m_SalesTaxes = dr.getString(1);
			m_SalesSubtotal = dr.getDouble(2);
			m_SalesTotal = dr.getDouble(3);
			m_SalesPayType = dr.getString(4);
			m_Unidades = dr.getDouble(5);
			m_TicketId = dr.getInt(6);
			m_Producto = dr.getString(7);
		}
		
		public String printProducto() {
			return StringUtils.encodeXML(m_Producto);
		}
		
		public String getProducto() {
			return m_Producto;
		}

		public String printTax() {
			return m_SalesTaxes;
		}

		public String printSubtotal() {
			return Formats.DOUBLE.formatValue(m_SalesSubtotal);
		}

		public String printTotal() {
			return Formats.DOUBLE.formatValue(m_SalesTotal);
		}

		public String printTaxtotal() {
			return Formats.DOUBLE.formatValue(m_SalesTotal - m_SalesSubtotal);
		}

		public String printTypePay() {
			return m_SalesPayType;
		}

		public String printUnidades() {
			return Formats.DOUBLE.formatValue(m_Unidades);
		}

		public String getTax() {
			return m_SalesTaxes;
		}

		public Double getSubtotal() {
			return m_SalesSubtotal;
		}

		public Double getTotal() {
			return m_SalesTotal;
		}

		public Double getUnidades() {
			return m_Unidades;
		}

		public Integer getTicketId() {
			return m_TicketId;
		}

		public String getTypePay() {
			return m_SalesPayType;
		}

	}

	public AbstractTableModel getSalesModel() {
		return new AbstractTableModel() {
			public String getColumnName(int column) {
				return AppLocal.getIntString(SALEHEADERS[column]);
			}

			public int getRowCount() {
				return m_lsales.size();
			}

			public int getColumnCount() {
				return SALEHEADERS.length;
			}

			public Object getValueAt(int row, int column) {
				SalesLine l = m_lsales.get(row);
				switch (column) {
				case 0:
					return l.getTax();
				case 1:
					return l.getSubtotal();
				case 2:
					return l.getTotal();
				default:
					return null;
				}
			}
		};
	}

	public static class PaymentsLine implements SerializableRead {

		private String m_PaymentType;
		private Double m_PaymentValue;

		public void readValues(DataRead dr) throws BasicException {
			m_PaymentType = dr.getString(1);
			m_PaymentValue = dr.getDouble(2);
		}

		public String printType() {
			String aka = AppLocal.getIntString("transpayment." + m_PaymentType);
			String padded = aka + ("                          ".substring(aka.length()));
			return padded;
		}

		public String getType() {
			return m_PaymentType;
		}

		public String printValue() {
			return Formats.DOUBLE.formatValue(m_PaymentValue);
		}

		public Double getValue() {
			return m_PaymentValue;
		}
	}

	/* 05/12/2014 JDP INI */
	public static class ReporteXZ implements SerializableRead {

		private String m_Categoria = "";
		private String m_Producto;
		private Double m_Unidad;
		private Double m_Precio = 0.0;
		private Double m_Total = 0.0;
		private Double m_Subtotal = 0.0;
		private Properties m_Propiedades;

		public void readValues(DataRead dr) throws BasicException {
			m_Categoria = dr.getString(1);
			m_Producto = dr.getString(2);
			m_Unidad = dr.getDouble(3);
			m_Precio = dr.getDouble(4);
			m_Total = dr.getDouble(5);
			m_Subtotal = dr.getDouble(6);
			m_Propiedades = new Properties();
			try {
				byte[] img = dr.getBytes(7);
				if (img != null) {
					m_Propiedades.loadFromXML(new ByteArrayInputStream(img));
				}
			} catch (IOException e) {
			}
		}

		public String printCategoria() {
			return StringUtils.encodeXML(m_Categoria);
		}

		public String printProducto() {
			return StringUtils.encodeXML(m_Producto);
		}

		public String printUnidad() {
			return Formats.DOUBLE.formatValue(m_Unidad);
		}

		public String printPrecio() {
			return Formats.DOUBLE.formatValue(m_Precio);
		}

		public String printTotal() {
			return Formats.DOUBLE.formatValue(m_Total);
		}
		
		public String printSubTotal() {
			return Formats.DOUBLE.formatValue(m_Subtotal);
		}

		public String getCategoria() {
			return m_Categoria;
		}

		public String getProducto() {
			return m_Producto;
		}

		public Double getUnidad() {
			return m_Unidad;
		}

		public Double getPrecio() {
			return m_Precio;
		}

		public Double getTotal() {
			return m_Total;
		}

		public void setTotal(Double a) {
			this.m_Total = a;
		}
		
		public Double getSubTotal() {
			return m_Subtotal;
		}
		
		public String getProperty(String key) {
			return m_Propiedades.getProperty(key);
		}

		public String getProperty(String key, String defaultvalue) {
			return m_Propiedades.getProperty(key, defaultvalue);
		}

		public void setProperty(String key, String value) {
			m_Propiedades.setProperty(key, value);
		}

		public Properties getProperties() {
			return m_Propiedades;
		}

	}

	public static class GrupoReporteXZ {

		private String m_Categoria = "";
		private java.util.List<ReporteXZ> m_LineasReporteXZ = new ArrayList<ReporteXZ>();
		private java.util.List<ReporteXZ> m_LineasReporteXZDev = new ArrayList<ReporteXZ>();
		private Double count = 0.0;
		private Double m_TotalCat = 0.0;
		private Double countDev = 0.0;
		private Double m_TotalDev = 0.0;

		public GrupoReporteXZ(String categoria, ReporteXZ reporte) {
			this.m_Categoria = categoria;

			if (reporte.getUnidad() < 0) {
				this.countDev += (reporte.getUnidad() * -1);
				this.m_TotalDev += (reporte.getTotal() * -1);
				reporte.m_Unidad *= -1;
				reporte.m_Total *= -1;
				this.m_LineasReporteXZDev.add(reporte);
			} else {
				this.m_LineasReporteXZ.add(reporte);
				this.count += reporte.getUnidad();
				this.m_TotalCat += reporte.getTotal();
			}
		}

		/* 30/09/2015 */
		public String printCategoria() {
			return StringUtils.encodeXML(m_Categoria);
		}

		public String getCategoria() {
			return m_Categoria;
		}

		public void addCount(Double i) {
			if (i < 0) {
				i *= -1;
				countDev += i;
			} else {
				count += i;
			}
		}

		public void addTotalCat(Double i) {
			if (i < 0) {
				i *= -1;
				m_TotalDev += i;
			} else {
				m_TotalCat += i;
			}

		}

		public java.util.List<ReporteXZ> getLineasReporteZX() {
			return m_LineasReporteXZ;
		}

		public java.util.List<ReporteXZ> getLineasReporteZXDev() {
			return m_LineasReporteXZDev;
		}

		public void setReporteXZ(ReporteXZ reporte) {
			if (reporte.getUnidad() < 0) {
				this.countDev += (reporte.getUnidad() * -1);
				this.m_TotalDev += (reporte.getTotal() * -1);
				reporte.m_Unidad *= -1;
				reporte.m_Total *= -1;
				this.m_LineasReporteXZDev.add(reporte);
			} else {
				this.m_LineasReporteXZ.add(reporte);
			}
		}

		public String printCount() {
			return Formats.DOUBLE.formatValue(count);
		}

		public String printTotalCat() {
			return Formats.DOUBLE.formatValue(m_TotalCat);
		}

		public String printCountDev() {
			return Formats.INT.formatValue(countDev);
		}

		public String printTotalDev() {
			return Formats.DOUBLE.formatValue(m_TotalDev);
		}

		public Double getCountDev() {
			return this.countDev;
		}

	}
	/* 05/12/2014 JDP FIN */

}