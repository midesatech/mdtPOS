package com.openbravo.pos.inventory;

import java.io.Serializable;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

public class ProductoVendidoInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2045978059242690764L;
	private String id_cus;
	private String id_pro;
	private String producto;
	private Double cantidad;
	
	public ProductoVendidoInfo(){
		this.setId_cus(null);
		this.setId_pro(null);
		this.setProducto(null);
		this.setCantidad(0.0);
	}
	
	public ProductoVendidoInfo(String id_cus, String id_pro, String producto, Double cantidad){
		this.setId_cus(id_cus);
		this.setId_pro(id_pro);
		this.setProducto(producto);
		this.setCantidad(cantidad);
	}

	@Override
	public Object getKey() {
		Object[] objeto= new Object[]{this.getId_cus(), this.getId_pro()};
		return objeto;
	}

	@Override
	public void writeValues(DataWrite dp) throws BasicException {
		dp.setString(1, this.getId_cus());
		dp.setString(2, this.getId_pro());
		dp.setString(3, this.getProducto());
		dp.setDouble(4,  this.getCantidad());
		
	}

	@Override
	public void readValues(DataRead dr) throws BasicException {
		this.setId_cus(dr.getString(1));
		this.setId_pro(dr.getString(2));
		this.setProducto(dr.getString(3));
		this.setCantidad(dr.getDouble(4));
	}	
	
	public String getId_pro() {
		return id_pro;
	}

	public void setId_pro(String id_pro) {
		this.id_pro = id_pro;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the id_cus
	 */
	public String getId_cus() {
		return id_cus;
	}

	/**
	 * @param id_cus the id_cus to set
	 */
	public void setId_cus(String id_cus) {
		this.id_cus = id_cus;
	}



}
