package com.openbravo.data.loader;


import com.openbravo.basic.BasicException;

public class SerializerWriteParams
  implements SerializerWrite<DataParams>
{
  public static final SerializerWrite INSTANCE = new SerializerWriteParams();
  
  public void writeValues(DataWrite paramDataWrite, DataParams paramDataParams)
    throws BasicException
  {
    paramDataParams.setDataWrite(paramDataWrite);
    paramDataParams.writeValues();
    paramDataParams.setDataWrite(null);
  }
}
