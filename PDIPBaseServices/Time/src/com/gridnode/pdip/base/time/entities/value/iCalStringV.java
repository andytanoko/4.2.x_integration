package com.gridnode.pdip.base.time.entities.value;


public class iCalStringV extends iCalValueV
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -713562330506531038L;
	protected  String _str;

  public iCalStringV(int kind)
  {
//    if(kind != iCalValueKind.ICAL_URI_VALUE)
//      throw new IllegalArgumentException("Wrong Kind="+kind);
    _kind = (short)kind;
  }

  public iCalStringV(int kind, String value)
  {
    this(kind);
    _str = value;
  }

  public String getString()
  {
    return _str;
  }

  static final int ICAL_STRING_VALUE = 1;
  static final ValueFieldInfo[] FieldInfo = new ValueFieldInfo[]
  {
    new ValueFieldInfo("_str", "String", ICAL_STRING_VALUE)
  };

  public ValueFieldInfo[] getFieldInfos()
  {
    return FieldInfo;
  }

  public iCalValueV parseStr(String in)
  {
     _str = in;
     return this;
  }

  public String toString()
  {
  	return _str;
  }

}
