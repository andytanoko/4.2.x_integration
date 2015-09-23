/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGlobals.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-24     Andrew Hill         Created
 * 2002-05-29     Andrew Hill         Remove login.jsp ref, add Document Mgr ref
 * 2002-10-24     Andrew Hill         Provided a key for the DocumentFinder and GlobalContext
 * 2002-10-25     Andrew Hill         Removed key for GlobalContext
 * 2002-12-11     Andrew Hill         Added WATCHLIST_MAPPING
 * 2003-04-08     Andrew Hill         Script url constants
 * 2003-05-18     Andrew Hill         Constant for DataFilterUtils script
 * 2003-06-04     Andrew Hill         Constant for MheReferenceUtils script
 * 2003-07-10     Andrew Hill         Constant for SectionMethods script
 * 2004-02-13     Neo Sok Lay         Constant for DateTimePicker script
 * 2005-01-11			SC									Constant for property script. Constant: ADD_UPDATE_ACTION, REMOVE_UPDATE_ACTION
 */
package com.gridnode.gtas.client.web;

public interface IGlobals
{
  /**
   * Source URLs for javascripts that can be included
   * 20030408AH
   */
  public static final String JS_ENTITY_FORM_METHODS   = "scripts/stdFormMethods.js";
  public static final String JS_LIST_FORM_METHODS     = "scripts/submitMultipleEntities.js";
  public static final String JS_NAVIGATION_METHODS    = "scripts/navigationMethods.js";
  public static final String JS_FRAME_METHODS         = JS_NAVIGATION_METHODS;
  public static final String JS_SELECT_UTILS          = "scripts/selectUtils.js";
  public static final String JS_SERVER_UTILS          = "scripts/serverUtils.js";
  public static final String JS_SHOW_EXCEPTION        = "scripts/showException.js";
  public static final String JS_TABLE_UTILS           = "scripts/tableUtils.js";
  public static final String JS_TAB_UTILS             = "scripts/tabUtils.js";
  public static final String JS_TREE_UTILS            = "scripts/treeUtils.js";
  public static final String JS_SUBSTITUTION_UTILS    = "scripts/insertSubstitutionUtils.js";
  public static final String JS_DATA_FILTER_UTILS     = "scripts/dataFilterUtils.js"; //20030518AH
  public static final String JS_DATEFIELD_UTILS       = "scripts/dateFieldUtils.js"; //20030518AH
  public static final String JS_MHEREFERENCE_UTILS    = "scripts/mheReferenceUtils.js"; //20030604AH
  public static final String JS_SECTION_METHODS       = "scripts/sectionMethods.js"; //20030710AH
  public static final String JS_DATE_TIME_PICKER      = "scripts/dateTimePicker.js"; //20040213NSL
  public static final String JS_PROPERTY      				= "scripts/property.js";

  /**
   * Key for the document manager attribute stored in servlet context.
   */
  public static final String DOCUMENT_MANAGER = "com.gridnode.gtas.client.web.xml.DocumentManager";
  public static final String DOCUMENT_FINDER  = "com.gridnode.gtas.client.web.xml.DocumentFinder";

  /**
   * Name of global mapping for the DefaultRenderingServlet
   */
  public static final String VIEW_FORWARD = "view";

  public static final String TEMPDIR = "javax.servlet.context.tempdir";

  public static final String EXCEPTION_ERROR_PROPERTY = "EXCEPTION_ERROR_PROPERTY";

  public static final String WATCHLIST_MAPPING = "partnerWatchList";
  
  /**
   * Constants for JS_PROPERTY script.
   */
  public static final String ADD_UPDATE_ACTION = "addUpdateAction";
  public static final String REMOVE_UPDATE_ACTION = "removeUpdateAction";
}