package com.openbravo.data.loader;

import com.openbravo.basic.BasicException;
import java.util.Date;

public abstract class DataParams
  implements DataWrite
{
  protected DataWrite dw;
  
  public abstract void writeValues()
    throws BasicException;
  
  public void setInt(int paramInt, Integer paramInteger)
    throws BasicException
  {
    this.dw.setInt(paramInt, paramInteger);
  }
  
  public void setString(int paramInt, String paramString)
    throws BasicException
  {
    this.dw.setString(paramInt, paramString);
  }
  
  public void setDouble(int paramInt, Double paramDouble)
    throws BasicException
  {
    this.dw.setDouble(paramInt, paramDouble);
  }
  
  public void setBoolean(int paramInt, Boolean paramBoolean)
    throws BasicException
  {
    this.dw.setBoolean(paramInt, paramBoolean);
  }
  
  public void setTimestamp(int paramInt, Date paramDate)
    throws BasicException
  {
    this.dw.setTimestamp(paramInt, paramDate);
  }
  
  public void setBytes(int paramInt, byte[] paramArrayOfByte)
    throws BasicException
  {
    this.dw.setBytes(paramInt, paramArrayOfByte);
  }
  
  public void setObject(int paramInt, Object paramObject)
    throws BasicException
  {
    this.dw.setObject(paramInt, paramObject);
  }
  
  public DataWrite getDataWrite()
  {
    return this.dw;
  }
  
  public void setDataWrite(DataWrite paramDataWrite)
  {
    this.dw = paramDataWrite;
  }
}
