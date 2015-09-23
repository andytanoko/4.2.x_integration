/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityOptionValueRetriever.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-16     Andrew Hill         Created
 * 2002-12-27     Andrew Hill         Basic support for additional display fields & fieldNames
 *                                    (Does not yet support deferencing of i18n enum display value
 *                                     or fer display value for referenced additional fields (todo))
 * 2003-08-27     Andrew Hill         Fixable primary display source (to support local display fields for fers)
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.GTClientException;

/**
 * Class that may be used by the xhtml rendering methods to obtain the
 * text to display and the value to be submitted for fields that invlove selecting
 * choice(s) from a list.
 * This class implements IOptionValueRetriever and assumes that the object passed to the
 * getOptionText() and getOptionValue() methods is an instance of IGTEntity.
 * It then uses the field ids passed to the constructor to retrive the desired values from the
 * entity for rendering by the caller. Now also supports additional display fields, generally
 * used for FER fields.
 */
public class EntityOptionValueRetriever implements IOptionValueRetriever
{
  private Number _textFieldId;
  private Number _valueFieldId;
  private int _maxDisplayLength = 0;

  private String _textFieldName;  //20021227AH
  private String _valueFieldName; //20021227AH

  private boolean _useName; //20021227AH

  private String[] _additionalDisplayFieldNames;    //20021227AH
  private StringBuffer _buffer= new StringBuffer(); //20021227AH
  String _delimeter = " - ";
  
  private Object _primaryDisplaySource; //20030827AH

  /**
   * Constructor.
   * Passing null to either value will cause a NullPointerException to be thrown.
   * @param textFieldId the field id of the choice that will be displayed
   * @param valueFieldId the value that will be submitted for the choice
   * @throws java.Lang.NullPointerException if either paremeter null
   */
  public EntityOptionValueRetriever(Number textFieldId, Number valueFieldId)
  {
    if(textFieldId == null)
    {
      throw new java.lang.IllegalArgumentException("No field id specified for option text");
    }

    if(valueFieldId == null)
    {
      throw new java.lang.IllegalArgumentException("No field id specified for value");
    }

    _textFieldId = textFieldId;
    _valueFieldId = valueFieldId;
    _useName = false;
  }

  public EntityOptionValueRetriever(String textFieldName, String valueFieldName)
  { //20021227AH
    if(textFieldName == null)
    {
      throw new java.lang.IllegalArgumentException("No field name specified for option text");
    }
    if(valueFieldName == null)
    {
      throw new java.lang.IllegalArgumentException("No field name specified for value");
    }
    _textFieldName = textFieldName;
    _valueFieldName = valueFieldName;
    _useName = true;
  }

  public void setAdditionalDisplayFieldNames(String[] additionalFields)
  { //20021227AH
    _additionalDisplayFieldNames = additionalFields;
  }

  public String[] getAdditionalDisplayFieldNames()
  { //20021227AH
    return _additionalDisplayFieldNames;
  }

  public void setDelimeter(String delimeter)
  { //20021227AH
    _delimeter = delimeter;
  }

  public String getDelimeter()
  { //20021227AH
    return _delimeter;
  }

  public void setMaxDisplayLength(int length)
  {
    _maxDisplayLength = length;
  }

  public int getMaxDisplayLength()
  {
    return _maxDisplayLength;
  }

