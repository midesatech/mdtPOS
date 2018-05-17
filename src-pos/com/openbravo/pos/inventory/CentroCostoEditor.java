package com.openbravo.pos.inventory;

import java.awt.Component;
import java.util.UUID;

import javax.swing.*;

import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppLocal;


public class CentroCostoEditor extends JPanel implements EditorRecord {
	
	  private Object m_oId;
	    
	    /** Creates new form taxEditor */
	    public CentroCostoEditor(DirtyManager dirty) {
	        initComponents();

	        m_jName.getDocument().addDocumentListener(dirty);
	        
	        writeValueEOF();
	    }
	    
	    public void writeValueEOF() {
	        m_oId = null;
	        m_jName.setText(null);
	        m_jName.setEnabled(false);
	        cb_Estado.setSelected(false);
	    }
	    public void writeValueInsert() {
	        m_oId = null;
	        m_jName.setText(null);
	        m_jName.setEnabled(true);
	        cb_Estado.setSelected(false);
	    }
	    public void writeValueDelete(Object value) {

	        Object[] taxcustcat = (Object[]) value;
	        m_oId = taxcustcat[0];
	        m_jName.setText(Formats.STRING.formatValue(taxcustcat[1]));
	        m_jName.setEnabled(false);
	        cb_Estado.setSelected((Boolean) taxcustcat[2]);
	        cb_Estado.setEnabled(false);
	    }    
	    public void writeValueEdit(Object value) {

	        Object[] taxcustcat = (Object[]) value;
	        m_oId = taxcustcat[0];
	        m_jName.setText(Formats.STRING.formatValue(taxcustcat[1]));
	        m_jName.setEnabled(true);
	        cb_Estado.setSelected((Boolean) taxcustcat[2]);
	        cb_Estado.setEnabled(true);
	    }

	    public Object createValue() throws BasicException {
	        
	        Object[] taxcustcat = new Object[3];

	        taxcustcat[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
	        taxcustcat[1] = m_jName.getText();
	        taxcustcat[2] = Boolean.valueOf(cb_Estado.isSelected());

	        return taxcustcat;
	    }    
	     
	    public Component getComponent() {
	        return this;
	    }

	    /** This method is called from within the constructor to
	     * initialize the form.
	     * WARNING: Do NOT modify this code. The content of this method is
	     * always regenerated by the Form Editor.
	     */
	    @SuppressWarnings("unchecked")
	    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	    private void initComponents() {

	        jLabel2 = new javax.swing.JLabel();
	        m_jName = new javax.swing.JTextField();
	        jLabel3 = new javax.swing.JLabel();
	        cb_Estado=new javax.swing.JCheckBox();

	        setLayout(null);

	        jLabel2.setText(AppLocal.getIntString("Label.Descripcion")); // NOI18N
	        add(jLabel2);
	        jLabel2.setBounds(20, 20, 80, 15);
	        add(m_jName);
	        m_jName.setBounds(100, 20, 200, 19);
	        jLabel3.setText(AppLocal.getIntString("Label.Estado")); // NOI18N
	        add(jLabel3);
	        jLabel3.setBounds(20, 50, 80, 15);
	        add(cb_Estado);
	        cb_Estado.setBounds(100, 50, 200, 19);
	        
	    
	        
	    }// </editor-fold>//GEN-END:initComponents


	    // Variables declaration - do not modify//GEN-BEGIN:variables
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JTextField m_jName;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JCheckBox cb_Estado;
	    // End of variables declaration//GEN-END:variables
}

