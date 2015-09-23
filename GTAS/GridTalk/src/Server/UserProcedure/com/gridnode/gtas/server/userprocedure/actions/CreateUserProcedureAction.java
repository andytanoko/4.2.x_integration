/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateUserProcedureAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh               Created
 * Jul 09 2003    Koh Han Sing            Add in GridDocField
 * Jul 28 2003    Koh Han Sing            Added SoapProcedure
 * Mar 05 2004    Neo Sok Lay             Explicitly Set ProcedureType into ProcedureDef 
 * Sep 07 2005    Neo Sok Lay             Change to extend from AbstractCreateEntityAction
 * May 20 2008    Tam Wei Xiang           #42: Init the ProcedureDef for the type "ShellExecutable"
 *                                        if the user didn't specify any argument in the form.
 *                                        Else NullPointerException will be thrown.
 */
package com.gridnode.gtas.server.userprocedure.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.gridnode.gtas.events.userprocedure.CreateUserProcedureEvent;
import com.gridnode.gtas.model.userprocedure.*;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.model.*;
import com.gridnode.pdip.base.userprocedure.model.IProcedureType;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

public class CreateUserProcedureAction extends AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4486610204194492196L;
	private static final String ACTION_NAME = "CreateUserProcedureAction";
	
  private ProcedureDefFile _procedureDefFile = null;

  protected Map convertToMap(AbstractEntity entity)
	{
  	return UserProcedure.convertToMap(entity, UserProcedureEntityFieldID.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		return getManager().createUserProcedure((UserProcedure)entity);
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {UserProcedure.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event)
	{
		CreateUserProcedureEvent _event = (CreateUserProcedureEvent)event;
    UserProcedure userProcedure = new UserProcedure();
    userProcedure.setName(_event.getName());
    userProcedure.setProcedureDefFile(_procedureDefFile);
    userProcedure.setDescription(_event.getDescription());
    userProcedure.setIsSynchronous(_event.isSynchronous());
    userProcedure.setProcedureDefAction(_event.getDefaultAction());
    userProcedure.setProcedureDefAlert(_event.getDefaultAlert());

    Vector procedureParamList  =  processPramList(_event.getParamList());
    Vector procedureReturnList =  processReturnList(_event.getReturnList());

    userProcedure.setProcedureParamList(procedureParamList);
    userProcedure.setProcedureReturnList(procedureReturnList);

    userProcedure.setProcedureType(_event.getProcedureType());
    userProcedure.setReturnDataType(_event.getReturnType());
    userProcedure.setGridDocField(_event.getGridDocField());

    ProcedureDef procDef = null;
    if(_event.getProcedureType() == IProcedureType.PROC_TYPE_JAVA)
    {
       procDef = (JavaProcedure) ProcedureDef.convertMapToEntity(
       JavaProcedureEntityFieldID.getEntityFieldID(),_event.getProcedureDef());
    }
    else if(_event.getProcedureType() == IProcedureType.PROC_TYPE_EXEC)
    {
       procDef = (ShellExecutable) ProcedureDef.convertMapToEntity(
       ShellExecutableEntityFieldID.getEntityFieldID(),_event.getProcedureDef());
       
       //TWX #42 init an empty argument list
       if(procDef == null)
       {
         ShellExecutable shellExe = new ShellExecutable();
         shellExe.setArguments("");
         procDef = shellExe;
         
         System.out.println("CreateUserProcedureAction: ProcedureDef: is null");
       }
    }
    else if(_event.getProcedureType() == IProcedureType.PROC_TYPE_SOAP)
    {
       procDef = (SoapProcedure) ProcedureDef.convertMapToEntity(
       SoapProcedureEntityFieldID.getEntityFieldID(),_event.getProcedureDef());
    }
    if (procDef != null)
      procDef.setType(userProcedure.getProcedureType());
      
    userProcedure.setProcedureDef(procDef);
    
    return userProcedure;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return  getManager().getUserProcedure(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreateUserProcedureEvent.class;
	}

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  	CreateUserProcedureEvent _event = (CreateUserProcedureEvent)event;
    _procedureDefFile = verifyValidProcedureDefFile(_event.getProcedureDefFileUID());
  }

  /**
   * Verify's the Validity of the ProcedureDefinitionFile for existance.
   * @param procedureDefFileUID - ProcedureDefinitionFile UID.
   * @return - ProcedureDefFile
   * @throws Exception - Thrown when Entity is not Found in the DataStore.
   */
  private ProcedureDefFile verifyValidProcedureDefFile(Long procedureDefFileUID)
    throws Exception
  {
    try
    {
      ProcedureDefFile procedureDefFile =
        (ProcedureDefFile)getManager().getProcedureDefinitionFile(procedureDefFileUID);
      return procedureDefFile;
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

  private Vector processPramList(Vector paramVect)
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
