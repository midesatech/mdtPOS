package com.openbravo.pos.reports;

import java.util.List;

public class JParamsTipoPagoRecargaWithFirst  extends JParamsTipoPagoRecarga {

/** Creates a new instance of JParamsLocationWithFirst */
public JParamsTipoPagoRecargaWithFirst() {
  super();
}

protected void addFirst(List a) {
  a.add(0, null);
}    
}
