/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTATConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 * Feb 05, 2007    Tam Wei Xiang       Added in remote event queue jndi name
 */
package com.gridnode.gtas.audit.extraction.helper;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IGTATConstant
{
  public static final String LOG_TYPE = "GN.AUDIT.GTAT";
  
  //Remote Event Q Jms Category
  public static final String JMS_CATEGORY = "gtat.jms.remote";
  
  //Archive Notify Q Jms Category
  public static final String ARCHIVE_NOTIFY_CATEGORY = "isat.jms.archive.notify";
}
