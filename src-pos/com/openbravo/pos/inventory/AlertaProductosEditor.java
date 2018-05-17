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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-130

package com.openbravo.pos.inventory;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import com.openbravo.basic.BasicException;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;

/**
 *
 * @author JDP cto@midesatech.net.co
 * 
 */
public class AlertaProductosEditor extends javax.swing.JPanel implements EditorRecord {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9137844259239329734L;
	public Object m_id;
	public Object m_ref;
	public Object m_sName;
	public Object m_sLocation;
	public Object m_sCustomer;

	/** Creates new form ProductsWarehouseEditor */
	public AlertaProductosEditor(DirtyManager dirty) {
		initComponents();
		this.chkBloqueado.addActionListener(dirty);
		this.chkVentaLim.addActionListener(dirty);
		this.m_jCantidad.getDocument().addDocumentListener(dirty);
	}

	public void writeValueEOF() {
		

		m_jTitle.setText(AppLocal.getIntString("label.recordeof"));
		m_id = null;
		m_ref = null;
		m_sName = null;
		m_sLocation = null;
		m_sCustomer = null;
		chkBloqueado.setSelected(false);
		chkVentaLim.setSelected(false);
		chkBloqueado.setEnabled(false);
		chkVentaLim.setEnabled(false);
		m_jCantidad.setText(null);
		m_jCantidad.setEnabled(false);

	}

	public void writeValueInsert() {
		
		m_jTitle.setText(AppLocal.getIntString("label.recordnew"));
		m_id = null;
		m_ref = null;
		m_sName = null;
		m_sLocation = null;
		m_sCustomer = null;
		chkBloqueado.setSelected(false);
		chkVentaLim.setSelected(false);
		chkBloqueado.setEnabled(true);
		chkVentaLim.setEnabled(true);
		m_jCantidad.setText(null);
		m_jCantidad.setEnabled(true);

	}

