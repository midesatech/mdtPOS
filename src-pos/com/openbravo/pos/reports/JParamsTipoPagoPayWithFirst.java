package com.openbravo.pos.reports;

import java.util.List;

public class JParamsTipoPagoPayWithFirst extends JParamsTipoPagoPay {

/** Creates a new instance of JParamsLocationWithFirst */
public JParamsTipoPagoPayWithFirst() {
  super();
}

protected void addFirst(List a) {
  a.add(0, null);
}    
}
