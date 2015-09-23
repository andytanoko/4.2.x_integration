/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITptMappingRegistry.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 25 2003    Jagadeesh              Created
 */

package com.gridnode.pdip.base.transport.helpers;


/**
 * This interface defines Transport Mapping Registry XML keys.
 *
 * @author Jagadeesh.
 * @since 2.3
 * @see TptMappingRegistry
 *
 */

public interface ITptMappingRegistry
{
  public static final String PACKAGE_TYPE = "PACKAGE-TYPE";

  public static final String ATTRIBUTE_TYPE = "TYPE";

  public static final String MESSAGE_HEADER_KEY = "MSG-HEADER-KEY";

  public static final String TPT_HEADER_KEY = "TPT-HEADER-KEY";
}