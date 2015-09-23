
// %1023788049340:com.gridnode.pdip.base.time.value%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File:
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

import com.gridnode.pdip.base.time.entities.model.iCalDate;
import com.gridnode.pdip.base.time.entities.model.iCalInt;
import com.gridnode.pdip.base.time.entities.model.iCalString;
import com.gridnode.pdip.base.time.entities.model.iCalText;
import com.gridnode.pdip.base.time.entities.model.iCalValue;
import com.gridnode.pdip.framework.util.ReflectionUtility;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

abstract public class iCalValueV
  implements java.io.Serializable,
             IEntityDAOs
{
  final static String LogCat = iCalValueV.class.getName();
  protected short _kind;  //parameter Kind

  //   private Object _value;

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public short getKind()
  {
    return _kind;
  }

  /**
   * DOCUMENT ME!
   *
   * @param value DOCUMENT ME!
   */
  public void setKind(short value)
  {
    _kind = value;
  }

  /**
   * Creates a new iCalValueV object.
   */
  public iCalValueV()
  {
  }

  /**
   * Creates a new iCalValueV object.
   *
   * @param kind DOCUMENT ME!
   */
  public iCalValueV(short kind)
  {
    _kind = kind;
  }

  /**
   * Creates a new iCalValueV object.
   *
   * @param kind DOCUMENT ME!
   */
  public iCalValueV(int kind)
  {
    _kind = (short)kind;
  }

  public ValueFieldInfo[] getFieldInfos()
  {
    return null;
  }

  ValueFieldInfo getFieldInfo(int valueKind)
  {
    ValueFieldInfo[] infoArray = getFieldInfos();
    for (int i = 0; i < infoArray.length; i++)
    {
      ValueFieldInfo fieldInfo = infoArray[i];
      if (valueKind == fieldInfo.getValueKind())
        return fieldInfo;
    }
    return null;
  }

  void convertExtraFieldToValueEntity(List[] res)
  {
  }

  void setExtraValueEntity(List entityList)
  {
  }

  //arrays of iCalValue List

  /**
   * DOCUMENT ME!
   *
   * @param compKind DOCUMENT ME!
   * @param cUid DOCUMENT ME!
   * @param propKind DOCUMENT ME!
   * @param paramKind DOCUMENT ME!
   * @param refKind DOCUMENT ME!
   * @param refId DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public List[] toiCalValues(Short compKind, Long cUid, Short propKind,
                             Short paramKind, Short refKind, Long refId)
                      throws Exception
  {
    List[] res = new List[4];
    for (int i = 0; i < 4; i++)
    {
      res[i] = new ArrayList();
    }
    ValueFieldInfo[] fieldInfoArray = getFieldInfos();
    ValueFieldInfo fieldInfo = null;
    for (int i = 0; i < fieldInfoArray.length; i++)
    {
      fieldInfo = fieldInfoArray[i];
      convertFieldToValue(fieldInfo, res);
    }
    convertExtraFieldToValueEntity(res);
    Long valueUid = _intDAO.getNextKeyId();
    iCalValue topValue = new iCalInt();
    topValue.setFieldValue(topValue.getKeyId(), valueUid);
    topValue.setValueKind(new Short(_kind));
    topValue.setRefFields(compKind, cUid, propKind, paramKind, refKind, refId);
    for (int j = 0; j < 4; j++)
    {
      int size = res[j].size();
      for (int i = 0; i < size; i++)
      {
        iCalValue field = (iCalValue)res[j].get(i);
        field.setRefFields(compKind, cUid, propKind, paramKind,
                           IValueRefKind.IREF_VALUE, valueUid);
      }
    }
    res[0].add(0, topValue);
    return res;
  }

  public static final int ICAL_INT = 0;
  public static final int ICAL_DATE = 1;
  public static final int ICAL_STRING = 2;
  public static final int ICAL_TEXT = 3;

  static iCalValue getIcalValue(Object value, int type)
  {
    switch (type)
    {

    case ICAL_INT:
    {
      if (!(value instanceof Integer))
      {
        if (value instanceof Boolean)
          value = convertToInteger((Boolean)value);
        else if (value instanceof Short)
          value = convertToInteger((Short)value);
      }
      return new iCalInt((Integer)value);
    }

    case ICAL_DATE:
      return new iCalDate((Date)value);

    case ICAL_STRING:
      return new iCalString((String)value);

    case ICAL_TEXT:
      return new iCalText((String)value);
    }
    return null;
  }

  /**
   * DOCUMENT ME!
   *
   * @param fieldInfo DOCUMENT ME!
   * @param res DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void convertFieldToValue(ValueFieldInfo fieldInfo, List[] res)
                           throws Exception
  {
    String fieldName = fieldInfo.getFieldName();
    String fieldType = fieldInfo.getFieldType();
    int valueKind = fieldInfo.getValueKind();
    boolean isMultiple = fieldInfo.getIsMultiple();
    Field field = ReflectionUtility.getAccessibleField(getClass(), fieldName);
    Object fieldValue = field.get(this);
    if (fieldValue == null)
      return;
    int storeType = -1;
    String toClassLC = fieldType.toLowerCase();
    List valueList = new ArrayList();
    if (isMultiple)  //only for direct type List
    {
      valueList = (List)fieldValue;
    }
    else
      valueList.add(fieldValue);
    if (toClassLC.endsWith("integer") || toClassLC.endsWith("int"))
    {
      storeType = 0;
    }
    else if (toClassLC.endsWith("short") || toClassLC.endsWith("boolean"))
    {  //this two type need conversion to integer type
      storeType = 0;
    }
    else if (toClassLC.endsWith("date"))
    {
      storeType = 1;
    }
    else if (toClassLC.endsWith("string"))
    {
      storeType = 2;
    }
    else if (toClassLC.endsWith("text"))
    {
      storeType = 3;
    }
    if (valueList.isEmpty())
      return;
    int valueNum = valueList.size();
    iCalValue value = null;
    for (int i = 0; i < valueNum; i++)
    {
      Object obj = valueList.get(i);
      value = getIcalValue(obj, storeType);
      value.setValueKind(new Short((short)valueKind));
      res[storeType].add(value);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param valueEntityList DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void fromiCalValues(List valueEntityList)
                      throws Exception
  {
    List extraEntityList = new ArrayList();
    int size = valueEntityList.size();
    for (int i = 0; i < size; i++)
    {
      iCalValue valueEntity = (iCalValue)valueEntityList.get(i);
      Short valueKind = valueEntity.getValueKind();
      Object value = valueEntity.getValue();
      ValueFieldInfo fieldInfo = getFieldInfo(valueKind.intValue());
      if (fieldInfo == null)
      {
        extraEntityList.add(valueEntity);
        continue;
      }
      setValueToField(fieldInfo, value);
    }
    if (!extraEntityList.isEmpty())
      setExtraValueEntity(extraEntityList);
  }

  /**
   * DOCUMENT ME!
   *
   * @param fieldInfo DOCUMENT ME!
   * @param value DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  public void setValueToField(ValueFieldInfo fieldInfo, Object value)
                       throws Exception
  {
    String fieldName = fieldInfo.getFieldName();
    String fieldType = fieldInfo.getFieldType();
    boolean isMultiple = fieldInfo.getIsMultiple();
    Field field = ReflectionUtility.getAccessibleField(getClass(), fieldName);
    String toClassLC = fieldType.toLowerCase();
    if (toClassLC.endsWith("integer") || toClassLC.endsWith("int") ||
        toClassLC.endsWith("date") || toClassLC.endsWith("string") ||
        toClassLC.endsWith("text"))
    {
    }
    else if (toClassLC.endsWith("short"))
    {
      value = convertToShort((Number)value);
    }
    else if (toClassLC.endsWith("boolean"))
    {
      value = convertToBoolean((Number)value);
    }
    else
      throw new IllegalArgumentException("Wrong fieldType");
    if (!isMultiple)
    {
      field.set(this, value);
    }
    else
    {
      List valueList = (List)field.get(this);
      if (valueList == null)
      {
        valueList = new ArrayList();
        field.set(this, valueList);
      }
      valueList.add(value);
    }
  }

  /**
   * DOCUMENT ME!
   *
   * @param valueKind DOCUMENT ME!
   * @return DOCUMENT ME!
   * @throws Exception DOCUMENT ME!
   */
  static public iCalValueV getValueByKind(short valueKind)
                                   throws Exception
  {
    Class valueClass = ValueKindUtil.getValueClass(valueKind);
    if (valueClass == null)
      throw new IllegalArgumentException("Wrong valueKind " + valueKind);
    Class[] paramTypes = new Class[] {int.class};
    Object[] args = new Object[] {new Integer(valueKind)};
    Constructor construct = valueClass.getConstructor(paramTypes);
    return (iCalValueV)construct.newInstance(args);
  }

  /**
   * DOCUMENT ME!
   *
   * @param num DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static Integer convertToInteger(Number num)
  {
    return new Integer(num.intValue());
  }

  /**
   * DOCUMENT ME!
   *
   * @param num DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static Short convertToShort(Number num)
  {
    return new Short(num.shortValue());
  }

  /**
   * DOCUMENT ME!
   *
   * @param bool DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static Integer convertToInteger(Boolean bool)
  {
    return bool.booleanValue() ? new Integer(1) : new Integer(0);
  }

  /**
   * DOCUMENT ME!
   *
   * @param num DOCUMENT ME!
   * @return DOCUMENT ME!
   */
  public static Boolean convertToBoolean(Number num)
  {
    return (num.intValue() == 0) ? new Boolean(false) : new Boolean(true);
  }


   public iCalValueV parseStr(String in) throws ParseException
  {
    throw new IllegalArgumentException(getClass().getName() + ".parseStr not supported yet");
  }

  public String toString()
  {
    throw new IllegalArgumentException(getClass().getName() + ".toString not supported yet");
  }

}
