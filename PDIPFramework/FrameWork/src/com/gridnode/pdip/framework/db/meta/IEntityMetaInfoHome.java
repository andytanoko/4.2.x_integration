/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityMetaInfoHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Neo Sok Lay         Renamed IEntityMetaInfo to IEntityMetaInfoObj
 *                                    to follow naming convention.
 *                                    Add findByEntityName() finder method.
 */
package com.gridnode.pdip.framework.db.meta;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.*;

/**
 * Home interface for EntityMetaInfo entity bean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0
 */
public interface IEntityMetaInfoHome extends EJBHome
{
  public IEntityMetaInfoObj create(EntityMetaInfo metaInfo)
    throws CreateException, RemoteException;

  public IEntityMetaInfoObj findByPrimaryKey(String primaryKey)
    throws FinderException, RemoteException;

  public Collection findAllEntityMetaInfo()
    throws FinderException, RemoteException;

  /**
   * Find EntityMetaInfo using the name of the entity.
   *
   * @param entityName Name of the Entity.
   * @return Remote object for EntityMetaInfoBean.
   */
  public IEntityMetaInfoObj findByEntityName(String entityName)
    throws FinderException, RemoteException;

}