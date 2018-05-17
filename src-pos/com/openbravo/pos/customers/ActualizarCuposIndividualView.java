package com.openbravo.pos.customers;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.forms.DataLogicSystem;
import com.openbravo.pos.inventory.SubCentroCostoInfo;
import com.openbravo.pos.panels.PaymentsModel;
import com.openbravo.pos.printer.TicketParser;
import com.openbravo.pos.printer.TicketPrinterException;
import com.openbravo.pos.scripting.ScriptEngine;
import com.openbravo.pos.scripting.ScriptException;
import com.openbravo.pos.scripting.ScriptFactory;
import com.openbravo.pos.util.StringUtils;

public class ActualizarCuposIndividualView extends javax.swing.JPanel implements EditorRecord {

    private Object m_oId;
    
    private DirtyManager m_Dirty;
    private SentenceList m_sentcentrocosto;
    private SentenceList m_sentsubcentrocosto;
    private ListKeyed subcentrocostocollection;
    private SentenceList m_senttipocuenta;
    
    private ComboBoxValModel  m_CentroCostoModel, m_SubCentroCostoModel, m_TipoCuentaModel;
    private DataLogicSales dlSales; 
    private DataLogicSystem m_dlSystem;
    private TicketParser m_TTP;
    
    private DataLogicCustomers dlcustomers;
    private CustomerInfoExt customerext;
    private RecargaInfo m_RecargaClose = null;
    private AppView m_App;
    
    private Boolean bChEfectivo = false;
    private Boolean bChConsignacion = false;
        
    /** Creates new form CustomersView */
    public ActualizarCuposIndividualView(AppView app, DirtyManager dirty) {
        
    	dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
    	dlcustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");
    	m_App = app;
    	m_dlSystem = (DataLogicSystem) m_App.getBean("com.openbravo.pos.forms.DataLogicSystemCreate");
    	
    	m_TTP = new TicketParser(m_App.getDeviceTicket(), m_dlSystem);
    	initComponents();
        
        m_Dirty = dirty;
        m_jTaxID.getDocument().addDocumentListener(dirty);
        m_jName.getDocument().addDocumentListener(dirty);
        m_jAddress.getDocument().addDocumentListener(dirty);
        m_jNotes.getDocument().addDocumentListener(dirty);
       // txtMaxdebt.getDocument().addDocumentListener(dirty);
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
        
        //txtCupArtEsp.getDocument().addDocumentListener(dirty);
        m_jCorEle.getDocument().addDocumentListener(dirty);
        m_jCelCon.getDocument().addDocumentListener(dirty);
        m_jNomCon.getDocument().addDocumentListener(dirty);
        txtConCupCli.getDocument().addDocumentListener(dirty);
        txtConArtEsp.getDocument().addDocumentListener(dirty);
       
        txtCupArtEsp.setEnabled(false);
        txtMaxdebt.setEnabled(false);
        bChEfectivo = false;
        bChConsignacion = false;
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
        
   	    this.m_jChMaxDebt.setSelected(false);
   	    this.m_jChCupArtEsp.setSelected(false);
   	    this.txtMaxdebt.setValue(new Double(0.0));             	  
	    this.txtCupArtEsp.setValue(0.0);
	    this.txtTotCup.setText("0.0");
	    this.m_jChEfectivo.setSelected(false);
  	    this.m_jChConsignacion.setSelected(false);
  	    this.bChEfectivo=false;
  	    this.bChConsignacion=false;
  	    this.m_jNumConsignacion.setText("");
  }
    
    public void writeValueEOF() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jName.setText(null);
        m_jAddress.setText(null);
        m_jNotes.setText(null);
       // txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);
        m_jVisible.setSelected(false);
        m_jSobreCupo.setSelected(false); /*15/12/2014*/
        jcard.setText(null);
        m_jTaxID.setEnabled(false);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        m_jNotes.setEnabled(false);
       // txtMaxdebt.setEnabled(false);
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
        
