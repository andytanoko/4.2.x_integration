// %1023949932905:com.gridnode.pdip.base.gwfbase.gwfentity%
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
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File:  Date           Author              Changes Jun 10
 * 2002   Mahesh          Created Jun 13 2002   Mathew       Repackaged
 */
/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File:  Date           Author              Changes Jun 11
 * 2002    Liu Xiao Hua          Created
 */
package com.gridnode.pdip.base.gwfbase.baseentity;

import com.gridnode.pdip.framework.db.entity.*;

public abstract class GWFTransition
  extends AbstractEntity
  implements IGWFTransition
{
  protected Long _processUId;
  protected String _processType;

  /**
   * DOCUMENT ME!
   * 
   * @param processUId DOCUMENT ME!
   */
  public void setProcessUID(Long processUId)
  {
    _processUId = processUId;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param processType DOCUMENT ME!
   */
  public void setProcessType(String processType)
  {
    _processType = processType;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
  public Long getProcessUID()
  {
    return _processUId;
  }

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
  public String getProcessType()
  {
    return _processType;
  }
}