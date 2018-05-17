package com.openbravo.pos.customers;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.format.Formats;
import com.openbravo.pos.customers.CustomerInfo;
import com.openbravo.pos.panels.PaymentsModel.ReporteXZ;
import com.openbravo.pos.payment.PaymentInfo;
import com.openbravo.pos.ticket.DiscountInfo;
import com.openbravo.pos.ticket.TicketLineInfo;
import com.openbravo.pos.ticket.UserInfo;
import com.openbravo.pos.util.*;

public class RecargaInfo implements SerializableRead, Externalizable{
	
	private static DateFormat m_dateformat = new SimpleDateFormat("hh:mm");
	private java.util.Date m_dDate= new Date();
    private CustomerInfo m_Customer;
    private String m_sId;
    private int m_iRecargaId;
    private Double m_valor;
    private String m_TipoCupo;
    private List<RecargaItems> listaItems;
    private Double m_Total;
    private String m_Card;
    private String m_NomCon;
    private String m_Cajero;
    
    private Boolean m_Efectivo;
    private Boolean m_Consignacion;
    private String m_NumConsignacion;

    
    

	

	public RecargaInfo()
    {
    	m_sId = UUID.randomUUID().toString();
    	m_iRecargaId = 0; // incrementamos 
   	    m_dDate= new Date();
   	    m_TipoCupo="";
   	    m_Customer = null;
   	    m_valor = new Double(0.0);    
   	    listaItems =new ArrayList<RecargaItems>();
   	    m_Total=new Double(0.0);
   	    m_Efectivo=false;
   	    m_Consignacion=false;
   	    m_NumConsignacion="";
    }
    
    @Override
	public void writeExternal(ObjectOutput out) throws IOException {
		// TODO Auto-generated method stub
    	out.writeObject(m_sId);
        out.writeInt(m_iRecargaId);    
        out.writeObject(m_Customer);
        out.writeObject(m_dDate);
        out.writeChars(m_TipoCupo);
        out.writeDouble(m_valor);
        out.writeBoolean(m_Efectivo);
        out.writeBoolean(m_Consignacion);
        out.writeChars(m_NumConsignacion);
		
	}
    
	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		// esto es solo para serializar tickets que no estan en la bolsa de tickets pendientes
        m_sId = (String) in.readObject();
        m_iRecargaId = in.readInt();
        m_Customer = (CustomerInfo) in.readObject();
        m_dDate = (Date) in.readObject();
        m_TipoCupo= in.readLine();
        m_valor = in.readDouble();
        m_Efectivo= in.readBoolean();
        m_Consignacion=in.readBoolean();
        m_NumConsignacion=(String) in.readObject();
        
		
	}

	

	@Override
	public void readValues(DataRead dr) throws BasicException {
		// TODO Auto-generated method stub
		    m_sId = dr.getString(1);
		    m_iRecargaId = dr.getInt(2).intValue();
	        m_dDate = dr.getTimestamp(3);	 
	        m_TipoCupo= dr.getString(4);
	        String customerid = dr.getString(5);
	        m_Customer = customerid == null 
	                ? null
	                : new CustomerInfo(dr.getString(5),dr.getString(6), dr.getString(7));
	        m_valor= dr.getDouble(9);
	        m_Efectivo= dr.getBoolean(10);
	        m_Consignacion=dr.getBoolean(11);
	        m_NumConsignacion=dr.getString(12);
	      
	}
	
	 public String getId() {
	        return m_sId;
	 }	 
	 public int getRecargaId() {
	        return m_iRecargaId;
	 }
     public Date getDate() {
	        return m_dDate;
	 }
     public String getTipoCupo() {
	        return m_TipoCupo;
	 }
     public CustomerInfo getCustomer() {
         return m_Customer;
     }
     public String getCustomerId() {
         if (m_Customer == null) {
             return null;
         } else {
             return m_Customer.getId();
         }
     }
     public Double getValor()
     {
    	 return m_valor;
     }
     public List<RecargaItems> getRecargaItems()
     {
    	 return listaItems;
     }
     public String getCard() {
	        return m_Card;
	 }
     public String getNomCon() {
 		return m_NomCon;
 	}
     
     public Boolean getEfectivo()
     {
    	 return m_Efectivo;
     }
     
     public Boolean getConsignacion()
     {
         return m_Consignacion;
     }
     
     public String getNumConsginacion()
     {
         return m_NumConsignacion;
     }
     
     

 	public void setNomCon(String m_NomCon) {
 		this.m_NomCon = m_NomCon;
 	}
     
     public void setId(String id) {
	        m_sId=id;;
	 }
     public void setRecargaId(int iRecargaId) {
         m_iRecargaId = iRecargaId;         
     }
     public void setDate(Date dDate) { 
	        m_dDate = dDate;
	 }
     public void setTipoCupo(String tipo) {
	        m_TipoCupo=tipo;
	 }
     public void setCustomer(CustomerInfo value) {
         m_Customer = value;
     }
     public void setValor (Double valor)
     {
    	 m_valor=valor;
     }
     public void setRecargaItem (String TipoCupo, Double Valor)
     {
    	 listaItems.add(new RecargaItems (TipoCupo,Valor));
    	 this.m_Total+=Valor;
     }
     public void setCard(String card) {
	        m_Card=card;
	 }     
     
     public Double getTotal()
     { 
    	 return m_Total;
     }
     
     
     public String getCajero() {
 		return m_Cajero;
 	 }

 	public void setCajero(String m_Cajero) {
 		this.m_Cajero = m_Cajero;
 	}
 	
 	
 	public void setEfectivo(Boolean a)
    {
   	 this.m_Efectivo=a;
    }
    
    public void setConsignacion(Boolean a)
    {
        this.m_Consignacion=a;
    }
    
    public void setNumConsginacion(String a)
    {
        this.m_NumConsignacion=a;
    }
     
     public String printId() {
         if (m_iRecargaId > 0) {
             // valid ticket id
             return Formats.INT.formatValue(new Integer(m_iRecargaId));
         } else {
             return "";
         }
     }
     public String printDate() {
         return Formats.TIMESTAMP.formatValue(m_dDate);
     }    
     public String printCustomerName() {
         return m_Customer == null ? "" : m_Customer.getName();
     }
     public String printTipoCupo() {
         return m_TipoCupo;
     }
     public String printValor()
     {
    	 return Formats.CURRENCY.formatValue(new Double(getValor()));
     } 
     
     public String printTotal()
     {
    	 return Formats.CURRENCY.formatValue(new Double(m_Total));
     }  
     public String printCard()
	 {
		 return m_Card;
	 }
     public String printNomCon()
	 {
		 return getNomCon();
	 }
     public String printCajero() {
  		return m_Cajero;
  	 }
     
     
     
     
     
     public String printNumConsignacion()
     {
         return m_NumConsignacion;
     }
     

     public static class RecargaItems 
     {
    	 public RecargaItems(String t, Double v)
    	 {
    	    this.tipocupo=t;
    	    this.valor=v;
    	 }
    	 
     	 private String tipocupo;
     	 public String getTipocupo() {
			return tipocupo;
		}
		public void setTipocupo(String tipocupo) {
			this.tipocupo = tipocupo;
		}
		public Double getValor() {
			return valor;
		}
		public void setValor(Double valor) {
			this.valor = valor;
		}
		private Double valor;
     	 
		 public String printTipoCupo() {
	         return tipocupo;
	     }
		 public String printValor()
	     {
	    	 return Formats.CURRENCY.formatValue(new Double(this.valor));
	     }   
     }
}


