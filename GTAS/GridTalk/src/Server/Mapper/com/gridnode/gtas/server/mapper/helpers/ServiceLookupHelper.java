/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateMappingFileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 06 2003    Koh Han Sing        Created
 * Nov 10 2005    Neo Sok Lay         Use local context to lookup XMLService
 */
package com.gridnode.gtas.server.mapper.helpers;

import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;

import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class perform service lookup.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ServiceLookupHelper
{

  public static IMappingManagerObj getManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }

  public static IXMLServiceLocalObj getXMLManager()
    throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }
}