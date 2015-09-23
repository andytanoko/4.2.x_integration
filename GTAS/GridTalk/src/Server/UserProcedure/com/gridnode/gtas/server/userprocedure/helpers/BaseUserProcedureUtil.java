/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BaseUserProcedureUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.userprocedure.helpers;

import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.Collection;

/**
 * This is an utility class for accessing Base User Procedure stuff.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class BaseUserProcedureUtil
{
  /**
   * Checks if a user procedure is valid, i.e. configured in database.
   * 
   * @param userProcName Name of the user procedure to check.
   * @return <b>true</b> if the user procedure exists in database, <b>false</b>
   * otherwise.
   * @throws Exception Unexpected exception when querying the database.
   */
  public static boolean isValidUserProcedure(String userProcName)
    throws Exception
  {
    IUserProcedureManagerObj mgr = getUserProcedureManager();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserProcedure.NAME, filter.getEqualOperator(),
      userProcName, false);
    Collection keys = mgr.getUserProcedureKeys(filter);
    return (keys.size() != 0);
  }
  
  /**
   * Get an user procedure from the database.
   * 
   * @param userProcName The name of the user procedure to get,
   * @return The retrieved UserProcedure.
   * @throws Exception Error retrieving the user procedure from database, or
   * no user procedure exists in the database with the specified name.
   */
  public static UserProcedure getUserProcedure(String userProcName)
    throws Exception
  {
    IUserProcedureManagerObj mgr = getUserProcedureManager();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserProcedure.NAME, filter.getEqualOperator(),
      userProcName, false);
    Collection results = mgr.getUserProcedure(filter);
    if (results != null && !results.isEmpty())
      return (UserProcedure)results.iterator().next();
    throw new Exception("UserProcedure does not exist - "+userProcName);    
  }
  
  /**
   * This method gets a handle to the UserProcedureManagerBean.
   * 
   * @return A handle to the UserProcedureManagerBean.
   * @throws Exception Error obtaining a handle to the UserProcedureManagerBean.
   */
  public static IUserProcedureManagerObj getUserProcedureManager()
     throws  Exception
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
        ServiceLocator.CLIENT_CONTEXT).getObj(
        IUserProcedureManagerHome.class.getName(),
        IUserProcedureManagerHome.class,
        new Object[]{});
  }
  
}
