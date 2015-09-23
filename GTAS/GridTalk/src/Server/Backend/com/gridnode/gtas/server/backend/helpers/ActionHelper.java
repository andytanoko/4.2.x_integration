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
 * Dec 18 2002    Koh Han Sing        Created
 * Nov 10 2005    Neo Sok Lay         Use Localcontext to lookup BackendServiceBean
 */
package com.gridnode.gtas.server.backend.helpers;

import com.gridnode.gtas.model.backend.PortEntityFieldID;
import com.gridnode.gtas.model.backend.RfcEntityFieldID;
import com.gridnode.gtas.server.backend.facade.ejb.IBackendServiceLocalHome;
import com.gridnode.gtas.server.backend.facade.ejb.IBackendServiceLocalObj;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.Rfc;

import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;
import java.util.Map;

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

  /**
   * Convert an Port to Map object.
   *
   * @param port The Port to convert.
   * @return A Map object converted from the specified Port.
   *
   * @since 2.0
   */
  public static Map convertPortToMap(Port port)
  {
    return Port.convertToMap(
             port,
             PortEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Port to Map objects.
   *
   * @param portList The collection of Port to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Port.
   *
   * @since 2.0
   */
  public static Collection convertPortToMapObjects(Collection portList)
  {
    return Port.convertEntitiesToMap(
             (Port[])portList.toArray(
             new Port[portList.size()]),
             PortEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert an Rfc to Map object.
   *
   * @param rfc The Rfc to convert.
   * @return A Map object converted from the specified Rfc.
   *
   * @since 2.0
   */
  public static Map convertRfcToMap(Rfc rfc)
  {
    return Rfc.convertToMap(
             rfc,
             RfcEntityFieldID.getEntityFieldID(),
             null);
  }

  /**
   * Convert a collection of Rfc to Map objects.
   *
   * @param rfcList The collection of Port to convert.
   * @return A Collection of Map objects converted from the specified collection
   * of Rfc.
   *
   * @since 2.0
   */
  public static Collection convertRfcToMapObjects(Collection rfcList)
  {
    return Rfc.convertEntitiesToMap(
             (Rfc[])rfcList.toArray(
             new Rfc[rfcList.size()]),
             RfcEntityFieldID.getEntityFieldID(),
             null);
  }

}