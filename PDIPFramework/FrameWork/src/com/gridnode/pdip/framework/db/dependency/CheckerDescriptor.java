/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CheckerDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 8 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

/**
 * This is a descriptor for a entity dependency checker method.
 * The descriptor contains the following:<p>
 * <PRE>
 * ClassName  - Full qualified class name of the dependency checker class
 * MethodName - Name of the checker method
 * Params     - Array of ParamDescriptor defining the method argument requirements.
 * DescriptorCreator - Defines a class that is capable to
 * creating entity descriptors for the objects returned by the
 * checker class.
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class CheckerDescriptor
{
  private String _className;
  private String _methodName;
  private ParamDescriptor[] _params;
  private DescriptorCreatorDescriptor _descriptorCreator;
  
  /**
   * Constructor for CheckerDescriptor.
   */
  public CheckerDescriptor()
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
  
  public void setParams(ParamDescriptor[] params)
  {
    _params = params;
  }
  
  public ParamDescriptor[] getParams()
  {
    return _params;
  }  
  
  public void setDescriptorCreator(DescriptorCreatorDescriptor descriptorCreator)
  {
    _descriptorCreator = descriptorCreator;
  }
  
  public DescriptorCreatorDescriptor getDescriptorCreator()
  {
    return _descriptorCreator;
  }
}
