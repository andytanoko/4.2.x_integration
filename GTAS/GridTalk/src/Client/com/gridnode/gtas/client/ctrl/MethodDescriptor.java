/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MethodDescriptor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

/**
 * A simple bean that describes a method. 
 */
public class MethodDescriptor implements Comparable
{
  protected static final Parameter[] EMPTY_PARAMETER_ARRAY = new Parameter[0];
  
  //Inner class Parameter==================================
  public class Parameter
  {   
    private String _type;
    private String _name;
    
    public String getName()
    {
      return _name;
    }

    public String getType()
    {
      return _type;
    }

    public void setName(String string)
    {
      _name = string;
    }

    public void setType(String string)
    {
      _type = string;
    }
    
    public String toString()
    {
      return _type + " " + _name;
    }
  }
  //========================================================
  
  private String _description;
  private String _name;
  private Parameter[] _parameters;
  private String returnType;
  //private String _id;
  
  public String getDescription()
  {
    if(_description == null)
    {
      initDescription();
    }
    return _description;
  }
  
  public String toString()
  {
    return getDescription();
  }
  
  protected void initDescription()
  {
    StringBuffer buffer = new StringBuffer();
    String returnType = getReturnType();
    buffer.append( returnType == null ? "void" : returnType );
    buffer.append( " " );
    buffer.append( getName() );
    buffer.append( "(" );
    Parameter[] params = getParameters();
    for(int i=0; i < params.length; i++)
    {
      buffer.append( params[i].toString() );
      if( i+1 != params.length )
      {
        buffer.append( "," );
      }
    }
    buffer.append( ")" );
    _description = buffer.toString();
  }

  public String getName()
  {
    return _name;
  }

  public Parameter[] getParameters()
  {
    return _parameters == null ? EMPTY_PARAMETER_ARRAY : _parameters;
  }

  public String getReturnType()
  {
    return returnType;
  }

  public void setName(String string)
  {
    _name = string;
  }

  public void setParameters(Parameter[] parameters)
  {
    _parameters = parameters;
  }

  public void setReturnType(String string)
  {
    returnType = string;
  }

  /*public String getId()
  {
    return _id;
  }

  public void setId(String string)
  {
    _id = string;
  }*/

  public int compareTo(Object o)
  {
    if(o instanceof MethodDescriptor)
    {
      return getName().compareTo( ((MethodDescriptor)o).getName() );
    }
    else
    {
      return 0;
    }
  }

}
