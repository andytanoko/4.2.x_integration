/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAttachmentLocalHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 01 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
 
/**
 * LocalHome interface for Attachment
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IAttachmentLocalHome
       extends EJBLocalHome
{
  /**
   * Create a new Attachment
   *
   * @param attachment The Attachment entity.
   * @return EJBLocalObject as a proxy to AttachmentBean for the created
   *         Attachment.
   */
  public IAttachmentLocalObj create(IEntity attachment)
    throws CreateException;

  /**
   * Load a AttachmentBean
   *
   * @param primaryKey The primary key to the Attachment record
   * @return EJBLocalObject as a proxy to the loaded AttachmentBean.
   */
  public IAttachmentLocalObj findByPrimaryKey(Long primaryKey)
    throws FinderException;

  /**
   * Find Attachment records, using a data filter.
   *
   * @param filter The data filter.
   * @return A Collection of the IAttachmentLocalObjs for the found attachments.
   */
  public Collection findByFilter(IDataFilter filter)
    throws FinderException;

}