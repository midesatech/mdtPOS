package com.openbravo.pos.inventory;

import java.io.Serializable;

import com.openbravo.basic.BasicException;
import com.openbravo.data.loader.DataRead;
import com.openbravo.data.loader.DataWrite;
import com.openbravo.data.loader.IKeyed;
import com.openbravo.data.loader.SerializableRead;
import com.openbravo.data.loader.SerializableWrite;

public class AlertaProductosInfo implements SerializableRead, SerializableWrite, Serializable, IKeyed{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6031424658432628572L;
	private String m_sId;
	private String m_sIdPro;
	private String m_sIdLoc;
	private Boolean m_bBloque;
	private Boolean m_bVtaLim;
	private Integer m_iMaximo;
	
	
	
	public AlertaProductosInfo()
	{
		this.setM_sId(null);
		this.setM_sIdPro(null);
		this.setM_sIdLoc(null);
		this.setM_bBloque(false);
		this.setM_bVtaLim(false);
		this.setM_iMaximo(0);
	}
	
	public AlertaProductosInfo(String m_sId, String m_sIdPro, String m_sIdLoc, Boolean m_bBloque, Boolean m_bVtaLim, Integer m_iMaximo)
	{
		this.setM_sId(m_sId);
		this.setM_sIdPro(m_sIdPro);
		this.setM_sIdLoc(m_sIdLoc);
		this.setM_bBloque(m_bBloque);
		this.setM_bVtaLim(m_bVtaLim);
		this.setM_iMaximo(m_iMaximo);
	}
	
	@Override
	public Object getKey() {
		Object[] objeto= new Object[]{this.getM_sId(), this.getM_sIdPro(), this.getM_sIdLoc()};
		return objeto;
	}

	@Override
	public void writeValues(DataWrite dp) throws BasicException {
		dp.setString(1, this.getM_sId());
		dp.setString(2, this.getM_sIdPro());
		dp.setString(3, this.getM_sIdLoc());
		dp.setBoolean(4, this.getM_bBloque());
		dp.setBoolean(5, this.getM_bVtaLim());
		dp.setInt(6,  this.getM_iMaximo());
	
		
	}

	@Override
	public void readValues(DataRead dr) throws BasicException {
		this.setM_sId(dr.getString(1));
		this.setM_sIdPro(dr.getString(2));
		this.setM_sIdLoc(dr.getString(3));
		this.setM_bBloque(dr.getBoolean(4));
		this.setM_bVtaLim(dr.getBoolean(5));
		this.setM_iMaximo(dr.getInt(6));
	}

	/**
	 * @return the m_sId
	 */
	public String getM_sId() {
		return m_sId;
	}

	/**
	 * @param m_sId the m_sId to set
	 */
	public void setM_sId(String m_sId) {
		this.m_sId = m_sId;
	}

	/**
	 * @return the m_sIdPro
	 */
	public String getM_sIdPro() {
		return m_sIdPro;
	}

	/**
	 * @param m_sIdPro the m_sIdPro to set
	 */
	public void setM_sIdPro(String m_sIdPro) {
		this.m_sIdPro = m_sIdPro;
	}

	/**
	 * @return the m_sIdLoc
	 */
	public String getM_sIdLoc() {
		return m_sIdLoc;
	}

	/**
	 * @param m_sIdLoc the m_sIdLoc to set
	 */
	public void setM_sIdLoc(String m_sIdLoc) {
		this.m_sIdLoc = m_sIdLoc;
	}

	/**
	 * @return the m_bBloque
	 */
	public Boolean getM_bBloque() {
		return m_bBloque;
	}

	/**
	 * @param m_bBloque the m_bBloque to set
	 */
	public void setM_bBloque(Boolean m_bBloque) {
		this.m_bBloque = m_bBloque;
	}

	/**
	 * @return the m_bVtaLim
	 */
	public Boolean getM_bVtaLim() {
		return m_bVtaLim;
	}

	/**
	 * @param m_bVtaLim the m_bVtaLim to set
	 */
	public void setM_bVtaLim(Boolean m_bVtaLim) {
		this.m_bVtaLim = m_bVtaLim;
	}

	/**
	 * @return the m_iMaximo
	 */
	public Integer getM_iMaximo() {
		return m_iMaximo;
	}

	/**
	 * @param m_iMaximo the m_iMaximo to set
	 */
	public void setM_iMaximo(Integer m_iMaximo) {
		this.m_iMaximo = m_iMaximo;
	}

}
