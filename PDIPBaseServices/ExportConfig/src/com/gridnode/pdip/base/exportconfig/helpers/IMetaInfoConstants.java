/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMetaInfoConstants.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 18 2004    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.exportconfig.helpers;

/**
 * Defines the constants used to access the entity/field meta infos.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3.3
 */
public interface IMetaInfoConstants
{
  static final String CONSTRAINT_TYPE = "type";
  static final String TYPE_FILE = "file";
  static final String FILE_SUBPATH = "file.subPath";
  static final String FILE_FIXEDKEY = "file.fixedKey";
  static final String FILE_PATHKEY = "file.pathKey";
  static final String TYPE_FOREIGN = "foreign";
  static final String FOREIGN_CACHED = "foreign.cached";
  static final String FOREIGN_KEY = "foreign.key";
  static final String TYPE_DYNAMIC = "dynamic";
  static final String TYPE_EMBEDDED = "embedded";
  
  static final String TRUE = "true";
  static final String FALSE = "false";
  static final String ESTR = "";
  static final String FSLASH = "/";
  static final String DOT = ".";
  static final String DOT_I = ".I"; 
  
  static final String FIELD_CAN_DELETE = "CAN_DELETE";
  static final String FIELD_VERSION = "VERSION";
  static final String FIELD_UID = "UID";
  
  static final String PACKAGE_MODEL = "model";
  static final String PACKAGE_MODEL_DOT = "model.";
  static final String PACKAGE_IMPORTS_DOT = "imports.";
  static final String PACKAGE_EXPORTS_DOT = "exports.";
  static final String PACKAGE_HELPERS_DOT = "helpers.";
  static final String CLASS_ENTITY_EXPORTER = "EntityExporter";
  static final String CLASS_ENTITY_IMPORTER = "EntityImporter";
  static final String CLASS_ENTITY_HANDLER = "EntityHandler";
  static final String CLASS_ENTITY_HELPER = "EntityHelper";
  static final String MTD_GET_INSTANCE = "getInstance";
}
