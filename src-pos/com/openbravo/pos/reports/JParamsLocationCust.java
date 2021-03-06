package com.openbravo.pos.reports;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.QBFCompareEnum;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.customers.DataLogicCustomers;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.pos.forms.DataLogicSales;

public class JParamsLocationCust extends javax.swing.JPanel implements ReportEditorCreator {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6639073992178780310L;
	private SentenceList m_sentlocations;
    private ComboBoxValModel m_LocationsModel;   
	private SentenceList m_sentCustomers;
	private ComboBoxValModel m_CustomersModel;
	private CustomerInfo m_customerInfo;
	private AppView appV;
    
    /** Creates new form JParamsLocation */
    public JParamsLocationCust() {
        initComponents();     
    }

    public void init(AppView app) {
    	appV = app;
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");
        
        // El modelo de locales
        m_sentlocations = dlSales.getLocationsList();
        m_LocationsModel = new ComboBoxValModel();  
        
        DataLogicCustomers dlCust = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");

		// El modelo de clientes
		m_sentCustomers = dlCust.getCustomerList();
		m_CustomersModel = new ComboBoxValModel();
		
    }
        
    public void activate() throws BasicException {
        List a = m_sentlocations.list();
        addFirst(a);
        m_LocationsModel = new ComboBoxValModel(a);
        m_LocationsModel.setSelectedFirst();
        m_jLocation.setModel(m_LocationsModel); // refresh model 
        
		List b = m_sentCustomers.list();
		addFirst(b);
		m_CustomersModel = new ComboBoxValModel(b);
		m_CustomersModel.setSelectedFirst();
		m_jCustomers.setModel(m_CustomersModel); // refresh model	
		
    }
    
    public SerializerWrite getSerializerWrite() {
        return new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING, Datas.OBJECT, Datas.STRING});
    }
    
    public Object createValue() throws BasicException {
        
        return new Object[] {
            m_LocationsModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, 
            m_LocationsModel.getSelectedKey(),
            m_CustomersModel.getSelectedKey() == null ? QBFCompareEnum.COMP_NONE : QBFCompareEnum.COMP_EQUALS, 
           	m_CustomersModel.getSelectedKey()
        };
    }    

    public Component getComponent() {
        return this;
    }
    
    protected void addFirst(List a) {
        // do nothing
    }
    
    public void addActionListener(ActionListener l) {
        m_jLocation.addActionListener(l);
        m_jCustomers.addActionListener(l);
    }
    
    public void removeActionListener(ActionListener l) {
        m_jLocation.removeActionListener(l);
        m_jCustomers.removeActionListener(l);
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        m_jLocation = new javax.swing.JComboBox();
        m_jCustomers = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabelCustomer = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createTitledBorder(AppLocal.getIntString("label.bywarehouse"))); // NOI18N
        setPreferredSize(new java.awt.Dimension(400, 60));
        setLayout(null);
        add(m_jLocation);
        m_jLocation.setBounds(150, 20, 220, 20);

        jLabel8.setText(AppLocal.getIntString("label.warehouse")); // NOI18N
        add(jLabel8);
        jLabel8.setBounds(20, 20, 110, 14);
        
        
		jLabelCustomer.setText(AppLocal.getIntString("Label.CustomerAlerta")); // NOI18N
		add(jLabelCustomer);
		jLabelCustomer.setBounds(475, 20, 80, 14);

		add(m_jCustomers);
		m_jCustomers.setBounds(575, 20, 220, 20);
    }// </editor-fold>//GEN-END:initComponents
    
    
    public void cargaCustomer() {
		m_customerInfo = appV.getCustomerInfo();
		if (m_customerInfo!=null){
			if (m_CustomersModel.getSize()>0)
			{  
				m_CustomersModel.setSelectedKey(m_customerInfo.getKey());
			}
		}		
	}
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel8;
    private javax.swing.JComboBox m_jLocation;
	private javax.swing.JLabel jLabelCustomer;
	private javax.swing.JComboBox m_jCustomers;
    // End of variables declaration//GEN-END:variables	
    
}