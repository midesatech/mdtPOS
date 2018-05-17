package com.openbravo.pos.reports;

import java.util.List;

public class JParamsTipoCuentaWithFirst extends JParamsTipoCuenta {

/** Creates a new instance of JParamsLocationWithFirst */
public JParamsTipoCuentaWithFirst() {
  super();
}

protected void addFirst(List a) {
  a.add(0, null);
}    
}
