package com.openbravo.data.loader;


/* 
 * JOSE DE PAZ -JDP-
 * 11/06/2016 Entidad para transferencia de información 
 *            APPView.
 * 
 * 
 * */

import java.util.Date;

public class EntidadApp{
	
	
	private String properties;
	private Date activeCashDateStart;
	private Date activeCashDateEnd;
	private String activeCashIndex;
	private Session session;
	
	public EntidadApp()
	{
		
	}
	
	public String getHost() {
		return properties;
	}
	public void setHost(String properties) {
		this.properties = properties;
	}
	public Date getActiveCashDateStart() {
		return activeCashDateStart;
	}
	public void setActiveCashDateStart(Date activeCashDateStart) {
		this.activeCashDateStart = activeCashDateStart;
	}
	public Date getActiveCashDateEnd() {
		return activeCashDateEnd;
	}
	public void setActiveCashDateEnd(Date activeCashDateEnd) {
		this.activeCashDateEnd = activeCashDateEnd;
	}
	public String getActiveCashIndex() {
		return activeCashIndex;
	}
	public void setActiveCashIndex(String activeCashIndex) {
		this.activeCashIndex = activeCashIndex;
	}
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}	
	
}

