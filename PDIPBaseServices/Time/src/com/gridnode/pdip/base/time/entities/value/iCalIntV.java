package com.gridnode.pdip.base.time.entities.value;

 
public class iCalIntV extends iCalValueV
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7154550594003738057L;
	protected  int _intValue=0;

  public iCalIntV(int kind)
  {
    if(kind != iCalValueKind.ICAL_INTEGER_VALUE)
      throw new IllegalArgumentException("Wrong Kind="+kind);
    _kind = (short)kind;
  }
  
    public iCalIntV(Integer value)
  {
    _kind = (short)iCalValueKind.ICAL_INTEGER_VALUE;
    _intValue = value.intValue();
  }

  public void setIntValue(int value)
  {
    _intValue = value;
  }
	
	public int getIntValue()
	{
	  return _intValue;
	}
	
  static final int ICAL_INT_VALUE = 1;
	static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[]
	{
	  new ValueFieldInfo("_intValue", "int", ICAL_INT_VALUE)
	};
	
 public  ValueFieldInfo[] getFieldInfos()
 {
   return FieldInfo;
 }
	
	public iCalValueV parseStr(String in) 
	{
    _intValue = Integer.parseInt(in);
    return this;
  }

  public String toString()
  {
  	return "" + getIntValue();
  }
}
