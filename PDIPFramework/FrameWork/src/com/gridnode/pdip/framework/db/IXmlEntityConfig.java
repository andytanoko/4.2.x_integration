/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IXmlEntityConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 18 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db;

/**
 * Interface for use by the XmlEntityHandler to obtain 
 * the configuration settings to manage the persistency
 * of XmlEntity(s).
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IXmlEntityConfig
{
  /**
   * Get the path key to obtain the storage path for the relevant XmlEntity.
   * 
   * @return Path Key of the storage path for the relevant XmlEntity. For
   * use with FileUtil.
   */
  String getStoragePathKey();
  
  /**
   * Get the name of the configuration file that contains all necessary
   * configuration settings for validating the fields in the XmlEntity.
   * 
   * @return Configuration name.
   */
  String getConfigName();
}
