/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Package.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * July 17 2003    Jagadeesh             Created.
 */

package com.gridnode.pdip.app.channel.helpers;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class Package implements IPackage, Serializable
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9007213605139039689L;
	private Map packKeyValueStore = new HashMap();
  private byte[] _unpackFileContent = null;

  public Package()
  {
  }

  public Package(Hashtable header, String[] dataContent, File[] fileContent)
  {
    Hashtable nHeader = (Hashtable) header.clone();
    packKeyValueStore.put(IPackageConstants.PACKAGE_HEADER, nHeader);
    packKeyValueStore.put(IPackageConstants.PACKAGE_DATACONTENT, dataContent);
    packKeyValueStore.put(IPackageConstants.PACKAGE_FILECONTENT, fileContent);
  }

  public Package(
    Hashtable header,
    String[] dataContent,
    byte[] unpackFileContent)
  {
    Hashtable nHeader = (Hashtable) header.clone();
    packKeyValueStore.put(IPackageConstants.PACKAGE_HEADER, nHeader);
    packKeyValueStore.put(IPackageConstants.PACKAGE_DATACONTENT, dataContent);
    //packKeyValueStore.put(IPackageConstants.PACKAGE_UNPACK_FILECONTENT)
    _unpackFileContent = unpackFileContent;
    //Stroing this in packKeyValueStore would be costly.
  }

  public Hashtable getHeader()
  {
    return (Hashtable) packKeyValueStore.get(IPackageConstants.PACKAGE_HEADER);
  }

  public String[] getDataContent()
  {
    return (String[]) packKeyValueStore.get(
      IPackageConstants.PACKAGE_DATACONTENT);
  }

  public File[] getFileContent()
  {
    return (File[]) packKeyValueStore.get(
      IPackageConstants.PACKAGE_FILECONTENT);
  }

  public byte[] getUnPackFileContent()
  {
    return _unpackFileContent;
  }

  public Object getValue(String key)
  {
    return packKeyValueStore.get(key);
  }

  public void setValue(String key, Object value)
  {
    packKeyValueStore.put(key, value);
  }

  public Enumeration getKeys()
  {
    if (packKeyValueStore.keySet() != null)
    {
      Set keySet = packKeyValueStore.keySet();
      return Collections.enumeration(keySet);
    }
    else
      return Collections.enumeration(Collections.EMPTY_SET);
  }

}