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
 * Sep 11 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.registration.helpers;

import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerHome;
import com.gridnode.gtas.server.gridnode.facade.ejb.IGridNodeManagerObj;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceHome;
import com.gridnode.gtas.server.registration.product.ejb.IRegistrationServiceObj;
import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerHome;
import com.gridnode.pdip.app.coyprofile.facade.ejb.ICoyProfileManagerObj;
import com.gridnode.pdip.app.license.facade.ejb.ILicenseManagerHome;
import com.gridnode.pdip.app.license.facade.ejb.ILicenseManagerObj;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerHome;
import com.gridnode.pdip.base.locale.facade.ejb.ILocaleManagerObj;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides helper methods to lookup Ejbs.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ServiceLookupHelper
{

  public ServiceLookupHelper()
  {
  }

  /**
   * Get the LocaleManagerBean.
   *
   * @return Ejb proxy object to the LocaleManagerBean.
   */
  public static ILocaleManagerObj getLocaleManager()
    throws ServiceLookupException
  {
    return (ILocaleManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ILocaleManagerHome.class.getName(),
      ILocaleManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the CoyProfileManagerBean.
   *
   * @return Ejb proxy object to the CoyProfileManagerBean.
   */
  public static ICoyProfileManagerObj getCoyProfileManager()
    throws ServiceLookupException
  {
    return (ICoyProfileManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ICoyProfileManagerHome.class.getName(),
      ICoyProfileManagerHome.class,
      new Object[0]);
  }

  /**
   * Get the LicenseManagerBean.
   *
   * @return Ejb proxy object to the LicenseManagerBean.
   */
  public static ILicenseManagerObj getLicenseManager()
    throws ServiceLookupException
  {
    return (ILicenseManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      ILicenseManagerHome.class.getName(),
      ILicenseManagerHome.class,
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
   * Obtain the RegistrationServiceBean.
   * @return Ejb proxy object to the RegistrationServiceBean.
   */
  public static IRegistrationServiceObj getRegistrationServiceBean() throws Exception
  {
    return (IRegistrationServiceObj)ServiceLocator.instance(
           ServiceLocator.CLIENT_CONTEXT).getObj(
           IRegistrationServiceHome.class.getName(),
           IRegistrationServiceHome.class,
           new Object[0]);
  }

  /**
   * Obtain the AlertManager.
   * @return Ejb proxy object to the AlertManager.
   */
/*030508NSL: Not required. Raise alert through Common module
  public static IAlertManagerObj getAlertManager() throws Exception
  {
    return (IAlertManagerObj)ServiceLocator.instance(
           ServiceLocator.CLIENT_CONTEXT).getObj(
           IAlertManagerHome.class.getName(),
           IAlertManagerHome.class,
           new Object[0]);
  }
*/
}