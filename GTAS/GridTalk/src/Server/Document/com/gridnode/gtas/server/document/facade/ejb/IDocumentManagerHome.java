/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentManagerHome.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.facade.ejb;

import javax.ejb.EJBHome;
import javax.ejb.CreateException;

import java.rmi.RemoteException;

/**
 * LocalHome interface for DocumentTypeManagerBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IDocumentManagerHome
  extends        EJBHome
{
  /**
   * Create the DocumentTypeManagerBean.
   *
   * @returns EJBLObject for the DocumentTypeManagerBean.
   */
  public IDocumentManagerObj create()
    throws CreateException, RemoteException;
}