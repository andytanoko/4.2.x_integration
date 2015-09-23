/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISessionAuditLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 07 2002    Ooi Hui Linn        Created
 */
package com.gridnode.pdip.base.session.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * LocalHome interface for SessionAudit
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public interface ISessionAuditLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new SessionAudit
   *
   * @param sessionAudit The new SessionAudit entity.
   * @return EJBLocalObject as a proxy to SessionAudit for the created SessionAudit.
   */
  public ISessionAuditLocalObj create(IEntity userAccount)
    throws CreateException;

  /**
   * Load a SessionAudit
   *
   * @param primaryKey The primary key to the SessionAudit record
   * @return EJBLocalObject as a proxy to the loaded SessionAuditBean.
   */
  public ISessionAuditLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find SessionAudit records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the ISessionAuditLocalObjs for the found users.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;


}
