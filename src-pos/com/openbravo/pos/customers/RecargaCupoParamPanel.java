package com.openbravo.pos.customers;

import java.util.Comparator;

import javax.swing.ListCellRenderer;

import com.openbravo.basic.BasicException;
import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.DataLogicSales;
import com.openbravo.pos.panels.JPanelTable;

public class RecargaCupoParamPanel extends JPanelTable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1338296909919901865L;
	private TableDefinition tRecargaCupoParam;
    private RecargaCupoParamEditor jeditor;
	
	public RecargaCupoParamPanel()
	{
		
	}
	
	@Override
	public void activate() throws BasicException { 
	       
	        
	        jeditor.activate();         
	        super.activate();	       
	        
	        this.ordenar();
	    }
	
	public void ordenar()
	{
		ComparatorCreator cc = this.tRecargaCupoParam.getComparatorCreator(new int[]{1});	        
        Comparator c= cc.createComparator(new int[] {0});	      
        try {
        	this.bd.actionLoad();
			this.bd.sort(c);
			
		} catch (BasicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getTitle() {
        return AppLocal.getIntString("Menu.RecargaCupoParam");

	}

	@Override
	protected void init() {
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");   
        DataLogicCustomers dlCustomers = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");   
        tRecargaCupoParam = dlSales.getTableRecargaCupoParam();
        jeditor = new RecargaCupoParamEditor(dirty, dlCustomers, this);
        
	}

	@Override
	public EditorRecord getEditor() {
		return this.jeditor;
	}

	@Override
	public ListProvider getListProvider() {
        return new ListProviderCreator(tRecargaCupoParam);
	}

	@Override
	public SaveProvider getSaveProvider() {
        return new SaveProvider(tRecargaCupoParam);
	}
	
    @Override
    public Vectorer getVectorer() {
        return tRecargaCupoParam.getVectorerBasic(new int[]{1});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return tRecargaCupoParam.getComparatorCreator(new int[] {1});
    }
    
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tRecargaCupoParam.getRenderStringBasic(new int[]{1}));
    }


}
