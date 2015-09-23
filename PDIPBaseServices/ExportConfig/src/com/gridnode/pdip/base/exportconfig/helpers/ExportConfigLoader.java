/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 20 2003    Koh Han Sing        Created
 * Nov 10 2005    Neo Sok Lay         Load files from SystemUtil.workingDir
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import java.util.*;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.util.SystemUtil;
import com.gridnode.xml.adapters.GNElement;

import java.io.File;
/**
 * This class will load the matching mapping for each entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ExportConfigLoader
{
  private static final String CONFIG_NAME     = "export-config";
  private static final String ENTITIES_FILE   = "exportable.entities.filename";
  private static final String ENTITY_ELEMENT  = "Entity";
  private static final String NAME_ATTR       = "Name";
  private static final String ENTITY_SELECT   = "Selectable";
  private static final String ENTITY_MAP      = "Map";

  private static ExportConfigLoader _self = null;
  private static Hashtable _mappings;
  private static ArrayList _selectables;

  private ExportConfigLoader()
  {
  }

  public static ExportConfigLoader getInstance()
    throws Exception
  {
    if (_self == null)
    {
      synchronized (ExportConfigLoader.class)
      {
        if (_self == null)
        {
          _self = new ExportConfigLoader();
          readConfigFile();
        }
      }
    }

    return _self;
  }

  public Hashtable getMappings()
  {
    return _mappings;
  }

  public Collection getSelectableEntitiesName()
  {
    return _selectables;
  }

  public boolean isExportable(String entityName)
  {
    Enumeration entities = _mappings.keys();
    while (entities.hasMoreElements())
    {
      String exportable = entities.nextElement().toString();
      if (exportable.equals(entityName))
      {
        return true;
      }
    }
    return false;
  }

  public String getMapping(String entityName)
    throws Exception
  {
    boolean serializable = _mappings.containsKey(entityName);
    if (serializable)
    {
      Object mapping = _mappings.get(entityName);
      if (mapping != null && !mapping.equals(""))
      {
        return mapping.toString();
      }
    }
    //throw new ExportConfigException("Unable to locate Mapping for "+entityName);
    return null;
  }

  private static void readConfigFile() throws Exception
  {
    _mappings = new Hashtable();
    _selectables = new ArrayList();

    Configuration exportConfig =
      ConfigurationManager.getInstance().getConfig(CONFIG_NAME);

    String entitiesFilename = exportConfig.getString(ENTITIES_FILE);
    File entitiesFile = new File(SystemUtil.getWorkingDirPath(), entitiesFilename); //NSL20051027
    GNElement root = XMLDelegate.getManager().getRoot(entitiesFile); //NSL20051027
    List entities = root.getChildren(ENTITY_ELEMENT);
    for (Iterator i = entities.iterator(); i.hasNext(); )
    {
      GNElement entity = (GNElement)i.next();
      String entityName = entity.getAttributeValue(NAME_ATTR).trim();
      if (entityName != null && !entityName.equals(""))
      {
        GNElement map = entity.getChild(ENTITY_MAP);
        if (map != null)
        {
          _mappings.put(entityName, map.getText().trim());
        }
        GNElement selectable = entity.getChild(ENTITY_SELECT);
        if (selectable != null)
        {
          String value = selectable.getText().trim().toLowerCase();
          if (value.equals("true"))
          {
            _selectables.add(entityName);
          }
        }
      }
    }
  }
}