  /**
   * Returns the text to be displayed for the specified choice.
   * If a primaryDisplaySource was set it will be used to pull the value for the primary field instead of
   * choice. If choice is null then all values will be pulled from the primaryDisplaySource. If both are
   * null a NullPointerException will be thrown.
   * @param choice an Object whose class implements IGTEntity
   * @return String text
   * @throws GTClientException
   */
  public String getOptionText(Object choice)
    throws GTClientException
  { //Mod 20021227AH - To provide rather basic support for additional fields
    //Mod 20021227AH - to support name or id referencing
    Object primaryDisplaySource = getPrimaryDisplaySource(); //20030827AH
    choice = choice == null ? primaryDisplaySource : choice; //20030827AH - Use pds if no choice provided
    if (choice == null) //20030827AH
      throw new NullPointerException("choice is null and no primaryDisplaySource was specified to use instead");
    StringBuffer buffer = _buffer;
    String fieldString = null;
    if(_useName)
    {
      fieldString = getFieldString( _textFieldName , primaryDisplaySource == null ? choice : primaryDisplaySource); //20030827AH
    }
    else
    {
      fieldString = getFieldString( _textFieldId , primaryDisplaySource == null ? choice : primaryDisplaySource); //20030827AH
    }
    buffer.replace(0,buffer.length(), fieldString);
    String[] additionalFields = getAdditionalDisplayFieldNames();
    if( (additionalFields != null) && (additionalFields.length > 0) )
    {
      for(int i=0; i < additionalFields.length; i++)
      {
        buffer.append(_delimeter);
        String fieldValue = getFieldString(additionalFields[i],choice);
        buffer.append(fieldValue);
      }
    }
    if( (_maxDisplayLength == 0) || (buffer.length() < _maxDisplayLength) )
    {
      return buffer.toString();
    }
    else
    {
      return buffer.substring(0,_maxDisplayLength);
    }
  }

  /**
   * Returns the value to be submitted/stored for the specified choice.
   * nb: The value will NOT be pulled from the primary display source if choice is null!
   * @param choice an Object whose class implements IGTEntity
   * @return String value
   * @throws GTClientException
   */
  public String getOptionValue(Object choice)
    throws GTClientException
  { //Mod 20021227AH - to support name or id referencing
    if(_useName)
    {
      return getFieldString( _valueFieldName , choice);
    }
    else
    {
      return getFieldString( _valueFieldId , choice);
    }
  }

  /**
   * Will attempt to retrieve a value from the specified field on the entity using the
   * IGTEntity.getFieldString() method.
   * @param fieldId
   * @param choice Object implementing IGTEntity
   * @return String text (empty string if entity value was null)
   * @throws GTClientException if there is a problem getting that field
   * @throws java.lang.NullPointerException if either parameter null
   */
  protected String getFieldString(Number fieldId, Object choice)
    throws GTClientException
  {
    if(fieldId == null)
    {
      throw new java.lang.NullPointerException("Null field id passed");
    }
    if(choice == null)
    {
      throw new java.lang.NullPointerException("Null choice object passed");
    }
    if(choice instanceof IGTEntity)
    {
      String fieldValue = ((IGTEntity)choice).getFieldString(fieldId);
      if(fieldValue == null)
      {
        return "";
      }
      else
      {
        return fieldValue;
      }
    }
    else
    {
      throw new GTClientException("Source object " + choice.getClass().getName()
                                  + " does not implement IGTEntity");
    }
  }

  protected String getFieldString(String fieldName, Object choice)
    throws GTClientException
  {
    if(fieldName == null)
    {
      throw new java.lang.NullPointerException("Null fieldName passed");
    }
    if(choice == null)
    {
      throw new java.lang.NullPointerException("Null choice object passed");
    }
    if(choice instanceof IGTEntity)
    {
      String fieldValue = ((IGTEntity)choice).getFieldString(fieldName);
      if(fieldValue == null)
      {
        return "";
      }
      else
      {
        return fieldValue;
      }
    }
    else
    {
      throw new GTClientException("Source object class " + choice.getClass().getName()
                                  + " does not implement IGTEntity");
    }
  }
  
  public Object getPrimaryDisplaySource()
  { //20030827AH
    return _primaryDisplaySource;
  }

  /**
   * The primary display source if not null is used instead of the choice object to retrieve the value
   * of the primary field, while if a choice object is passed the additional display fields will come from
   * that. (This feature was added to allow support for the concept of local displayFields for FERs the
   * original use case being the processInstanceUid field on gridDocument whose display field is the
   * processInstanceId field also on gridDocument.)
   * @param primaryDisplaySource an object (entity) to be used instead of the choice for getting the display value of the promary display field, or null for normal behaviour
   */
  public void setPrimaryDisplaySource(Object primaryDisplaySource)
  { //20030827AH
    _primaryDisplaySource = primaryDisplaySource;
  }

}