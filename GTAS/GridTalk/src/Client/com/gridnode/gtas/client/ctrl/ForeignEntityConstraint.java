/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ForeignEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2002-12-27     Andrew Hill         Support for additional display fields
 * 2003-08-27     Andrew Hill         Support the isLocalDisplayFieldName() property
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Properties;
import java.util.StringTokenizer;

import com.gridnode.gtas.client.GTClientException;

class ForeignEntityConstraint extends AbstractEntityConstraint implements IForeignEntityConstraint
{
  protected String _keyFieldName;
  protected String _displayFieldName;
  protected boolean _isCached;
  protected String[] _additionalDisplayFieldNames;
  protected boolean _isLocalDisplayFieldName; //20030827AH

  ForeignEntityConstraint(Properties detail)
    throws GTClientException
  {
    super(IConstraint.TYPE_FOREIGN_ENTITY, detail);
  }

  protected void initialiseEntityConstraint(int type, Properties detail)
    throws GTClientException
  {
    try
    {
      String key = detail.getProperty("foreign.key",null);
      if(key == null) throw new java.lang.IllegalArgumentException("No foreign entity key field defined");
      _keyFieldName = DefaultSharedFMI.extractFieldName(key);
      _entityType = DefaultSharedFMI.extractEntityName(key);
      String display = detail.getProperty("foreign.display",null);
      if(display == null) throw new java.lang.IllegalArgumentException("No foreign entity display field defined");
      _displayFieldName = DefaultSharedFMI.extractFieldName(display);
      
      //20030827AH - Determine if we are using the local or foreign entity as source of the displayField. Basically
      //if the metaInfo specifies the foreign entity type before the fieldName its buisness as usual.
      //Anything else is assumed to be the local entity (we cant actually assert this aswe dont know what
      //the local entity is here!).
      _isLocalDisplayFieldName = ("true".equals( detail.getProperty("foreign.displayLocal","false")))
                                || (!_entityType.equals( DefaultSharedFMI.extractEntityName(display) )); //20030827AH      
      
      
      String cached = detail.getProperty("foreign.cached","false");
      _isCached = DefaultSharedFMI.makeBoolean(cached);

      //20021227AH - Support additional display fields
      String fieldNames = detail.getProperty("foreign.additionalDisplay",null);
      if(fieldNames == null)
      {
        _additionalDisplayFieldNames = StaticCtrlUtils.EMPTY_STRING_ARRAY;
      }
      else
      {
        StringTokenizer tokenizer = new StringTokenizer(fieldNames,",");
        int numFields = tokenizer.countTokens();
        if(numFields > 0)
        { //If we have additional fields use a StringTokenizer to read in their names.
          //nb: we dont check if they exist at this point, though we do check for empty strings
          _additionalDisplayFieldNames = new String[numFields];
          for(int i=0; i < numFields; i++)
          {
            String fieldName = tokenizer.nextToken();
            if( "".equals(fieldName) || (fieldName == null) )
            {
              throw new java.lang.IllegalArgumentException( "Additional display field "
                                                            + i + " not specified");
            }
            _additionalDisplayFieldNames[i] = fieldName.trim();
          }
        }
        else // No additional display fields defined
        { //Rather than create a new empty STring[] we use the static one that can be shared
          //and thus reduce object creation
          _additionalDisplayFieldNames = StaticCtrlUtils.EMPTY_STRING_ARRAY;
        }
      }
      //..
    }
    catch(Throwable t)
    { //20021227AH
      throw new GTClientException("Error initialising ForeignEntityConstraint object",t);
    }
  }

  public String getEntityType()
  {
    return _entityType;
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
    return _isCached;
  }

  public String[] getAdditionalDisplayFieldNames()
  { //20021227AH
    //In theory I should clone the array here to prevent it being tampered with,
    //however in the interests of efficiency I shall not do so.
    return _additionalDisplayFieldNames;
  }
  
  public boolean isLocalDisplayFieldName()
  { //20030827AH
    return _isLocalDisplayFieldName;
  }

}