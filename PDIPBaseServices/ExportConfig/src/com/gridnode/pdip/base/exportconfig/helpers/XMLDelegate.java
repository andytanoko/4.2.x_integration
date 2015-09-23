/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: XMLDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 13 2003    Koh Han Sing        Created
 * Nov 10 2005    Neo Sok Lay         Use Local context for lookup
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides XML services.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class XMLDelegate
{

  /**
   * Obtain the EJBObject for the XMLServiceBean.
   *
   * @return The EJBObject to the XMLServiceBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IXMLServiceLocalObj getManager()
    throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }
}