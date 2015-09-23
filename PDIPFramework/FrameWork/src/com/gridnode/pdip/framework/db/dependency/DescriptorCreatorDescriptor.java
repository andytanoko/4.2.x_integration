/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DescriptorCreatorDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 11 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

/**
 * This is a descriptor for an object that is
 * capable of creating entity descriptors.
 * The descriptor contains the following:<p>
 * <PRE>
 * ClassName  - Full qualified class name of the descriptor creator class
 * MethodName - Name of the creation method
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class DescriptorCreatorDescriptor
{
  private String _className;
  private String _methodName;
  
  /**
   * Constructor for CheckerDescriptor.
   */
  public DescriptorCreatorDescriptor()
  {
  }

  public void setClassName(String className)
  {
    _className = className;
  }
  
  public String getClassName()
  {
    return _className;
  }
  
  public void setMethodName(String methodName)
  {
    _methodName = methodName;
  }
  
  public String getMethodName()
  {
    return _methodName;
  }
}
