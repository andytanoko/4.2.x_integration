/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LicenseEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 16 2002    Neo Sok Lay         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.pdip.app.license.helpers;

import com.gridnode.pdip.app.license.entities.ejb.ILicenseLocalHome;
import com.gridnode.pdip.app.license.entities.ejb.ILicenseLocalObj;
import com.gridnode.pdip.app.license.model.License;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the License.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public final class LicenseEntityHandler
  extends          LocalEntityHandler
{
  private LicenseEntityHandler()
  {
    super(License.ENTITY_NAME);
  }

  /**
   * Get an instance of a LicenseEntityHandler.
   */
  public static LicenseEntityHandler getInstance()
  {
    LicenseEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(License.ENTITY_NAME, true))
    {
      handler = (LicenseEntityHandler)EntityHandlerFactory.getHandlerFor(
                  License.ENTITY_NAME, true);
    }
    else
    {
      handler = new LicenseEntityHandler();
      EntityHandlerFactory.putEntityHandler(License.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }


  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      ILicenseLocalHome.class.getName(),
      ILicenseLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return ILicenseLocalHome.class;
	}

	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return ILicenseLocalObj.class;
  }

  // ********************** Own methods ******************************

}