	public void writeValueEdit(Object value) {
		

		Object[] myprod = (Object[]) value;
		m_id = myprod[0];
		m_ref = myprod[1];
		m_sName = myprod[2];
		m_sLocation = myprod[3];
		m_sCustomer = myprod[4];
		if (Formats.STRING.formatValue(myprod[1]).equals(Formats.STRING.formatValue(myprod[2])))
			m_jTitle.setText(AppLocal.getIntString("Menu.Materials") + " - " + Formats.STRING.formatValue(myprod[2]));
		else
			m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[2]));
		if (myprod[5] != null)
			this.chkBloqueado.setSelected(((Boolean) myprod[5]).booleanValue());
		else
			this.chkBloqueado.setSelected(false);
		
		
		
		if (myprod[6] != null)
			this.chkVentaLim.setSelected(((Boolean) myprod[6]).booleanValue());
		else
			this.chkVentaLim.setSelected(false);
		this.chkBloqueado.setEnabled(true);
		this.chkVentaLim.setEnabled(true);
		m_jCantidad.setText(Formats.DOUBLE.formatValue(myprod[7]));
		m_jCantidad.setEnabled(true);
		
		
		if (this.chkBloqueado.isSelected())
		{
			this.chkVentaLim.setEnabled(false);
			this.m_jCantidad.setEnabled(false);
		}
		else
		{
			if (this.chkVentaLim.isSelected())
			{
				this.m_jCantidad.setEnabled(true);
			}
			else
			{
				this.m_jCantidad.setEnabled(false);
			}
		}

	}

	public void writeValueDelete(Object value) {
		

		Object[] myprod = (Object[]) value;
		m_id = myprod[0];
		m_ref = myprod[1];
		m_sName = myprod[2];
		m_sLocation = myprod[3];
		m_sCustomer = myprod[4];
		
		if (Formats.STRING.formatValue(myprod[1]).equals(Formats.STRING.formatValue(myprod[2])))
			m_jTitle.setText(AppLocal.getIntString("Menu.Materials") + " - " + Formats.STRING.formatValue(myprod[2])
					+ " " + AppLocal.getIntString("label.recorddeleted"));
		else
			m_jTitle.setText(Formats.STRING.formatValue(myprod[1]) + " - " + Formats.STRING.formatValue(myprod[2]) + " "
					+ AppLocal.getIntString("label.recorddeleted"));

		if (myprod[5] != null)
			this.chkBloqueado.setSelected(((Boolean) myprod[5]).booleanValue());
		else
			this.chkBloqueado.setSelected(false);
		if (myprod[6] != null)
			this.chkVentaLim.setSelected(((Boolean) myprod[6]).booleanValue());
		else
			this.chkVentaLim.setSelected(false);
		this.chkBloqueado.setEnabled(false);
		this.chkVentaLim.setEnabled(false);
		this.m_jCantidad.setText(Formats.DOUBLE.formatValue(myprod[7]));
		this.m_jCantidad.setEnabled(false);

	}

	public Object createValue() throws BasicException {
		Object[] productstock = new Object[8];
		
		productstock[0] = m_id;
		productstock[1] = m_ref;
		productstock[2] = m_sName;
		productstock[3] = m_sLocation;
		productstock[4] = m_sCustomer;
		productstock[5] = Boolean.valueOf(this.chkBloqueado.isSelected());
		productstock[6] = Boolean.valueOf(this.chkVentaLim.isSelected());
		productstock[7] = Formats.DOUBLE.parseValue(m_jCantidad.getText());

		return productstock;
	}

	public Component getComponent() {
		return this;
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		m_jTitle = new javax.swing.JLabel();
		lblBloqueado = new javax.swing.JLabel();
		m_jCantidad = new javax.swing.JTextField();
		lblVentaLim = new javax.swing.JLabel();
		m_jMinimum = new javax.swing.JTextField();
		lblMaxPorDia = new javax.swing.JLabel();
		m_jMaximum = new javax.swing.JTextField();
		chkBloqueado = new javax.swing.JCheckBox();
		chkVentaLim = new javax.swing.JCheckBox();

		setLayout(null);

		m_jTitle.setFont(new java.awt.Font("SansSerif", 3, 18));
		add(m_jTitle);
		m_jTitle.setBounds(10, 10, 320, 30);

		lblBloqueado.setText(AppLocal.getIntString("Label.Bloquear"));
		add(lblBloqueado);
		lblBloqueado.setBounds(10, 50, 150, 15);

		add(chkBloqueado);
		chkBloqueado.setBounds(160, 50, 80, 19);
		chkBloqueado.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
               
                if (e.getStateChange() == ItemEvent.SELECTED){
                	chkVentaLim.setEnabled(false);
                	m_jCantidad.setEnabled(false);
                }
                else
                {
                	chkVentaLim.setEnabled(true);
                	m_jCantidad.setEnabled(true);
                }
            }
        });

		lblVentaLim.setText(AppLocal.getIntString("Label.VentaLim"));
		add(lblVentaLim);
		lblVentaLim.setBounds(10, 80, 150, 15);

		add(chkVentaLim);
		chkVentaLim.setBounds(160, 80, 80, 19);
		chkVentaLim.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                
                if (e.getStateChange() == ItemEvent.SELECTED){
                	
                	m_jCantidad.setEnabled(true);
                }
                else
                {
                	
                	m_jCantidad.setEnabled(false);
                }
            }
        });

		lblMaxPorDia.setText(AppLocal.getIntString("Label.MaxPorDia"));
		add(lblMaxPorDia);
		lblMaxPorDia.setBounds(10, 110, 150, 15);

		m_jCantidad.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
		add(m_jCantidad);
		m_jCantidad.setBounds(160, 110, 80, 19);

	}// </editor-fold>//GEN-END:initComponents

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel lblBloqueado;
	private javax.swing.JLabel lblVentaLim;
	private javax.swing.JLabel lblMaxPorDia;
	private javax.swing.JCheckBox chkBloqueado;
	private javax.swing.JCheckBox chkVentaLim;
	private javax.swing.JTextField m_jMaximum;
	private javax.swing.JTextField m_jMinimum;
	private javax.swing.JTextField m_jCantidad;
	private javax.swing.JLabel m_jTitle;
	// End of variables declaration//GEN-END:variables

}
