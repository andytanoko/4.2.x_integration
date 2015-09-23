/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureServiceManagerBean.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh              Created
 * Feb 10 2003    Jagadeesh              Modified : ParamDef Vector is used in
 *                                       constructing ProcedureHandlerInfo.
 *                                       execute() Method to use ParamDef Vector.
 * Feb 26 2003    Neo Sok Lay            Raise alert if error executing the
 *                                       user procedure.
 * Apr 24 2003    Neo Sok Lay            Adjust raiseAlert() to new AlertRequestNotification.
 * May 12 2003    Neo Sok Lay            Delegate raiseAlert() to AlertUtil.
 * Feb 11 2004    Koh Han Sing           New method to retreive using procedure name.
 * Nov 10 2005    Neo Sok Lay            Use ServiceLocator instead of ServiceLookup
 * Oct 31 2006    Tam Wei Xiang          Modified method performUserProcedure(... , ...)
 *                                       to broadcast the status of the execution of the
 *                                       user procedure.
 * Dec 26 2006    Tam Wei Xiang          modified sendDocumentFlowNotification(...)
 *                                       change the type of docFlowType from string to enum
 *                                       EDocumentFlowType
 * May 16 2007    Tam Wei Xiang          commented the gdoc is null msg to avoid confusing in
 *                                       the log (normal user procedure trigger from scheduler will
 *                                       not have gdoc)
 *                                       . Amended method sendDocumentFlowNotification()
 */


