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
 * Apr 16 2002    Neo Sok Lay         Add XML manager lookup.
 * Jul 10 2003    Neo Sok Lay         Add Alert manager lookup.
 * Nov 10 2005    Neo Sok Lay        Change XmlManager to LocalContext lookup
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerHome;
import com.gridnode.pdip.app.user.facade.ejb.IUserManagerObj;

import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerHome;
import com.gridnode.pdip.base.acl.facade.ejb.IACLManagerObj;

import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;

import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;

import com.gridnode.pdip.framework.util.ServiceLocator;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;

public class ServiceLookupHelper
{

  public static IACLManagerObj getAclManager() throws ServiceLookupException
  {
    return (IACLManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IACLManagerHome.class.getName(),
             IACLManagerHome.class,
             new Object[0]);
  }

  public static IUserManagerObj getUserManager() throws ServiceLookupException
  {
    return (IUserManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IUserManagerHome.class.getName(),
             IUserManagerHome.class,
             new Object[0]);
  }

  public static IXMLServiceLocalObj getXmlManager() throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
             ServiceLocator.LOCAL_CONTEXT).getObj(
             IXMLServiceLocalHome.class.getName(),
             IXMLServiceLocalHome.class,
             new Object[0]);
  }

  public static IAlertManagerObj getAlertManager() throws ServiceLookupException
  {
    return (IAlertManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IAlertManagerHome.class.getName(),
             IAlertManagerHome.class,
             new Object[0]);
  }

  public static IiCalTimeMgrObj getICalManager() throws ServiceLookupException
  {
    return (IiCalTimeMgrObj) ServiceLocator.instance(
              ServiceLocator.CLIENT_CONTEXT).getObj(
              IiCalTimeMgrHome.class.getName(),
              IiCalTimeMgrHome.class,
              new Object[0]);
  }
}