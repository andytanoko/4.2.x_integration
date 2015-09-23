/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunctionDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.partnerfunction.facade.ejb.IPartnerFunctionManagerHome;
import com.gridnode.gtas.server.partnerfunction.facade.ejb.IPartnerFunctionManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
 
/**
 * This class provides services to the PartnerFunction module.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class PartnerFunctionDelegate
{
  /**
   * Obtain the EJBObject for the PartnerFunctionManagerBean.
   *
   * @return The EJBObject to the PartnerFunctionManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IPartnerFunctionManagerObj getManager()
    throws ServiceLookupException
  {
    return (IPartnerFunctionManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerFunctionManagerHome.class.getName(),
      IPartnerFunctionManagerHome.class,
      new Object[0]);
  }

  /**
   * This method will retrieve the version of the PartnerFunction.
   *
   * @param partnerFunctionID The PartnerFunctionID of the PartnerFunction.
   * return the version number of the PartnerFunction.
   *
   * @since 2.0
   */
  public static double getPartnerFunctionVersion(String partnerFunctionID)
    throws Exception
  {
    if (partnerFunctionID!=null && !partnerFunctionID.equals(""))
    {
      if (getManager().findPartnerFunction(partnerFunctionID) != null)
      {
        return getManager().findPartnerFunction(partnerFunctionID).getVersion();
      }
    }
    return 1.0;
  }

  /**
   * This method will retrieve the version of the PartnerFunction.
   *
   * @param partnerFunctionID The PartnerFunctionID of the PartnerFunction.
   * return the version number of the PartnerFunction.
   *
   * @since 2.0
   */
  public static Integer getPartnerFunctionTrigger(String partnerFunctionID)
    throws Exception
  {
    if (partnerFunctionID!=null && !partnerFunctionID.equals(""))
    {
      if (getManager().findPartnerFunction(partnerFunctionID) != null)
      {
        return getManager().findPartnerFunction(partnerFunctionID).getTriggerOn();
      }
    }
    return new Integer(-1);
  }
}