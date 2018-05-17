package com.openbravo.pos.customers;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListKeyed;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.user.EditorCreator;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.SubCentroCostoInfo;
import com.openbravo.pos.scale.ScaleException;
import com.openbravo.pos.ticket.ProductInfoExt;
import com.openbravo.pos.ticket.TaxInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.customers.CustomerInfo;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.ListModel;

public class JCustomerFinderEspecial extends javax.swing.JDialog implements EditorCreator {

    private CustomerInfo selectedCustomer;
    private ListProvider lpr;
    private ComboBoxValModel  m_CentroCostoModel, m_SubCentroCostoModel;
    private SentenceList m_sentcentrocosto;
    private SentenceList m_sentsubcentrocosto;
    private ListKeyed subcentrocostocollection;
    private DataLogicSales dlSalesAux; 
	private StringBuffer m_sBarcode;
	private DataLogicCustomers dlCust;
	private Boolean isLector=false;
	private Boolean isTodos=false;
    
   
    /** Creates new form JCustomerFinder */
    private JCustomerFinderEspecial(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    /** Creates new form JCustomerFinder */
    private JCustomerFinderEspecial(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);
    }
    
    public static JCustomerFinderEspecial getCustomerFinder(Component parent, DataLogicCustomers dlCustomers, DataLogicSales dlSales) {
        Window window = getWindow(parent);
       
        JCustomerFinderEspecial myMsg;
        if (window instanceof Frame) { 
            myMsg = new JCustomerFinderEspecial((Frame) window, true);
        } else {
            myMsg = new JCustomerFinderEspecial((Dialog) window, true);
        }
        myMsg.init(dlCustomers,dlSales);
        return myMsg;
    }
    
    public CustomerInfo getSelectedCustomer() {
        return selectedCustomer;
    }
    
    public List<CustomerInfo> getSelectedCustomers() {
    	List<CustomerInfo> ret=new ArrayList();
    	if (this.isTodos)
    	{
    		MyListData abc=(MyListData) jListCustomers.getModel();    		
    		java.util.List data= abc.m_data;
    		ret=(List<CustomerInfo>) data;
    		
    	}
    	else
    	{
    		ret= null;
    	}
    	return ret;
    }
    
    

    private void init(DataLogicCustomers dlCustomers, DataLogicSales dlSales) {

    	dlSalesAux=dlSales;
        dlCust=dlCustomers;
        initComponents();

        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(35, 35));

        m_jtxtName.addEditorKeys(m_jKeys);

        m_jtxtName.reset();
        m_jtxtName.activate();

        lpr = new ListProviderCreator(dlCustomers.getCustomerList(), this);

        jListCustomers.setCellRenderer(new CustomerRenderer());

       // getRootPane().setDefaultButton(jcmdOK);

        selectedCustomer = null;
        
        m_CentroCostoModel = new ComboBoxValModel();        
        m_SubCentroCostoModel = new ComboBoxValModel();
        
