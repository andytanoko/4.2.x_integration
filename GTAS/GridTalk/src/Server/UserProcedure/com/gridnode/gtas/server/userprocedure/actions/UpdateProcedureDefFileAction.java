/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateProcedureDefFileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 12 2002    Jagadeesh         Created
 * Sep 25 2002    Jagadeesh         Included - to handle FileUpload/Update using
 *                                  file services.
 * Jul 30 2003    Koh Han Sing      Add in WSDL.
 */


package com.gridnode.gtas.server.userprocedure.actions;


import java.util.Map;

import com.gridnode.gtas.events.userprocedure.UpdateProcedureDefFileEvent;
import com.gridnode.gtas.model.userprocedure.ProcedureDefFileEntityFieldID;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.gtas.server.userprocedure.helpers.Logger;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerHome;
import com.gridnode.pdip.base.userprocedure.facade.ejb.IUserProcedureManagerObj;
import com.gridnode.pdip.base.userprocedure.helpers.IProcedureDefFilePathConfig;
import com.gridnode.pdip.base.userprocedure.model.IProcedureType;
import com.gridnode.pdip.base.userprocedure.model.ProcedureDefFile;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class UpdateProcedureDefFileAction extends  AbstractUpdateEntityAction
//GridTalkEJBAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3979869400501099001L;

	public static final String ACTION_NAME = "UpdateProcedureDefFileAction";

  private static final String CLASSNAME = "UpdateProcedureDefFileAction";


  private ProcedureDefFile _procedureDefFileToUpdate;


/** Method's Specific to AbstractUpdateEntityAction **/


  protected Class getExpectedEventClass()
  {
    return UpdateProcedureDefFileEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  protected Map convertToMap(AbstractEntity entity)
  {
      return ProcedureDefFile.convertToMap((ProcedureDefFile)entity,
                ProcedureDefFileEntityFieldID.getEntityFieldID(),null);
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
   UpdateProcedureDefFileEvent _event = (UpdateProcedureDefFileEvent) event;
    if (_event.getType() != IProcedureType.PROC_TYPE_EXEC &&
        _event.getType() != IProcedureType.PROC_TYPE_JAVA &&
        _event.getType() != IProcedureType.PROC_TYPE_SOAP)
      throw new EventException("Procedure type not defined: " + _event.getType());
   _procedureDefFileToUpdate = verifyValidProcedureDefFile(_event);
  }

  protected AbstractEntity prepareUpdateData(IEvent event)
  {

    UpdateProcedureDefFileEvent updEvent = (UpdateProcedureDefFileEvent)event;

    _procedureDefFileToUpdate.setName(updEvent.getName());
    _procedureDefFileToUpdate.setDescription(updEvent.getDescription());
    _procedureDefFileToUpdate.setType(updEvent.getType());

    String fileName = updEvent.getFileName();
    try
    {
      if (!fileName.equals(""))
      {
        String oldpathkey = _procedureDefFileToUpdate.getFilePath();
        String oldfilename = _procedureDefFileToUpdate.getFileName();
        Logger.debug("["+CLASSNAME+"] "+"prepareUpdData: Delete Old File oldpathkey :"+oldpathkey);
        Logger.debug("["+CLASSNAME+"] "+"prepareUpdData: Delete Old File oldfilename :"+oldfilename);
        FileUtil.delete(oldpathkey, oldfilename);
        if(_procedureDefFileToUpdate.getType() == IProcedureType.PROC_TYPE_EXEC)
        {
          String uniqueFilename = moveToExecutable(fileName);
          _procedureDefFileToUpdate.setFileName(uniqueFilename);
          _procedureDefFileToUpdate.setFilePath(IProcedureDefFilePathConfig.PATH_EXEC);
        }
        else if(_procedureDefFileToUpdate.getType() == IProcedureType.PROC_TYPE_JAVA)
        {
          if(fileName.endsWith(".class"))
          {
            String uniqueFilename = moveToClasses(fileName);
            _procedureDefFileToUpdate.setFileName(uniqueFilename);
            _procedureDefFileToUpdate.setFilePath(IProcedureDefFilePathConfig.PATH_CLASSES);
          }
          else if(fileName.endsWith(".jar"))
          {
            String uniqueFilename = moveToJars(fileName);
            _procedureDefFileToUpdate.setFileName(uniqueFilename);
            _procedureDefFileToUpdate.setFilePath(IProcedureDefFilePathConfig.PATH_JARS);
          }
        }
        else if(_procedureDefFileToUpdate.getType() == IProcedureType.PROC_TYPE_SOAP)
        {
          String uniqueFilename = moveToWsdl(fileName);
          _procedureDefFileToUpdate.setFileName(uniqueFilename);
          _procedureDefFileToUpdate.setFilePath(IProcedureDefFilePathConfig.PATH_WSDL);
        }
      }
    }
    catch(FileAccessException ex)
    {
       Logger.debug("["+CLASSNAME+"][prepareUpdateData] Cannot Perform Action"+
       ex.getMessage() );
       ex.printStackTrace();
    }
    Logger.debug("["+CLASSNAME+"] "+"prepareUpdData : success");
    return _procedureDefFileToUpdate;
  }

  protected void updateEntity(AbstractEntity entity) throws Exception
  {
    getManager().updateProcedureDefinitionFile((ProcedureDefFile)entity);
  }

  protected AbstractEntity retrieveEntity(Long key) throws Exception
  {
    return getManager().getProcedureDefinitionFile(key);
  }

  protected Object[] getErrorMessageParams(IEvent event)
  {
    UpdateProcedureDefFileEvent updEvent = (UpdateProcedureDefFileEvent)event;
    return new Object[]
           {
             ProcedureDefFile.ENTITY_NAME,
             updEvent.getFileName(),
           };
  }

  private ProcedureDefFile verifyValidProcedureDefFile(UpdateProcedureDefFileEvent updEvent)
      throws Exception
  {
    try
    {
      ProcedureDefFile procedureDefFile = (ProcedureDefFile) getManager(
        ).getProcedureDefinitionFile(updEvent.getUID());
      return procedureDefFile;
    }
    catch (FindEntityException ex)
    {
      throw new Exception("Invalid ProcedureDefinitionFile specified!");
    }
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

  private IUserProcedureManagerObj getManager()
    throws ServiceLookupException
  {
    return (IUserProcedureManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               IUserProcedureManagerHome.class.getName(),
               IUserProcedureManagerHome.class,
               new Object[0]);
  }

}


