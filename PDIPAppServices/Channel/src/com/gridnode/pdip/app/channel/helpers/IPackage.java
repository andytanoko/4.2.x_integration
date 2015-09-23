/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPackage.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel.helpers;

import java.io.File;
import java.util.Hashtable;

public interface IPackage
{
  public Hashtable getHeader();

  public String[] getDataContent();

  public File[] getFileContent();

  public Object getValue(String key);
}