        m_sentcentrocosto=dlSalesAux.getCentroCostoList();
        m_sentsubcentrocosto=dlSalesAux.getSubCentroCostoList();
        
        
        List b;
		try {
			b = m_sentcentrocosto.list();
			 b.add(0, ""); // The null item
			 m_CentroCostoModel = new ComboBoxValModel(b);
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       
        m_jCentroCosto.setModel(m_CentroCostoModel);
        
        
        
//        List c;
//		try {
//			c = m_sentsubcentrocosto.list();
//			 c.add(0, null); // The null item
//			 m_SubCentroCostoModel = new ComboBoxValModel(c);
//		} catch (BasicException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
       
       
        m_jSubCentroCosto.setModel(m_SubCentroCostoModel);  
        
        java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				m_jTarjeta.requestFocus();
			}
		});
		m_sBarcode = new StringBuffer();
    }
    
    public void search(CustomerInfo customer) {
        
        if (customer == null || customer.getName() == null || customer.getName().equals("")) {
            m_jtxtName.reset();
            m_jtxtName.activate();       
            cleanSearch();
        } else {
            m_jtxtName.setText(customer.getName());
            m_jtxtName.activate();       
            executeSearch();
        }
    }
    
    private void cleanSearch() {
        jListCustomers.setModel(new MyListData(new ArrayList()));
    }
    
    public void executeSearch() {
    	Boolean mulTar=false;
    	
    	for (String retval: this.m_jTarjeta.getText().split(",")){
    		if (!retval.isEmpty())
    		{
              this.encontrarA(null,retval);
              mulTar=true;
    		}
        }
		if (!mulTar) {
			int tarara=this.m_jCentroCosto.getSelectedIndex();
			if (this.m_jCentroCosto.getSelectedIndex() <= 0) {
				try {
					jListCustomers.setModel(new MyListData(lpr.loadData()));
					if (jListCustomers.getModel().getSize() > 0) {
						jListCustomers.setSelectedIndex(0);
					}
				} catch (BasicException e) {
					e.printStackTrace();
				}
			}
			else
			{
				int dont=this.m_jSubCentroCosto.getSelectedIndex();
				if (this.m_jSubCentroCosto.getSelectedIndex() <= -1) {
					/*SOLO SELECCIONO CENTRO DE COSTO */
					try {
						DataLogicSales ax;
						
						String trt=this.m_CentroCostoModel.getSelectedKey().toString();
						java.util.List listaCosto= new java.util.ArrayList();
						//listaCosto= 
						listaCosto= dlSalesAux.getCentroCostoCustomer1(trt);
						//listaCosto=ax.getCentroCostoCustomer(this.m_CentroCostoModel.getSelectedKey().toString());
						if (listaCosto!=null)
						{
							java.util.List data=listaCosto;
							
							//Iterator<CustomerInfoList> iteraX=listaCosto.iterator();
							//while(iteraX.hasNext()){
						//		this.encontrarA(iteraX.next(),"");
						//	}
							//for (CustomerInfo cinfo:listaCosto)
							//{
						//		this.encontrarA(cinfo,"");
						//	}
							
							
							jListCustomers.setModel(new MyListData(data));
						}
					} catch (BasicException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}/*SOLO SELECCIONO CENTRO DE COSTO */
					try {
						
						
						String trt=this.m_CentroCostoModel.getSelectedKey().toString();
						java.util.List listaCosto= new java.util.ArrayList();
						//listaCosto= 
						listaCosto= dlSalesAux.getCentroCostoCustomer1(trt);
						//listaCosto=ax.getCentroCostoCustomer(this.m_CentroCostoModel.getSelectedKey().toString());
						if (listaCosto!=null)
						{
							java.util.List data=listaCosto;
							
							//Iterator<CustomerInfoList> iteraX=listaCosto.iterator();
							//while(iteraX.hasNext()){
						//		this.encontrarA(iteraX.next(),"");
						//	}
							//for (CustomerInfo cinfo:listaCosto)
							//{
						//		this.encontrarA(cinfo,"");
						//	}
							
							
							jListCustomers.setModel(new MyListData(data));
						}
					} catch (BasicException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					/*SELECCIONO CENTRO DE COSTO y SUBCENTRO COSTO */
					try {
						
						
						String trt=this.m_CentroCostoModel.getSelectedKey().toString();
						String tar=this.m_SubCentroCostoModel.getSelectedKey().toString();
						java.util.List listaCosto= new java.util.ArrayList();
						//listaCosto= 
						listaCosto= dlSalesAux.getCentroCostoCustomerAndSub(trt,tar);
						//listaCosto=ax.getCentroCostoCustomer(this.m_CentroCostoModel.getSelectedKey().toString());
						if (listaCosto!=null)
						{
							java.util.List data=listaCosto;
							
							//Iterator<CustomerInfoList> iteraX=listaCosto.iterator();
							//while(iteraX.hasNext()){
						//		this.encontrarA(iteraX.next(),"");
						//	}
							//for (CustomerInfo cinfo:listaCosto)
							//{
						//		this.encontrarA(cinfo,"");
						//	}
							
							
							jListCustomers.setModel(new MyListData(data));
						}
					} catch (BasicException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
    }
    
    public Object createValue() throws BasicException {
        
        Object[] afilter = new Object[2];
        
        // Name
        if (m_jtxtName.getText() == null || m_jtxtName.getText().equals("")) {
            afilter[0] = QBFCompareEnum.COMP_NONE;
            afilter[1] = null;
        } else {
            afilter[0] = QBFCompareEnum.COMP_RE;
            afilter[1] = "%" + m_jtxtName.getText() + "%";
        }
        
        return afilter;
    } 

    private static Window getWindow(Component parent) {
        if (parent == null) {
            return new JFrame();
        } else if (parent instanceof Frame || parent instanceof Dialog) {
            return (Window) parent;
        } else {
            return getWindow(parent.getParent());
        }
    }
    
    private static class MyListData extends javax.swing.AbstractListModel {
        
        private java.util.List m_data;
        
        public MyListData(java.util.List data) {
            m_data = data;
        }
        
        public Object getElementAt(int index) {
            return m_data.get(index);
        }
        
        public int getSize() {
            return m_data.size();
        } 
       
        public void addItems(Object ax)
        {
          m_data.add(ax);	
        }
    }   
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        m_jKeys = new com.openbravo.editor.JEditorKeys();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        m_jtxtName = new com.openbravo.editor.JEditorString();
        jPanel6 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListCustomers = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        jcmdOK = new javax.swing.JButton();
        jcmdCancel = new javax.swing.JButton();
        
        
        jLabelTarjeta = new javax.swing.JLabel();
        jLabelCentroCosto = new javax.swing.JLabel();
        jLabelSubCentroCosto = new javax.swing.JLabel();
        m_jCentroCosto = new javax.swing.JComboBox();
        m_jSubCentroCosto = new javax.swing.JComboBox();
        m_jTarjeta = new javax.swing.JTextField();
        jLabelLector = new javax.swing.JLabel();
        m_jChLector = new javax.swing.JCheckBox();
        jcmdLimpiar = new javax.swing.JButton();
        m_jChTodos = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(AppLocal.getIntString("form.customertitle")); // NOI18N

        jPanel2.setLayout(new java.awt.BorderLayout());
        jPanel2.add(m_jKeys, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_END);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        jPanel7.setPreferredSize(new java.awt.Dimension(100, 140));
        jPanel7.setLayout(null);

        jLabel5.setText(AppLocal.getIntString("label.prodname")); // NOI18N
        jPanel7.add(jLabel5);
        jLabel5.setBounds(10, 10, 80, 14);
        jPanel7.add(m_jtxtName);
        m_jtxtName.setBounds(140, 10, 210, 25);
        
        
        jLabelTarjeta.setText(AppLocal.getIntString("label.cardnumber")); // NOI18N
        jPanel7.add(jLabelTarjeta);
        jLabelTarjeta.setBounds(10, 40, 80, 14);
        
        m_jTarjeta.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				m_jTarjetaKeyTyped(evt);
			}
		});
        jPanel7.add(m_jTarjeta);
        m_jTarjeta.setBounds(140, 40, 180, 20);
        
        jLabelLector.setText("Lector"); // NOI18N
       // jPanel7.add(jLabelLector);
       // jLabelLector.setBounds(330, 40, 40, 20);
        
        
        jPanel7.add(m_jChLector);
        m_jChLector.setBounds(330, 40, 80, 20);
        m_jChLector.setText("Lector");
        m_jChLector.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	
            	 int state = itemEvent.getStateChange();
                 if (state == ItemEvent.SELECTED) {
                	 isLector=true;
                	 m_jTarjeta.setText(null);
                	 m_jTarjeta.requestFocus();
                 }
                 else
                 {
                	 if (state == ItemEvent.DESELECTED) {
                	 isLector=false;
                	 m_jTarjeta.requestFocus();
                	 }
                 }               
              }
            });
        
        jLabelCentroCosto.setText(AppLocal.getIntString("Label.CentroCosto")); // NOI18N
        jPanel7.add(jLabelCentroCosto);
        jLabelCentroCosto.setBounds(10, 70, 120, 14);
        
        m_jCentroCosto.addItemListener(new java.awt.event.ItemListener() {
        	@Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
               if (evt.getStateChange()==java.awt.event.ItemEvent.SELECTED)
				try {
					
					 if(evt.getItem().toString().length()>0)
					CentroCostoActionPerformed(evt);
					 else {
						 List bb = new ArrayList();
					     bb.add(0,"");
					     m_SubCentroCostoModel = new ComboBoxValModel(bb);
					     m_jSubCentroCosto.setModel(m_SubCentroCostoModel);  
					 }
				} catch (BasicException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        jPanel7.add(m_jCentroCosto);
        m_jCentroCosto.setBounds(140, 70, 180, 19);
        
        jLabelSubCentroCosto.setText(AppLocal.getIntString("Label.SubCentroCosto")); // NOI18N
        jPanel7.add(jLabelSubCentroCosto);
        jLabelSubCentroCosto.setBounds(10, 100, 120, 14);
        
        jPanel7.add( m_jSubCentroCosto);
        m_jSubCentroCosto.setBounds(140, 100, 180, 19);

        jPanel5.add(jPanel7, java.awt.BorderLayout.CENTER);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/launch.png"))); // NOI18N
        jButton3.setText(AppLocal.getIntString("button.executefilter")); // NOI18N
        jButton3.setFocusPainted(false);
        jButton3.setFocusable(false);
        jButton3.setRequestFocusEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3);
        jcmdLimpiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/editdelete.png"))); // NOI18N
        jcmdLimpiar.setText("Limpiar Filtro");
        jPanel6.add(jcmdLimpiar);
        jcmdLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	jcmdLimpiarActionPerformed(evt);
            }
        });
        
        m_jChTodos.setText("Sel. Todos");
        jPanel6.add(m_jChTodos);
        m_jChTodos.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent itemEvent) {
            	
            	 int state = itemEvent.getStateChange();
                 if (state == ItemEvent.SELECTED) {
                	 isTodos=true;
                	 jcmdOK.setEnabled(true);
                 }
                 else
                 {
                	 if (state == ItemEvent.DESELECTED) {
                	  isTodos=false;
                	
                	 }
                 }               
              }
            });
        

        jPanel5.add(jPanel6, java.awt.BorderLayout.SOUTH);

        jPanel3.add(jPanel5, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jListCustomers.setFocusable(false);
        jListCustomers.setRequestFocusEnabled(false);
        jListCustomers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListCustomersMouseClicked(evt);
            }
        });
        jListCustomers.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCustomersValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListCustomers);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        
        

        jcmdOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_ok.png"))); // NOI18N
        jcmdOK.setText(AppLocal.getIntString("Button.OK")); // NOI18N
        jcmdOK.setEnabled(false);
        jcmdOK.setFocusPainted(false);
        jcmdOK.setFocusable(false);
        jcmdOK.setMargin(new java.awt.Insets(8, 16, 8, 16));
        jcmdOK.setRequestFocusEnabled(false);
        jcmdOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdOKActionPerformed(evt);
            }
        });
        jPanel1.add(jcmdOK);

        jcmdCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/openbravo/images/button_cancel.png"))); // NOI18N
        jcmdCancel.setText(AppLocal.getIntString("Button.Cancel")); // NOI18N
        jcmdCancel.setFocusPainted(false);
        jcmdCancel.setFocusable(false);
        jcmdCancel.setMargin(new java.awt.Insets(8, 16, 8, 16));
        jcmdCancel.setRequestFocusEnabled(false);
        jcmdCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcmdCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jcmdCancel);

        jPanel3.add(jPanel1, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-632)/2, (screenSize.height-635)/2, 632, 635);
    }// </editor-fold>//GEN-END:initComponents
    private void jcmdOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdOKActionPerformed

    	if (!this.isTodos){
    		try{
              selectedCustomer = (CustomerInfo) jListCustomers.getSelectedValue();
    		}catch(Exception ex)
    		{
    			CustomerInfoList ax= (CustomerInfoList) jListCustomers.getSelectedValue();
    			selectedCustomer=new CustomerInfo(ax.getID(),ax.getTaxId(),ax.getName());
    		}
    	}
    	else
    	{
    		selectedCustomer=null;
    	}
        dispose();
        
    }//GEN-LAST:event_jcmdOKActionPerformed

    private void jcmdCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcmdCancelActionPerformed

        dispose();
        
    }//GEN-LAST:event_jcmdCancelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        executeSearch();
        
    }//GEN-LAST:event_jButton3ActionPerformed
    
    private void jcmdLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    	java.util.List<CustomerInfo> lstData = new ArrayList<CustomerInfo>();
    	jListCustomers.setModel(new MyListData(lstData) );
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jListCustomersValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCustomersValueChanged

        jcmdOK.setEnabled(jListCustomers.getSelectedValue() != null);

    }//GEN-LAST:event_jListCustomersValueChanged

    private void jListCustomersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListCustomersMouseClicked
        
        if (evt.getClickCount() == 2) {
            selectedCustomer = (CustomerInfo) jListCustomers.getSelectedValue();
            dispose();
        }
        
    }//GEN-LAST:event_jListCustomersMouseClicked
    
    
    private void CentroCostoActionPerformed(java.awt.event.ItemEvent evt) throws BasicException {//GEN-FIRST:event_jButton3ActionPerformed	

    	
    	java.util.List<SubCentroCostoInfo> subCentrosCosto = dlSalesAux.getSubCentroCostoList1(m_CentroCostoModel.getSelectedKey().toString());
    	subcentrocostocollection = new ListKeyed<SubCentroCostoInfo>(subCentrosCosto);
    	m_SubCentroCostoModel=new ComboBoxValModel(subCentrosCosto);
    	m_jSubCentroCosto.setModel(m_SubCentroCostoModel);
    }

    private void m_jTarjetaKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_m_jKeyFactoryKeyTyped

    	if (isLector){
    	 m_jTarjeta.setText(null);
		 stateTransition(evt.getKeyChar());
    	}
    	else
    	{
			/* 05/06/2017 */
			/* Admitir solo digitos */
    		if (!Character.isDigit(evt.getKeyChar()))    		   
    			evt.consume();
    	}
    }// GEN-LAST:event_m_jKeyFactoryKeyTyped
    
    private void stateTransition(char cTrans) {

		if (cTrans == '\n') {
			// Codigo de barras introducido
			if (m_sBarcode.length() > 0) {
				String sCode = m_sBarcode.toString();
				
					/* 05/12/2014 JDP */
					// barcode of a customers card
					try {
						CustomerInfo newcustomer = dlCust.findCustomer(sCode);
						if (newcustomer != null) {
							if (jListCustomers==null)
							{
								java.util.List<CustomerInfo> lstData = new ArrayList<CustomerInfo>();
								lstData.add(newcustomer);
								jListCustomers.setModel(new MyListData(lstData));
								
								m_sBarcode = new StringBuffer();
							}
							else
							{
								
								//Boolean encontro=false;
								m_sBarcode = new StringBuffer();
								this.encontrarA(newcustomer,"");
								//ListModel mModelo=jListCustomers.getModel();
								//MyListData abc=(MyListData) jListCustomers.getModel();
								//abc.addItems(newcustomer);
								//java.util.List data= abc.m_data;
								
								//Iterator<CustomerInfo> itera= data.iterator();
								//while (itera.hasNext())
								//{
								//	CustomerInfo adf=itera.next();
								//	if (adf.getId().equals(newcustomer.getId()))
								//	{
								//		encontro=true;
								//		break;
								//	}
								//}
								//if (encontro==false)
								//{
								//	data.add(newcustomer);										
								//	jListCustomers.setModel(new MyListData(data));
								//}
								
								//java.util.List lst=(List) mModelo;
								//MyListData abc=new MyListData(mModelo);
								
								//jListCustomers.setModel((abc));
								
								
							}
						} 
						else 
						{
							
						}
					} catch (BasicException e) {
						Toolkit.getDefaultToolkit().beep();
						new MessageInf(MessageInf.SGN_WARNING,
								AppLocal.getIntString("message.nocustomer"), e)
								.show(this);
					}
				}
			else 
			{
				Toolkit.getDefaultToolkit().beep();
			}
				/* 05/12/2000015034250
				 * 014 JDP */
			m_sBarcode = new StringBuffer();
		}
		else
		{
			/* 05/06/2017 */
			/* Admitir solo digitos */			
			if (Character.isDigit(cTrans))
			    m_sBarcode.append(cTrans);
		}

}
    
    private void encontrarA(CustomerInfo newcustomer, final String uiTarjeta)
    {
    	Boolean encontro=false;
		String uid="";
		CustomerInfo aux=null;
		
		
		if (newcustomer==null)
		{
			try {
				uid=uiTarjeta;
				aux= dlCust.findCustomer(uid);
				if (aux==null)
					return;
			} catch (BasicException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			aux=newcustomer;
		}
		
		MyListData abc=(MyListData) jListCustomers.getModel();
		
		java.util.List data= abc.m_data;
		
		Iterator<CustomerInfo> itera= data.iterator();
		while (itera.hasNext())
		{
			CustomerInfo adf = itera.next();
			
				if (adf.getId().equals(aux.getId()))
				{
					encontro=true;
					break;
				}
			
			
		}
		if (encontro==false)
		{
			
				//newcustomer = dlCust.findCustomer(uid);
				data.add(aux);										
				jListCustomers.setModel(new MyListData(data));
			
			
		}
		
    }
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jListCustomers;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton jcmdCancel;
    private javax.swing.JButton jcmdOK;
    private com.openbravo.editor.JEditorKeys m_jKeys;
    private com.openbravo.editor.JEditorString m_jtxtName;
    
    private javax.swing.JLabel jLabelTarjeta;
    private javax.swing.JLabel jLabelCentroCosto;
    private javax.swing.JLabel jLabelSubCentroCosto;
    
    private javax.swing.JTextField m_jTarjeta;
    private javax.swing.JComboBox m_jCentroCosto;
    private javax.swing.JComboBox m_jSubCentroCosto;
    
    private javax.swing.JLabel jLabelLector;
    private javax.swing.JCheckBox m_jChLector;
    private javax.swing.JButton jcmdLimpiar;
    private javax.swing.JCheckBox m_jChTodos;
    
    
    // End of variables declaration//GEN-END:variables
}