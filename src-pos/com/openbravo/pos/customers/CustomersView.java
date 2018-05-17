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

package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.util.StringUtils;

import java.awt.Component;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.openbravo.pos.inventory.SubCentroCostoInfo;
import com.openbravo.data.gui.ListKeyed;

import java.util.List;

/**
 *
 * @author  adrianromero
 */

/*25-08-14  JOSE DE PAZ   JDP     Se actualiza el campo jcard a editable
 *17-12-14  JOSE DE PAZ   JDP     Se agregan mas campos 
 *06-06-17  JOSE DE PAZ   JDP     Se agrega evento keytyped a jcard
 *10-06-17  JOSE DE PAZ   JDP001  Se agrega campo APLICA IVA
 *                                Control de cambios ALERTA PRODUCTOS
 *07-09-17  JOSE DE PAZ   JDP002  CUPO PROGRAMADO                                
 * */

public class CustomersView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;
    
    private DirtyManager m_Dirty;
    private SentenceList m_sentcentrocosto;
    private SentenceList m_sentsubcentrocosto;
    private ListKeyed subcentrocostocollection;
    private SentenceList m_senttipocuenta;
    
    private ComboBoxValModel  m_CentroCostoModel, m_SubCentroCostoModel, m_TipoCuentaModel;
    private  DataLogicSales dlSales; 
    
    //JDP001
    private CustomerInfo customerInfoAlerta;
    private AppView appView;
        
    /** Creates new form CustomersView */
    public CustomersView(AppView app, DirtyManager dirty) {
        appView = app;
    	dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        initComponents();
        
        m_Dirty = dirty;
        m_jTaxID.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jAddress.getDocument().addDocumentListener(dirty);
        m_jNotes.getDocument().addDocumentListener(dirty);
        txtMaxdebt.getDocument().addDocumentListener(dirty);
        m_jVisible.addActionListener(dirty);
        m_jSobreCupo.addActionListener(dirty); /*15/12/2014*/
        
        m_sentcentrocosto=dlSales.getCentroCostoList();
        m_sentsubcentrocosto=dlSales.getSubCentroCostoList();
        m_CentroCostoModel = new ComboBoxValModel();        
        m_SubCentroCostoModel = new ComboBoxValModel();
        m_senttipocuenta=dlSales.getTipoCuentaList(); /*17/12/2014*/
        m_TipoCuentaModel = new ComboBoxValModel();        /*17/12/2014*/
        
        m_jCentroCosto.addActionListener(dirty);
        m_jSubCentroCosto.addActionListener(dirty);
        m_jTipCue.addActionListener(dirty);          /*17/12/2014*/
        
        txtCupArtEsp.getDocument().addDocumentListener(dirty);
        m_jCorEle.getDocument().addDocumentListener(dirty);
        m_jCelCon.getDocument().addDocumentListener(dirty);
        m_jNomCon.getDocument().addDocumentListener(dirty);
        txtConCupCli.getDocument().addDocumentListener(dirty);
        txtConArtEsp.getDocument().addDocumentListener(dirty);
        m_jAplicaIva.addActionListener(dirty);
        
        //JDP002
        txtCupoAuto.getDocument().addDocumentListener(dirty);
        
        writeValueEOF(); 
    }
    
 public void activate() throws BasicException {    
    
        
        List b = m_sentcentrocosto.list();
        b.add(0, null); // The null item
        m_CentroCostoModel = new ComboBoxValModel(b);
        m_jCentroCosto.setModel(m_CentroCostoModel);
        
        
        
        List c = m_sentsubcentrocosto.list();
        c.add(0, null); // The null item
        m_SubCentroCostoModel = new ComboBoxValModel(c);
        m_jSubCentroCosto.setModel(m_SubCentroCostoModel);  
        
        /*17/12/2014*/
        List d = m_senttipocuenta.list();
        d.add(0, null); // The null item
        m_TipoCuentaModel = new ComboBoxValModel(d);
        m_jTipCue.setModel(m_TipoCuentaModel);  
        
  }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jName.setText(null);
        m_jAddress.setText(null);
        m_jNotes.setText(null);
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);
        m_jVisible.setSelected(false);
        m_jSobreCupo.setSelected(false); /*15/12/2014*/
        jcard.setText(null);
        m_jTaxID.setEnabled(false);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        m_jNotes.setEnabled(false);
        txtMaxdebt.setEnabled(false);
        txtCurdebt.setEnabled(false);
        txtCurdate.setEnabled(false);
        m_jVisible.setEnabled(false);
        m_jSobreCupo.setEnabled(false); /*15/12/2014*/
        jcard.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        
        m_CentroCostoModel.setSelectedKey(null);
        m_SubCentroCostoModel.setSelectedKey(null); 
        m_jCentroCosto.setEnabled(false);
        m_jSubCentroCosto.setEnabled(false);
        m_TipoCuentaModel.setSelectedKey(null);
        m_jTipCue.setEnabled(false);
        
        txtCupArtEsp.setText(null);
        txtTotCup.setText(null);
        txtConCupCli.setText(null);
        txtConArtEsp.setText(null);
        m_jCorEle.setText(null);
        m_jCelCon.setText(null);
        m_jNomCon.setText(null);
        
        
        txtCupArtEsp.setEnabled(false);
        txtTotCup.setEnabled(false);
        txtConCupCli.setEnabled(false);
        txtConArtEsp.setEnabled(false);
        m_jCorEle.setEnabled(false);
        m_jCelCon.setEnabled(false);
        m_jNomCon.setEnabled(false);
        
        
        m_jAplicaIva.setSelected(false);
        m_jAplicaIva.setEnabled(false);
        
        //JDP002
        txtCupoAuto.setText(null);
        txtCupoAuto.setEnabled(false);
    } 
    
    public void writeValueInsert() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jName.setText(null);
        m_jAddress.setText(null);
        m_jNotes.setText(null);
        txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);        
        m_jVisible.setSelected(true);
        m_jSobreCupo.setSelected(true); /*15/12/2014*/
        jcard.setText(null);
        m_jTaxID.setEnabled(true);
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        m_jNotes.setEnabled(true);
        txtMaxdebt.setEnabled(true);
        txtCurdebt.setEnabled(true);
        txtCurdate.setEnabled(true);
        m_jVisible.setEnabled(true);
        m_jSobreCupo.setEnabled(true); /*15/12/2014*/
        jcard.setEnabled(true);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        
        m_jCentroCosto.setEnabled(true);
        m_jSubCentroCosto.setEnabled(true);
        m_CentroCostoModel.setSelectedKey(null);
        m_SubCentroCostoModel.setSelectedKey(null); 
        m_TipoCuentaModel.setSelectedKey(null);
        m_jTipCue.setEnabled(true);
        
        
        txtCupArtEsp.setText(null);
        txtTotCup.setText(null);
        txtConCupCli.setText(null);
        txtConArtEsp.setText(null);
        m_jCorEle.setText(null);
        m_jCelCon.setText(null);
        m_jNomCon.setText(null);
        
        txtCupArtEsp.setEnabled(true);
        txtTotCup.setEnabled(true);
        txtConCupCli.setEnabled(true);
        txtConArtEsp.setEnabled(true);
        m_jCorEle.setEnabled(true);
        m_jCelCon.setEnabled(true);
        m_jNomCon.setEnabled(true);
        
        m_jAplicaIva.setSelected(true);
        m_jAplicaIva.setEnabled(true);
        
        //JDP002
        txtCupoAuto.setText(null);
        txtCupoAuto.setEnabled(true);
    }

    public void writeValueDelete(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jTaxID.setText((String) customer[1]);
        m_jName.setText((String) customer[2]);
        m_jAddress.setText((String) customer[3]);
        m_jNotes.setText((String) customer[4]);
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));        
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));        
        m_jTaxID.setEnabled(false);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        m_jNotes.setEnabled(false);
        txtMaxdebt.setEnabled(false);
        txtCurdebt.setEnabled(false);
        txtCurdate.setEnabled(false);
        m_jVisible.setEnabled(false);
        m_jSobreCupo.setEnabled(false); /*15/12/2014*/
        jcard.setEnabled(false);
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
        
        m_jCentroCosto.setEnabled(false);
        m_jSubCentroCosto.setEnabled(false);
        m_CentroCostoModel.setSelectedKey(customer[10]);
        m_SubCentroCostoModel.setSelectedKey(customer[11]);
        m_jSobreCupo.setSelected(((Boolean) customer[12]).booleanValue()); /*15/12/2014*/
        
        m_jTipCue.setEnabled(false);                          /*17/12/2014*/
        m_TipoCuentaModel.setSelectedKey(customer[13]);
        
        
        
        m_jCorEle.setText((String) customer[14]);
        m_jCelCon.setText((String) customer[15]);
        m_jNomCon.setText((String) customer[16]);
        txtCupArtEsp.setText(Formats.CURRENCY.formatValue(customer[17]));
        txtConCupCli.setText(Formats.CURRENCY.formatValue(customer[18]));
        txtConArtEsp.setText(Formats.CURRENCY.formatValue(customer[19]));

              	
        String sTotCup=Formats.CURRENCY.formatValue(customer[7]);
		Double dTotCup;
		try {
			dTotCup = Double.valueOf(Formats.CURRENCY.parseValue(sTotCup).toString());
			dTotCup+= Double.valueOf(Formats.CURRENCY.parseValue(Formats.CURRENCY.formatValue(customer[17])).toString());
			txtTotCup.setText(Formats.CURRENCY.formatValue(dTotCup));
	        
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
          
                
        txtCupArtEsp.setEnabled(false);
        txtTotCup.setEnabled(false);
        txtConCupCli.setEnabled(false);
        txtConArtEsp.setEnabled(false);
        m_jCorEle.setEnabled(false);
        m_jCelCon.setEnabled(false);
        m_jNomCon.setEnabled(false);
        
        m_jAplicaIva.setSelected(((Boolean) customer[20]).booleanValue());
        m_jAplicaIva.setEnabled(false);
        
        //JDP002
        txtCupoAuto.setText(Formats.CURRENCY.formatValue(customer[21]));
        txtCupoAuto.setEnabled(false);
        
    }

    public void writeValueEdit(Object value) {
        Object[] customer = (Object[]) value;
        m_oId = customer[0];
        m_jTaxID.setText((String) customer[1]);        
        m_jName.setText((String) customer[2]);
        m_jAddress.setText((String) customer[3]);
        m_jNotes.setText((String) customer[4]);     
        m_jVisible.setSelected(((Boolean) customer[5]).booleanValue());
        jcard.setText((String) customer[6]);
        txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));        
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));      
        m_CentroCostoModel.setSelectedKey(customer[10]);
        m_SubCentroCostoModel.setSelectedKey(customer[11]);
        m_jSobreCupo.setSelected(((Boolean) customer[12]).booleanValue()); /*15/12/2014*/
        m_TipoCuentaModel.setSelectedKey(customer[13]);
        m_jTaxID.setEnabled(true);
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        m_jNotes.setEnabled(true);
        txtMaxdebt.setEnabled(true);
        txtCurdebt.setEnabled(true);
        txtCurdate.setEnabled(true);        
        m_jVisible.setEnabled(true);
        m_jSobreCupo.setEnabled(true); /*15/12/2014*/
        jcard.setEnabled(true);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        
      
        m_jCentroCosto.setEnabled(true);
        m_jSubCentroCosto.setEnabled(true);
        m_jTipCue.setEnabled(true);      /*17/12/2014*/
        
        
        m_jCorEle.setText((String) customer[14]);
        m_jCelCon.setText((String) customer[15]);
        m_jNomCon.setText((String) customer[16]);
        txtCupArtEsp.setText(Formats.CURRENCY.formatValue(customer[17]));
        txtConCupCli.setText(Formats.CURRENCY.formatValue(customer[18]));
        txtConArtEsp.setText(Formats.CURRENCY.formatValue(customer[19]));

       
       	String sTotCup=Formats.CURRENCY.formatValue(customer[7]);
		Double dTotCup;
		try {
			dTotCup = Double.valueOf(Formats.CURRENCY.parseValue(sTotCup).toString());
			dTotCup+= Double.valueOf(Formats.CURRENCY.parseValue(Formats.CURRENCY.formatValue(customer[17])).toString());
			txtTotCup.setText(Formats.CURRENCY.formatValue(dTotCup));
	        
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		
        
        txtCupArtEsp.setEnabled(true);
        txtTotCup.setEnabled(true);
        txtConCupCli.setEnabled(true);
        txtConArtEsp.setEnabled(true);
        m_jCorEle.setEnabled(true);
        m_jCelCon.setEnabled(true);
        m_jNomCon.setEnabled(true);  
        
        m_jAplicaIva.setSelected(((Boolean) customer[20]).booleanValue());
        m_jAplicaIva.setEnabled(true);
        
        //JDP001
        customerInfoAlerta = new CustomerInfo((String) customer[0], (String) customer[1], (String) customer[2]);
        
        //JDP002
        txtCupoAuto.setText(Formats.CURRENCY.formatValue(customer[21]));
        txtCupoAuto.setEnabled(true);

    }
    
    public Object createValue() throws BasicException {
        Object[] customer = new Object[22];  //21 07/09/2017 JDP002
        customer[0] = m_oId == null ? UUID.randomUUID().toString() : m_oId;
        customer[1] = m_jTaxID.getText();
        customer[2] = m_jName.getText();
        customer[3] = m_jAddress.getText();
        customer[4] = m_jNotes.getText();
        customer[5] = Boolean.valueOf(m_jVisible.isSelected());
        customer[6] = Formats.STRING.parseValue(jcard.getText()); // Format to manage NULL values
        customer[7] = Formats.CURRENCY.parseValue(txtMaxdebt.getText(), new Double(0.0));
        customer[8] = Formats.TIMESTAMP.parseValue(txtCurdate.getText()); // not saved
        customer[9] = Formats.CURRENCY.parseValue(txtCurdebt.getText()); // not saved
        customer[10] = m_CentroCostoModel.getSelectedKey();
        customer[11] = m_SubCentroCostoModel.getSelectedKey();
        customer[12] = Boolean.valueOf(m_jSobreCupo.isSelected()); /*15/12/2014*/
        customer[13] = m_TipoCuentaModel.getSelectedKey();
        customer[14] = m_jCorEle.getText();
        customer[15] = m_jCelCon.getText();
        customer[16] = m_jNomCon.getText();
        customer[17] = Formats.CURRENCY.parseValue(txtCupArtEsp.getText(), new Double(0.0));
        customer[18] = Formats.CURRENCY.parseValue(txtConCupCli.getText(), new Double(0.0));
        customer[19] = Formats.CURRENCY.parseValue(txtConArtEsp.getText(), new Double(0.0));
        customer[20] = Boolean.valueOf(m_jAplicaIva.isSelected());
        customer[21] = Formats.CURRENCY.parseValue(txtCupoAuto.getText(), new Double(0.0));
        return customer;
    }   
    
    public Component getComponent() {
        return this;
    }    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        m_jName = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        m_jNotes = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        m_jAddress = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        m_jVisible = new javax.swing.JCheckBox();
        jLabel5 = new javax.swing.JLabel();
        jcard = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txtMaxdebt = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtCurdebt = new javax.swing.JTextField();
        txtCurdate = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        m_jTaxID = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        
        
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        m_jCentroCosto = new javax.swing.JComboBox();
        m_jSubCentroCosto = new javax.swing.JComboBox();
        m_jSobreCupo = new javax.swing.JCheckBox(); /*15/12/2014*/
        jLabel27 = new javax.swing.JLabel();
        
        /*17/12/2014 JDP*/
         jLabelCorEle = new javax.swing.JLabel();
         jLabelCelCon = new javax.swing.JLabel();
         jLabelNomCon = new javax.swing.JLabel();
         jLabelTipCue = new javax.swing.JLabel();
         jLabelCupArtEsp = new javax.swing.JLabel();
         jLabelTotCup = new javax.swing.JLabel();
         jLabelConCupCli = new javax.swing.JLabel();
         jLabelConArtEsp = new javax.swing.JLabel();
         
         txtCupArtEsp = new javax.swing.JTextField();
         txtTotCup = new javax.swing.JTextField();
         txtConCupCli = new javax.swing.JTextField();
         txtConArtEsp = new javax.swing.JTextField();
         m_jCorEle = new javax.swing.JTextField();
         m_jCelCon = new javax.swing.JTextField();
         m_jNomCon = new javax.swing.JTextField();
         m_jTipCue = new javax.swing.JComboBox();
        /*17/12/2014 JDP*/
         
         jLabelAplicaIva = new javax.swing.JLabel();
         m_jAplicaIva =  new javax.swing.JCheckBox();
         jLabelSelPro = new javax.swing.JLabel();
         cmdSelProd = new javax.swing.JButton();
         
         jLabelCupoAuto  = new javax.swing.JLabel();
         txtCupoAuto = new javax.swing.JTextField();
         
         
        setLayout(null);

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 30, 140, 15);
        add(m_jName);
        m_jName.setBounds(160, 30, 270, 19);

        jLabel12.setText(AppLocal.getIntString("label.notes")); // NOI18N
        add(jLabel12);
        jLabel12.setBounds(20, 165, 140, 15);

        jScrollPane1.setViewportView(m_jNotes);

        add(jScrollPane1);
        jScrollPane1.setBounds(160, 165, 270, 70);

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        add(jLabel13);
        jLabel13.setBounds(20, 85, 140, 15);

        jScrollPane2.setViewportView(m_jAddress);

        add(jScrollPane2);
        jScrollPane2.setBounds(160, 85, 270, 70);

        jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
        add(jLabel4);
        jLabel4.setBounds(20, 450, 140, 15);
        add(m_jVisible);
        m_jVisible.setBounds(160, 450, 140, 20);

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        add(jLabel5);
        jLabel5.setBounds(20, 55, 140, 15);

        jcard.setEditable(true);  /*25-08-14 JDP*/
        jcard.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				m_jCardKeyTyped(evt);
			}
		});
        add(jcard);
        jcard.setBounds(160, 55, 270, 19);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        jButton2.setBounds(440, 55, 50, 26);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        jButton3.setBounds(500, 55, 50, 26);

        jLabel1.setText(AppLocal.getIntString("label.maxdebt")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 245, 140, 15);
        
        /*15/12/2014 JDP*/
        jLabelCupArtEsp.setText(AppLocal.getIntString("Label.CupArtEsp")); // NOI18N
        add(jLabelCupArtEsp);
        jLabelCupArtEsp.setBounds(20, 275, 140, 15);
        txtCupArtEsp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtCupArtEsp);
        txtCupArtEsp.setBounds(160, 275, 110, 19);        
                
        jLabelTotCup.setText(AppLocal.getIntString("Label.TotCup")); // NOI18N
        add(jLabelTotCup);
        jLabelTotCup.setBounds(20, 303, 140, 15);
        txtTotCup.setEditable(false);
        txtTotCup.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtTotCup);
        txtTotCup.setBounds(160, 303, 110, 19);
        
        jLabelConCupCli.setText(AppLocal.getIntString("Label.ConCupCli")); // NOI18N
        add(jLabelConCupCli);
        jLabelConCupCli.setBounds(20, 333, 140, 15);
        txtConCupCli.setEditable(false);
        txtConCupCli.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtConCupCli);
        txtConCupCli.setBounds(160, 333, 110, 19);
        
        jLabelConArtEsp.setText(AppLocal.getIntString("Label.ConArtEsp")); // NOI18N
        add(jLabelConArtEsp);
        jLabelConArtEsp.setBounds(20, 362, 140, 15);
        txtConArtEsp.setEditable(false);
        txtConArtEsp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtConArtEsp);
        txtConArtEsp.setBounds(160, 362, 110, 19);
        
        
        /*15/12/2014 JDP*/
        

        txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtMaxdebt);
        txtMaxdebt.setBounds(160, 245, 110, 19);

        jLabel2.setText(AppLocal.getIntString("label.curdebt")); // NOI18N
        add(jLabel2);
        jLabel2.setBounds(20, 390, 140, 15);

        txtCurdebt.setEditable(false);
        txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtCurdebt);
        txtCurdebt.setBounds(160, 390, 110, 19);

        txtCurdate.setEditable(false);
        txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        add(txtCurdate);
        txtCurdate.setBounds(160, 420, 110, 19);

        jLabel6.setText(AppLocal.getIntString("label.curdate")); // NOI18N
        add(jLabel6);
        jLabel6.setBounds(20, 420, 140, 15);
        add(m_jTaxID);
        m_jTaxID.setBounds(160, 5, 270, 19);

        jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
        add(jLabel7);
        jLabel7.setBounds(20, 5, 140, 15);
        
        
        jLabel25.setText(AppLocal.getIntString("Label.CentroCosto")); // NOI18N
        add(jLabel25);
        jLabel25.setBounds(20, 480, 140, 15);
        m_jCentroCosto.addItemListener(new java.awt.event.ItemListener() {
        	@Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
               if (evt.getStateChange()==java.awt.event.ItemEvent.SELECTED)
				try {
					CentroCostoActionPerformed(evt);
				} catch (BasicException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        add(m_jCentroCosto);
        m_jCentroCosto.setBounds(160, 480, 270, 19);
        
        
      
        
        
        jLabel26.setText(AppLocal.getIntString("Label.SubCentroCosto")); // NOI18N
        add(jLabel26);
        jLabel26.setBounds(20, 510, 140, 15);
        add(m_jSubCentroCosto);
        m_jSubCentroCosto.setBounds(160, 510, 160, 19);
        
        /*15/12/2014*/
        jLabel27.setText("Sobrecupo"); // NOI18N
        add(jLabel27);
        jLabel27.setBounds(20, 540, 140, 15);
        add(m_jSobreCupo);
        m_jSobreCupo.setBounds(160, 540, 160, 19);
        
        /*17/12/2014 JDP*/    
        jLabelCorEle.setText(AppLocal.getIntString("Label.CorEle")); // NOI18N
        add(jLabelCorEle);
        jLabelCorEle.setBounds(280, 303, 140, 15);
        add(m_jCorEle);
        m_jCorEle.setBounds(410, 303, 190, 19);
        
        jLabelCelCon.setText(AppLocal.getIntString("Label.CelCon")); // NOI18N
        add(jLabelCelCon);
        jLabelCelCon.setBounds(280, 333, 140, 15);
        add(m_jCelCon);
        m_jCelCon.setBounds(410, 333, 190, 19);
        
        jLabelNomCon.setText(AppLocal.getIntString("Label.NomCon")); // NOI18N
        add(jLabelNomCon);
        jLabelNomCon.setBounds(280, 362, 140, 15);
        add(m_jNomCon);
        m_jNomCon.setBounds(410, 362, 190, 19);
        
        jLabelTipCue.setText(AppLocal.getIntString("Label.TipCue")); // NOI18N
        add(jLabelTipCue);
        jLabelTipCue.setBounds(280, 390, 140, 15);
        add(m_jTipCue);
        m_jTipCue.setBounds(410, 390, 190, 19);
        
        /*17/12/2014 JDP*/
        
        
        jLabelAplicaIva.setText(AppLocal.getIntString("Label.AplicaIva")); // NOI18N
        add(jLabelAplicaIva);
        jLabelAplicaIva.setBounds(280, 245, 140, 15);
        
        add(m_jAplicaIva);
        m_jAplicaIva.setBounds(410, 245, 140, 15);
        
        
      //JDP001
        jLabelSelPro.setText(AppLocal.getIntString("Label.SelProds")); // NOI18N
        add(jLabelSelPro);
        jLabelSelPro.setBounds(280, 418, 140, 15);
                
        cmdSelProd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/btnplus.png"))); // NOI18N
        cmdSelProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdSelProdActionPerformed(evt);
            }
        });
        add(cmdSelProd);
        cmdSelProd.setBounds(420, 418, 50, 26);
        
        
        jLabelCupoAuto.setText(AppLocal.getIntString("Label.CupoAuto")); // NOI18N
        add(jLabelCupoAuto);
        jLabelCupoAuto.setBounds(280, 274, 140, 15);
        
        add(txtCupoAuto);
        txtCupoAuto.setBounds(410, 274, 110, 19);
        
        
        
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardnew"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {            
            jcard.setText("c" + StringUtils.getCardNumber());
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (JOptionPane.showConfirmDialog(this, AppLocal.getIntString("message.cardremove"), AppLocal.getIntString("title.editor"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            jcard.setText(null);
            m_Dirty.setDirty(true);
        }
    }//GEN-LAST:event_jButton3ActionPerformed
    
    
    private void CentroCostoActionPerformed(java.awt.event.ItemEvent evt) throws BasicException {//GEN-FIRST:event_jButton3ActionPerformed	
    	java.util.List<SubCentroCostoInfo> subCentrosCosto = dlSales.getSubCentroCostoList1(m_CentroCostoModel.getSelectedKey().toString());
    	subcentrocostocollection = new ListKeyed<SubCentroCostoInfo>(subCentrosCosto);
    	m_SubCentroCostoModel=new ComboBoxValModel(subCentrosCosto);
    	m_jSubCentroCosto.setModel(m_SubCentroCostoModel);
    }
    
    private void m_jCardKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_m_jKeyFactoryKeyTyped
    	
			/* 06/06/2017 */
			/* Admitir solo digitos */
    		if (!Character.isDigit(evt.getKeyChar()))    		   
    			evt.consume();
    	
    }// GEN-LAST:event_m_jKeyFactoryKeyTyped
    
  //JDP001
    private void cmdSelProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       
    	//Cargar el formulario de alertas
    	appView.setCustomerInfo(customerInfoAlerta);
    	appView.getAppUserView().showTask("com.openbravo.pos.inventory.AlertaProductosPanel");     	
    	
    }//GEN-LAST:event_cmdSelProdActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jcard;
    private javax.swing.JTextArea m_jAddress;
    private javax.swing.JTextField m_jName;
    private javax.swing.JTextArea m_jNotes;
    private javax.swing.JTextField m_jTaxID;
    private javax.swing.JCheckBox m_jVisible;
    private javax.swing.JTextField txtCurdate;
    private javax.swing.JTextField txtCurdebt;
    private javax.swing.JTextField txtMaxdebt;
    // End of variables declaration//GEN-END:variables
    
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JComboBox m_jCentroCosto;
    private javax.swing.JComboBox m_jSubCentroCosto;
    private javax.swing.JCheckBox m_jSobreCupo; /*15/12/2014*/
    private javax.swing.JLabel jLabel27;
    
    /*17/12/2014 JDP*/
    private javax.swing.JLabel jLabelCorEle;
    private javax.swing.JLabel jLabelCelCon;
    private javax.swing.JLabel jLabelNomCon;
    private javax.swing.JLabel jLabelTipCue;
    private javax.swing.JLabel jLabelCupArtEsp;
    private javax.swing.JLabel jLabelTotCup;
    private javax.swing.JLabel jLabelConCupCli;
    private javax.swing.JLabel jLabelConArtEsp;

    private javax.swing.JTextField txtCupArtEsp;
    private javax.swing.JTextField txtTotCup;
    private javax.swing.JTextField txtConCupCli;
    private javax.swing.JTextField txtConArtEsp;
    private javax.swing.JTextField m_jCorEle;
    private javax.swing.JTextField m_jCelCon;
    private javax.swing.JTextField m_jNomCon;
    private javax.swing.JComboBox m_jTipCue;
    /*17/12/2014 JDP */
    
    /*10/07/2017 JDP */
    private javax.swing.JLabel jLabelAplicaIva;
    private javax.swing.JCheckBox m_jAplicaIva;
    private javax.swing.JLabel jLabelSelPro;
    private javax.swing.JButton cmdSelProd;
    
    /*07/09/2017 JDP*/
    private javax.swing.JLabel jLabelCupoAuto;
    private javax.swing.JTextField txtCupoAuto;
}
