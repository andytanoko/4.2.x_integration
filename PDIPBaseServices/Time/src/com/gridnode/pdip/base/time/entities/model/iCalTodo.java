// %1023788042887:com.gridnode.pdip.base.entity%
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

import java.util.Date;

public class iCalTodo
  extends iCalComponent
  implements IiCalTodo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2940167739397839243L;
	//getNextOccurTime(DateTime  time);
  // fields for todo only
  protected Date _completeDt;  //UTC time
  protected Integer _percentCompleted;  // <100

  // ******************* Methods from AbstractEntity ******************

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalTodo.class.getName();
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
  public Date getCompleteDt()
  {
    return _completeDt;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setCompleteDt(Date value)
  {
    _completeDt = value;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public Integer getPercentCompleted()
  {
    return _percentCompleted;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param value DOCUMENT ME!
   */
  public void setPercentCompleted(Integer value)
  {
    _percentCompleted = value;
  }
}
