/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMapperPathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2002    Koh Han Sing        Create
 * Feb 13 2003    Koh Han Sing        Add in XPath
 */
package com.gridnode.pdip.app.mapper.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface IMapperPathConfig extends IPathConfig
{
  // MappingFile paths
  public static final String PATH_XSL              = "mapper.path.xsl";
  public static final String PATH_CONVERSION_RULE  = "mapper.path.cvrules";
  public static final String PATH_REFERENCE_DOC    = "mapper.path.refdoc";
  public static final String PATH_TEMPLATE         = "mapper.path.template";
  public static final String PATH_DTD              = "mapper.path.dtd";
  public static final String PATH_DICT             = "mapper.path.dict";
  public static final String PATH_SCHEMA           = "mapper.path.schema";
  public static final String PATH_XPATH            = "mapper.path.xpath";
  public static final String JAVA_BINARY           = "mapper.path.javabinary";
}