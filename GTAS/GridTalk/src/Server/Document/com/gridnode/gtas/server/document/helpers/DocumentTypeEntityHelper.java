/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.model.DocumentType;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the DocumentType entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class DocumentTypeEntityHelper implements ICheckConflict
{

  private static DocumentTypeEntityHelper _self = null;

  private DocumentTypeEntityHelper()
  {
    super();
  }

  public static DocumentTypeEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(DocumentTypeEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new DocumentTypeEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity documentType) throws Exception
  {
    Logger.debug("[DocumentTypeEntityHelper.checkDuplicate] Start");
    String docType = documentType.getFieldValue(DocumentType.DOC_TYPE).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DocumentType.DOC_TYPE, filter.getEqualOperator(),
      docType, false);

    DocumentTypeEntityHandler handler = DocumentTypeEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (DocumentType)results.iterator().next();
    }
    return null;
  }
}