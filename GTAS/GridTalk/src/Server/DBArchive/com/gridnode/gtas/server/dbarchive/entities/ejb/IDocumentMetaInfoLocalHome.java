/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentMetaInfoHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13, 2005        Tam Wei Xiang       Created
 */
package com.gridnode.gtas.server.dbarchive.entities.ejb;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

import java.util.Collection;
import javax.ejb.EJBLocalHome;
import javax.ejb.CreateException;
import javax.ejb.FinderException;

/**
 *
 *
 * Tam Wei Xiang
 * 
 * @version
 * @since
 */
public interface IDocumentMetaInfoLocalHome extends EJBLocalHome
{
  /**
   * Create a new DocumentMetaInfo
   *
   * @param DocumentMetaInfo The Attachment entity.
   * @return EJBLocalObject as a proxy to AttachmentBean for the created
   *         Attachment.
   */
  public IDocumentMetaInfoLocalObj create(IEntity documentMetaInfo)
    throws CreateException;
  
  /**
   * Create a new DocumentMetaInfo, can specify whether use the PK
   * in the entity obj or create a new PK.
   *
   * @param DocumentMetaInfo The Attachment entity.
   * @return EJBLocalObject as a proxy to AttachmentBean for the created
   *         Attachment.
   */
  public IDocumentMetaInfoLocalObj create(IEntity documentMetaInfo, Boolean isCreateNewUID)
    throws CreateException;
  
  /**
   * Load a AttachmentBean
   *
   * @param primaryKey The primary key to the DocumentMetaInfo record
   * @return EJBLocalObject as a proxy to the loaded DocumentMetaInfoBean.
   */
  public IDocumentMetaInfoLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find DocumentMetaInfo records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IDocumentMetaInfoLocalObjs for the found DocumentMetaInfo.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;
}
