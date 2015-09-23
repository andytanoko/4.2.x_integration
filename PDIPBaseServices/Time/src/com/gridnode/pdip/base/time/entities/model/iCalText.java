// %1023788042856:com.gridnode.pdip.base.entity%
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

public class iCalText
  extends iCalValue
  implements IiCalText
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3369707592719803538L;
	protected String _textValue;

  /**
   * Creates a new iCalText object.
   */
  public iCalText()
  {
  }

  /**
   * Creates a new iCalText object.
   * 
   * @param value DOCUMENT ME!
   */
  public iCalText(String value)
  {
    _textValue = value;
  }

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalText.class.getName();
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
  public String getTextValue()
  {
    return _textValue;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setTextValue(String value)
  {
    _textValue = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Object getValue()
  {
    return getTextValue();
  }
}
