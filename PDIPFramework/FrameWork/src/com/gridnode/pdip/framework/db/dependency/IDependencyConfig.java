/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDependencyConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 10 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

/**
 * This interface defines the constants for the dependency checking configuration file.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IDependencyConfig
{
  /**
   * Name key of the dependency config file.
   */
  static final String CONFIG_NAME             = "entity.dependency";
  
  /**
   * Key for getting the descriptor file rootpath
   */
  static final String KEY_DESCR_PATH          = "descriptor.path";
  
  /**
   * Key for getting the descriptor filename pattern
   */
  static final String KEY_DESCR_NAME_PATTERN  = "descriptor.name.pattern";
  
  /**
   * Default descriptor file rootpath value.
   */
  static final String DEFAULT_DESCR_PATH          = "conf/default/dependency";
  
  /**
   * Default descriptor filename pattern
   */
  static final String DEFAULT_DESCR_NAME_PATTERN  = "dependency-{0}.xml";
}
