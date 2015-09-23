/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAttachmentRegistryLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
 
/**
 * LocalHome interface for AttachmentRegistry
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAttachmentRegistryLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new AttachmentRegistry
   *
   * @param attachment The AttachmentRegistry entity.
   * @return EJBLocalObject as a proxy to AttachmentRegistryBean for the created
   *         AttachmentRegistry.
   */
  public IAttachmentRegistryLocalObj create(IEntity attachment)
    throws CreateException;

  /**
   * Load a AttachmentRegistryBean
   *
   * @param primaryKey The primary key to the AttachmentRegistry record
   * @return EJBLocalObject as a proxy to the loaded AttachmentRegistryBean.
   */
  public IAttachmentRegistryLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find AttachmentRegistry records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAttachmentRegistryLocalObjs for the found attachments.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}