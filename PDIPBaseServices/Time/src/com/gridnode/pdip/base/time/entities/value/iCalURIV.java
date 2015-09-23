// %1023788049231:com.gridnode.pdip.base.time.value%
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

 
public class iCalURIV
  extends iCalTextV
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3162755357545292800L;

	/**
   * Creates a new iCalURIV object.
   * 
   * @param kind DOCUMENT ME!
   */
  public iCalURIV(int kind)
  {
    if (kind != iCalValueKind.ICAL_URI_VALUE)
      throw new IllegalArgumentException("Wrong Kind=" + kind);
    _kind = (short)kind;
  }

  /**
   * Creates a new iCalURIV object.
   * 
   * @param value DOCUMENT ME!
   */
  public iCalURIV(String value)
  {
    _kind = (short) iCalValueKind.ICAL_URI_VALUE;
    _text = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getURI()
  {
    return getTextValue();
  }

}