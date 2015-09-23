/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateUserProcedureAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh           Created
 * Feb 14 2003    Jagadeesh           Modified: Added Conversion of ParamDef
 *                                    and ReturnDef.
 * Jul 09 2003    Koh Han Sing        Add in GridDocField
 * Jul 28 2003    Koh Han Sing        Added SoapProcedure
 * Mar 05 2004    Neo Sok Lay         Explicitly Set ProcedureType into ProcedureDef
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction
 */
package com.gridnode.gtas.server.userprocedure.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.gridnode.gtas.events.userprocedure.UpdateUserProcedureEvent;
import com.gridnode.gtas.model.userprocedure.*;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.*;
import com.gridnode.pdip.base.userprocedure.model.IProcedureType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class UpdateUserProcedureAction extends AbstractUpdateEntityAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7884231264262386276L;

	private static final String ACTION_NAME = "UpdateUserProcedureAction";

  private UserProcedure _userProcedure;
  private ProcedureDefFile _procedureDefFile;

  protected Map convertToMap(AbstractEntity entity)
	{
		return UserProcedure.convertToMap(entity, UserProcedureEntityFieldID.getEntityFieldID(), null);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateUserProcedureEvent updEvent = (UpdateUserProcedureEvent)event;
		return new Object[] {UserProcedure.ENTITY_NAME, updEvent.getUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateUserProcedureEvent updEvent = (UpdateUserProcedureEvent)event;
    _userProcedure.setDescription(updEvent.getDescription());
    _userProcedure.setName(updEvent.getName());
    _userProcedure.setIsSynchronous(updEvent.isSynchronous());
    _userProcedure.setProcedureDefAction(updEvent.getDefaultAction());
    _userProcedure.setProcedureDefAlert(updEvent.getDefaultAlert());
    /**todo : need special handling here */
    //_userProcedure.setProcedureDef(updEvent.getProcedureDef());

    ProcedureDef procDef = null;
    if(updEvent.getProcedureType() == IProcedureType.PROC_TYPE_JAVA)
    {
       procDef = (JavaProcedure) ProcedureDef.convertMapToEntity(
       JavaProcedureEntityFieldID.getEntityFieldID(),updEvent.getProcedureDef());
    }
    else if(updEvent.getProcedureType() == IProcedureType.PROC_TYPE_EXEC)
    {
       procDef = (ShellExecutable) ProcedureDef.convertMapToEntity(
       ShellExecutableEntityFieldID.getEntityFieldID(),updEvent.getProcedureDef());
    }
    else if(updEvent.getProcedureType() == IProcedureType.PROC_TYPE_SOAP)
    {
       procDef = (SoapProcedure) ProcedureDef.convertMapToEntity(
       SoapProcedureEntityFieldID.getEntityFieldID(),updEvent.getProcedureDef());
    }

    if (procDef != null)
    {
      procDef.setType(updEvent.getProcedureType());
      Logger.debug("Procedure Defintion :"+procDef.toString());
    }
      
    _userProcedure.setProcedureDef(procDef);
    _userProcedure.setProcedureDefFile(_procedureDefFile);
    _userProcedure.setProcedureParamList(processParamList(updEvent.getParamList()));
    _userProcedure.setProcedureReturnList(processReturnList(updEvent.getReturnList()));
    _userProcedure.setProcedureType(updEvent.getProcedureType());
    _userProcedure.setReturnDataType(updEvent.getReturnType());
    _userProcedure.setGridDocField(updEvent.getGridDocField());
    return _userProcedure;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().getUserProcedure(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		getManager().updateUserProcedure((UserProcedure)entity);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateUserProcedureEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
  {
    UpdateUserProcedureEvent updEvent = (UpdateUserProcedureEvent)event;
    _userProcedure = verifyValidUserProcedure(updEvent);
    _procedureDefFile = getProcedureDefFile(updEvent.getProcedureDefFileUID());
  }

  private UserProcedure verifyValidUserProcedure(UpdateUserProcedureEvent updEvent)
    throws Exception
  {
    try
    {
      UserProcedure procedureDef = (UserProcedure)getManager().getUserProcedure(updEvent.getUID());
      //ProcedureDefFile procedureDefFile = getProcedureDefFile(updEvent.getProcedureDefFileUID());
      return procedureDef;
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid ProcedureDefinition specified!");
    }
  }

  private ProcedureDefFile getProcedureDefFile(Long uID) throws Exception
  {
    try
    {
      return getManager().getProcedureDefinitionFile(uID);
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid ProcedureDefinitionFile specified!");
    }
  }

  private IUserProcedureManagerObj getManager()
    throws ServiceLookupException
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserProcedureManagerHome.class.getName(),
               IUserProcedureManagerHome.class,
               new Object[0]);
  }

  private Vector processParamList(Vector paramVect)
  {
    if(paramVect != null)
    {
      Vector paramDefVect = new Vector();
      for(int i=0;i<paramVect.size();i++)
      {
        HashMap keyValues = (HashMap)paramVect.get(i);
        ParamDef paramDef = (ParamDef)ParamDef.convertMapToEntity(
        ParamDefEntitiyFieldID.getEntityFieldID(),keyValues);
        paramDefVect.add(paramDef);
      }
      return paramDefVect;
    }
    else
      return paramVect;
  }

  private Vector processReturnList(Vector returnVect)
  {
    if(returnVect != null)
    {
      Vector returnDefVect = new Vector();

      for(int i=0;i<returnVect.size();i++)
      {
        HashMap keyValues = (HashMap)returnVect.get(i);
        ReturnDef returnDef = (ReturnDef)ReturnDef.convertMapToEntity(
        ReturnDefEntityFieldID.getEntityFieldID(),keyValues);
        returnDefVect.add(returnDef);
      }
      return returnDefVect;
    }
    else
      return returnVect;
  }
}
