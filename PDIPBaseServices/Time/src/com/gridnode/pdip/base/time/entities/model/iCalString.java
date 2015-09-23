// %1023788042715:com.gridnode.pdip.base.entity%
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

public class iCalString
  extends iCalValue
  implements IiCalString
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 680804551650960778L;
	protected String _strValue;

  /**
   * Creates a new iCalString object.
   */
  public iCalString()
  {
  }

  /**
   * Creates a new iCalString object.
   * 
   * @param value DOCUMENT ME!
   */
  public iCalString(String value)
  {
    _strValue = value;
  }

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalString.class.getName();
  }

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
  public String getStrValue()
  {
    return _strValue;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setStrValue(String value)
  {
    _strValue = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Object getValue()
  {
    return getStrValue();
  }
}
