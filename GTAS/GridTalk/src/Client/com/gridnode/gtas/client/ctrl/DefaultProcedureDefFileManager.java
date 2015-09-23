/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultProcedureDefFileManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2003-07-16     Andrew Hill         getMethodsForClass() and getClassesInJar()
 * 2003-07-30     Andrew Hill         list wsdl methods
 */
package com.gridnode.gtas.client.ctrl;

import java.util.*;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.userprocedure.*;
import com.gridnode.pdip.framework.rpf.event.EventException;


class DefaultProcedureDefFileManager extends DefaultAbstractManager
  implements IGTProcedureDefFileManager
{  
  //20030716AH - Constants for indexes into object array returned by GetMethodsFromClassEvent
  private static final int METHOD_INDEX_NAME = 0;
  private static final int METHOD_INDEX_RETURN_TYPE = 1;
  private static final int METHOD_INDEX_PARAM_NAMES = 2;
  private static final int METHOD_INDEX_PARAM_TYPES = 3;
  //private static final int METHOD_INDEX_ID = 4;
  //..  
  
  DefaultProcedureDefFileManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PROCEDURE_DEF_FILE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTProcedureDefFileEntity procedureDefFile = (IGTProcedureDefFileEntity)entity;

      Long uid = (Long)procedureDefFile.getFieldValue(IGTProcedureDefFileEntity.UID);
      String name = procedureDefFile.getFieldString(IGTProcedureDefFileEntity.NAME);
      String description = procedureDefFile.getFieldString(IGTProcedureDefFileEntity.DESCRIPTION);
      Integer type = (Integer)procedureDefFile.getFieldValue(IGTProcedureDefFileEntity.TYPE);
      String filename = procedureDefFile.getFieldString(IGTProcedureDefFileEntity.FILE_NAME);

      UpdateProcedureDefFileEvent event = new UpdateProcedureDefFileEvent(uid, name, description, type, filename);

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
      IGTProcedureDefFileEntity procedureDefFile = (IGTProcedureDefFileEntity)entity;

      String name = procedureDefFile.getFieldString(IGTProcedureDefFileEntity.NAME);
      String description = procedureDefFile.getFieldString(IGTProcedureDefFileEntity.DESCRIPTION);
      Integer type = (Integer)procedureDefFile.getFieldValue(IGTProcedureDefFileEntity.TYPE);
      String filename = procedureDefFile.getFieldString(IGTProcedureDefFileEntity.FILE_NAME);

      CreateProcedureDefFileEvent event = new CreateProcedureDefFileEvent(name, description, type, filename);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PROCEDURE_DEF_FILE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PROCEDURE_DEF_FILE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcedureDefFileEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcedureDefFileListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteProcedureDefFileEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PROCEDURE_DEF_FILE.equals(entityType))
    {
      return new DefaultProcedureDefFileEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
  
  
  public List listClassesInJar(Long procDefFileUid) throws GTClientException
  { //20030716AH
    if (procDefFileUid == null)
      throw new NullPointerException("procedureDefUid is null");
    try
    {
      GetClassesFromJarEvent event = new GetClassesFromJarEvent(procDefFileUid);
      List classes = (List)handleEvent(event);
      return classes == null ? Collections.EMPTY_LIST : classes; 
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list of classes in jar file for procedureDefFile with uid="
                                  + procDefFileUid,t);
    }
  }

  public List listMethodsOfClass(String className) throws GTClientException
  { //20030716AH
    if (className == null)
      throw new NullPointerException("className is null");
    try
    {
      GetMethodsFromClassEvent event = new GetMethodsFromClassEvent(className);
      Object retval = handleEvent(event);
      if (retval != null)
      {
        if( !(retval instanceof Collection) )
        {
          throw new ClassCastException("Expecting to be returned an instance of Collection by handleEvent() but given "
                                        + retval.getClass().getName() + " (= " + retval + " )");
        }
      }
      Collection methodsCollection = (Collection)retval;
      return createMethodsList(methodsCollection); //20030730AH
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list of methods in class "
                                  + className,t);
    }
  }
  
  private List createMethodsList(Collection methodsCollection) throws GTClientException
  { //20030730AH - Factored method list creation code out of listmethodsOfClass
    try
    {
      if( (methodsCollection == null) || (methodsCollection.isEmpty()) )
      {
        return Collections.EMPTY_LIST;
      }
      else
      {
        int numMethods = methodsCollection.size();
        Iterator iterator = methodsCollection.iterator();
        List methods = new ArrayList( numMethods );
        for(int i=0; iterator.hasNext(); i++)
        {
          Object[] methodArray = (Object[])iterator.next();
          MethodDescriptor method = new MethodDescriptor();
          method.setName( (String)methodArray[METHOD_INDEX_NAME] );
          method.setReturnType( (String)methodArray[METHOD_INDEX_RETURN_TYPE] );
          //method.setId( (String)methodArray[METHOD_INDEX_ID] );
          String[] paramArray = (String[])methodArray[METHOD_INDEX_PARAM_NAMES];
          String[] paramTypeArray = (String[])methodArray[METHOD_INDEX_PARAM_TYPES];
          if( (paramArray) != null && (paramTypeArray != null) )
          {
            if(paramArray.length != paramTypeArray.length)
            { //Sanity check
              throw new IllegalStateException("Inconsistent method parameter data returned."
                          + " paramArray.length="
                          + paramArray.length
                          + " but paramTypeArray.length="
                          + paramTypeArray);
            }
            MethodDescriptor.Parameter[] params = new MethodDescriptor.Parameter[paramArray.length];
            for(int j=0; j < paramArray.length; j++)
            {
              MethodDescriptor.Parameter param = method.new Parameter();
              param.setName(paramArray[j]);
              param.setType(paramTypeArray[j]);
              params[j] = param;
            }
            method.setParameters(params);
          }
          methods.add(method);
        }
        return methods;
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error creating method descriptor list",t);
    }
  }

  public List listGroupedMethodsOfClass(String className) throws GTClientException
  {
    List methods = listMethodsOfClass(className);
    return groupMethodList(methods); //20030330AH
  }
  
  private List groupMethodList(List methods) throws GTClientException
  { //20030330AH - Factored grouping code out of listGroupedMethodsOfClass
    try
    {
      Collections.sort(methods);
      ArrayList groups = new ArrayList();
      String currentName = null;
      ArrayList currentGroup = null;
      Iterator i = methods.iterator();
      while(i.hasNext())
      {
        MethodDescriptor method = (MethodDescriptor)i.next();
        if(!method.getName().equals(currentName))
        {
          currentGroup = new ArrayList();
          groups.add(currentGroup);
        }
        currentGroup.add(method);
        currentName = method.getName();
      }
      return groups;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error grouping methods",t);
    }
  }

  public List listGroupedMethodsOfWSDL(Long procDefFileUid) throws GTClientException
  { //20030730AH
    List methods = listMethodsOfWSDL(procDefFileUid);
    return groupMethodList(methods); //20030330AH
  }

  public List listMethodsOfWSDL(Long procDefFileUid) throws GTClientException
  { //20030730AH
    if (procDefFileUid == null)
      throw new NullPointerException("procDefFileUid is null");
    try
    {
      GetMethodsFromWSDLEvent event = new GetMethodsFromWSDLEvent(procDefFileUid);
      Object retval = handleEvent(event);
      if (retval != null)
      {
        if( !(retval instanceof Collection) )
        {
          throw new ClassCastException("Expecting to be returned an instance of Collection by handleEvent() but given "
                                        + retval.getClass().getName() + " (= " + retval + " )");
        }
      }
      Collection methodsCollection = (Collection)retval;
      return createMethodsList(methodsCollection); 
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list of methods in WSDL for procDefFile with uid "
                                  + procDefFileUid,t);
    }
  }

}