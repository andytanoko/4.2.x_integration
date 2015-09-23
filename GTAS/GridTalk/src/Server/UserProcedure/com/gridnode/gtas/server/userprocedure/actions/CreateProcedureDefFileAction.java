/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateProcedureDefFileAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh               Created
 *                                        Map representation of the Entity
 * Sep 25 2002    Jagadeesh               Included Process to upload the file,
 *                                        and handle file processing using FileServices.
 * Jan 29 2003    Jagadeesh               Modified: To move files to respective folders.
 * Jul 30 2003    Koh Han Sing            Add in WSDL.
 */



package com.gridnode.gtas.server.userprocedure.actions;



import java.util.Map;

import com.gridnode.gtas.events.userprocedure.CreateProcedureDefFileEvent;
import com.gridnode.gtas.model.userprocedure.ProcedureDefFileEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.helpers.IProcedureDefFilePathConfig;
import com.gridnode.pdip.base.userprocedure.model.IProcedureType;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;



public class CreateProcedureDefFileAction extends AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7013484555037046821L;
	private CreateProcedureDefFileEvent _event = null;
  //private ProcedureDefFile _procedureDefFile = null;

  public static final String ACTION_NAME = "CreateProcedureDefFileAction";


  protected Class getExpectedEventClass()
  {
    return CreateProcedureDefFileEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }


