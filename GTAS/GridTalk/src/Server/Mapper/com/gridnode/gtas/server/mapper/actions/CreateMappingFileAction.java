/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateMappingFileAction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 * Jul 25 2002    Koh Han Sing        Add new field to store pathkey.
 * Sep 07 2005    Neo Sok Lay         Change to extend from AbstractCreateEntityAction
 * Nov 10 2005    Neo Sok Lay         Use Localcontext to lookup XMLService
 */
package com.gridnode.gtas.server.mapper.actions;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gridnode.gtas.events.mapper.CreateMappingFileEvent;
import com.gridnode.gtas.model.mapper.MappingFileEntityFieldID;
import com.gridnode.gtas.server.mapper.helpers.MappingFilePathHelper;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractCreateEntityAction;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerHome;
import com.gridnode.pdip.app.mapper.facade.ejb.IMappingManagerObj;
import com.gridnode.pdip.app.mapper.model.IXpathMappingFile;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalHome;
import com.gridnode.pdip.base.xml.facade.ejb.IXMLServiceLocalObj;
import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This Action class handles the creation of a new MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class CreateMappingFileAction
  extends    AbstractCreateEntityAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4549321218607796478L;
	private static final String ACTION_NAME = "CreateMappingFileAction";

	private String _pathKey;
	private String _uniqueFilename;
	
	protected Map convertToMap(AbstractEntity entity)
	{
		return MappingFile.convertToMap(entity, MappingFileEntityFieldID.getEntityFieldID(), null);
	}

	protected Long createEntity(AbstractEntity entity) throws Exception
	{
		MappingFile newMappingFile = getManager().createMappingFile((MappingFile)entity);

    if (newMappingFile.getType().equals(MappingFile.XPATH))
    {
      createXpathMapping(newMappingFile);
    }
		return (Long)newMappingFile.getKey();
	}

	protected Object[] getErrorMessageParams(IEvent event)
	{
		return new Object[] {MappingFile.ENTITY_NAME};
	}

	protected AbstractEntity prepareCreationData(IEvent event) 
	{
		CreateMappingFileEvent createEvent = (CreateMappingFileEvent)event;

    MappingFile newMappingFile = new MappingFile();
    newMappingFile.setName(createEvent.getMappingFileName());
    newMappingFile.setDescription(createEvent.getMappingFileDesc());
    newMappingFile.setFilename(_uniqueFilename);
    newMappingFile.setPath(_pathKey);
    newMappingFile.setSubPath(createEvent.getMappingFileSubPath()); // 20040304 DDJ
    newMappingFile.setType(createEvent.getMappingFileType());

    return newMappingFile;
	}

	protected AbstractEntity retrieveEntity(Long key) throws Exception
	{
		return getManager().findMappingFile(key);
	}

	protected String getActionName()
	{
		return ACTION_NAME;
	}

	protected Class getExpectedEventClass()
	{
		return CreateMappingFileEvent.class;
	}

	protected void doSemanticValidation(IEvent event) throws Exception
	{
		//move mapping file from user location to temp directory
		CreateMappingFileEvent createEvent = (CreateMappingFileEvent)event;
    String fullpath = createEvent.getMappingFilePath();
    fullpath = FileUtil.extractRelativeFilePath(IPathConfig.PATH_TEMP,
                                                fullpath,
                                                false);
    String subpath = FileUtil.extractPath(fullpath);
    String filename = FileUtil.extractFilename(fullpath);
    _pathKey = MappingFilePathHelper.getInstance().getConfigPath(createEvent.getMappingFileType());

    _uniqueFilename = FileUtil.move(IPathConfig.PATH_TEMP,
                                    subpath,
                                    _pathKey,
                                    createEvent.getMappingFileSubPath(),	// 20040304 DDJ
                                    filename);
    
	}

  private void createXpathMapping(MappingFile newXpathFile)
    throws Exception
  {
    Long uid = new Long(newXpathFile.getUId());
    String filename = newXpathFile.getFilename();
    String path = MappingFilePathHelper.getInstance().getConfigPath(newXpathFile.getType());
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
    XpathMapping newMapping = new XpathMapping();
    newMapping.setRootElement(rootElementName);
    newMapping.setXpathUid(uid);
    getManager().createXpathMapping(newMapping);
  }

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