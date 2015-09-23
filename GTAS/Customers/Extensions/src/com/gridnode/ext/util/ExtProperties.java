/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExtProperties.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 22 2003    Neo Sok Lay         Created
 */
package com.gridnode.ext.util;

import java.util.Properties;

/**
 * This is an extension to the Java Properties to allow adding properties
 * from an array of property strings specified in the form: &lt;property&gt;=&lt;value&gt;
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3 I1
 * @since GT 2.3 I1
 */
public class ExtProperties extends Properties
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -542751276080243782L;
	public static final String KEY_VALUE_SEPARATOR = "=";

  public ExtProperties()
  {
  }

  /**
   * Add properties from the specified array
   * 
   * @param args The array of property strings, each of the form:
   * &lt;property&gt;=&lt;value&gt;
   */
  public void addPropertiesFromArray(String[] args)
  {
    for (int i = 0; i < args.length; i++)
      addPropertyString(args[i]);
  }

  /**
   * Add a property from the specified property string of the form:
   * &lt;property&gt;=&lt;value&gt;
   * @param propertyStr The property string.
   */
  public void addPropertyString(String propertyStr)
  {
    if (!StringUtil.isBlank(propertyStr))
    {
      String propKey = StringUtil.left(propertyStr, KEY_VALUE_SEPARATOR);
      String propVal = StringUtil.right(propertyStr, KEY_VALUE_SEPARATOR);

      if (!StringUtil.isBlank(propKey))
        setProperty(propKey, propVal);
    }
  }

}