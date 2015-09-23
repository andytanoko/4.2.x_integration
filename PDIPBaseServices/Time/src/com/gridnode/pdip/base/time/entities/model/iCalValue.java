// %1023788043106:com.gridnode.pdip.base.entity%
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
package com.gridnode.pdip.base.time.entities.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
 
abstract public class iCalValue
  extends AbstractEntity
  implements IiCalValue
{
  protected Short _compKind;  //the table iCalCompId refers to.
  protected Short _propKind;  //the property's kind this value refers to
  protected Short _paramKind;  //The parameter's Kind the value refers to
  protected Short _refKind;  //the kind the refId value refers to:
  protected Short _valueKind;
  protected Long _iCalCompId;
  protected Long _refId;  //which iCal Property or Parameter or Value

  /**
   * DOCUMENT ME!
   * 
   * @param compKind DOCUMENT ME!
   * @param iCalCompId DOCUMENT ME!
   * @param propKind DOCUMENT ME!
   * @param paramKind DOCUMENT ME!
   * @param refKind DOCUMENT ME!
   * @param refId DOCUMENT ME!
   */
  public void setRefFields(Short compKind, Long iCalCompId, Short propKind, 
                           Short paramKind, Short refKind, Long refId)
  {
    _compKind = compKind;
    _propKind = propKind;
    _paramKind = paramKind;
    _refKind = refKind;
    //      _valueKind = valueKind;
    _iCalCompId = iCalCompId;
    _refId = refId;
  }

  //    static getiCalValueFor(Short compKind, Short  propKind, Short  paramKind,
  //    Short  refKind, Short  valueKind,  Long  iCalCompId, Long  iCalCompId, Long  refId, Object value)
  //    {
  //       if(Object instanceof Integer)
  //         new ICal
  //    }
  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityDescr()
  {
    return getEntityName();
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Number getKeyId()
  {
    return UID;
  }

  // ******************* get/set Methods******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getCompKind()
  {
    return _compKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setCompKind(Short value)
  {
    _compKind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getPropKind()
  {
    return _propKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setPropKind(Short value)
  {
    _propKind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getParamKind()
  {
    return _paramKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setParamKind(Short value)
  {
    _paramKind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getRefKind()
  {
    return _refKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setRefKind(Short value)
  {
    _refKind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Short getValueKind()
  {
    return _valueKind;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setValueKind(Short value)
  {
    _valueKind = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Long getiCalCompId()
  {
    return _iCalCompId;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setiCalCompId(Long value)
  {
    _iCalCompId = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Long getRefId()
  {
    return _refId;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setRefId(Long value)
  {
    _refId = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  abstract public Object getValue();
}
