/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.document.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.document.entities.ejb.IDocumentTypeLocalHome;
import com.gridnode.gtas.server.document.entities.ejb.IDocumentTypeLocalObj;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the DocumentTypeBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class DocumentTypeEntityHandler
  extends          LocalEntityHandler
{
  private DocumentTypeEntityHandler()
  {
    super(DocumentType.ENTITY_NAME);
  }

  /**
   * Get an instance of a DocumentTypeEntityHandler.
   */
  public static DocumentTypeEntityHandler getInstance()
  {
    DocumentTypeEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(DocumentType.ENTITY_NAME, true))
    {
      handler = (DocumentTypeEntityHandler)EntityHandlerFactory.getHandlerFor(
                  DocumentType.ENTITY_NAME, true);
    }
    else
    {
      handler = new DocumentTypeEntityHandler();
      EntityHandlerFactory.putEntityHandler(DocumentType.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the DocumentType whose name is the specified.
   *
   * @param docType The name of the document type.
   * @return the DocumentType having the specified name.
   */
  public DocumentType findByDocumentType(String docType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DocumentType.DOC_TYPE, filter.getEqualOperator(),
      docType, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (DocumentType)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IDocumentTypeLocalHome.class.getName(),
      IDocumentTypeLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IDocumentTypeLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IDocumentTypeLocalObj.class;
  }
}