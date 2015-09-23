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
 * Oct 02 2002    Neo Sok Lay         Add getCertificateManager().
 * Nov 06 2002    Neo Sok Lay         Add getPostOffice().
 * Sep 03 2003    Neo Sok Lay         Change IBizRegManagerObj to IBizRegManager.
 * Sep 08 2003    Neo Sok Lay         Add getPublicRegistryMgr().
 * Oct 17 2005    Neo Sok Lay         Change IBizRegistryManager and IPublicRegistryManager
 *                                    to IBizRegistryManagerObj and IPublicRegistryManagerObj
 *                                    respectively.
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.enterprise.facade.ejb.*;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeHome;
import com.gridnode.gtas.server.enterprise.post.ejb.IPostOfficeObj;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IBizRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerHome;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerHome;
import com.gridnode.pdip.app.partner.facade.ejb.IPartnerManagerObj;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class ServiceLookupHelper
{

  // *********************** Utility methods *********************************

  /**
   * Obtain the EJBObject for the EnterpriseHierarchyManagerBean.
   *
   * @return The EJBObject to the EnterpriseHierarchyManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I4
   */
  public static IEnterpriseHierarchyManagerObj getEnterpriseHierarchyMgr()
    throws ServiceLookupException
  {
    return (IEnterpriseHierarchyManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IEnterpriseHierarchyManagerHome.class.getName(),
      IEnterpriseHierarchyManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the ResourceLinkManagerBean.
   *
   * @return The EJBObject to the ResourceLinkManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I4
   */
  public static IResourceLinkManagerObj getResourceLinkMgr()
    throws ServiceLookupException
  {
    return (IResourceLinkManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IResourceLinkManagerHome.class.getName(),
      IResourceLinkManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the SharedResourceManagerBean.
   *
   * @return The EJBObject to the SharedResourceManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I4
   */
  public static ISharedResourceManagerObj getSharedResourceMgr()
    throws ServiceLookupException
  {
    return (ISharedResourceManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ISharedResourceManagerHome.class.getName(),
      ISharedResourceManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the BizRegistryManagerBean.
   *
   * @return The EJBObject to the BizRegistryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I4
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
   * Obtain the EJBObject for the UserManagerBean.
   *
   * @return The EJBObject to the UserManagerBean.
   * @exception ServiceLookupException Error in looking up UserManagerBean.
   *
   * @since 2.0 I4
   */
  public static IUserManagerObj getUserManager()
    throws ServiceLookupException
  {
    return (IUserManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IUserManagerHome.class.getName(),
      IUserManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the ChannelManagerBean.
   *
   * @return The EJBObject to the ChannelManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0 I4
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
   * Obtain the EJBObject for the PartnerManagerBean.
   *
   * @return The EJBObject to the PartnerManagerBean.
   * @exception ServiceLookupException Error in looking up PartnerManagerBean.
   *
   * @since 2.0 I4
   */
  public static IPartnerManagerObj getPartnerManager()
    throws ServiceLookupException
  {
    return (IPartnerManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerManagerHome.class.getName(),
      IPartnerManagerHome.class,
      new Object[0]);
  }

  /**
   * Obtain the EJBObject for the GridNodeManagerBean.
   *
   * @return The EJBObject to the GridNodeManagerBean.
   * @exception ServiceLookupException Error in looking up GridNodeManagerBean.
   *
   * @since 2.0 I5
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
   * Obtain the EJBObject for the CertificateManagerBean.
   *
   * @return The EJBObject to the CertificateManagerBean.
   * @exception ServiceLookupException Error in looking up CertificateManagerBean.
   *
   * @since 2.0 I6
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
   * Obtain the EJBObject for the PostOfficeBean.
   *
   * @return The EJBObject to the PostOfficeBean.
   * @exception ServiceLookupException Error in look up.
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

  /**
   * Obtain the interface for the PublicRegistryManagerBean.
   *
   * @return The interface to the PublicRegistryManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since GT 2.2
   */
  public static IPublicRegistryManagerObj getPublicRegistryMgr()
    throws ServiceLookupException
  {
    return (IPublicRegistryManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPublicRegistryManagerHome.class.getName(),
      IPublicRegistryManagerHome.class,
      new Object[0]);
  }
  

}