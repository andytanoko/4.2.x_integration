/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AppManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 23-02-2004     Mahesh              Modified to remove SFSB AppMapper bean
 * Feb 09 2007		Alain Ah Ming				Log warning message if throwing up exception.
 */
package com.gridnode.pdip.base.appinterface.facade.ejb;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.pdip.base.appinterface.data.AppDefinitionDoc;
import com.gridnode.pdip.base.appinterface.data.AppParam;
import com.gridnode.pdip.base.appinterface.exception.AppExecutionException;
import com.gridnode.pdip.base.appinterface.exception.AppNotInitializedException;
import com.gridnode.pdip.base.appinterface.helpers.AppInterfaceHelper;
import com.gridnode.pdip.base.appinterface.helpers.Logger;
import com.gridnode.pdip.base.appinterface.interfaces.IExecutable;

/**
 * This bean provides the app service layer implementation of the
 * app interface module. It serves as the facade to the business
 * methods of this module.
 *
 * @author Yap Kim Boon
 *
 * @version GT 4.0 VAN
 */
public class AppManagerBean implements SessionBean
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7236236283951210008L;
	private SessionContext sessionContext;

  /****************************** EJB required methods **************************************/
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }

  /****************************** Business methods **************************************/
  public Object executeApp(AppDefinitionDoc appDefDoc, int appType, Object params) throws AppNotInitializedException, AppExecutionException
  {
    boolean isSuccess = false;
    long applicationIndex=AppInterfaceHelper.enterApplication();
    Logger.log("[AppManagerBean.executeApp] Enter, applicationIndex="+applicationIndex+", appDefDoc=" + appDefDoc + ", appType=" + appType);
    Object retObject = null;
    long timeTaken=System.currentTimeMillis();
    try
    {
      IExecutable executable = AppInterfaceHelper.createApp(appDefDoc, appType);
      AppParam appParam = new AppParam();
      appParam.setAppDoc(params);
      appParam = executable.execute(appParam);
      retObject = appParam.getAppDoc();
      isSuccess=true;
      return retObject;
    }
    catch (AppNotInitializedException ex)
    {
      Logger.warn("[AppManagerBean.executeApp] initialization Error ", ex);
      throw ex;
    }
    catch (AppExecutionException ex)
    {
      Logger.warn("[AppManagerBean.executeApp] execution Error ", ex);
      throw ex;
    }
    catch (Throwable th)
    {
      Logger.warn("[AppManagerBean.executeApp] Error ", th);
      throw new AppExecutionException("Error in executing application", th);
    }
    finally
    { 
      timeTaken=(System.currentTimeMillis()-timeTaken)/1000;
      Logger.log("[AppManagerBean.executeApp] Exit, applicationIndex="+applicationIndex+", cocurrentApplicationCount="+AppInterfaceHelper.exitApplication()+", secondsTakenToExecute="+timeTaken+((!isSuccess)?(", params=" + params):"") + ", retObject=" + retObject);
    }
  }


}