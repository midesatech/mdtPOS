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


/*************************************************************************************/


// 06/10/2015  JDP   JOSE DE PAZ   Se agrega funcionalidad para cambiar el almacén 
//                                 del POS.

/*************************************************************************************/

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppProperties;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;

/**
 *
 * @author adrianromero
 */
public class LocationsView extends javax.swing.JPanel implements EditorRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = -467126568024331695L;

	// private DirtyManager m_Dirty = new DirtyManager();
	private String m_sID;

	/* 06/10/2015 */
	private SentenceList m_sentlocations;
	private ComboBoxValModel m_LocationsModel;
	private DataLogicSales dlSales;
	private DataLogicSystem m_dlSystem;
	private AppProperties m_props;
	private Properties m_propsdb = null;
	private String m_sInventoryLocation;
	private String sWareHouse = "";

	/** Creates new form LocationsEditor */
	public LocationsView(DirtyManager dirty, AppView app) {
		initComponents();

		m_jName.getDocument().addDocumentListener(dirty);
		m_jAddress.getDocument().addDocumentListener(dirty);

		writeValueEOF();

		dlSales = (DataLogicSales) app
				.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
		m_dlSystem = (DataLogicSystem) app
				.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
		
		m_props = app.getProperties();
		// Cargamos las propiedades de base de datos
		m_propsdb = m_dlSystem.getResourceAsProperties(m_props.getHost()
				+ "/properties");
		this.LlenaCombo();
	}

	/**
	 * Actualiza los items del ComboAlmacenes
	 */
	@SuppressWarnings("rawtypes")
	private void LlenaCombo() {
		// El modelo de locales
		m_sentlocations = dlSales.getLocationsList();
		m_LocationsModel = new ComboBoxValModel();

		List a = null;
		try {
			a = m_sentlocations.list();
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addFirst(a);
		m_LocationsModel = new ComboBoxValModel(a);
		m_LocationsModel.setSelectedFirst();
		jcboAlmacen.setModel(m_LocationsModel); // refresh model

		m_sInventoryLocation = m_propsdb.getProperty("location");

		try {
			sWareHouse = m_dlSystem.findLocationName(m_sInventoryLocation);
			jlblAlmacenActual.setText(sWareHouse);

		} catch (BasicException e) {
			sWareHouse = null; // no he encontrado el almacen principal
		}
	}

	@SuppressWarnings("rawtypes")
	protected void addFirst(List a) {
		// do nothing
	}

	public void writeValueEOF() {

		m_sID = null;
		m_jName.setText(null);
		m_jAddress.setText(null);

		m_jName.setEnabled(false);
		m_jAddress.setEnabled(false);
	}

	public void writeValueInsert() {

		m_sID = UUID.randomUUID().toString();
		m_jName.setText(null);
		m_jAddress.setText(null);

		m_jName.setEnabled(true);
		m_jAddress.setEnabled(true);
	}

	public void writeValueDelete(Object value) {

		Object[] location = (Object[]) value;
		m_sID = Formats.STRING.formatValue(location[0]);
		m_jName.setText(Formats.STRING.formatValue(location[1]));
		m_jAddress.setText(Formats.STRING.formatValue(location[2]));

		m_jName.setEnabled(false);
		m_jAddress.setEnabled(false);
	}

	public void writeValueEdit(Object value) {

		Object[] location = (Object[]) value;
		m_sID = Formats.STRING.formatValue(location[0]);
		m_jName.setText(Formats.STRING.formatValue(location[1]));
		m_jAddress.setText(Formats.STRING.formatValue(location[2]));

		m_jName.setEnabled(true);
		m_jAddress.setEnabled(true);
	}

	public Object createValue() throws BasicException {
		Object[] location = new Object[3];
		location[0] = m_sID;
		location[1] = m_jName.getText();
		location[2] = m_jAddress.getText();
		return location;
	}

	public Component getComponent() {
		return this;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc=" Generated Code ">//GEN-BEGIN:initComponents
	private void initComponents() {
		jLabel2 = new javax.swing.JLabel();
		m_jName = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		m_jAddress = new javax.swing.JTextField();

		/* 06/10/2015 */
		jlblAlmacen = new javax.swing.JLabel();
		jlblAlmacenActual = new javax.swing.JLabel();
		jlblAlmacenActualLbl = new javax.swing.JLabel();
		jcboAlmacen = new javax.swing.JComboBox();
		jpnAlmacen = new javax.swing.JPanel();
		jbtnAlmacen = new javax.swing.JButton();
		jbtnRefresh = new javax.swing.JButton();
		setLayout(null);

		jLabel2.setText(AppLocal.getIntString("label.locationname"));
		add(jLabel2);
		jLabel2.setBounds(20, 20, 80, 15);

		add(m_jName);
		m_jName.setBounds(100, 20, 260, 19);

		jLabel3.setText(AppLocal.getIntString("label.locationaddress"));
		add(jLabel3);
		jLabel3.setBounds(20, 50, 80, 15);

		add(m_jAddress);
		m_jAddress.setBounds(100, 50, 260, 19);

		/* 06/10/2015 */
		jpnAlmacen.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Seleccionar almacén para POS"));
		jpnAlmacen.setPreferredSize(new java.awt.Dimension(400, 100));
		jpnAlmacen.setLayout(null);

		jlblAlmacenActualLbl.setText(AppLocal
				.getIntString("label.warehouseActual"));
		jpnAlmacen.add(jlblAlmacenActualLbl);
		jlblAlmacenActualLbl.setBounds(10, 30, 100, 14);

		jlblAlmacenActual.setBorder(javax.swing.BorderFactory
				.createLineBorder(java.awt.Color.black));
		jlblAlmacenActual.setText(sWareHouse);
		jpnAlmacen.add(jlblAlmacenActual);
		jlblAlmacenActual.setBounds(110, 30, 120, 20);

		jlblAlmacen.setText(AppLocal.getIntString("label.warehouse"));
		jpnAlmacen.add(jlblAlmacen);
		jlblAlmacen.setBounds(10, 60, 100, 14);

		jcboAlmacen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// jcboMachineScannerActionPerformed(evt);
			}
		});
		jpnAlmacen.add(jcboAlmacen);

		jcboAlmacen.setBounds(110, 60, 220, 20);

		jbtnAlmacen.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/com/openbravo/images/filesave.png")));
		jbtnAlmacen.setToolTipText("Guardar el almacén para este POS.");
		jbtnAlmacen.setPreferredSize(new Dimension(150, 25));
		jbtnAlmacen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jActualizaAlmacenActionPerformed(evt);
			}
		});
		jpnAlmacen.add(jbtnAlmacen);
		jbtnAlmacen.setBounds(180, 90, 50, 30);

		jbtnRefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/com/openbravo/images/reload.png")));
		jbtnRefresh.setToolTipText("Refrescar");
		jbtnRefresh.setPreferredSize(new Dimension(150, 25));
		jbtnRefresh.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				LlenaCombo();
			}
		});
		jpnAlmacen.add(jbtnRefresh);
		jbtnRefresh.setBounds(110, 90, 50, 30);

		add(jpnAlmacen);
		jpnAlmacen.setBounds(20, 140, 350, 130);

	}// </editor-fold>//GEN-END:initComponents

	/**
	 * Realiza el update del almacen para el POS
	 * 
	 * @param evt
	 */
	private void m_jActualizaAlmacenActionPerformed(
			java.awt.event.ActionEvent evt) {
		String llave = m_LocationsModel.getSelectedKey().toString();
		if (llave == null) {
			m_sInventoryLocation = "0";
			m_propsdb.setProperty("location", m_sInventoryLocation);
			m_dlSystem.setResourceAsProperties(m_props.getHost()
					+ "/properties", m_propsdb);
		} else {
			m_sInventoryLocation = llave;
			m_propsdb.setProperty("location", m_sInventoryLocation);
			m_dlSystem.setResourceAsProperties(m_props.getHost()
					+ "/properties", m_propsdb);
			JOptionPane.showMessageDialog(this,
					AppLocal.getIntString("message.restartchanges"),
					AppLocal.getIntString("message.title"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JTextField m_jAddress;
	private javax.swing.JTextField m_jName;

	/* 06/10/2015 */
	private javax.swing.JLabel jlblAlmacen;
	private javax.swing.JLabel jlblAlmacenActual;
	private javax.swing.JLabel jlblAlmacenActualLbl;
	private javax.swing.JComboBox jcboAlmacen;
	private javax.swing.JPanel jpnAlmacen;
	private javax.swing.JButton jbtnAlmacen;
	private javax.swing.JButton jbtnRefresh;
	// End of variables declaration//GEN-END:variables

}
