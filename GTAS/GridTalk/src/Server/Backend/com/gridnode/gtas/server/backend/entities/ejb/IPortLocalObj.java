/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPortLocalObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 17 2002    Koh Han Sing        Created
 * May 27 2003    Jagadeesh           Added : To support generating sequential no.
 */
package com.gridnode.gtas.server.backend.entities.ejb;

import com.gridnode.pdip.framework.db.ejb.IEntityLocalObject;

/**
 * LocalObj interface for PortBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPortLocalObj
       extends IEntityLocalObject
{
  /**
   * Returns a sequential no for port entity.
   * @return String a sequential no, using port entity fields such as
   * startNum,rollOverNum,currentNum,isPadded,fixNumLength.
   *
   */
  public String getNextSeqRunningNo();
}