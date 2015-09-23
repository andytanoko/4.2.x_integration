/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpgradeLicenseAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 09 2003    Koh Han Sing        Created
 * Jun 22 2006    Tam Wei Xiang       GNDB00026893 modified doProcess(...) 
 *                                    to handle the LicenseFileExpiredException.
 */
package com.gridnode.gtas.server.registration.actions;

import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.events.registration.UpgradeLicenseEvent;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.registration.helpers.IRegistrationPathConfig;
import com.gridnode.gtas.server.registration.exceptions.InvalidLicenseFileException;
import com.gridnode.gtas.server.registration.exceptions.LicenseFileExpiredException;
import com.gridnode.gtas.server.registration.exceptions.NodeLockException;
import com.gridnode.gtas.server.registration.exceptions.ProductRegistrationException;

import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

import java.util.List;

/**
 * This action handles the UpgradeLicenseEvent.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class UpgradeLicenseAction extends AbstractRegistrationAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3149064271041699392L;
	private final String ACTION_NAME = "UpgradeLicenseAction";

  public UpgradeLicenseAction()
  {
  }

  protected Class getExpectedEventClass()
  {
    return UpgradeLicenseEvent.class;
  }

  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };
    return constructEventResponse(
             IErrorCode.INVALID_REGISTRATION_ERROR,
             params,
             ex);
  }

  protected IEventResponse doProcess(IEvent event) throws java.lang.Throwable
  {
    UpgradeLicenseEvent upgradeEvent = (UpgradeLicenseEvent)event;

    String licenseFile = upgradeEvent.getLicenseFile();
    if (FileUtil.exist(IRegistrationPathConfig.PATH_LICENSE, licenseFile))
    {
      FileUtil.delete(IRegistrationPathConfig.PATH_LICENSE, licenseFile);
    }
    licenseFile = FileUtil.move(IRegistrationPathConfig.PATH_TEMP,
                                _userID+"/in/",
                                IRegistrationPathConfig.PATH_LICENSE,
                                "",
                                licenseFile);

    try
    {
      List productKey = getRegistrationBean().checkNodeLock(licenseFile);

      RegistrationInfo regInfo = getRegistrationBean().upgradeLicense(
                                   productKey.get(0).toString(),
                                   productKey.get(1).toString(),
                                   productKey.get(2).toString(),
                                   productKey.get(3).toString(),
                                   productKey.get(4).toString(),  //OS Name
                                   productKey.get(5).toString(),  //OS Version
                                   productKey.get(6).toString()); //Machine Name

      Object[] params = {
                        };
      return constructEventResponse(
               IErrorCode.NO_ERROR,
               params,
               convertToMap(regInfo));
    }
    catch (NodeLockException ex)
    {
      Object[] params = {
                        };
      return constructEventResponse(IErrorCode.NODELOCK_ERROR,
                                    params,
                                    ex);
    }
    catch (InvalidLicenseFileException ex)
    {
      Object[] params = {
                        };
      return constructEventResponse(IErrorCode.INVALID_LICENSE_FILE_ERROR,
                                    params,
                                    ex);
    }
    
    //TWX 22062006
    catch(ProductRegistrationException ex)
    {
    	if(ex instanceof LicenseFileExpiredException)
    	{
    		Object[] params = {
    											};
    		return constructEventResponse(IErrorCode.LICENSE_EXPIRED_ERROR,
    		                              params,
    		                              ex);
    	}
    	else
    	{
    		throw ex;
    	}
    }
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}