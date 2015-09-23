/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FakeForeignEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-30     Andrew Hill         Created
 * 2002-12-27     Andrew Hill         getAdditionalDisplayFieldNames()
 */
package com.gridnode.gtas.client.web.strutsbase;

import java.util.Properties;

import com.gridnode.gtas.client.ctrl.IConstraint;
import com.gridnode.gtas.client.ctrl.IForeignEntityConstraint;
 
public class FakeForeignEntityConstraint implements IForeignEntityConstraint
{
  private static final String[] EMPTY_STRING_ARRAY = new String[0]; //20021227AH

  private String _entityType;
  private String _keyFieldName;
  private String _displayFieldName;
  private String[] _additionalDisplayFieldNames = EMPTY_STRING_ARRAY; //20021227AH
  private boolean _isLocalDisplayFieldName = false; //20030827AH

  public FakeForeignEntityConstraint( String entityType,
                                      String keyFieldName,
                                      String displayFieldName)
  {
    if(entityType == null) throw new NullPointerException("entityType is null"); //20030416AH
    if(keyFieldName == null) throw new NullPointerException("keyFieldName is null"); //20030416AH
    if(displayFieldName == null) throw new NullPointerException("displayFieldName is null"); //20030416AH
    _entityType = entityType;
    _keyFieldName = keyFieldName;
    _displayFieldName = displayFieldName;
  }

  public FakeForeignEntityConstraint( String entityType,
                                      String keyFieldName,
                                      String displayFieldName,
                                      String[] additionalDisplayFieldNames)
  { //20021227AH
    this(entityType,keyFieldName,displayFieldName);
    if(additionalDisplayFieldNames != null)
    {
      _additionalDisplayFieldNames = additionalDisplayFieldNames;
      for(int i=0; i < additionalDisplayFieldNames.length; i++)
      {
        if(additionalDisplayFieldNames[i] == null)
        {
          throw new java.lang.NullPointerException( "additionalDisplayFieldNames["
                                                    + i + "] is null");
        }
      }
    }
  }
  
  public FakeForeignEntityConstraint( String entityType,
                                      String keyFieldName,
                                      String displayFieldName,
                                      String[] additionalDisplayFieldNames,
                                      boolean isLocalDisplayFieldName)
  { //20030827AH
    this(entityType,keyFieldName,displayFieldName,additionalDisplayFieldNames);
    _isLocalDisplayFieldName = isLocalDisplayFieldName;
  }

  public String[] getAdditionalDisplayFieldNames()
  { //20021227AH
    return _additionalDisplayFieldNames;
  }

  public String getKeyFieldName()
  {
    return _keyFieldName;
  }

  public String getDisplayFieldName()
  {
    return _displayFieldName;
  }

  public boolean isCached()
  {
    return false;
  }

  public String getEntityType()
  {
    return _entityType;
  }

  public int getType()
  {
    return IConstraint.TYPE_FOREIGN_ENTITY;
  }

  public Properties getProperties()
  {
    throw new java.lang.UnsupportedOperationException("Fake constraint has no properties");
  }
  
  public boolean isLocalDisplayFieldName()
  { //20030827AH
    return _isLocalDisplayFieldName;
  }

}