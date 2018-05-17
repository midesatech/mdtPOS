package com.openbravo.pos.inventory;

import javax.swing.ListCellRenderer;

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

public class TipoCuentaPanel extends JPanelTable {
	
	private TableDefinition ttipocuenta;
    private TipoCuentaEditor jeditor;
    
    /** Creates a new instance of JPanelDuty */
    public TipoCuentaPanel() {
    }
    
    protected void init() {
        DataLogicSales dlSales = (DataLogicSales) app.getBean("com.openbravo.pos.forms.DataLogicSalesCreate");        
        ttipocuenta = dlSales.getTableTipoCuenta();
        jeditor = new TipoCuentaEditor(dirty);
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(ttipocuenta);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(ttipocuenta);      
    }
    
    @Override
    public Vectorer getVectorer() {
        return ttipocuenta.getVectorerBasic(new int[]{1});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return ttipocuenta.getComparatorCreator(new int[] {1});
    }
    
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(ttipocuenta.getRenderStringBasic(new int[]{1}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }
        
    public String getTitle() {
        return AppLocal.getIntString("Menu.TipodeCuenta");
    }     

}
