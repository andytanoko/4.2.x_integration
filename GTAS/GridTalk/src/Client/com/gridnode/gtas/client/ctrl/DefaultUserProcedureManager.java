/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultUserProcedureManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-22     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2003-07-16     Andrew Hill         Add support for the GRID_DOC_FIELD field
 * 2003-07-30     Andrew Hill         Support for soapProcedure
 * 2006-03-21     Neo Sok Lay         Add GridDocField in update event.
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.userprocedure.*;
import com.gridnode.gtas.model.userprocedure.IJavaProcedure;
import com.gridnode.gtas.model.userprocedure.IParamDef;
import com.gridnode.gtas.model.userprocedure.IReturnDef;
import com.gridnode.gtas.model.userprocedure.IShellExecutable;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultUserProcedureManager extends DefaultAbstractManager
  implements IGTUserProcedureManager
{
  DefaultUserProcedureManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_USER_PROCEDURE, session);
  }

  private HashMap extractProcDef(IGTUserProcedureEntity userProcedure)
    throws GTClientException
  {
    try
    {
      IGTEntity procDefEntity = (IGTEntity)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_DEF);
      if(procDefEntity != null)
      {
        HashMap procDef = new HashMap();
        if(procDefEntity instanceof IGTShellExecutableEntity)
        {
          IGTShellExecutableEntity shellExecutable = (IGTShellExecutableEntity)procDefEntity;
          procDef.put(IShellExecutable.ARGUMENTS, shellExecutable.getFieldValue(IGTShellExecutableEntity.EXEC_ARGUMENTS));
        }
        else if(procDefEntity instanceof IGTJavaProcedureEntity)
        {
          IGTJavaProcedureEntity javaProcedure = (IGTJavaProcedureEntity)procDefEntity;
          procDef.put(IJavaProcedure.CLASS_NAME,  javaProcedure.getFieldValue(IGTJavaProcedureEntity.JAVA_CLASS_NAME));
          procDef.put(IJavaProcedure.IS_LOCAL,    javaProcedure.getFieldValue(IGTJavaProcedureEntity.JAVA_IS_LOCAL));
          procDef.put(IJavaProcedure.METHOD_NAME, javaProcedure.getFieldValue(IGTJavaProcedureEntity.JAVA_METHOD_NAME));
          procDef.put(IJavaProcedure.JVM_OPTIONS, javaProcedure.getFieldValue(IGTJavaProcedureEntity.JAVA_JVM_OPTIONS));
          procDef.put(IJavaProcedure.ARGUMENTS,   javaProcedure.getFieldValue(IGTJavaProcedureEntity.JAVA_ARGUMENTS));
        }
        else if(procDefEntity instanceof IGTSoapProcedureEntity)
        { //20030730AH
          IGTSoapProcedureEntity soapProcedure = (IGTSoapProcedureEntity)procDefEntity;
          procDef.put(IGTSoapProcedureEntity.SOAP_METHOD_NAME,  soapProcedure.getFieldValue(IGTSoapProcedureEntity.SOAP_METHOD_NAME));
          procDef.put(IGTSoapProcedureEntity.SOAP_USER_NAME,    soapProcedure.getFieldValue(IGTSoapProcedureEntity.SOAP_USER_NAME));
          procDef.put(IGTSoapProcedureEntity.SOAP_PASSWORD,     soapProcedure.getFieldValue(IGTSoapProcedureEntity.SOAP_PASSWORD));
        }
        else
        {
          throw new java.lang.IllegalStateException("Invalid entity class for ProcDef = " + procDef);
        }
        return procDef;
      }
      else
      {
        return null;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting procDef from " + userProcedure, t);
    }
  }

  private List extractProcParamList(IGTUserProcedureEntity userProcedure)
    throws GTClientException
  {
    try
    {
      List procParamList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_PARAM_LIST);
      if(procParamList != null)
      {
        List paramList = new Vector(procParamList.size());
        Iterator i = procParamList.iterator();
        while(i.hasNext())
        {
          IGTParamDefEntity paramDef = (IGTParamDefEntity)i.next();
          if(i == null) throw new java.lang.NullPointerException("Null ParamDef entity found in procParamList of " + userProcedure);
          HashMap procParam = new HashMap();
          procParam.put(IParamDef.NAME,         paramDef.getFieldValue(IGTParamDefEntity.PARAM_LIST_NAME));
          procParam.put(IParamDef.DESCRIPTION,  paramDef.getFieldValue(IGTParamDefEntity.PARAM_LIST_DESCRIPTION));
          procParam.put(IParamDef.TYPE,         paramDef.getFieldValue(IGTParamDefEntity.PARAM_LIST_TYPE));
          procParam.put(IParamDef.SOURCE,       paramDef.getFieldValue(IGTParamDefEntity.PARAM_LIST_SOURCE));
          procParam.put(IParamDef.VALUE,        paramDef.getFieldValue(IGTParamDefEntity.PARAM_LIST_VALUE));
          procParam.put(IParamDef.DATE_FORMAT,  paramDef.getFieldValue(IGTParamDefEntity.PARAM_LIST_DATE_FORMAT));
          paramList.add(procParam);
        }
        return paramList;
      }
      else
      {
        return new Vector(0);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting procParamList from " + userProcedure, t);
    }
  }

  private List extractProcReturnList(IGTUserProcedureEntity userProcedure)
    throws GTClientException
  {
    try
    {
      List procReturnList = (List)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_RETURN_LIST);
      if(procReturnList != null)
      {
        List returnList = new Vector(procReturnList.size());
        Iterator i = procReturnList.iterator();
        while(i.hasNext())
        {
          IGTReturnDefEntity returnDef = (IGTReturnDefEntity)i.next();
          if(i == null) throw new java.lang.NullPointerException("Null ReturnDef entity found in procReturnList of " + userProcedure);
          HashMap procReturn = new HashMap();
          procReturn.put(IReturnDef.OPERATOR, returnDef.getFieldValue(IGTReturnDefEntity.RETURN_LIST_OPERATOR));
          procReturn.put(IReturnDef.VALUE,    returnDef.getFieldValue(IGTReturnDefEntity.RETURN_LIST_VALUE));
          procReturn.put(IReturnDef.ACTION,   returnDef.getFieldValue(IGTReturnDefEntity.RETURN_LIST_ACTION));
          procReturn.put(IReturnDef.ALERT,    returnDef.getFieldValue(IGTReturnDefEntity.RETURN_LIST_ALERT));
          returnList.add(procReturn);
        }
        return returnList;
      }
      else
      {
        return new Vector(0);
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error extracting procReturnList from " + userProcedure, t);
    }
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)entity;

      Long uid              = (Long)userProcedure.getFieldValue(IGTUserProcedureEntity.UID);
      String name           = userProcedure.getFieldString(IGTUserProcedureEntity.NAME);
      String description    = userProcedure.getFieldString(IGTUserProcedureEntity.DESCRIPTION);
      Boolean isSynchronous = (Boolean)userProcedure.getFieldValue(IGTUserProcedureEntity.IS_SYNCHRONOUS);
      Integer procType      = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_TYPE);
      Long procDefFile      = (Long)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_DEF_FILE);
      HashMap procDef       = extractProcDef(userProcedure);
      Vector procParamList  = (Vector)extractProcParamList(userProcedure);
      Integer returnType    = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.RETURN_DATA_TYPE);
      Integer defAction     = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.DEF_ACTION);
      Long defAlert         = (Long)userProcedure.getFieldValue(IGTUserProcedureEntity.DEF_ALERT);
      Vector procReturnList = (Vector)extractProcReturnList(userProcedure);
      Integer gridDocField  = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.GRID_DOC_FIELD); //NSL20060321

      UpdateUserProcedureEvent event = new UpdateUserProcedureEvent(uid,
                                                                    name,
                                                                    description,
                                                                    isSynchronous,
                                                                    procType,
                                                                    procDefFile,
                                                                    procDef,
                                                                    procParamList,
                                                                    returnType,
                                                                    defAction,
                                                                    defAlert,
                                                                    procReturnList,
                                                                    gridDocField); //NSL20060321

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)entity;

      String name           = userProcedure.getFieldString(IGTUserProcedureEntity.NAME);
      String description    = userProcedure.getFieldString(IGTUserProcedureEntity.DESCRIPTION);
      Boolean isSynchronous = (Boolean)userProcedure.getFieldValue(IGTUserProcedureEntity.IS_SYNCHRONOUS);
      Integer procType      = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_TYPE);
      Long procDefFile      = (Long)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_DEF_FILE);
      HashMap procDef       = extractProcDef(userProcedure);
      Vector procParamList  = (Vector)extractProcParamList(userProcedure);
      Integer returnType    = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.RETURN_DATA_TYPE);
      Integer defAction     = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.DEF_ACTION);
      Long defAlert         = (Long)userProcedure.getFieldValue(IGTUserProcedureEntity.DEF_ALERT);
      Vector procReturnList = (Vector)extractProcReturnList(userProcedure);
      Integer gridDocField  = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.GRID_DOC_FIELD); //20030716AH

      CreateUserProcedureEvent event = new CreateUserProcedureEvent(name,
                                                                    description,
                                                                    isSynchronous,
                                                                    procType,
                                                                    procDefFile,
                                                                    procDef,
                                                                    procParamList,
                                                                    returnType,
                                                                    defAction,
                                                                    defAlert,
                                                                    procReturnList,
                                                                    gridDocField);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_USER_PROCEDURE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_USER_PROCEDURE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetUserProcedureEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetUserProcedureListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteUserProcedureEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_USER_PROCEDURE.equals(entityType))
    {
      return new DefaultUserProcedureEntity();
    }
    else if(IGTEntity.ENTITY_SHELL_EXECUTABLE.equals(entityType))
    {
      return new DefaultShellExecutableEntity();
    }
    else if(IGTEntity.ENTITY_JAVA_PROCEDURE.equals(entityType))
    {
      return new DefaultJavaProcedureEntity();
    }
    else if(IGTEntity.ENTITY_SOAP_PROCEDURE.equals(entityType))
    { //20030730AH
      return new DefaultSoapProcedureEntity();
    }
    else if(IGTEntity.ENTITY_PARAM_DEF.equals(entityType))
    {
      return new DefaultParamDefEntity();
    }
    else if(IGTEntity.ENTITY_RETURN_DEF.equals(entityType))
    {
      return new DefaultReturnDefEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  protected String getDynamicType(Number fieldId,
                                  IDynamicEntityConstraint constraint,
                                  Map serverMap,
                                  IGTEntity entity,
                                  int index)
    throws GTClientException
  {
    IGTUserProcedureEntity userProcedure = (IGTUserProcedureEntity)entity;
    Integer procType = (Integer)userProcedure.getFieldValue(IGTUserProcedureEntity.PROC_TYPE);
    if(IGTUserProcedureEntity.TYPE_EXECUTABLE.equals(procType))
    {
      return IGTEntity.ENTITY_SHELL_EXECUTABLE;
    }
    else if(IGTUserProcedureEntity.TYPE_JAVA.equals(procType))
    {
      return IGTEntity.ENTITY_JAVA_PROCEDURE;
    }
    else if(IGTUserProcedureEntity.TYPE_SOAP.equals(procType))
    { //20030730AH
      return IGTEntity.ENTITY_SOAP_PROCEDURE;
    }

    return null;
  }

  public IGTShellExecutableEntity newShellExecutable() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SHELL_EXECUTABLE);
    entity.setNewEntity(true);
    return (IGTShellExecutableEntity)entity;
  }

  public IGTJavaProcedureEntity newJavaProcedure() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_JAVA_PROCEDURE);
    entity.setNewEntity(true);
    return (IGTJavaProcedureEntity)entity;
  }

  public IGTSoapProcedureEntity newSoapProcedure() throws GTClientException
  { //20030730AH
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_SOAP_PROCEDURE);
    entity.setNewEntity(true);
    return (IGTSoapProcedureEntity)entity;
  }

  public IGTParamDefEntity newParamDef() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_PARAM_DEF);
    entity.setNewEntity(true);
    return (IGTParamDefEntity)entity;
  }

  public IGTReturnDefEntity newReturnDef() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_RETURN_DEF);
    entity.setNewEntity(true);
    return (IGTReturnDefEntity)entity;
  }
}