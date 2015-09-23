/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceLookupHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 23 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.searchquery.helpers;

import com.gridnode.pdip.app.searchquery.facade.ejb.ISearchQueryManagerHome;
import com.gridnode.pdip.app.searchquery.facade.ejb.ISearchQueryManagerObj;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class perform service lookup.
 *
 * @author Koh Han Sing
 *
 * @version 2.3
 * @since 2.3
 */

public class ServiceLookupHelper
{

  public static ISearchQueryManagerObj getManager()
    throws ServiceLookupException
  {
    return (ISearchQueryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ISearchQueryManagerHome.class.getName(),
      ISearchQueryManagerHome.class,
      new Object[0]);
  }
}