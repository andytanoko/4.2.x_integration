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
 * Oct 10 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Add getPostOffice().
 */
package com.gridnode.gtas.server.activation.helpers;

import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeHome;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeObj;
import com.gridnode.gtas.server.enterprise.facade.ejb.ISharedResourceManagerObj;
import com.gridnode.gtas.server.enterprise.facade.ejb.ISharedResourceManagerHome;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerHome;
import com.gridnode.gtas.server.enterprise.facade.ejb.IEnterpriseHierarchyManagerObj;
import com.gridnode.gtas.server.activation.facade.ejb.IActivationManagerHome;
import com.gridnode.gtas.server.activation.facade.ejb.IActivationManagerObj;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;

import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

/**
 * This class contains helper methods for looking up the EJBs from within
 * the Activation module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public class ServiceLookupHelper
{

  public ServiceLookupHelper()
  {
  }

  /**
   * Get the ActivationManagerBean proxy object.
   *
   * @return The EJB proxy object for ActivationManagerBean.
   * @exception ServiceLookupException Error in looking up the ActivationManagerBean.
   *
   * @since 2.0 I6
   */
  public static IActivationManagerObj getActivationManager()
    throws ServiceLookupException
  {
    return (IActivationManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IActivationManagerHome.class.getName(),
      IActivationManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the GridNodeManagerBean proxy object.
   *
   * @return The EJB proxy object for GridNodeManagerBean.
   * @exception ServiceLookupException Error in looking up the GridNodeManagerBean.
   *
   * @since 2.0 I6
   */
  public static IGridNodeManagerObj getGridNodeManager()
    throws ServiceLookupException
  {
    return (IGridNodeManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridNodeManagerHome.class.getName(),
      IGridNodeManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the BizRegistryManagerBean proxy object.
   *
   * @return The EJB proxy object for BizRegistryManagerBean.
   * @exception ServiceLookupException Error in looking up the BizRegistryManagerBean.
   *
   * @since 2.0 I6
   */
  public static IBizRegistryManagerObj getBizRegManager()
    throws ServiceLookupException
  {
    return (IBizRegistryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IBizRegistryManagerHome.class.getName(),
      IBizRegistryManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the EnterpriseHierarchyManagerBean proxy object.
   *
   * @return The EJB proxy object for EnterpriseHierarchyManagerBean.
   * @exception ServiceLookupException Error in looking up the EnterpriseHierarchyManagerBean.
   *
   * @since 2.0 I6
   */
  public static IEnterpriseHierarchyManagerObj getEnterpriseManager()
    throws ServiceLookupException
  {
    return (IEnterpriseHierarchyManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IEnterpriseHierarchyManagerHome.class.getName(),
      IEnterpriseHierarchyManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the SharedResourceManagerBean proxy object.
   *
   * @return The EJB proxy object for SharedResourceManagerBean.
   * @exception ServiceLookupException Error in looking up the SharedResourceManagerBean.
   *
   * @since 2.0 I6
   */
  public static ISharedResourceManagerObj getSharedResourceManager()
    throws ServiceLookupException
  {
    return (ISharedResourceManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ISharedResourceManagerHome.class.getName(),
      ISharedResourceManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the PostOfficeBean proxy object.
   *
   * @return The EJB proxy object for PostOfficeBean.
   * @exception ServiceLookupException Error in looking up the PostOfficeBean.
   *
   * @since 2.0 I6
   */
  public static IPostOfficeObj getPostOffice()
    throws ServiceLookupException
  {
    return (IPostOfficeObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPostOfficeHome.class.getName(),
      IPostOfficeHome.class,
      new Object[0]);
  }



}