package com.openbravo.pos.customers;
import javax.swing.ListCellRenderer;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ComboBoxValModel;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.AppView;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.DirtyManager;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.inventory.ProductsWarehouseEditor;
import com.openbravo.pos.panels.JPanelTable;


public class CustomerProductsPanel extends javax.swing.JPanel {

	 private AppView m_App;
	 
	 private CustomerProductsEditor jeditor;
	 protected DirtyManager dirty;    
	
	 public CustomerProductsPanel() 
	 {   
		 dirty = new DirtyManager();
	 
	 }
	 
	
	 /*@Override
	  public void activate() throws BasicException {
		 jeditor.activate();
		  super.activate();	      
	  }*/
	
	
	
	

	
	
	public String getTitle() {
		// TODO Auto-generated method stub
		return AppLocal.getIntString("Menu.ProductsWarehouse");
	}

	protected void init() {
		// TODO Auto-generated method stub
		
		
		jeditor = new CustomerProductsEditor(dirty);   
		//jeditor.setLayout(new java.awt.BorderLayout());

	}

//	@Override
//	public EditorRecord getEditor() {
//		// TODO Auto-generated method stub
//		  return jeditor;
//	}
//
//	@Override
//	public ListProvider getListProvider() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public SaveProvider getSaveProvider() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
}