        //txtCupArtEsp.setText(null);
        txtTotCup.setText(null);
        txtConCupCli.setText(null);
        txtConArtEsp.setText(null);
        m_jCorEle.setText(null);
        m_jCelCon.setText(null);
        m_jNomCon.setText(null);
        
        
      //  txtCupArtEsp.setEnabled(false);
        txtTotCup.setEnabled(false);
        txtConCupCli.setEnabled(false);
        txtConArtEsp.setEnabled(false);
        m_jCorEle.setEnabled(false);
        m_jCelCon.setEnabled(false);
        m_jNomCon.setEnabled(false);
    } 
    
    public void writeValueInsert() {
        m_oId = null;
        m_jTaxID.setText(null);
        m_jName.setText(null);
        m_jAddress.setText(null);
        m_jNotes.setText(null);
        //txtMaxdebt.setText(null);
        txtCurdebt.setText(null);
        txtCurdate.setText(null);        
        m_jVisible.setSelected(true);
        m_jSobreCupo.setSelected(true); /*15/12/2014*/
        jcard.setText(null);
        m_jTaxID.setEnabled(true);
        m_jName.setEnabled(true);
        m_jAddress.setEnabled(true);
        m_jNotes.setEnabled(true);
        //txtMaxdebt.setEnabled(true);
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
        
        
       // txtCupArtEsp.setText(null);
        txtTotCup.setText(null);
        txtConCupCli.setText(null);
        txtConArtEsp.setText(null);
        m_jCorEle.setText(null);
        m_jCelCon.setText(null);
        m_jNomCon.setText(null);
        
     //   txtCupArtEsp.setEnabled(true);
        txtTotCup.setEnabled(true);
        txtConCupCli.setEnabled(true);
        txtConArtEsp.setEnabled(true);
        m_jCorEle.setEnabled(true);
        m_jCelCon.setEnabled(true);
        m_jNomCon.setEnabled(true);
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
        //txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
        txtCurdate.setText(Formats.DATE.formatValue(customer[8]));        
        txtCurdebt.setText(Formats.CURRENCY.formatValue(customer[9]));        
        m_jTaxID.setEnabled(false);
        m_jName.setEnabled(false);
        m_jAddress.setEnabled(false);
        m_jNotes.setEnabled(false);
        //txtMaxdebt.setEnabled(false);
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
        //txtCupArtEsp.setText(Formats.CURRENCY.formatValue(customer[17]));
        txtConCupCli.setText(Formats.CURRENCY.formatValue(customer[18]));
        txtConArtEsp.setText(Formats.CURRENCY.formatValue(customer[19]));

              	
//        String sTotCup=Formats.CURRENCY.formatValue(customer[7]);
//		Double dTotCup;
//		try {
//			dTotCup = Double.valueOf(Formats.CURRENCY.parseValue(sTotCup).toString());
//			dTotCup+= Double.valueOf(Formats.CURRENCY.parseValue(Formats.CURRENCY.formatValue(customer[17])).toString());
//			txtTotCup.setText(Formats.CURRENCY.formatValue(dTotCup));
//	        
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BasicException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
		
          
                
        txtCupArtEsp.setEnabled(false);
        txtTotCup.setEnabled(false);
        txtConCupCli.setEnabled(false);
        txtConArtEsp.setEnabled(false);
        m_jCorEle.setEnabled(false);
        m_jCelCon.setEnabled(false);
        m_jNomCon.setEnabled(false);
//        return;
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
        //txtMaxdebt.setText(Formats.CURRENCY.formatValue(customer[7]));
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
        //txtMaxdebt.setEnabled(true);
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
        //txtCupArtEsp.setText(Formats.CURRENCY.formatValue(customer[17]));
        txtConCupCli.setText(Formats.CURRENCY.formatValue(customer[18]));
        txtConArtEsp.setText(Formats.CURRENCY.formatValue(customer[19]));

       
//       	String sTotCup=Formats.CURRENCY.formatValue(customer[7]);
//		Double dTotCup;
//		try {
//			dTotCup = Double.valueOf(Formats.CURRENCY.parseValue(sTotCup).toString());
//			dTotCup+= Double.valueOf(Formats.CURRENCY.parseValue(Formats.CURRENCY.formatValue(customer[17])).toString());
//			txtTotCup.setText(Formats.CURRENCY.formatValue(dTotCup));
//	        
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (BasicException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
		
		
        
   //     txtCupArtEsp.setEnabled(true);
        txtTotCup.setEnabled(true);
        txtConCupCli.setEnabled(true);
        txtConArtEsp.setEnabled(true);
        m_jCorEle.setEnabled(true);
        m_jCelCon.setEnabled(true);
        m_jNomCon.setEnabled(true);        
    }
    
    public Object createValue() throws BasicException {
        Object[] customer = new Object[20];  //14 18/12/2014
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
        txtMaxdebt = new com.openbravo.editor.JEditorCurrency();
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
         
         txtCupArtEsp = new com.openbravo.editor.JEditorCurrency();
         txtTotCup = new javax.swing.JTextField();
         txtConCupCli = new javax.swing.JTextField();
         txtConArtEsp = new javax.swing.JTextField();
         m_jCorEle = new javax.swing.JTextField();
         m_jCelCon = new javax.swing.JTextField();
         m_jNomCon = new javax.swing.JTextField();
         m_jTipCue = new javax.swing.JComboBox();
         
     	 m_jActualizarCupo = new javax.swing.JButton();
         m_jKeys = new com.openbravo.editor.JEditorKeys();
         
         m_jChMaxDebt = new javax.swing.JCheckBox();
         m_jChCupArtEsp = new javax.swing.JCheckBox();
         
        /*17/12/2014 JDP*/
         
         /* 08/01/2014 JDP */
         jLabelEfectivo = new javax.swing.JLabel();
         jLabelConsignacion = new javax.swing.JLabel();
         jLabelNunConsignacion = new javax.swing.JLabel();
         
         m_jChEfectivo = new javax.swing.JCheckBox();
         m_jChConsignacion = new javax.swing.JCheckBox();
         m_jNumConsignacion = new javax.swing.JTextField();
        
        setLayout(null);
        
        
        
        m_jKeys.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_jKeysActionPerformed(evt);
            }
        });
        add(m_jKeys);
		m_jKeys.setBounds(360, 60, 200, 200);

        jLabel3.setText(AppLocal.getIntString("label.name")); // NOI18N
        add(jLabel3);
        jLabel3.setBounds(20, 30, 140, 15);
        add(m_jName);
        m_jName.setBounds(170, 30, 270, 19);
        m_jName.setEditable(false);

        jLabel12.setText(AppLocal.getIntString("label.notes")); // NOI18N
        add(jLabel12);
        //jLabel12.setBounds(20, 165, 140, 15);

        jScrollPane1.setViewportView(m_jNotes);

        add(jScrollPane1);
        //jScrollPane1.setBounds(160, 165, 270, 70);

        jLabel13.setText(AppLocal.getIntString("label.address")); // NOI18N
        add(jLabel13);
        //jLabel13.setBounds(20, 85, 140, 15);

        jScrollPane2.setViewportView(m_jAddress);

        add(jScrollPane2);
        //jScrollPane2.setBounds(160, 85, 270, 70);

        jLabel4.setText(AppLocal.getIntString("label.visible")); // NOI18N
        add(jLabel4);
        //jLabel4.setBounds(20, 450, 140, 15);
        add(m_jVisible);
        //m_jVisible.setBounds(160, 450, 140, 20);
        m_jVisible.setEnabled(false);

        jLabel5.setText(AppLocal.getIntString("label.card")); // NOI18N
        add(jLabel5);
        //jLabel5.setBounds(20, 55, 140, 15);

        jcard.setEditable(false);  /*25-08-14 JDP*/
        add(jcard);
       // jcard.setBounds(160, 55, 270, 19);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/color_line16.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        add(jButton2);
        //jButton2.setBounds(440, 55, 50, 26);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/fileclose.png"))); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        add(jButton3);
        //jButton3.setBounds(500, 55, 50, 26);

        jLabel1.setText(AppLocal.getIntString("label.maxdebt")); // NOI18N
        add(jLabel1);
        jLabel1.setBounds(20, 65, 140, 15);
        
        //txtMaxdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtMaxdebt.addEditorKeys(m_jKeys);
      
        add(txtMaxdebt);
        txtMaxdebt.setBounds(170, 65, 140, 19);
        txtMaxdebt.addPropertyChangeListener("Edition", new RecalcularTotal());
        
        add(m_jChMaxDebt);
        m_jChMaxDebt.setBounds(310, 65, 40, 19);
        m_jChMaxDebt.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	
            	 int state = itemEvent.getStateChange();
                 if (state == ItemEvent.SELECTED) {
                	 txtMaxdebt.setEnabled(true);
                 }
                 else
                 {
                	 txtMaxdebt.setEnabled(false);
                 }               
              }
            });
        
        m_jChMaxDebt.setSelected(false);
        txtMaxdebt.setEnabled(false);
        
        /*15/12/2014 JDP*/
        jLabelCupArtEsp.setText(AppLocal.getIntString("Label.CupArtEsp")); // NOI18N
        add(jLabelCupArtEsp);
        jLabelCupArtEsp.setBounds(20, 95, 140, 15);
        //txtCupArtEsp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtCupArtEsp.addEditorKeys(m_jKeys);
        txtCupArtEsp.addPropertyChangeListener("Edition", new RecalcularTotal());
        
        add(txtCupArtEsp);
        txtCupArtEsp.setBounds(170, 95, 140, 19);   
        
       
        m_jChCupArtEsp.setBounds(310, 95, 40, 19);
       
        m_jChCupArtEsp.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	
            	 int state = itemEvent.getStateChange();
                 if (state == ItemEvent.SELECTED) {
                	 txtCupArtEsp.setEnabled(true);
                 }
                 else
                 {
                	 txtCupArtEsp.setEnabled(false);
                 }               
              }
            });
        add(m_jChCupArtEsp);
        m_jChCupArtEsp.setSelected(false);
        txtCupArtEsp.setEnabled(false);
                
        jLabelTotCup.setText(AppLocal.getIntString("Label.TotCup")); // NOI18N
        add(jLabelTotCup);
        jLabelTotCup.setBounds(20, 125, 140, 15);
        txtTotCup.setEditable(false);
        txtTotCup.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtTotCup);
        txtTotCup.setBounds(170, 125, 110, 19);
        
        
        
        jLabelEfectivo.setText(AppLocal.getIntString("Label.CupoEfectivo")); // NOI18N
        add(jLabelEfectivo);
        jLabelEfectivo.setBounds(20, 155, 150, 15);
        
        
        jLabelConsignacion.setText(AppLocal.getIntString("Label.Consignacion")); // NOI18N
        add(jLabelConsignacion);
        jLabelConsignacion.setBounds(20, 185, 150, 15);
        
        
        jLabelNunConsignacion.setText(AppLocal.getIntString("Label.NumConsignacion")); // NOI18N
        add(jLabelNunConsignacion);
        jLabelNunConsignacion.setBounds(20, 215, 150, 15);
        
        
        add(m_jChEfectivo);
        m_jChEfectivo.setBounds(170, 155, 40, 19);
        m_jChEfectivo.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	
            	 int state = itemEvent.getStateChange();
                 if (state == ItemEvent.SELECTED) {
                	 bChEfectivo=true;
                	 bChConsignacion=false;
                	 m_jNumConsignacion.setEditable(false);
                	 m_jChConsignacion.setSelected(false);
                 }
                 else
                 {
                	 bChEfectivo=false;
                	 bChConsignacion=true;
                	 m_jNumConsignacion.setEditable(true);
                	 m_jChConsignacion.setSelected(true);
                 }               
              }
            });
        
        add(m_jChConsignacion);
        m_jChConsignacion.setBounds(170, 185, 40, 19);
        m_jChConsignacion.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	
            	 int state = itemEvent.getStateChange();
                 if (state == ItemEvent.SELECTED) {
                	 bChConsignacion=true;
                	 m_jNumConsignacion.setEditable(true);
                	 bChEfectivo=false;
                	 m_jChEfectivo.setSelected(false);
                 }
                 else
                 {
                	 bChConsignacion=false;
                	 m_jNumConsignacion.setEditable(false);
                	 bChEfectivo=true;
                	 m_jChEfectivo.setSelected(true);
                 }               
              }
            });
        
        m_jNumConsignacion.setEditable(false);
        m_jNumConsignacion.setColumns(20);
        m_jNumConsignacion.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(m_jNumConsignacion);
        m_jNumConsignacion.setBounds(170, 215, 150, 19);
        
        
        
        
        
        jLabelConCupCli.setText(AppLocal.getIntString("Label.ConCupCli")); // NOI18N
        add(jLabelConCupCli);
        //jLabelConCupCli.setBounds(20, 333, 140, 15);
        txtConCupCli.setEditable(false);
        txtConCupCli.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtConCupCli);
        //txtConCupCli.setBounds(160, 333, 110, 19);
        
        jLabelConArtEsp.setText(AppLocal.getIntString("Label.ConArtEsp")); // NOI18N
        add(jLabelConArtEsp);
        //jLabelConArtEsp.setBounds(20, 362, 140, 15);
        txtConArtEsp.setEditable(false);
        txtConArtEsp.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtConArtEsp);
        //txtConArtEsp.setBounds(160, 362, 110, 19);
        
        
        /*15/12/2014 JDP*/
        

       

        jLabel2.setText(AppLocal.getIntString("label.curdebt")); // NOI18N
        add(jLabel2);
        //jLabel2.setBounds(20, 390, 140, 15);

        txtCurdebt.setEditable(false);
        txtCurdebt.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        add(txtCurdebt);
        //txtCurdebt.setBounds(160, 390, 110, 19);

        txtCurdate.setEditable(false);
        txtCurdate.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        add(txtCurdate);
        //txtCurdate.setBounds(160, 420, 110, 19);

        jLabel6.setText(AppLocal.getIntString("label.curdate")); // NOI18N
        add(jLabel6);
       // jLabel6.setBounds(20, 420, 140, 15);
      
        add(m_jTaxID);
        m_jTaxID.setBounds(170, 5, 270, 19);
        m_jTaxID.setEditable(false);
        jLabel7.setText(AppLocal.getIntString("label.taxid")); // NOI18N
        add(jLabel7);
        jLabel7.setBounds(20, 5, 140, 15);
        
        
        jLabel25.setText(AppLocal.getIntString("Label.CentroCosto")); // NOI18N
        add(jLabel25);
       // jLabel25.setBounds(20, 480, 140, 15);
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
        //m_jCentroCosto.setBounds(160, 480, 270, 19);
        
        
      
        
        
        jLabel26.setText(AppLocal.getIntString("Label.SubCentroCosto")); // NOI18N
        add(jLabel26);
        //jLabel26.setBounds(20, 510, 140, 15);
        add(m_jSubCentroCosto);
        //m_jSubCentroCosto.setBounds(160, 510, 160, 19);
        
        /*15/12/2014*/
        jLabel27.setText("Sobrecupo"); // NOI18N
        add(jLabel27);
        //jLabel27.setBounds(20, 540, 140, 15);
        add(m_jSobreCupo);
        //m_jSobreCupo.setBounds(160, 540, 160, 19);
        
        /*17/12/2014 JDP*/    
        jLabelCorEle.setText(AppLocal.getIntString("Label.CorEle")); // NOI18N
        add(jLabelCorEle);
        //jLabelCorEle.setBounds(280, 303, 140, 15);
        add(m_jCorEle);
        //m_jCorEle.setBounds(410, 303, 190, 19);
        
        jLabelCelCon.setText(AppLocal.getIntString("Label.CelCon")); // NOI18N
        add(jLabelCelCon);
        //jLabelCelCon.setBounds(280, 333, 140, 15);
        add(m_jCelCon);
        //m_jCelCon.setBounds(410, 333, 190, 19);
        
        jLabelNomCon.setText(AppLocal.getIntString("Label.NomCon")); // NOI18N
        add(jLabelNomCon);
        //jLabelNomCon.setBounds(280, 362, 140, 15);
        add(m_jNomCon);
        //m_jNomCon.setBounds(410, 362, 190, 19);
        
        jLabelTipCue.setText(AppLocal.getIntString("Label.TipCue")); // NOI18N
        add(jLabelTipCue);
        //jLabelTipCue.setBounds(280, 390, 140, 15);
        add(m_jTipCue);
        //m_jTipCue.setBounds(410, 390, 190, 19);
        
        m_jActualizarCupo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/filesave.png")));
		m_jActualizarCupo.setText("Actualizar");
		m_jActualizarCupo.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				m_jActualizaCuposActionPerformed(evt);
			}
		});

		add(m_jActualizarCupo);
		m_jActualizarCupo.setBounds(20, 245, 130, 30);
        
		
		
		
		
        /*17/12/2014 JDP*/
        
        
        
    }// </editor-fold>//GEN-END:initComponents
    
    
    private class RecalcularTotal implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
        	Double dTotCup=0.0;
        	try {
        		dTotCup=txtMaxdebt.getValue();
    		    dTotCup+= txtCupArtEsp.getValue();
    			txtTotCup.setText(Formats.CURRENCY.formatValue(dTotCup));
    	        
    		} catch (NumberFormatException e) {
    			
        		   
    			
    				txtTotCup.setText(Formats.CURRENCY.formatValue(0.0));
    			
    		} catch (BasicException e) {
    			try {
					dTotCup = txtCupArtEsp.getValue();
				    dTotCup+= txtMaxdebt.getValue();
				} catch (BasicException e1) {
					 txtTotCup.setText(Formats.CURRENCY.formatValue(dTotCup));
				}
    			
    		}   
        }
    }    
    
    private void m_jKeysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_jKeysActionPerformed

    	
        
    }//GEN-LAST:event_m_jKeysActionPerformed

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
    
    private void m_jActualizaCuposActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	String linea1="";
    	String linea2="";
    	boolean bMaxDebt=false;
    	boolean bCupArtEsp=false;
    	String sSQL=
    			"INSERT INTO RECARGA (ID,CONSECUTIVO,DATE,TIPOCUPO,IDCUSTOMER,VALOR,TIPOPAGORECID,NUMCONSIGNACION) " +
    	"VALUES (?,?,?,?,?,?,?,?);";
    	customerext = new CustomerInfoExt(m_oId.toString(),m_jTaxID.getText(),m_jName.getText());
    	customerext.setCard(jcard.getText());
    	customerext.setNomCon(m_jNomCon.getText());
    	try {
    		
    		
    		if (this.m_jChMaxDebt.isSelected())
    			if (this.txtMaxdebt.getText().length()>0)
    			{
    				customerext.setMaxdebt(txtMaxdebt.getValue());
    				linea1=this.jLabel1.getText() + ":                   " + 
                            Formats.CURRENCY.formatValue(this.txtMaxdebt.getValue()) + "\n";
    				bMaxDebt=true;
    			}
    			else
    				customerext.setMaxdebt(new Double(0.0));
    		else
    			customerext.setMaxdebt(new Double(0.0));
    		
    		if (this.m_jChCupArtEsp.isSelected())
    			if (this.txtCupArtEsp.getText().length()>0)
    			{
    				customerext.setCupArtEsp(txtCupArtEsp.getValue());
    				linea2=this.jLabelCupArtEsp.getText() +": " +
                    Formats.CURRENCY.formatValue(this.txtCupArtEsp.getValue()) + "\n";
    				bCupArtEsp=true;
    			}
    			else
    				customerext.setCupArtEsp(new Double(0.0));
    		else
				customerext.setCupArtEsp(new Double(0.0));
    		
    		if ((bMaxDebt)|| (bCupArtEsp))
    		{
        		String datos= "\n\n" + 
                        this.m_jName.getText() + "\n" +
                        linea1 +
                        linea2 + "\n\n";
        		m_RecargaClose = new RecargaInfo();
       	
        		
       
        		
        	
             	int res = JOptionPane.showConfirmDialog(this,
    				AppLocal.getIntString("message.wannaupdatecupo")+datos,
    				AppLocal.getIntString("message.title"),
    				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
             	
             	if(res== JOptionPane.YES_OPTION)
             	{
             		/*Hago la recarga*/
               	  Date dNow = new Date();
               	  int iConsecutivo = 0;
               	  
               	  iConsecutivo = dlSales.getNextRecargaIndex().intValue();
               	  String sUID=UUID.randomUUID().toString();
               	  
               	  m_RecargaClose.setId(sUID);
               	  m_RecargaClose.setRecargaId(iConsecutivo);
               	  m_RecargaClose.setDate(dNow);
               	  m_RecargaClose.setCustomer(customerext.getCustomerInfo());
               	  m_RecargaClose.setCard(customerext.getCard());
               	  m_RecargaClose.setNomCon(customerext.getNomCon());
               	  m_RecargaClose.setCajero(m_App.getAppUserView().getUser().getName());
               	  m_RecargaClose.setEfectivo(this.bChEfectivo);
               	  m_RecargaClose.setConsignacion(this.bChConsignacion);
               	  int iLargo=this.m_jNumConsignacion.getText().length();
               	  if (iLargo>20)
               		  iLargo=20;
               		  
               	  if(this.bChConsignacion)
               		  m_RecargaClose.setNumConsginacion(this.m_jNumConsignacion.getText().substring(0,iLargo));
               	  else
               		m_RecargaClose.setNumConsginacion("");
               	  
               	  String iTipoPagoRecarga;
               	  if(this.bChEfectivo)
               		iTipoPagoRecarga="1";
               	  else
               		iTipoPagoRecarga="2";
               	  
             	   if (bMaxDebt==true)
             	   {
             		   if (bCupArtEsp==true)
             		   {
             			   /// update a CUPARTESP y MAXDEBT
             			  dlcustomers.updateCustomerCupos(customerext);
             			  new StaticSentence(
     							m_App.getSession(),
     							sSQL,
     							new SerializerWriteBasic(new Datas[] {
     									Datas.STRING, Datas.INT,
     									Datas.TIMESTAMP, Datas.STRING,
     									Datas.STRING, Datas.DOUBLE,
     									Datas.STRING, Datas.STRING})).exec(new Object[] { sUID, iConsecutivo,
     											dNow,"1",customerext.getId(),customerext.getMaxdebt(),
     											iTipoPagoRecarga,m_RecargaClose.getNumConsginacion()});
             			 new StaticSentence(
      							m_App.getSession(),
      							sSQL,
      							new SerializerWriteBasic(new Datas[] {
      									Datas.STRING, Datas.INT,
      									Datas.TIMESTAMP, Datas.STRING,
      									Datas.STRING, Datas.DOUBLE,
      									Datas.STRING, Datas.STRING})).exec(new Object[] { sUID, iConsecutivo,
      											dNow,"2",customerext.getId(),customerext.getCupArtEsp(),
      											iTipoPagoRecarga,m_RecargaClose.getNumConsginacion()});
             			m_RecargaClose.setRecargaItem("Cupo Cliente", customerext.getMaxdebt());
             			m_RecargaClose.setRecargaItem("Cupo Articulo Especial", customerext.getCupArtEsp());
             			
             		   }
             		   else
             		   {
             			  dlcustomers.updateCustomerCupoMAXDEBT(customerext);
             			  new StaticSentence(
      							m_App.getSession(),
      							sSQL,
      							new SerializerWriteBasic(new Datas[] {
     									Datas.STRING, Datas.INT,
     									Datas.TIMESTAMP, Datas.STRING,
     									Datas.STRING, Datas.DOUBLE,
     									Datas.STRING, Datas.STRING})).exec(new Object[] { sUID, iConsecutivo,
     											dNow,"1",customerext.getId(),customerext.getMaxdebt(),
     											iTipoPagoRecarga,m_RecargaClose.getNumConsginacion()});
             			 m_RecargaClose.setRecargaItem("Cupo Cliente", customerext.getMaxdebt());
             			   // update solo a MAXDEBT
             			  
             		   }
             	   }
             	   else
             	   {
             		  if (bCupArtEsp==true)
            		   {
            			   /// update solo a CUPARTESP
             			 dlcustomers.updateCustomerCupoCUPARTESP(customerext);
             			 new StaticSentence(
       							m_App.getSession(),
       							sSQL,
       							new SerializerWriteBasic(new Datas[] {
      									Datas.STRING, Datas.INT,
      									Datas.TIMESTAMP, Datas.STRING,
      									Datas.STRING, Datas.DOUBLE,
      									Datas.STRING, Datas.STRING})).exec(new Object[] { sUID, iConsecutivo,
      											dNow,"2",customerext.getId(),customerext.getCupArtEsp(),
      											iTipoPagoRecarga,m_RecargaClose.getNumConsginacion()});
             			m_RecargaClose.setRecargaItem("Cupo Articulo Especial", customerext.getCupArtEsp());
            		   }            		   
             	   }
             	   
             	  printPayments();
					
             	   
             	  JOptionPane.showMessageDialog(this,"El CUPO fue actualizado con éxito.", "Cupo Cliente", JOptionPane.PLAIN_MESSAGE);
             	  this.m_jChMaxDebt.setSelected(false);
             	  this.m_jChCupArtEsp.setSelected(false);
             	  this.txtMaxdebt.setValue(new Double(0.0));             	  
             	  this.txtCupArtEsp.setValue(0.0);
             	  this.txtTotCup.setText("0.0");
            	 
            	  
             	  
             	  this.m_jChEfectivo.setSelected(false);             	  
            	  this.m_jChConsignacion.setSelected(false);
            	
            	  this.bChEfectivo=false;
            	  this.bChConsignacion=false;
            	  this.m_jNumConsignacion.setText("");
             	}
    		}
    		
         //   dlcustomers.updateCustomerMaxDebt(customerext);
         //   JOptionPane.showMessageDialog(this,"El CUPO fue actualizado con éxito.", "Cupo Cliente", JOptionPane.PLAIN_MESSAGE);
                
           
        } catch (BasicException e) {
            MessageInf msg = new MessageInf(MessageInf.SGN_NOTICE, AppLocal.getIntString("message.nosavemaxdebt"), e);
            msg.show(this);
        }	 
    	
   }
    
    private void printPayments() {

    	
		String sresource = m_dlSystem.getResourceAsXML("Printer.Recarga");
		if (sresource == null) {
			MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
					AppLocal.getIntString("message.cannotprintticket"));
			msg.show(this);
		} else {
			try {
				ScriptEngine script = ScriptFactory
						.getScriptEngine(ScriptFactory.VELOCITY);
				script.put("recarga", m_RecargaClose);
				m_TTP.printTicket(script.eval(sresource).toString());
			} catch (ScriptException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(this);
			} catch (TicketPrinterException e) {
				MessageInf msg = new MessageInf(MessageInf.SGN_WARNING,
						AppLocal.getIntString("message.cannotprintticket"), e);
				msg.show(this);
			}
		}
	}
    
    
    
    
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
    private com.openbravo.editor.JEditorCurrency txtMaxdebt;
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

    private com.openbravo.editor.JEditorCurrency txtCupArtEsp;
    private javax.swing.JTextField txtTotCup;
    private javax.swing.JTextField txtConCupCli;
    private javax.swing.JTextField txtConArtEsp;
    private javax.swing.JTextField m_jCorEle;
    private javax.swing.JTextField m_jCelCon;
    private javax.swing.JTextField m_jNomCon;
    private javax.swing.JComboBox m_jTipCue;
    
    private javax.swing.JButton m_jActualizarCupo;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    
    private javax.swing.JCheckBox m_jChMaxDebt;
    private javax.swing.JCheckBox m_jChCupArtEsp;

    /*17/12/2014 JDP*/
    
    /* 08/01/2014 JDP*/
    
    private javax.swing.JLabel jLabelEfectivo;
    private javax.swing.JLabel jLabelConsignacion;
    private javax.swing.JLabel jLabelNunConsignacion;
    
    private javax.swing.JCheckBox m_jChEfectivo;
    private javax.swing.JCheckBox m_jChConsignacion;
    private javax.swing.JTextField m_jNumConsignacion;
    
}