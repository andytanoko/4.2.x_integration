/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcedureEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * AUG 01 2002    Jagadeesh         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */


package com.gridnode.pdip.base.userprocedure.helpers;


import java.util.Collection;

import com.gridnode.pdip.base.userprocedure.entities.ejb.IUserProcedureLocalHome;
import com.gridnode.pdip.base.userprocedure.entities.ejb.IUserProcedureLocalObj;
import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class UserProcedureEntityHandler extends LocalEntityHandler
{

  public UserProcedureEntityHandler()
  {
    super(UserProcedure.ENTITY_NAME);
    Logger.log("UserProcedure Logger "+UserProcedure.ENTITY_NAME);
  }


  public static UserProcedureEntityHandler getInstance()
  {
    UserProcedureEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(UserProcedure.ENTITY_NAME, true))
    {
      handler = (UserProcedureEntityHandler)EntityHandlerFactory.getHandlerFor(
                  UserProcedure.ENTITY_NAME, true);
    }
    else
    {
      handler = new UserProcedureEntityHandler();
      EntityHandlerFactory.putEntityHandler(UserProcedure.ENTITY_NAME,
         true, handler);
    }
     return handler;
   }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IUserProcedureLocalHome.class.getName(),
      IUserProcedureLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IUserProcedureLocalHome.class;
	}


	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IUserProcedureLocalObj.class;
  }


  /**
   * Find a UserProcedure with the specified ProcedureDefinitionFile UID.
   *
   * @param procDefFileUID   The Procedure DefintionFile UID.
   *
   * @return The User Procedure that is linked to the specified ProcedureDefFile UID
   * or <B>null</B> if none found.
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection findByProcedureDefFile(Long procDefFileUID) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, UserProcedure.PROC_DEF_FILE, filter.getEqualOperator(),
                                 procDefFileUID, false);
    return getEntityByFilterForReadOnly(filter);
  }


  /**
   * Find a UserProcedure with the specified ProcedureDefinitionFile Type.
   *
   * @param procDefFileType   The File type of the Procedure Defintion File.
   *
   * @return The User Procedure that is linked to the specified ProcedureDefFile Type
   * or <B>null</B> if none found.
   * @exception thrown when an error occurs.
   *
   * @since   2.0
   * @version 2.0
   */
  public Collection findByProcedureDefFileType(Integer procDefFileType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,UserProcedure.PROC_TYPE,filter.getEqualOperator(),
     procDefFileType,false);

    return getEntityByFilter(filter);
  }
}


