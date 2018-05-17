package com.openbravo.pos.customers;

import java.awt.Component;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;

public class CustomerProductsEditor extends javax.swing.JPanel implements EditorRecord{

	
	 public CustomerProductsEditor(DirtyManager dirty) {
	        initComponents();
	        
	        
	    }
	 
	 public void activate() throws BasicException {
	       
	    }
	 
	 public Component getComponent() {
	        return this;
	    }
	 
	 private void initComponents() {
	       // java.awt.GridBagConstraints gridBagConstraints;

	        jPanelP = new javax.swing.JPanel();
	        jPanel1 = new javax.swing.JPanel();
	        jLabel8 = new javax.swing.JLabel();
	        setLayout(new java.awt.BorderLayout());
	       // setLayout(null);
	        
	        jPanelP.setLayout(null);
	        jPanelP.setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.bywarehouse"))); // NOI18N
	        jPanelP.setPreferredSize(new java.awt.Dimension(400, 60));
            add(jPanelP,java.awt.BorderLayout.PAGE_START);
	        
	       /* jPanel1.setLayout(null);
	        
	        jLabel8.setText(AppLocal.getIntString("label.warehouse")); // NOI18N
	        jPanel1.add(jLabel8);
	        jLabel8.setBounds(10, 90, 150, 15);
	        
	        add(jPanel1, java.awt.BorderLayout.CENTER);*/
	 }
	 
	 private javax.swing.JLabel jLabel8;
	 private javax.swing.JPanel jPanel1;
	 private javax.swing.JPanel jPanelP;
	 
	@Override
	public Object createValue() throws BasicException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeValueEOF() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeValueInsert() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeValueEdit(Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeValueDelete(Object value) throws BasicException {
		// TODO Auto-generated method stub
		
	}

}
