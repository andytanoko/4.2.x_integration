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
 * Oct 21 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Add getPostOffice().
 */
package com.gridnode.gtas.server.connection.helpers;

import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeObj;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeHome;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceHome;
import com.gridnode.gtas.server.connection.facade.ejb.IConnectionServiceObj;
import com.gridnode.gtas.server.connection.facade.ejb.IConnectionServiceHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;

import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

/**
 * This class provides helper methods to lookup Ejbs.
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
   * Get the RegistrationServiceBean.
   *
   * @return Ejb proxy object to the RegistrationServiceBean.
   */
  public static IRegistrationServiceObj getRegistrationService()
    throws ServiceLookupException
  {
    return (IRegistrationServiceObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IRegistrationServiceHome.class.getName(),
      IRegistrationServiceHome.class,
      new Object[0]);
  }

  /**
   * Get the ChannelManagerBean.
   *
   * @return Ejb proxy object to the ChannelManagerBean.
   */
  public static IChannelManagerObj getChannelManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IChannelManagerHome.class.getName(),
      IChannelManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the ConnectionServiceBean.
   *
   * @return Ejb proxy object to the ConnectionServiceBean.
   */
  public static IConnectionServiceObj getConnectionService()
    throws ServiceLookupException
  {
    return (IConnectionServiceObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IConnectionServiceHome.class.getName(),
      IConnectionServiceHome.class,
      new Object[0]);
  }

  /**
   * Get the GridNodeManagerBean.
   *
   * @return Ejb proxy object to the GridNodeManagerBean.
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
   * Get the CertificateManagerBean.
   *
   * @return Ejb proxy object to the CertificateManagerBean.
   */
  public static ICertificateManagerObj getCertificateManager()
    throws ServiceLookupException
  {
    return (ICertificateManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ICertificateManagerHome.class.getName(),
      ICertificateManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the IiCalTimeMgrBean.
   * @return Ejb proxy object to the IiCalTimeMgrBean.
   */
  public static IiCalTimeMgrObj getTimeManager() throws Exception
  {
    return (IiCalTimeMgrObj)ServiceLocator.instance(
           ServiceLocator.CLIENT_CONTEXT).getObj(
           IiCalTimeMgrHome.class.getName(),
           IiCalTimeMgrHome.class,
           new Object[0]);
  }

  /**
   * Get the PostOfficeBean.
   *
   * @return Ejb proxy object to the PostOfficeBean.
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