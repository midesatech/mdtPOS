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
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  US

package com.openbravo.pos.customers;

import java.util.List;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.Datas;
import com.openbravo.data.loader.PreparedSentence;
import com.openbravo.data.loader.QBFBuilder;
import com.openbravo.data.loader.SentenceExec;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.data.loader.SentenceList;
import com.openbravo.data.loader.SerializerRead;
import com.openbravo.data.loader.SerializerReadBasic;
import com.openbravo.data.loader.SerializerReadClass;
import com.openbravo.data.loader.SerializerWrite;
import com.openbravo.data.loader.SerializerWriteBasic;
import com.openbravo.data.loader.SerializerWriteBasicExt;
import com.openbravo.data.loader.SerializerWriteString;
import com.openbravo.data.loader.Session;
import com.openbravo.data.loader.StaticSentence;
import com.openbravo.data.loader.TableDefinition;
import com.openbravo.format.Formats;
import com.openbravo.pos.forms.AppLocal;
import com.openbravo.pos.forms.BeanFactoryDataSingle;
import com.openbravo.pos.inventory.SubCentroCostoInfo;
import com.openbravo.pos.inventory.TipoCuentaInfo;
import com.openbravo.data.loader.ImageUtils;
/**
 *
 * @author adrianromero
 */

/* 15-09-2014 JOSE DE PAZ JDP REQ140914 Se crea m�todo para actualizar campo MAXDEBT 
 *                                      en tabla Customers
 * 07-09-2017 JOSE DE PAZ JDP001        Se crea metodo para actualizar campo MAXDEBT
 *                                      con cupo automatico programado                                     
 * */
public class DataLogicCustomers extends BeanFactoryDataSingle {
    
