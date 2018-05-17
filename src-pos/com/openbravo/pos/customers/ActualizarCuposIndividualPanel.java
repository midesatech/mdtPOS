package com.openbravo.pos.customers;

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
import com.openbravo.pos.panels.JPanelTable;

public class ActualizarCuposIndividualPanel  extends JPanelTable {
    
    private TableDefinition tcustomers;
    private ActualizarCuposIndividualView jeditor;
    
    /** Creates a new instance of CustomersPanel */
    public ActualizarCuposIndividualPanel() {    
    }
    
    protected void init() {        
        DataLogicCustomers dlCustomers  = (DataLogicCustomers) app.getBean("com.openbravo.pos.customers.DataLogicCustomersCreate");       
        tcustomers = dlCustomers.getTableCustomers();        
        jeditor = new ActualizarCuposIndividualView(app, dirty);  
        
    }
    
    
    @Override
    public void activate() throws BasicException { 
       
        
        jeditor.activate();         
        super.activate();
        this.bd.setDeleteData();   /* JDP */
        this.bd.setInsertData(); 
        this.bd.setUpdateData();
        this.bd.actionInsert();
        this.bd.moveFirst();
        this.bd.actionLoad();
    }
    
    public ListProvider getListProvider() {
        return new ListProviderCreator(tcustomers);
    }
    
    public SaveProvider getSaveProvider() {
        return new SaveProvider(tcustomers, new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19});      //7 /*18/12/2014  JDP*/
    }
    
    @Override
    public Vectorer getVectorer() {
        return tcustomers.getVectorerBasic(new int[]{1, 2, 3, 4});
    }
    
    @Override
    public ComparatorCreator getComparatorCreator() {
        return tcustomers.getComparatorCreator(new int[] {1, 2, 3, 4});
    }
    
    @Override
    public ListCellRenderer getListCellRenderer() {
        return new ListCellRendererBasic(tcustomers.getRenderStringBasic(new int[]{2}));
    }
    
    public EditorRecord getEditor() {
        return jeditor;
    }       
    
    public String getTitle() {
        return AppLocal.getIntString("Menu.StockActualizarCupos");
    }    
}
