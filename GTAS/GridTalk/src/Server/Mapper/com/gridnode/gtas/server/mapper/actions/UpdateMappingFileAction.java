/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateMappingFileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Jul 25 2002    Koh Han Sing        Add new field to store pathkey.
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractUpdateEntityAction.
 * Nov 10 2005    Neo Sok Lay         Use LocalContext to lookup XMLService
 * Jun 03 2010    Tam Wei Xiang       #1100 - Mark the mapping jar as deleted if the system
 *                                    still caching it when user perform update
 */
package com.gridnode.gtas.server.mapper.actions;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gridnode.gtas.events.mapper.UpdateMappingFileEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.mapper.MappingFileEntityFieldID;
import com.gridnode.gtas.server.mapper.helpers.Logger;
import com.gridnode.gtas.server.mapper.helpers.MappingFilePathHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractUpdateEntityAction;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.helpers.MappingFileHelper;
import com.gridnode.pdip.app.mapper.model.IXpathMappingFile;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the update of a MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class UpdateMappingFileAction
  extends    AbstractUpdateEntityAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1249227980008224566L;
	private static final String ACTION_NAME = "UpdateMappingFileAction";

	private MappingFile _mappingFileToUpd;
	private String _uniqueFilename;
	private String _pathKey;
	private String _newPathKey;
	private String _newSubPath;
	
	protected Map convertToMap(AbstractEntity entity)
	{
		return MappingFile.convertToMap(entity, MappingFileEntityFieldID.getEntityFieldID(), null);
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		UpdateMappingFileEvent updEvent = (UpdateMappingFileEvent)event;

		_mappingFileToUpd = getManager().findMappingFile(updEvent.getMappingFileUID());
    String fullpath = updEvent.getMappingFilePath();
    if (!fullpath.equals("")) //file is replaced, move from user location to server
    {
      String oldpathkey = _mappingFileToUpd.getPath();
      String oldSubPath = _mappingFileToUpd.getSubPath();
      String oldfilename = _mappingFileToUpd.getFilename();
      System.out.println("OLDPATHKEY "+oldpathkey);
      System.out.println("OLDSUBPATH "+oldSubPath);
      System.out.println("OLDFILENAME "+oldfilename);
      
      //TWX 20100603 #1100
      try
      {
        FileUtil.delete(oldpathkey, oldSubPath==null?"":oldSubPath, oldfilename);
      }
      catch(FileAccessException e)
      {
        File mapFile = FileUtil.getFile(oldpathkey, oldSubPath==null?"":oldSubPath, oldfilename);
        if(mapFile != null && mapFile.exists())
        {
          Logger.debug("[MappingFileEntityHandler.remove] MappingFile="+mapFile.getAbsolutePath()+" is still referenced by jvm, will be deleted when system is shutdown");
          //mapFile.deleteOnExit();
          MappingFileHelper.markMappingFileAsDeleted(oldpathkey, oldfilename);
        }
        
        Logger.warn("Can not remove Mapping file="+_mappingFileToUpd.getFilename()+ " under subpath="+_mappingFileToUpd.getSubPath(), e);
      }

      fullpath = FileUtil.extractRelativeFilePath(IPathConfig.PATH_TEMP,
                                                fullpath,
                                                false);

      String subpath = FileUtil.extractPath(fullpath);
      String filename = FileUtil.extractFilename(fullpath);
      _pathKey = MappingFilePathHelper.getInstance().getConfigPath(updEvent.getMappingFileType());
      _uniqueFilename = FileUtil.move(
                                IPathConfig.PATH_TEMP,
                                subpath,
                                _pathKey,
                                updEvent.getMappingFileSubPath(),	                                        // 20040304 DDJ
                                filename);
      
    }
    else //mapping file type may change
    {
      Short oldType = _mappingFileToUpd.getType();
      Short newType = updEvent.getMappingFileType();
      String oldSubPath = (oldType.equals(MappingFile.SCHEMA)) ? _mappingFileToUpd.getSubPath() : "";         // 20040304 DDJ
      _newSubPath = (newType.equals(MappingFile.SCHEMA)) ? updEvent.getMappingFileSubPath() : "";          // 20040304 DDJ
      if (!oldType.equals(newType) || !oldSubPath.equals(_newSubPath)) //040309NSL also need to move the file if subpath is changed
      {
        _newPathKey = MappingFilePathHelper.getInstance().getConfigPath(newType);
        String oldPathkey = MappingFilePathHelper.getInstance().getConfigPath(oldType);
        FileUtil.move(oldPathkey, oldSubPath, _newPathKey, _newSubPath, _mappingFileToUpd.getFilename()); // 20040304 DDJ
      }
    }
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		UpdateMappingFileEvent updEvent = (UpdateMappingFileEvent)event;
		return new Object[] {MappingFile.ENTITY_NAME, updEvent.getMappingFileUID()};
	}

	protected AbstractEntity prepareUpdateData(IEvent event)
	{
		UpdateMappingFileEvent updEvent = (UpdateMappingFileEvent)event;
		
		if (_uniqueFilename != null) 
		{
			_mappingFileToUpd.setFilename(_uniqueFilename);
			_mappingFileToUpd.setType(updEvent.getMappingFileType());
			_mappingFileToUpd.setPath(_pathKey);
			_mappingFileToUpd.setSubPath(updEvent.getMappingFileSubPath());               // 20040304 DDJ
		} 
    else if (_newPathKey != null)
    {
    	_mappingFileToUpd.setType(updEvent.getMappingFileType());
    	_mappingFileToUpd.setPath(_newPathKey);
    	_mappingFileToUpd.setSubPath(_newSubPath);                                    // 20040304 DDJ
    }
		_mappingFileToUpd.setDescription(updEvent.getMappingFileDesc());
		return _mappingFileToUpd;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findMappingFile(key);
	}

	protected void updateEntity(AbstractEntity entity) throws Exception
	{
		MappingFile mappingFile = (MappingFile)entity;
		getManager().updateMappingFile(mappingFile);

    if (mappingFile.getType().equals(MappingFile.XPATH))
    {
      updateXpathMapping(mappingFile);
    }
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return UpdateMappingFileEvent.class;
	}
