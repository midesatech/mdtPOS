package com.openbravo.pos.inventory;

import com.openbravo.pos.panels.*;

import javax.swing.ListCellRenderer;

import com.openbravo.data.gui.ListCellRendererBasic;
import com.openbravo.data.loader.ComparatorCreator;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.data.loader.Vectorer;
import com.openbravo.data.user.EditorRecord;
import com.openbravo.data.user.SaveProvider;
import com.openbravo.data.user.ListProvider;
import com.openbravo.data.user.ListProviderCreator;
import com.openbravo.pos.forms.DataLogicSales;

public class CentroCostoPanel extends JPanelTable {
	
	private TableDefinition tcentrocosto;
    private CentroCostoEditor jeditor;
    
    /** Creates a new instance of JPanelDuty */
    public CentroCostoPanel() {
    }
    
    protected void init() {
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");        
        tcentrocosto = dlSales.getTableCentroCosto();
        jeditor = new CentroCostoEditor(dirty);
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(tcentrocosto);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tcentrocosto);      
    }
    
    @Override
    public Vectorer getVectorer() {
        return tcentrocosto.getVectorerBasic(new int[]{1});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return tcentrocosto.getComparatorCreator(new int[] {1});
    }
    
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tcentrocosto.getRenderStringBasic(new int[]{1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
        
    public String getTitle() {
        return AppLocal.getIntString("Menu.StockCentroCosto");
    }     

}
