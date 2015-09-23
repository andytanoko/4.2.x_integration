/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcedureDefLogger.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * AUG 01 2002    Jagadeesh         Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */


package com.gridnode.pdip.base.userprocedure.helpers;

import com.gridnode.pdip.base.userprocedure.entities.ejb.IProcedureDefFileLocalHome;
import com.gridnode.pdip.base.userprocedure.entities.ejb.IProcedureDefFileLocalObj;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.util.ServiceLocator;




public class ProcedureDefFileEntityHandler extends LocalEntityHandler
{

  public ProcedureDefFileEntityHandler()
  {
    super(ProcedureDefFile.ENTITY_NAME);
    Logger.log("Procedure Definition File Logger "+ProcedureDefFile.ENTITY_NAME);
  }


  public static ProcedureDefFileEntityHandler getInstance()
  {
    ProcedureDefFileEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(ProcedureDefFile.ENTITY_NAME, true))
    {
      handler = (ProcedureDefFileEntityHandler)EntityHandlerFactory.getHandlerFor(
                  ProcedureDefFile.ENTITY_NAME, true);
    }
    else
    {
      handler = new ProcedureDefFileEntityHandler();
      EntityHandlerFactory.putEntityHandler(ProcedureDefFile.ENTITY_NAME,
         true, handler);
    }
     return handler;
   }

  // ************** AbstractEntityHandler methods *******************
  /*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IProcedureDefFileLocalHome.class.getName(),
      IProcedureDefFileLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IProcedureDefFileLocalHome.class;
	}


	protected Class getProxyInterfaceClass() throws java.lang.Exception
  {
    return IProcedureDefFileLocalObj.class;
  }








}


