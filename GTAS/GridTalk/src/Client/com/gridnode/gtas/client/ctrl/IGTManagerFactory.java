/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTManagerFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-07-26     Andrew Hill         Added getManagerType() method
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

/**
 * Used by GTSession to obtain managers.
 */
interface IGTManagerFactory
{
  /**
   * Return an IGTManager implementing object.
   * @param managerType as defined in IGTManager
   * @return IGTmanager object
   * @throws GTClientException
   */
  public IGTManager getManager(int managerType) throws GTClientException;

  public int getManagerType(String entityType) throws GTClientException;
}