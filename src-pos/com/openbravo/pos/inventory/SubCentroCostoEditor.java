package com.openbravo.pos.inventory;

import java.awt.Component;
import java.util.List;
import java.util.UUID;

import javax.swing.*;

import com.openbravo.format.Formats;
import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;

public class SubCentroCostoEditor extends JPanel implements EditorRecord {
	 private Object m_oId;
	 
	   private SentenceList m_sentcentrocosto;
	   private ComboBoxValModel m_CentroCostoModel;
	    
	    /** Creates new form taxEditor */
	    public SubCentroCostoEditor(AppView app, DirtyManager dirty) {
	    	DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
	        initComponents();

	        m_sentcentrocosto = dlSales.getCentroCostoList();
	        m_jName.getDocument().addDocumentListener(dirty);
	        m_jCentroCosto.addActionListener(dirty);
	        m_CentroCostoModel = new ComboBoxValModel();	        
	        writeValueEOF();
	    }
	    
	    
	    public void activate() throws BasicException {
	        
	        List a = m_sentcentrocosto.list();
	        a.add(0, null); // The null item	
	    	m_CentroCostoModel = new ComboBoxValModel(a);
	        m_jCentroCosto.setModel(m_CentroCostoModel);
	    }
	    
	    public void writeValueEOF() {
	        m_oId = null;
	        m_jName.setText(null);
	        m_jName.setEnabled(false);
	        cb_Estado.setSelected(false);
	        m_jCentroCosto.setEnabled(false);
	        m_CentroCostoModel.setSelectedKey(null);
	    }
	    public void writeValueInsert() {
	        m_oId = null;
	        m_jName.setText(null);
	        m_jName.setEnabled(true);
	        m_CentroCostoModel.setSelectedKey(null);
	        m_jCentroCosto.setEnabled(true);
	        cb_Estado.setSelected(false);
	    }
	    public void writeValueDelete(Object value) {

	        Object[] taxcustcat = (Object[]) value;
	        m_oId = taxcustcat[0];
	        m_jName.setText(Formats.STRING.formatValue(taxcustcat[1]));
	        m_jName.setEnabled(false);
	        cb_Estado.setSelected((Boolean) taxcustcat[2]);
	        m_CentroCostoModel.setSelectedKey(taxcustcat[3]);
	        m_jCentroCosto.setEnabled(false);
	        cb_Estado.setEnabled(false);
	    }    
	    public void writeValueEdit(Object value) {

	        Object[] taxcustcat = (Object[]) value;
	        m_oId = taxcustcat[0];
	        m_jName.setText(Formats.STRING.formatValue(taxcustcat[1]));
	        m_jName.setEnabled(true);
	        m_jCentroCosto.setEnabled(true);
	        m_CentroCostoModel.setSelectedKey(taxcustcat[3]);
	        cb_Estado.setSelected((Boolean) taxcustcat[2]);
	        cb_Estado.setEnabled(true);
	    }

	    public Object createValue() throws BasicException {
	        
	        Object[] taxcustcat = new Object[4];

	        taxcustcat[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
	        taxcustcat[1] = m_jName.getText();
	        taxcustcat[2] = Boolean.valueOf(cb_Estado.isSelected());
	        taxcustcat[3] = m_CentroCostoModel.getSelectedKey();
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
	        jLabel4 = new javax.swing.JLabel();
	        cb_Estado=new javax.swing.JCheckBox();
	        m_jCentroCosto=new javax.swing.JComboBox();

	        setLayout(null);

	        jLabel2.setText(AppLocal.getIntString("Label.Descripcion")); // NOI18N
	        add(jLabel2);
	        jLabel2.setBounds(20, 20, 120, 15);
	        add(m_jName);
	        m_jName.setBounds(140, 20, 200, 19);
	        
	        jLabel4.setText(AppLocal.getIntString("Label.CentroCosto")); // NOI18N
	        add(jLabel4);
	        jLabel4.setBounds(20, 50, 120, 15);
	        add(m_jCentroCosto);
	        m_jCentroCosto.setBounds(140, 50, 200, 19);
	        
	        
	        jLabel3.setText(AppLocal.getIntString("Label.Estado")); // NOI18N
	        add(jLabel3);
	        jLabel3.setBounds(20, 80, 120, 15);
	        add(cb_Estado);
	        cb_Estado.setBounds(140, 80, 200, 19);
	        
	    
	        
	    }// </editor-fold>//GEN-END:initComponents


	    // Variables declaration - do not modify//GEN-BEGIN:variables
	    private javax.swing.JLabel jLabel2;
	    private javax.swing.JTextField m_jName;
	    private javax.swing.JLabel jLabel3;
	    private javax.swing.JLabel jLabel4;
	    
	    private javax.swing.JCheckBox cb_Estado;
	    private javax.swing.JComboBox m_jCentroCosto;
	    // End of variables declaration//GEN-END:variables

}