    protected Session s;
    private TableDefinition tcustomers;
    private static Datas[] customerdatas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.TIMESTAMP, Datas.STRING, Datas.STRING, Datas.STRING, Datas.INT, Datas.BOOLEAN, Datas.STRING};
    
    public void init(Session s){
        
        this.s = s;
        tcustomers = new TableDefinition(s,
            "CUSTOMERS"
            , new String[] {"ID", "TAXID", "NAME", "ADDRESS", "NOTES", "VISIBLE", "CARD", "MAXDEBT", "CURDATE", "CURDEBT", 
        		"CCOSTOID", "SUBCCOSTOID", "SOBRECUPO", "TIPOCUENTAID", "CORELE", "CELCON", "NOMCON", "CUPARTESP", "CONCUPCLI", "CONARTESP", "APLICAIVA", "CUPOAUTO"}
            , new String[] {"ID", AppLocal.getIntString("label.taxid"), AppLocal.getIntString("label.name"), AppLocal.getIntString("label.address"), AppLocal.getIntString("label.notes"), "VISIBLE", "CARD", AppLocal.getIntString("label.maxdebt"), AppLocal.getIntString("label.curdate"), AppLocal.getIntString("label.curdebt")
        		,"CCOSTOID", "SUBCCOSTOID", "SOBRECUPO", "TIPOCUENTAID", "CORELE", "CELCON", "NOMCON", "CUPARTESP", "CONCUPCLI", "CONARTESP", "APLICAIVA", "CUPOAUTO"}
            , new Datas[] {Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.DOUBLE, Datas.TIMESTAMP, Datas.DOUBLE
        		, Datas.STRING, Datas.STRING, Datas.BOOLEAN, Datas.STRING, Datas.STRING, Datas.STRING, Datas.STRING, Datas.DOUBLE, Datas.DOUBLE, Datas.DOUBLE, Datas.BOOLEAN, Datas.DOUBLE }
            , new Formats[] {Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.CURRENCY, Formats.TIMESTAMP, Formats.CURRENCY
        		, Formats.STRING, Formats.STRING, Formats.BOOLEAN, Formats.STRING, Formats.STRING, Formats.STRING, Formats.STRING, Formats.CURRENCY, Formats.CURRENCY, Formats.CURRENCY, Formats.BOOLEAN, Formats.DOUBLE}
            , new int[] {0}
        );   
        
    }
    
    // CustomerList list
    public SentenceList getCustomerList() {
        return new StaticSentence(s
            , new QBFBuilder("SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE VISIBLE = TRUE AND ?(QBF_FILTER) ORDER BY NAME", new String[] {"NAME"})
            , new SerializerWriteBasic(new Datas[] {Datas.OBJECT, Datas.STRING})
            , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3));
                    }                
                });
    }
    
    public List<CustomerInfo> getCentroCostoCustomer(String Id) throws BasicException  {
        return new PreparedSentence(s
            , "SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE CCOSTOID=? ORDER BY NAME"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(CustomerInfo.class)).list(Id);
    }   
    
    public CustomerInfo findCustomer(String card) throws BasicException {
        return (CustomerInfo) new PreparedSentence(s
                , "SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE VISIBLE = TRUE AND CARD = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3));
                    }
                }).find(card);
    }
    
    public CustomerInfo findCustomer1(String costo) throws BasicException {
        return (CustomerInfo) new PreparedSentence(s
                , "SELECT ID, TAXID, NAME FROM CUSTOMERS WHERE VISIBLE = TRUE AND CCOSTOID = ?"
                , SerializerWriteString.INSTANCE
                , new SerializerRead() {
                    public Object readValues(DataRead dr) throws BasicException {
                        return new CustomerInfo(
                                dr.getString(1),
                                dr.getString(2),
                                dr.getString(3));
                    }
                }).find(costo);
    }
    
    /*15-09-2014 JDP*/
    public int updateCustomerMaxDebt(CustomerInfoExt customer) throws BasicException {
     
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET MAXDEBT = ?"
                , new SerializerWrite() {
                    public void writeValues(DataWrite dp, Object obj) throws BasicException {
                        CustomerInfoExt c = (CustomerInfoExt) obj;
                        dp.setDouble(1, c.getMaxdebt());                      
                    }
                }).exec(customer);        
    } 
    
    /*28/12/2014 JDP*/
    public int updateCustomerCupos(CustomerInfoExt customer) throws BasicException {
    	if (customer.getCurdebt()==null)
    	{
    		customer.setCurdebt(new Double(0.0));
    	}
    	//"UPDATE CUSTOMERS SET MAXDEBT = IF(? >= CURDEBT, IF(MAXDEBT IS NULL, 0, MAXDEBT + ?) - IF(CURDEBT IS NULL, 0,CURDEBT), IF(CURDEBT IS NULL, 0, CURDEBT-? )), CURDEBT = IF(? >= CURDEBT, 0, IF(CURDEBT IS NULL, ?, CURDEBT - ?) ), CUPARTESP = CUPARTESP + ? WHERE ID = ?"
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS " +
                  "SET MAXDEBT = IF(? >= CURDEBT, " +
                  "IF(MAXDEBT IS NULL, 0, MAXDEBT + ?) - IF(CURDEBT IS NULL, 0, CURDEBT), " +
                  "IF(CURDEBT IS NULL, IF(MAXDEBT IS NULL, ?, MAXDEBT + ?), " +
                  "IF(MAXDEBT IS NULL, ?, IF(CURDEBT <= ?, MAXDEBT + (CURDEBT - ?), MAXDEBT + (IF (? - CURDEBT >0, ? - CURDEBT, ? - CURDEBT)))))), " + 
                  "CURDEBT = IF(? >= CURDEBT, " + 
                  "0, " + 
                  "IF(CURDEBT IS NULL, 0, IF (MAXDEBT>=CURDEBT,0, IF ((MAXDEBT + ?) >= CURDEBT,0, CURDEBT - ? )))), " +
                  "CUPARTESP = CUPARTESP + ? " +
                  "WHERE ID = ? "
                , new SerializerWrite() {
                    public void writeValues(DataWrite dp, Object obj) throws BasicException {
                        CustomerInfoExt c = (CustomerInfoExt) obj;
                        dp.setDouble(1, c.getMaxdebt());     
                        dp.setDouble(2, c.getMaxdebt()); 
                        dp.setDouble(3, c.getMaxdebt());
                        dp.setDouble(4, c.getMaxdebt()); 
                        dp.setDouble(5, c.getMaxdebt()); 
                        dp.setDouble(6, c.getMaxdebt()); 
                        dp.setDouble(7, c.getMaxdebt()); 
                        dp.setDouble(8, c.getMaxdebt()); 
                        dp.setDouble(9, c.getMaxdebt()); 
                        dp.setDouble(10, c.getMaxdebt()); 
                        dp.setDouble(11, c.getMaxdebt()); 
                        dp.setDouble(12, c.getMaxdebt()); 
                        dp.setDouble(13, c.getMaxdebt());
                        dp.setDouble(14, c.getCupArtEsp());
                        dp.setString(15, c.getId());
                    }
                }).exec(customer);        
    } 
    
    public int updateCustomerCupoMAXDEBT(CustomerInfoExt customer) throws BasicException {
        
    	if (customer.getCurdebt()==null)
    	{
    		customer.setCurdebt(new Double(0.0));
    	}
    	//UPDATE CUSTOMERS SET MAXDEBT = IF(? >= CURDEBT, IF(MAXDEBT IS NULL, 0, MAXDEBT + ?) - IF(CURDEBT IS NULL, 0, CURDEBT), IF(CURDEBT IS NULL, ?, CURDEBT -?) ), CURDEBT = IF(? >= CURDEBT, 0, IF(CURDEBT IS NULL, 0, CURDEBT - ?) ) WHERE ID = ?
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS " +
                  "SET MAXDEBT = IF(? >= CURDEBT, " +
                  "IF(MAXDEBT IS NULL, 0, MAXDEBT + ?) - IF(CURDEBT IS NULL, 0, CURDEBT), " +
                  "IF(CURDEBT IS NULL, IF(MAXDEBT IS NULL, ?, MAXDEBT + ?), " +
                  "IF(MAXDEBT IS NULL, ?, IF(CURDEBT <= ?, MAXDEBT + (CURDEBT - ?), MAXDEBT + (IF (? - CURDEBT >0, ? - CURDEBT, ? - CURDEBT)))))), " + 
                  "CURDEBT = IF(? >= CURDEBT, " + 
                  "0, " + 
                  "IF(CURDEBT IS NULL, 0, IF (MAXDEBT>=CURDEBT,0, IF ((MAXDEBT + ?) >= CURDEBT,0, CURDEBT - ? )))) " + 
                  "WHERE ID = ? "
                , new SerializerWrite() {
                    public void writeValues(DataWrite dp, Object obj) throws BasicException {
                        CustomerInfoExt c = (CustomerInfoExt) obj;
                        dp.setDouble(1, c.getMaxdebt());     
                        dp.setDouble(2, c.getMaxdebt()); 
                        dp.setDouble(3, c.getMaxdebt());
                        dp.setDouble(4, c.getMaxdebt()); 
                        dp.setDouble(5, c.getMaxdebt()); 
                        dp.setDouble(6, c.getMaxdebt()); 
                        dp.setDouble(7, c.getMaxdebt()); 
                        dp.setDouble(8, c.getMaxdebt()); 
                        dp.setDouble(9, c.getMaxdebt()); 
                        dp.setDouble(10, c.getMaxdebt()); 
                        dp.setDouble(11, c.getMaxdebt()); 
                        dp.setDouble(12, c.getMaxdebt()); 
                        dp.setDouble(13, c.getMaxdebt()); 
                        dp.setString(14, c.getId());
                    }
                }).exec(customer);        
    } 
    
    public int updateCustomerCupoCUPARTESP(CustomerInfoExt customer) throws BasicException {
        
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET CUPARTESP = CUPARTESP + ? WHERE ID = ?"
                , new SerializerWrite() {
                    public void writeValues(DataWrite dp, Object obj) throws BasicException {
                        CustomerInfoExt c = (CustomerInfoExt) obj;                  
                        dp.setDouble(1, c.getCupArtEsp());
                        dp.setString(2, c.getId());
                    }
                }).exec(customer);        
    } 
    
    public int updateCustomerExt(CustomerInfoExt customer) throws BasicException {
     
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET ADDRESS = ?, NOTES = ? WHERE ID = ?"
                , new SerializerWrite() {
                    public void writeValues(DataWrite dp, Object obj) throws BasicException {
                        CustomerInfoExt c = (CustomerInfoExt) obj;
                        dp.setString(1, c.getAddress());
                        dp.setString(2, c.getNotes());
                        dp.setString(3, c.getId());
                    }
                }).exec(customer);        
    }
    
    public CustomerInfoExt findCustomerExt(String card) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, NAME, CARD, ADDRESS, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT, SOBRECUPO, TIPOCUENTAID, CORELE, CELCON, NOMCON, CUPARTESP, CONCUPCLI, CONARTESP FROM CUSTOMERS WHERE CARD = ?"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(card);        
    }    
    
    public CustomerInfoExt loadCustomerExt(String id) throws BasicException {
        return (CustomerInfoExt) new PreparedSentence(s
                , "SELECT ID, TAXID, NAME, CARD, ADDRESS, NOTES, MAXDEBT, VISIBLE, CURDATE, CURDEBT, SOBRECUPO, TIPOCUENTAID, CORELE, CELCON, NOMCON, CUPARTESP, CONCUPCLI, CONARTESP, APLICAIVA FROM CUSTOMERS WHERE ID = ?"
                , SerializerWriteString.INSTANCE
                , new CustomerExtRead()).find(id);        
    }
    
    public final SentenceList getReservationsList() {
        return new PreparedSentence(s
            , "SELECT R.ID, R.CREATED, R.DATENEW, C.CUSTOMER, CUSTOMERS.TAXID, COALESCE(CUSTOMERS.NAME, R.TITLE),  R.CHAIRS, R.ISDONE, R.DESCRIPTION " +
              "FROM RESERVATIONS R LEFT OUTER JOIN RESERVATION_CUSTOMERS C ON R.ID = C.ID LEFT OUTER JOIN CUSTOMERS ON C.CUSTOMER = CUSTOMERS.ID " +
              "WHERE R.DATENEW >= ? AND R.DATENEW < ?"
            , new SerializerWriteBasic(new Datas[] {Datas.TIMESTAMP, Datas.TIMESTAMP})
            , new SerializerReadBasic(customerdatas));             
    }
    
    public final SentenceExec getReservationsUpdate() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                return new PreparedSentence(s
                    , "UPDATE RESERVATIONS SET ID = ?, CREATED = ?, DATENEW = ?, TITLE = ?, CHAIRS = ?, ISDONE = ?, DESCRIPTION = ? WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 5, 6, 7, 8, 0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsDelete() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                new PreparedSentence(s
                    , "DELETE FROM RESERVATION_CUSTOMERS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
                return new PreparedSentence(s
                    , "DELETE FROM RESERVATIONS WHERE ID = ?"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0})).exec(params);
            }
        };
    }
    
    public final SentenceExec getReservationsInsert() {
        return new SentenceExecTransaction(s) {
            public int execInTransaction(Object params) throws BasicException {  
    
                int i = new PreparedSentence(s
                    , "INSERT INTO RESERVATIONS (ID, CREATED, DATENEW, TITLE, CHAIRS, ISDONE, DESCRIPTION) VALUES (?, ?, ?, ?, ?, ?, ?)"
                    , new SerializerWriteBasicExt(customerdatas, new int[]{0, 1, 2, 5, 6, 7, 8})).exec(params);

                if (((Object[]) params)[3] != null) {
                    new PreparedSentence(s
                        , "INSERT INTO RESERVATION_CUSTOMERS (ID, CUSTOMER) VALUES (?, ?)"
                        , new SerializerWriteBasicExt(customerdatas, new int[]{0, 3})).exec(params);                
                }
                return i;
            }
        };
    }
    
    public final TableDefinition getTableCustomers() {
        return tcustomers;
    }  
    
    private static class CustomerExtRead implements SerializerRead {
        public Object readValues(DataRead dr) throws BasicException {
            CustomerInfoExt c = new CustomerInfoExt(
                    dr.getString(1),
                    dr.getString(2),
                    dr.getString(3));
                    c.setCard(dr.getString(4));
                    c.setAddress(dr.getString(5));
                    c.setNotes(dr.getString(6));
                    c.setMaxdebt(dr.getDouble(7));
                    c.setVisible(dr.getBoolean(8).booleanValue());
                    c.setCurdate(dr.getTimestamp(9));
                    c.setCurdebt(dr.getDouble(10));
                    c.setSobreCupo(dr.getBoolean(11).booleanValue());   /*15/12/2014*/
                    c.setTipoCuenta(dr.getString(12));                  /*17/12/2014*/
                    c.setCorEle(dr.getString(13));
                    c.setCelCon(dr.getString(14));
                    c.setNomCon(dr.getString(15));
                    c.setCupArtEsp(dr.getDouble(16));
                    c.setConCupCli(dr.getDouble(17));
                    c.setConArtEsp(dr.getDouble(18));   
                    c.setAplicaIva(dr.getBoolean(19).booleanValue());
            return c;
        }                  
    }
    
    /* 25/06/2017 JDP */
    public List<RecargaCupoParamInfo> getRecargaAutomaticaFecha() throws BasicException  {
        return new PreparedSentence(s
            , "SELECT ID, FECHA, VALOR, ACTIVO FROM RECARGACUPOPARAM WHERE DATE(FECHA)=CURRENT_DATE() AND ACTIVO=TRUE"
            , SerializerWriteString.INSTANCE
            , new SerializerReadClass(RecargaCupoParamInfo.class)).list();
    }
    
    public int updateRecargaCupoParam(String id) throws BasicException {
    	 Object[] values = new Object[] {id};
         Datas[] datas = new Datas[] {Datas.STRING};
        
         return new PreparedSentence(s
                , "UPDATE RECARGACUPOPARAM SET ACTIVO = FALSE WHERE ID=?"
                , new SerializerWriteBasicExt(datas, new int[] {0})).exec(values);
    }

    public int insertRecargaCupoParam(String id, CalendarioInfo itemRecarga) throws BasicException {
		
		 Object[] values = new Object[] {id, itemRecarga.getFecha(), itemRecarga.getValor()};
         Datas[] datas = new Datas[] {Datas.STRING, Datas.TIMESTAMP, Datas.DOUBLE};
        
         return new PreparedSentence(s
                , "INSERT INTO RECARGACUPOPARAM (ID,FECHA,VALOR) VALUES(?,?,?)"
                , new SerializerWriteBasicExt(datas, new int[] {0,1,2})).exec(values);
    } 

    /// JDP001
    public int updateCustomerMaxDebt() throws BasicException {
     
        return new PreparedSentence(s
                , "UPDATE CUSTOMERS SET MAXDEBT = CUPOAUTO"
                , null
                ).exec();        
    } 
	
	
   
    
    
}
