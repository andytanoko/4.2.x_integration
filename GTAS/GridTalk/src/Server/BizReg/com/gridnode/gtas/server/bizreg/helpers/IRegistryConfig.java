/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistryConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 15 2003    Neo Sok Lay         Created
 * Sep 24 2003    Neo Sok Lay         Remove keys for Registry Connect Info file.
 */
package com.gridnode.gtas.server.bizreg.helpers;

/**
 * This interface defines the constants for the public registry configuration file.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public interface IRegistryConfig
  extends com.gridnode.pdip.app.bizreg.pub.IRegistryConfig
{
  /**
   * Key for getting the technicalspecs file rootpath
   */
  static final String KEY_SPECS_PATH          = "techspecs.path";
  
  /**
   * Key for getting the technicalspecs filename pattern
   */
  static final String KEY_SPECS_NAME_PATTERN  = "techspecs.name.pattern";

  /**
   * Default technicalspecs file rootpath value.
   */
  static final String DEFAULT_SPECS_PATH          = "conf/default/registry";
  
  /**
   * Default technicalspecs filename pattern
   */
  static final String DEFAULT_SPECS_NAME_PATTERN  = "technicalspecs-{0}.xml";

  /**
   * The name (portion) of the Default TechnicalSpecs provided by GridTalk.
   */
  static final String DEFAULT_TECH_SPECS_NAME = "default";

}
