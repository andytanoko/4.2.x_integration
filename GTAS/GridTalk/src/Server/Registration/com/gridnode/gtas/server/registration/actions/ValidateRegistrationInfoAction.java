/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ValidateRegistrationInfoAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 11 2002    Neo Sok Lay         Created
 * Jun 22 2006    Tam Wei Xiang       GNDB00026893 modified doProcess(...) 
 *                                    to handle the LicenseFileExpiredException.
 */
package com.gridnode.gtas.server.registration.actions;

import java.util.List;

import com.gridnode.gtas.events.registration.ValidateRegistrationInfoEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.server.registration.exceptions.InvalidLicenseFileException;
import com.gridnode.gtas.server.registration.exceptions.LicenseFileExpiredException;
import com.gridnode.gtas.server.registration.exceptions.NodeLockException;
import com.gridnode.gtas.server.registration.exceptions.ProductRegistrationException;
import com.gridnode.gtas.server.registration.helpers.IRegistrationPathConfig;
import com.gridnode.gtas.server.registration.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

/**
 * Action class for validating the RegistrationInfo for product registration.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class ValidateRegistrationInfoAction extends AbstractRegistrationAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 918799085704244383L;
	private final String ACTION_NAME = "ValidateRegistrationInfoAction";

  public ValidateRegistrationInfoAction()
  {
  }

  // ********* Methods from AbstractGridTalkAction **************

  protected Class getExpectedEventClass()
  {
    return ValidateRegistrationInfoEvent.class;
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
    ValidateRegistrationInfoEvent valEvent = (ValidateRegistrationInfoEvent)event;

    // save the company profile
    ServiceLookupHelper.getGridNodeManager().saveMyCompanyProfile(
      valEvent.getCompanyProfile());
    CompanyProfile profile = ServiceLookupHelper.getGridNodeManager().getMyCompanyProfile();

//KHS20030325 Start
    String licenseFile = valEvent.getLicenseFile();
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

      //initialize the registration info (with validating of product key)
  //    RegistrationInfo regInfo = getRegistrationBean().initRegistration(
  //                                 valEvent.getGridnodeID(),
  //                                 valEvent.getGridnodeName(),
  //                                 valEvent.getProdKeyF1(),
  //                                 valEvent.getProdKeyF2(),
  //                                 valEvent.getProdKeyF3(),
  //                                 valEvent.getProdKeyF4(),
  //                                 profile);

      RegistrationInfo regInfo = getRegistrationBean().initRegistration(
                                   valEvent.getGridnodeID(),
                                   valEvent.getGridnodeName(),
                                   productKey.get(0).toString(),
                                   productKey.get(1).toString(),
                                   productKey.get(2).toString(),
                                   productKey.get(3).toString(),
                                   profile,
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
    //KHS20030325 End
    
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