package com.gridnode.gtas.server.userprocedure.facade.ejb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.model.userprocedure.IAction;
import com.gridnode.gtas.server.document.folder.ExportFolder;
import com.gridnode.gtas.server.document.folder.ImportFolder;
import com.gridnode.gtas.server.document.folder.InboundFolder;
import com.gridnode.gtas.server.document.folder.OutboundFolder;
import com.gridnode.gtas.server.document.helpers.Logger;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.userprocedure.exceptions.AbortUserProcedureException;
import com.gridnode.gtas.server.userprocedure.exceptions.InvalidParamDefException;
import com.gridnode.gtas.server.userprocedure.exceptions.InvalidReturnDefException;
import com.gridnode.gtas.server.userprocedure.exceptions.UserProcedureException;
import com.gridnode.gtas.server.userprocedure.handlers.UserProcedureHandler;
import com.gridnode.gtas.server.userprocedure.helpers.AlertUtil;
import com.gridnode.pdip.base.userprocedure.exceptions.UserProcedureExecutionException;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.jms.JMSRedeliveredHandler;
import com.gridnode.pdip.framework.notification.DocumentFlowNotification;
import com.gridnode.pdip.framework.notification.DocumentFlowNotifyHandler;
import com.gridnode.pdip.framework.notification.EDocumentFlowType;
import com.gridnode.pdip.framework.notification.IDocumentFlow;
import com.gridnode.pdip.framework.notification.Notifier;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class UserProcedureServiceManagerBean implements SessionBean
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3633354599280108944L;
	private SessionContext _sessionCtx = null;
  private static final String CLASS_NAME = "UserProcedureServiceManagerBean";

  /**
   *
   *
   * @param gridDocs - Collection of GridDocuments
   * @param userProcedureUID - UserProcedure Entity UID.
   * @return - Collection of GridDocuments.
   * @throws InvalidParamDefException - thrown when improper ParamDefinition specified.
   * @throws UserProcedureException - thrown when this UserProcedure Cannot be executed.
   * @throws InvalidReturnDefException - thrown when improper ReturnDefinition specified.
   * @throws UserProcedureExecutionException - thrown when UserProcedure Executing engine cannot execute.
   */

  public Collection performUserProcedure(Collection gridDocs,Long userProcedureUID)
     throws  InvalidParamDefException,
             UserProcedureException,
             InvalidReturnDefException,
             UserProcedureExecutionException, SystemException
  {
    //Collection returnGDocs = gridDocs;
    ArrayList returnGDocs = new ArrayList();
    if((gridDocs != null) && (userProcedureUID != null))
    {
       Iterator gridDocIterator = gridDocs.iterator();
       UserProcedure userProcedureEntity = getUserProcedureEntity(userProcedureUID);
       while(gridDocIterator.hasNext())
       {
         GridDocument gridDoc = (GridDocument)gridDocIterator.next();

         returnGDocs.add(performUserProcedure(gridDoc, userProcedureEntity));
       }
    }
    return returnGDocs;
  }

  /**
   * Perform the specified user procedure
   *
   * @param userProcedureName - UserProcedure Name.
   * @throws InvalidParamDefException - thrown when improper ParamDefinition specified.
   * @throws UserProcedureException - thrown when this UserProcedure Cannot be executed.
   * @throws InvalidReturnDefException - thrown when improper ReturnDefinition specified.
   * @throws UserProcedureExecutionException - thrown when UserProcedure Executing engine cannot execute.
   */

  public void performUserProcedure(String userProcedureName)
     throws  InvalidParamDefException,
             UserProcedureException,
             InvalidReturnDefException,
             UserProcedureExecutionException,
             SystemException
  {
    if(userProcedureName != null)
    {
      UserProcedure userProcedureEntity = getUserProcedureEntity(userProcedureName);
      performUserProcedure(null, userProcedureEntity);
    }
  }

  private GridDocument performUserProcedure(GridDocument gdoc,
                                            UserProcedure userProcedure)
     throws  InvalidParamDefException,
             UserProcedureException,
             InvalidReturnDefException,
             UserProcedureExecutionException,
             SystemException
  {
    Throwable th = null;
    try
    {
       Vector paramDefVect = UserProcedureHandler.preProcessUserProcedure(
                                 gdoc,userProcedure);
       Object processedValue = UserProcedureHandler.executeUserProcedure(
                                 paramDefVect,userProcedure);
       int returnAction = UserProcedureHandler.postProcessUserProcedure(
                                gdoc,processedValue,userProcedure);
       if (returnAction == (IAction.ABORT.intValue()) )
          throw new AbortUserProcedureException(
                      "User procedure return action indicates ABORT! "+returnAction);

       if (userProcedure.getGridDocField() != null)
       {
         gdoc.setFieldValue(userProcedure.getGridDocField(),
                            processedValue.toString());
       }

    }
    catch (InvalidParamDefException ex)
    {
      th = ex;
      raiseAlert(gdoc, userProcedure, ex);
      throw ex;
    }
    catch (UserProcedureException ex)
    {
      th = ex;
      raiseAlert(gdoc, userProcedure, ex);
      throw ex;
    }
    catch (InvalidReturnDefException ex)
    {
      th = ex;
      raiseAlert(gdoc, userProcedure, ex);
      throw ex;
    }
    catch (UserProcedureExecutionException ex)
    {
      th = ex;
      raiseAlert(gdoc, userProcedure, ex);
      throw ex;
    }
    catch (AbortUserProcedureException ex)
    {
      th = ex;
      raiseAlert(gdoc, userProcedure, ex);
      /**@todo to decide whether to abort the whole process or continue*/
    }
    catch (Throwable t)
    {
      th = t;
      raiseAlert(gdoc, userProcedure, t);
      throw new UserProcedureExecutionException("Unexpected Error executing UserProcedure!", t);
    }
    finally //TWX 31102006 broadcast the status of the execution of user procedure
    {
      boolean isSuccess = true;
      String errReason = "";
      if(th != null)
      {
        isSuccess = false;
        errReason = th.getMessage();
      }
      sendDocumentFlowNotification(gdoc, EDocumentFlowType.USER_PROCEDURE,isSuccess, (th != null ? th.getMessage() : ""), new Date(), userProcedure.getName(), th);
    }
    
    /**@todo NOTE: it might be better if the exceptions extend from a root exception e.g. UserProcedureException */
    return gdoc;
  }

  private void raiseAlert(GridDocument gdoc, UserProcedure userProcedure, Throwable t)
  {
    AlertUtil.alertUserProcFailed(gdoc, userProcedure, t);
  }

  /**
   * This method gets the IUserProcedureManagerObj(Remote Object).
   * @return - IUserProcedureManagerObj remote reference.
   * @throws ServiceLookupException - thrown when cannot lookup the bean.
   * @throws Exception
   */

  private IUserProcedureManagerObj getUserProcedureManager()
     throws  ServiceLookupException,Exception
  {
  	/*
    IUserProcedureManagerHome home = (IUserProcedureManagerHome)ServiceLookup.getInstance(
    ServiceLookup.CLIENT_CONTEXT).getHome(IUserProcedureManagerHome.class);
    IUserProcedureManagerObj remote = home.create();
    */
  	IUserProcedureManagerObj remote = (IUserProcedureManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
  	                                     IUserProcedureManagerHome.class.getName(),
  	                                     IUserProcedureManagerHome.class,
  	                                     new Object[0]);
    return remote;
  }

 /** EJB Specific CallBack methods */
  public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
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

  private UserProcedure getUserProcedureEntity(Long uid)
    throws UserProcedureException
  {
   try
   {
     IUserProcedureManagerObj userProcedureManagerBean = getUserProcedureManager();
     UserProcedure userProcedureEntity = userProcedureManagerBean.getUserProcedure(uid);
     return userProcedureEntity;
   }catch(Exception ex){
     throw new UserProcedureException(ex.getMessage(),ex);
   }
  }

  private UserProcedure getUserProcedureEntity(String userProcedureName)
    throws UserProcedureException
  {
   try
   {
     DataFilterImpl filter = new DataFilterImpl();
     filter.addSingleFilter(
       null,
       UserProcedure.NAME,
       filter.getEqualOperator(),
       userProcedureName,
       false);

     IUserProcedureManagerObj userProcedureManagerBean = getUserProcedureManager();
     Collection userProcedureList = userProcedureManagerBean.getUserProcedure(filter);
     if (userProcedureList.isEmpty())
     {
       return null;
     }
     else
     {
       UserProcedure userProcedureEntity = (UserProcedure)userProcedureList.iterator().next();
       return userProcedureEntity;
     }
   }
   catch(Exception ex)
   {
     throw new UserProcedureException(ex.getMessage(),ex);
   }
  }
  
 /**
   * TWX 31102006 Send out the document flow activity that we have performed on the business document (Signal included).
   * @param gdoc The GridDocument that has associated a business document
   * @param docFlowType The type of the doc flow activity
   * @param status The status of performing the doc flow activity
   * @param errorReason If the status is false, an errorReason will be provided
   * @param occurDate The date we performed the doc flow activity
   * @param docFlowAddInfo To include additional information about the docFlowType. Eg we can include
   *                       the port name for the docFlowType IDocumentFlow.DOCUMENT_EXPORT
   */
  private void sendDocumentFlowNotification(GridDocument gdoc, EDocumentFlowType docFlowType, boolean status,
                                            String errorReason, Date occurDate, String docFlowAddInfo,
                                            Throwable th) throws SystemException
  {
    if(gdoc == null)
    {
      //Logger.warn("["+CLASS_NAME+".sendDocumentFlowNotification] GDOC is NULL !");
      return;
    }

    DocumentFlowNotifyHandler.triggerNotification(docFlowType, occurDate, gdoc.getFolder(), gdoc.getGdocId(), status,
                                                                errorReason, gdoc.getTracingID(), gdoc.getUdocDocType(),
                                                                gdoc.getSenderBizEntityId(), gdoc.getRecipientBizEntityId(),
                                                                docFlowAddInfo, (Long)gdoc.getKey(), gdoc.getSrcFolder(), th);
  }
}







