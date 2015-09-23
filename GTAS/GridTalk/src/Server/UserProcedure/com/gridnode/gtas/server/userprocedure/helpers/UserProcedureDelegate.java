/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureDelegate.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 21 2003    Jagadeesh              Created
 * Oct 20 2003    Neo Sok Lay             Add methods: 
 *                                        - execute(UserProcName,GridDocument)
 * Nov 10 2005    Neo Sok Lay             Use ServiceLocator instead of ServiceLookup                                       
 */
package com.gridnode.gtas.server.userprocedure.helpers;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.CreateException;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.userprocedure.exceptions.UserProcedureException;
import com.gridnode.gtas.server.userprocedure.facade.ejb.IUserProcedureServiceManagerHome;
import com.gridnode.gtas.server.userprocedure.facade.ejb.IUserProcedureServiceManagerObj;
import com.gridnode.gtas.server.userprocedure.handlers.UserProcedureHandler;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Class is a Business Delegate that hides the underlying
 * details of the business services, such as lookup and access
 * details.
 * 
 * @version GT 2.3 I1
 */

public class UserProcedureDelegate
{
  /**
   * This method is essentially responsible to delegate the execution of
   * userprocedure service facade(a stateless session ejb).
   *
   * @param gdocs - A Collection of GridDocuments.
   * @param uid -  UID of UserProcedure Entity.
   * @return - Collection of GridDocuments.
   * @throws Exception - when cannot Execute this userprocedure.
   */

  public static Collection execute(String uprocUID, Collection gdocs)
    throws Exception
  {
    IUserProcedureServiceManagerObj serviceFacade =  lookupUserProcedureServiceFacade();
    Collection processReturn = null;
    if ((gdocs != null) && (uprocUID != null && !uprocUID.equals("")))
    {
     Long uid = new Long(uprocUID);
     processReturn = serviceFacade.performUserProcedure(gdocs,uid);
    }
    else
      throw new UserProcedureException("GridDocs Or UserProcedure Id Cannot Be Null");
    return processReturn;
  }

  /**
   * This method is essentially responsible to delegate the execution of
   * userprocedure service facade(a stateless session ejb).
   *
   * @param uprocName -  Name of UserProcedure Entity.
   * @throws Exception - when cannot Execute this userprocedure.
   */

  public static void execute(String uprocName)
    throws Exception
  {
    IUserProcedureServiceManagerObj serviceFacade =  lookupUserProcedureServiceFacade();
    Collection processReturn = null;
    if ((uprocName != null && !uprocName.equals("")))
    {
      serviceFacade.performUserProcedure(uprocName);
    }
    else
      throw new UserProcedureException("UserProcedure Name Cannot Be Null");
  }

 /**
  * This method performs a Remote Lookup to retrieve UserProcedureServiceManager
  * remote reference.
  *
  * @return - IUserProcedureServiceManagerObj - remote object reference of IUserProcedureServiceManagerObj
  * @throws ServiceLookupException - thrown when cannot lookup a Bean.
  * @throws CreateException - thrown when cannot create a remote reference.
  * @throws RemoteException - EJB(Stub Exception).
  */
  private static IUserProcedureServiceManagerObj lookupUserProcedureServiceFacade()
    throws ServiceLookupException,CreateException,RemoteException
  {
  	/*
     IUserProcedureServiceManagerHome home = (IUserProcedureServiceManagerHome)ServiceLookup.getInstance(
     ServiceLookup.CLIENT_CONTEXT).getHome(IUserProcedureServiceManagerHome.class);
     IUserProcedureServiceManagerObj userProcedureObj = home.create();
     */
  	IUserProcedureServiceManagerObj userProcedureObj = (IUserProcedureServiceManagerObj)ServiceLocator.instance(
  	    ServiceLocator.CLIENT_CONTEXT).getObj(IUserProcedureServiceManagerHome.class.getName(),
  	                                          IUserProcedureServiceManagerHome.class,
  	                                          new Object[0]);
     return userProcedureObj;
  }

  /**
   * Execute an user procedure on the specified GridDocument and return the value
   * returned from the user procedure execution, if any. 
   * Note that the Abort/Continue actions will not be processed.
   * 
   * @param userProc The name of the user procedure to execute.
   * @param gdoc The GridDocument to execute on.
   * @return The return value from the user procedure, if successful execution.
   * @throws Exception Error trying to execute the user procedure, or unhandled exception
   * while executing the user procedure.
   */
  public static Object execute(String userProcName, GridDocument gdoc)
    throws Exception
  {
    IUserProcedureServiceManagerObj serviceFacade =  lookupUserProcedureServiceFacade();
    
    UserProcedure userProc = BaseUserProcedureUtil.getUserProcedure(userProcName);
    
    Object returnVal = null;

    Vector paramDefVect = UserProcedureHandler.preProcessUserProcedure(
                              gdoc, userProc);
    returnVal = UserProcedureHandler.executeUserProcedure(paramDefVect, userProc);
    UserProcedureHandler.postProcessUserProcedure(gdoc, returnVal, userProc);

    return returnVal;
  }
}
