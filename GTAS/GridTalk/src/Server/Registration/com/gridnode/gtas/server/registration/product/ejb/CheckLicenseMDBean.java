/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CheckLicenseMDBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 04 2003    Koh Han Sing        Created
 * Mar 31 2006    Neo Sok Lay         Call RegistryServiceBean to check license
 *                                    instead of direct call
 */
package com.gridnode.gtas.server.registration.product.ejb;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.registration.product.RegistrationLogic;
import com.gridnode.gtas.server.registration.helpers.Logger;
import com.gridnode.gtas.server.registration.helpers.ServiceLookupHelper;

import com.gridnode.pdip.base.time.entities.value.AlarmInfo;
import com.gridnode.pdip.base.time.facade.ejb.TimeInvokeMDBean;

/**
 * Listener for alarm for time to check for license expiry and node lock.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0 I8
 */
public class CheckLicenseMDBean extends TimeInvokeMDBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7884540755832940083L;

	protected void invoke(AlarmInfo info)
  {
    try
    {
      Logger.log("[CheckLicenseMDBean.invoke] Enter");
      //RegistrationLogic.getInstance().checkLicense();
      ServiceLookupHelper.getRegistrationServiceBean().checkLicense();
    }
    catch (Throwable ex)
    {
      Logger.error(ILogErrorCodes.GT_CHECK_LICENSE_MDB,
                   "[CheckLicenseMDBean.invoke] "+ex.getMessage(), ex);
    }
    finally
    {
      Logger.log("[CheckLicenseMDBean.invoke] End");
    }
  }

}