/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ActionHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 12 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.helpers;

import java.util.Collection;
import java.util.Map;

import com.gridnode.gtas.model.partnerfunction.PartnerFunctionEntityFieldID;
import com.gridnode.gtas.server.partnerfunction.facade.ejb.IPartnerFunctionManagerHome;
import com.gridnode.gtas.server.partnerfunction.facade.ejb.IPartnerFunctionManagerObj;
import com.gridnode.gtas.server.partnerfunction.model.PartnerFunction;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class provides common services used by the action classes.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class ActionHelper
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
   * Convert an PartnerFunction to Map object.
   *
   * @param partnerFunction The PartnerFunction to convert.
   * @return A Map object converted from the specified PartnerFunction.
   *
   * @since 2.0
   */
  public static Map convertPartnerFunctionToMap(PartnerFunction partnerFunction)
  {
    return PartnerFunction.convertToMap(
             partnerFunction,
             PartnerFunctionEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of PartnerFunction to Map objects.
   *
   * @param partnerFunctionList The collection of PartnerFunction to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of PartnerFunctions.
   *
   * @since 2.0
   */
  public static Collection convertPartnerFunctionToMapObjects(Collection partnerFunctionList)
  {
    return PartnerFunction.convertEntitiesToMap(
             (PartnerFunction[])partnerFunctionList.toArray(
             new PartnerFunction[partnerFunctionList.size()]),
             PartnerFunctionEntityFieldID.getEntityFieldID(),
             null);
  }

}