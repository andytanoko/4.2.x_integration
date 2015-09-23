/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ParamDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 8 2003     Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.dependency;

/**
 * This is a descriptor for a parameter requirement of a dependency checker
 * method. This descriptor contains the following:<p>
 * <PRE>
 * Type         - The class type of the parameter.
 * GetterMethod - Name of the method to obtain the parameter value from
 *                the entity that is being checked for dependency.
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class ParamDescriptor
{
  private String _type;
  private String _getterMethod;
  
  public ParamDescriptor()
  {
  }

  public void setType(String type)
  {
    _type = type;
  }
  
  public String getType()
  {
    return _type;
  }
  
  public void setGetterMethod(String getterMethod)
  {
    _getterMethod = getterMethod;
  }
  
  public String getGetterMethod()
  {
    return _getterMethod;
  }
}
