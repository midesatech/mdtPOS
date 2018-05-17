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

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ListCellRenderer;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.ComparatorCreatorBasic;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.RenderStringRefName;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.loader.VectorerBasic;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.panels.JPanelTable;
import com.openbravo.pos.reports.JParamsLocationCust;

/**
 *
 * @author JDP cto@midesatech.net.co
 * 
 */
public class AlertaProductosPanel extends JPanelTable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -483508681934567972L;

	private JParamsLocationCust m_paramslocationCust;

	private AlertaProductosEditor jeditor;
	private ListProvider lpr;
	private SaveProvider spr;

	private Datas[] prodstock = new Datas[] {  Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN,
			Datas.BOOLEAN, Datas.DOUBLE };

	
	public AlertaProductosPanel() {
	}

	protected void init() {
		
		m_paramslocationCust = new JParamsLocationCust();
		m_paramslocationCust.init(app);
		m_paramslocationCust.addActionListener(new ReloadActionListener());
		

		lpr = new ListProviderCreator(new PreparedSentence(app.getSession(),
				"SELECT "
		                + "PRODUCTS.ID, "
						+ "PRODUCTS.REFERENCE, "
		                + "PRODUCTS.NAME, "
						+ "?, "
		                + "?, "
		                + "S.BLOQUE, "
						+ "S.VTALIM, "
		                + "COALESCE(S.MAXIMO, 0) "
						+ "FROM PRODUCTS LEFT OUTER JOIN "
						+ "(SELECT ID_CUS, ID_PRO, ID_LOC, BLOQUE, VTALIM, MAXIMO FROM ALERTAPRODUCTOS WHERE ID_LOC = ? AND ID_CUS = ?) S ON PRODUCTS.ID = S.ID_PRO "
						+ "WHERE PRODUCTS.ID NOT IN (SELECT DISTINCT PRODUCT FROM PRODUCTS_MAT) AND CATEGORY NOT LIKE '0' ORDER BY PRODUCTS.NAME",
				new SerializerWriteBasicExt(new Datas[] { Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING }, new int[] { 1, 3, 1, 3 }),
				new SerializerReadBasic(prodstock)), m_paramslocationCust);

		SentenceExec updatesent = new SentenceExecTransaction(app.getSession()) {
			public int execInTransaction(Object params) throws BasicException {
				if (new PreparedSentence(app.getSession(),
						"UPDATE ALERTAPRODUCTOS SET BLOQUE = ?, VTALIM = ?, MAXIMO = ? WHERE ID_CUS = ? AND ID_PRO = ? AND ID_LOC = ? ",
						new SerializerWriteBasicExt(prodstock, new int[] { 5, 6, 7, 4, 0, 3 })).exec(params) == 0) {
					return new PreparedSentence(app.getSession(),
							"INSERT INTO ALERTAPRODUCTOS (ID_CUS, ID_PRO, ID_LOC, BLOQUE, VTALIM, MAXIMO) VALUES (?, ?, ?, ?, ?, ?)",
							new SerializerWriteBasicExt(prodstock, new int[] {4, 0, 3, 5, 6, 7 })).exec(params);
				} else {
					return 1;
				}
			}
		};

		spr = new SaveProvider(updatesent, null, null);

		jeditor = new AlertaProductosEditor(dirty);
		
	}

	public ListProvider getListProvider() {
		return lpr;
	}

	public SaveProvider getSaveProvider() {
		return spr;
	}

	@Override
	public Vectorer getVectorer() {
		return new VectorerBasic(
				new String[] { "CUSTOMER",  AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodname"), "ALMACEN",
						"BLOQUEADO", "VENTA_LIMITADA", "CANTIDAD" },
				new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN,
						Formats.BOOLEAN, Formats.DOUBLE },
				new int[] { 1, 2 });
	}

	@Override
	public ComparatorCreator getComparatorCreator() {
		return new ComparatorCreatorBasic(new String[] { "CUSTOMER",  AppLocal.getIntString("label.prodref"), AppLocal.getIntString("label.prodname"), "ALMACEN",
				"BLOQUEADO", "VENTA_LIMITADA", "CANTIDAD" }, prodstock,
				new int[] { 1, 2 });
	}

	@Override
	public ListCellRenderer getListCellRenderer() {
		return new ListCellRendererBasic(new RenderStringRefName(
				new Formats[] { Formats.STRING, Formats.STRING, Formats.STRING }, new int[] { 1, 2 }));
	}

	@Override
	public Component getFilter() {
		return m_paramslocationCust.getComponent();
	}

	public EditorRecord getEditor() {
		return jeditor;
	}

	@Override
	public void activate() throws BasicException {
		m_paramslocationCust.activate();		
		super.activate();
		m_paramslocationCust.cargaCustomer();
	}

	public String getTitle() {
		return AppLocal.getIntString("Menu.VentaUnica");
	}

	private class ReloadActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				AlertaProductosPanel.this.bd.actionLoad();
			} catch (BasicException w) {
			}
		}
	}
}
