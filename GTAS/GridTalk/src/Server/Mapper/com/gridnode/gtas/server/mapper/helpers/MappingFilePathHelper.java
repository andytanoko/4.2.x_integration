/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 15 2002    Koh Han Sing        Created
 * Aug 15 2002    Koh Han Sing        Change to use IMapperPathConfig
 * Jan 23 2003    Ang Meng Hua        Modified constructor to use constants in
 *                                    IMapperPathConfig for DTD, SCHEMA and DICT
 * Feb 13 2003    Koh Han Sing        Add in XPath
 */
package com.gridnode.gtas.server.mapper.helpers;

import com.gridnode.gtas.model.mapper.IMappingFile;
import com.gridnode.pdip.app.mapper.helpers.IMapperPathConfig;

import java.util.Hashtable;

public class MappingFilePathHelper
{
  Hashtable table = null;

  private static MappingFilePathHelper instance = null;

  private MappingFilePathHelper()
  {
    table = new Hashtable();
    table.put(IMappingFile.XSL, IMapperPathConfig.PATH_XSL);
    table.put(IMappingFile.CONVERSION_RULE, IMapperPathConfig.PATH_CONVERSION_RULE);
    table.put(IMappingFile.REFERENCE_DOC, IMapperPathConfig.PATH_REFERENCE_DOC);
    table.put(IMappingFile.DTD, IMapperPathConfig.PATH_DTD);
    table.put(IMappingFile.SCHEMA, IMapperPathConfig.PATH_SCHEMA);
    table.put(IMappingFile.DICT, IMapperPathConfig.PATH_DICT);
    table.put(IMappingFile.XPATH, IMapperPathConfig.PATH_XPATH);
    table.put(IMappingFile.JAVA_BINARY, IMapperPathConfig.JAVA_BINARY);
  }
  public static MappingFilePathHelper getInstance()
  {
    if(instance == null)
    {
      synchronized(MappingFilePathHelper.class)
      {
        if(instance == null)
        {
          instance = new MappingFilePathHelper();
        }
      }
    }
    return instance;
  }

  public String getConfigPath(Short mappingFileType)
  {
    Object result = table.get(mappingFileType);
    if (result != null)
    {
      return result.toString();
    }
    return null;
  }

}