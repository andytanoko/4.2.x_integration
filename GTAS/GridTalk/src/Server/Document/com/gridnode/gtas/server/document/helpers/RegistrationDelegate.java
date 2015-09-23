/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistrationDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 11 2003    Koh Han Sing        Created
 * May 20 2003    Neo Sok Lay         Add hasValidLicense() method.
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceHome;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This class provides Registration related services.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class RegistrationDelegate
{

  /**
   * Obtain the EJBObject for the RegistrationServiceBean.
   *
   * @return The EJBObject to the RegistrationServiceBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IRegistrationServiceObj getManager()
    throws ServiceLookupException
  {
    return (IRegistrationServiceObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IRegistrationServiceHome.class.getName(),
      IRegistrationServiceHome.class,
      new Object[0]);
  }

  public static boolean hasValidLicense() throws Exception
  {
    return getManager().isLicenseValid();
  }
}