/** Please Note the following method's are commented as this Action is using AbstractXXXAction. **/



  /*  public IEventResponse perform(IEvent event)
    throws EventException
  {
    _event = (CreateProcedureDefFileEvent)event;

    IEventResponse response  = null;
    Object[] params = null;
    try
    {
      _procedureDefFile = (ProcedureDefFile)prepareActionData(_event);
      Long uid = getManager().createProcedureDefinitionFile(_procedureDefFile);
      _procedureDefFile = (ProcedureDefFile)getManager().getProcedureDefinitionFile(uid);

      params = new Object[] { _procedureDefFile.getEntityName(), _procedureDefFile.getEntityDescr()};
      response = constructEventResponse(params);
    }
    catch (TypedException ex)
    {
      response = constructEventResponse(params, _event, ex);
    }
    catch (Throwable ex)
    {
      response = constructEventResponse(params, _event, new SystemException(ex));
    }
    return response;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
    _event = (CreateProcedureDefFileEvent) event;
    if (_event.getType() != IProcedureType.PROC_TYPE_EXEC &&
        _event.getType() != IProcedureType.PROC_TYPE_JAVA)
      throw new EventException("Procedure type not defined: " + _event.getType());
  }


  private Object prepareActionData(IEvent event)
    throws Exception
  {
    _event = (CreateProcedureDefFileEvent)event;
    ProcedureDefFile procedureDefFile = new ProcedureDefFile();
    procedureDefFile.setName(_event.getName());
    procedureDefFile.setDescription(_event.getDescription());
    String newFilename = FileUtil.copy(IPathConfig.PATH_TEMP,
                                         _userID+"/in/",
                                         filename,
                                         IProcedureDefFilePathConfig.PATH_EXEC,
                                         "",
                                         filename);

    procedureDefFile.setFileName(_event.getFileName());
    procedureDefFile.setFilePath(_event.getFilePath());
    procedureDefFile.setType(_event.getType());
    return procedureDefFile;
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    Logger.debug("[CreateProcedureDefFileAction] [constructEventResponse]" +
      "Event Successful ");
    BasicEventResponse response = null;
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                  _procedureDefFile.convertToMap(
                    _procedureDefFile,
                    ProcedureDefFileEntityFieldID.getEntityFieldID(),
                    null));
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    Logger.err("[CreateProcedureDefFileAction] [perform]" +
    "Event Error ", ex);
    BasicEventResponse response = null;
    response = new BasicEventResponse(
                 IErrorCode.CREATE_ENTITY_ERROR,
                 params,
                 ex.getType(),
                 ex.getLocalizedMessage(),
                 ex.getStackTraceString());
    return response;
  }


  */



   // ******************* AbstractCreateEntityAction methods *************

  protected AbstractEntity retrieveEntity(Long key) throws java.lang.Exception
  {
    return getManager().getProcedureDefinitionFile(key);
  }

  protected AbstractEntity prepareCreationData(IEvent event)
  {
    _event = (CreateProcedureDefFileEvent)event;
    ProcedureDefFile procedureDefFile = new ProcedureDefFile();
    procedureDefFile.setName(_event.getName());
    procedureDefFile.setDescription(_event.getDescription());
    procedureDefFile.setType(_event.getType());
    String filename = _event.getFileName();
    String newFileName = null;
    if(_event.getType() == IProcedureType.PROC_TYPE_EXEC)
    {
       try
       {
         newFileName = moveToExecutable(filename);
         procedureDefFile.setFileName(newFileName);
         procedureDefFile.setFilePath(IProcedureDefFilePathConfig.PATH_EXEC); //Confirmed with Hang Seng.
       }
       catch(FileAccessException ex)
       {
         Logger.debug("[CreateProcedureDefFileAction][prepareCreationData]"+
          "Cannot Perform Action"+ex.getMessage() );
         ex.printStackTrace();
       }
    }
    else if(_event.getType() == IProcedureType.PROC_TYPE_JAVA)
    {
       try
       {
         if(filename.endsWith(".jar"))
         {
            String newFilename = moveToJars(filename);
            procedureDefFile.setFileName(newFilename);
            procedureDefFile.setFilePath(IProcedureDefFilePathConfig.PATH_JARS);
         }
         else if(filename.endsWith(".class"))
         {
            String newFilename = moveToClasses(filename);
            procedureDefFile.setFileName(newFilename);
            procedureDefFile.setFilePath(IProcedureDefFilePathConfig.PATH_CLASSES);
         }
       }
       catch(FileAccessException ex)
       {
          Logger.debug("[CreateProcedureDefFileAction][prepareCreationData] Cannot Perform Action"+
          ex.getMessage() );
          ex.printStackTrace();
       }
    }
    else if(_event.getType() == IProcedureType.PROC_TYPE_SOAP)
    {
       try
       {
         newFileName = moveToWsdl(filename);
         procedureDefFile.setFileName(newFileName);
         procedureDefFile.setFilePath(IProcedureDefFilePathConfig.PATH_WSDL);
       }
       catch(FileAccessException ex)
       {
         Logger.debug("[CreateProcedureDefFileAction][prepareCreationData]"+
          "Cannot Perform Action"+ex.getMessage() );
         ex.printStackTrace();
       }
    }

    return procedureDefFile;
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    CreateProcedureDefFileEvent procedureEvent = (CreateProcedureDefFileEvent) event;

    return new Object[]
           {
             ProcedureDefFile.ENTITY_NAME,
             procedureEvent.getFileName(),
           };
  }

  protected Long createEntity(AbstractEntity entity) throws java.lang.Exception
  {
    return getManager().createProcedureDefinitionFile((ProcedureDefFile)entity);
  }

  protected Map convertToMap(AbstractEntity entity)
  {
      return ProcedureDefFile.convertToMap((ProcedureDefFile)entity,
                ProcedureDefFileEntityFieldID.getEntityFieldID(),null);

  }

  // ****************** Own methods **********************************


  private IUserProcedureManagerObj getManager()
    throws ServiceLookupException
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserProcedureManagerHome.class.getName(),
               IUserProcedureManagerHome.class,
               new Object[0]);
  }



  private String moveToJars(String fileName)
      throws FileAccessException
  {
    String newFilename = FileUtil.move(IPathConfig.PATH_TEMP,_userID+"/in/",
                              IProcedureDefFilePathConfig.PATH_JARS,"",fileName);
    return newFilename;
  }

  private String moveToClasses(String fileName)
      throws FileAccessException
  {
    String newFilename = FileUtil.move(IPathConfig.PATH_TEMP,_userID+"/in/",
                              IProcedureDefFilePathConfig.PATH_CLASSES,"",fileName);
    return newFilename;
  }

  private String moveToExecutable(String fileName)
      throws FileAccessException
  {
    String newFilename = FileUtil.move(IPathConfig.PATH_TEMP,_userID+"/in/",
                              IProcedureDefFilePathConfig.PATH_EXEC,"",fileName);
    return newFilename;
  }

  private String moveToWsdl(String fileName)
      throws FileAccessException
  {
    String newFilename = FileUtil.move(IPathConfig.PATH_TEMP,_userID+"/in/",
                              IProcedureDefFilePathConfig.PATH_WSDL,"",fileName);
    return newFilename;
  }

}




