/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITriggerLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 08 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerprocess.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 * LocalHome interface for TriggerBean
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface ITriggerLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new Trigger
   *
   * @param trigger The Trigger entity.
   * @return EJBLocalObject as a proxy to TriggerBean for the created
   *         Trigger.
   */
  public ITriggerLocalObj create(IEntity trigger)
    throws CreateException;

  /**
   * Load a TriggerBean
   *
   * @param primaryKey The primary key to the Trigger record
   * @return EJBLocalObject as a proxy to the loaded TriggerBean.
   */
  public ITriggerLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Trigger records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the ITriggerLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}