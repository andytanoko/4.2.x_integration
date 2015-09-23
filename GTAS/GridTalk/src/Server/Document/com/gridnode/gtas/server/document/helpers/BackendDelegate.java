/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BackendDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 20 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.backend.facade.ejb.IBackendServiceLocalHome;
import com.gridnode.gtas.server.backend.facade.ejb.IBackendServiceLocalObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides backend services.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class BackendDelegate
{

  /**
   * Obtain the EJBObject for the BackendServiceBean.
   *
   * @return The EJBObject to the BackendServiceBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IBackendServiceLocalObj getManager()
    throws ServiceLookupException
  {
    return (IBackendServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IBackendServiceLocalHome.class.getName(),
      IBackendServiceLocalHome.class,
      new Object[0]);
  }
}