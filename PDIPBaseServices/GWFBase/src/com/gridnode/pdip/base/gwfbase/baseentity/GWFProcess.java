// %1023949932842:com.gridnode.pdip.base.gwfbase.gwfentity%
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
/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. Date           Author              Changes May 23 2002
 * Mahesh              Created
 */
package com.gridnode.pdip.base.gwfbase.baseentity;

import com.gridnode.pdip.framework.db.entity.*;

public abstract class GWFProcess
  extends AbstractEntity
  implements IGWFProcess
{
  Integer _maxConcurrency;

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
  public Integer getMaxConcurrency()
  {
    return _maxConcurrency;
  }

  /**
   * DOCUMENT ME!
   * 
   * @param maxConcurrency DOCUMENT ME!
   */
  public void setMaxConcurrency(Integer maxConcurrency)
  {
    _maxConcurrency = maxConcurrency;
  }
}