/*
	public IEventResponse perform(IEvent event)
    throws EventException
  {
    MappingFile mappingFileToUpdate = null;
    UpdateMappingFileEvent updEvent;
    IEventResponse response = null;
    IMappingManagerObj mgr;

    updEvent = (UpdateMappingFileEvent)event;

    try
    {
      if (updEvent == null)
        throw new EventException("Event is null");
      validateCurrentState();
      mgr = getManager();

      mappingFileToUpdate = mgr.findMappingFile(updEvent.getMappingFileUID());

      updateActionData(mappingFileToUpdate, updEvent);
      mgr.updateMappingFile(mappingFileToUpdate);

      if (mappingFileToUpdate.getType().equals(MappingFile.XPATH))
      {
        updateXpathMapping(mappingFileToUpdate);
      }

      response = constructEventResponse(mappingFileToUpdate);
    }
    catch (FindEntityException ex)
    {
      response = constructEventResponse(updEvent,
                   new UpdateEntityException("Unable to find mapping file"));
    }
    catch (TypedException ex)
    {
      response = constructEventResponse(updEvent, ex);
    }
    catch (Throwable ex)
    {
      response = constructEventResponse(updEvent,
                   new SystemException(ex));
    }
    return response;
  }

  private void updateActionData(
    MappingFile updateMappingFile,
    UpdateMappingFileEvent event)
    throws FileAccessException
  {
    String fullpath = event.getMappingFilePath();
//System.out.println("[DEBUG] UpdateMappingFileAction.updateActionData(): fullpath=" + fullpath);
//System.out.println("[DEBUG] UpdateMappingFileAction.updateActionData(): event.getMappingFileSubPath()=" + event.getMappingFileSubPath());
    if (!fullpath.equals(""))
    {
      String oldpathkey = updateMappingFile.getPath();
      String oldSubPath = updateMappingFile.getSubPath();
      String oldfilename = updateMappingFile.getFilename();
      FileUtil.delete(oldpathkey, oldSubPath==null?"":oldSubPath, oldfilename);

      fullpath = FileUtil.extractRelativeFilePath(IPathConfig.PATH_TEMP,
                                                fullpath,
                                                false);

      String subpath = FileUtil.extractPath(fullpath);
      String filename = FileUtil.extractFilename(fullpath);
      String pathkey = MappingFilePathHelper.getInstance().getConfigPath(
                                  event.getMappingFileType());
      String uniqueFilename = FileUtil.move(
                                IPathConfig.PATH_TEMP,
                                subpath,
                                pathkey,
                                event.getMappingFileSubPath(),	                                        // 20040304 DDJ
                                filename);
      updateMappingFile.setFilename(uniqueFilename);
      updateMappingFile.setType(event.getMappingFileType());
      updateMappingFile.setPath(pathkey);
      updateMappingFile.setSubPath(event.getMappingFileSubPath());                                      // 20040304 DDJ
    }
    else
    {
      Short oldType = updateMappingFile.getType();
      Short newType = event.getMappingFileType();
      String oldSubPath = (oldType.equals(MappingFile.SCHEMA)) ? updateMappingFile.getSubPath() : "";         // 20040304 DDJ
      String newSubPath = (newType.equals(MappingFile.SCHEMA)) ? event.getMappingFileSubPath() : "";          // 20040304 DDJ
//System.out.println("[DEBUG] UpdateMappingFileAction.updateActionData(): oldType=" + oldType + " newType=" + newType + " MappingFile.SCHEMA=" + MappingFile.SCHEMA);
//System.out.println("[DEBUG] UpdateMappingFileAction.updateActionData(): oldSubPath=" + oldSubPath);
//System.out.println("[DEBUG] UpdateMappingFileAction.updateActionData(): newSubPath=" + newSubPath);
      if (!oldType.equals(newType) || !oldSubPath.equals(newSubPath)) //040309NSL also need to move the file if subpath is changed
      {
        String oldPathkey = MappingFilePathHelper.getInstance().getConfigPath(oldType);
        String newPathkey = MappingFilePathHelper.getInstance().getConfigPath(newType);
        //FileUtil.move(oldPathkey, newPathkey, updateMappingFile.getFilename());                       // 20040304 DDJ
        FileUtil.move(oldPathkey, oldSubPath, newPathkey, newSubPath, updateMappingFile.getFilename()); // 20040304 DDJ
        updateMappingFile.setType(newType);
        updateMappingFile.setPath(newPathkey);
        updateMappingFile.setSubPath(newSubPath);                                                       // 20040304 DDJ
      }
    }
    updateMappingFile.setDescription(event.getMappingFileDesc());
  }

  private IEventResponse constructEventResponse(
    UpdateMappingFileEvent event, TypedException ex)
  {
    BasicEventResponse response = null;
    Object[] params = null;

    params = (event==null)?
               new Object[]
               {
                 MappingFile.ENTITY_NAME,
                 "null",
                 "null"
               } :
               new Object[]
               {
                 MappingFile.ENTITY_NAME,
                 event.getMappingFileUID(),
                 event.getMappingFileDesc()
               };

    response = new BasicEventResponse(
                   IErrorCode.UPDATE_ENTITY_ERROR,
                   params,
                   ex.getType(),
                   ex.getLocalizedMessage(),
                   ex.getStackTraceString());

    Logger.err("[UpdateMappingFileAction.perform] Event Error ", ex);

    return response;
  }

  private IEventResponse constructEventResponse(MappingFile data)
  {
    BasicEventResponse response = null;
    Object[] params = null;
    Object objparam = null;

    if (data != null)
    {
      params = new Object[] { data.getName(), data.getDescription()};
      objparam = data.convertToMap(
                        data,
                        MappingFileEntityFieldID.getEntityFieldID(),
                        null);
    }

    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   objparam);

    return response;
  }
*/
  private void updateXpathMapping(MappingFile updXpathFile)
    throws Exception
  {
    Long uid = new Long(updXpathFile.getUId());
    String filename = updXpathFile.getFilename();
    String path = MappingFilePathHelper.getInstance().getConfigPath(updXpathFile.getType());
    File xpathFile = FileUtil.getFile(path, filename);
    String rootElementName = "";
    List values = getXMLManager().getXPathValues(xpathFile.getAbsolutePath(),
                                                 IXpathMappingFile.PROPERTY_XPATH);
    for (Iterator i = values.iterator(); i.hasNext(); )
    {
      String xpathValue = i.next().toString();
      if ((xpathValue.indexOf("/") > 0) && (!xpathValue.startsWith("\"")))
      {
        rootElementName = xpathValue.substring(0, xpathValue.indexOf("/"));
        break;
      }
    }
    XpathMapping newMapping = getManager().findXpathUid(uid);
    newMapping.setRootElement(rootElementName);
    newMapping.setXpathUid(uid);   
    getManager().updateXpathMapping(newMapping);
  }
  /*
  private IGridTalkMappingManagerObj getGTMappingManager()
    throws ServiceLookupException
  {
    return (IGridTalkMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IGridTalkMappingManagerHome.class.getName(),
      IGridTalkMappingManagerHome.class,
      new Object[0]);
  }*/

  private IMappingManagerObj getManager()
    throws ServiceLookupException
  {
    return (IMappingManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IMappingManagerHome.class.getName(),
      IMappingManagerHome.class,
      new Object[0]);
  }

  private IXMLServiceLocalObj getXMLManager()
    throws ServiceLookupException
  {
    return (IXMLServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IXMLServiceLocalHome.class.getName(),
      IXMLServiceLocalHome.class,
      new Object[0]);
  }
}