// %1023788050840:com.gridnode.pdip.base.time.value%
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

public class ValueFieldInfo
{
  protected String _fieldName;
  protected int _valueKind;
  protected String _fieldType;
  protected boolean _isMultiple;

  /**
   * Creates a new ValueFieldInfo object.
   * 
   * @param name DOCUMENT ME!
   * @param type DOCUMENT ME!
   * @param valueKind DOCUMENT ME!
   */
  public ValueFieldInfo(String name, String type, int valueKind)
  {
    this(name, type, valueKind, false);
  }

  /**
   * Creates a new ValueFieldInfo object.
   * 
   * @param name DOCUMENT ME!
   * @param type DOCUMENT ME!
   * @param valueKind DOCUMENT ME!
   * @param isMultiple DOCUMENT ME!
   */
  public ValueFieldInfo(String name, String type, int valueKind, 
                        boolean isMultiple)
  {
    _fieldName = name;
    _fieldType = type;
    _valueKind = valueKind;
    _isMultiple = isMultiple;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getFieldName()
  {
    return _fieldName;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getFieldType()
  {
    return _fieldType;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public int getValueKind()
  {
    return _valueKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public boolean getIsMultiple()
  {
    return _isMultiple;
  }
}