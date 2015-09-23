// %1023788042387:com.gridnode.pdip.base.entity%
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

package com.gridnode.pdip.base.time.entities.model;

public class iCalEvent
  extends iCalComponent
  implements IiCalEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9066391787134486589L;
	//fields for event only
  protected Integer _timeTransparency;  //defined in ITransparent

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   *
   * @return DOCUMENT ME!
   */
  public String getEntityName()
  {
    return iCalEvent.class.getName();
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
  public Integer getTimeTransparency()
  {
    return _timeTransparency;
  }

  /**
   * DOCUMENT ME!
   *
   * @param value DOCUMENT ME!
   */
  public void setTimeTransparency(Integer value)
  {
    _timeTransparency = value;
  }

}
