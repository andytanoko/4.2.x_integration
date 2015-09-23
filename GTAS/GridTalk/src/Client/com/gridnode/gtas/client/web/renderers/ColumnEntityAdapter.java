/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ColumnEntityAdapter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created
 * 2002-09-06     Andrew Hill
 * 2002-11-13     Andrew Hill         getColumnEntity()
 * 2005-03-15     Andrew Hill         isColumnLinkEnabled()
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.ctrl.IGTManager;
import com.gridnode.gtas.client.utils.StaticUtils;

/**
 * Default implementation of IColumnEntityAdapter, suitable for most situations.
 */
public class ColumnEntityAdapter implements IColumnEntityAdapter
{
  protected Object[] _columns;
  protected IColumnTransformer[] _transformers;
  protected IGTManager _manager;
  protected String _entityType;
  protected boolean[] _enabledLinks; //20050315AH (if not init def to true)

  /**
   * Constructor takes an array of fieldnames defining the required columns.
   * The fieldnames come from the appropriate IGTEntity subinterface definition.
   * warning: Behaviour for 'unknown' fields is undefined.
   * @param columns String[]
   */
  public ColumnEntityAdapter(Object[] columns, IGTManager manager, String entityType)
  {
    if(columns == null) throw new NullPointerException("columns is null"); //20030416AH
    if(manager == null) throw new NullPointerException("manager is null"); //20030416AH
    if(entityType == null) throw new NullPointerException("entityType is null"); //20030416AH
    setColumns(columns);
    setManager(manager);
    setEntityType(entityType);
  }
  
  /**
   * Set the link enablement for the columns. This takes an array of booleans
   * corresponding to columns for the same indexes. Must be same length as the columns
   * array so init that first if not done it yet. Note that when the columns are set
   * an array of booleans for the link enebaled is created with all elements in the array
   * being set to true by default. To disable particlur ones you may find one of the
   * setColunLinkEnabled or setColumnLinksForFieldEnabled methods more convenient.
   * @param enabledLinks
   */
  public void setEnabledLinks(boolean[] enabledLinks)
  { //20050315AH
    if(enabledLinks.length != _columns.length)
    {
      throw new IllegalStateException("array length mismatch");
    }
    _enabledLinks = enabledLinks;
  }
  
  public boolean[] getEnabledLinks()
  { //20050315AH
    return _enabledLinks;
  }

  public void setManager(IGTManager manager)
  {
    _manager = manager;
  }

  public IGTManager getManager()
  {
    return _manager;
  }

  public String getEntityType()
  {
    return _entityType;
  }

  public void setEntityType(String entityType)
  {
    _entityType = entityType;
  }
  
  private boolean[] getTrueBools(int size)
  { //20050315AH
    boolean[] array = new boolean[size];
    for(int i=0; i < array.length; array[i++]=true)
    {
    }
    return array;
  }

  /**
   * Change the columns to be rendered.
   * notes: This will also clear all transformers and reset the column link enablements
   * @param columns Object[] of entity fieldnames
   */
  public void setColumns(Object[] columns)
  {
    if(columns == null)
    {
      throw new java.lang.NullPointerException("No column array specified");
    }
    _columns = columns;
    _transformers = new IColumnTransformer[columns.length];
    _enabledLinks = getTrueBools(columns.length); //20050315AH
  }

  /**
   * Add a transformer to modify the value returned for a specific column.
   * @param transformer
   * @param column
   */
  public void setTransformer(IColumnTransformer transformer, int column)
  {
    assertGoodColumn(column);
    _transformers[column] = transformer;
  }

  /**
   * Retrieve columns array
   * @return Object[] of column fieldname mappings
   */
  public Object[] getColumns()
  {
    return _columns;
  }

  /**
   * Returns the number of columns
   */
  public int getSize()
  {
    return _columns.length;
  }

  public IGTFieldMetaInfo getColumnMetaInfo(Object object, int column)
    throws GTClientException
  {
    try
    {
      assertGoodColumn(column);
      IGTEntity entity = (IGTEntity)object;
      IGTFieldMetaInfo metaInfo = null;
      Object field = _columns[column];
      if(field instanceof String)
      {
        metaInfo = entity.getFieldMetaInfo((String)field);
      }
      else
      {
        if(field instanceof Number)
        {
          metaInfo = entity.getFieldMetaInfo((Number)field);
        }
        else
        {
          throw new java.lang.IllegalArgumentException("Column " + column + " does not refer to a field");
        }
      }
/*System.out.println("columnEntityAdapter returning fmi for field " + metaInfo.getFieldId()
+ " (" + metaInfo.getFieldName() + ")"
+ " for column " + column + " of entity:" + entity);*/
      return metaInfo;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting field meta info for column " + column
                                    + " of object " + object,t);
    }
  }

  public IGTEntity getColumnEntity(Object object, int column)
    throws GTClientException
  {
    try
    {
      assertGoodColumn(column);
      IGTEntity entity = (IGTEntity)object;
      IGTEntity colEntity = null;
      Object field = _columns[column];
      if(field instanceof String)
      {
        colEntity = entity.getFieldContainer((String)field);
      }
      else
      {
        if(field instanceof Number)
        {
          colEntity = entity.getFieldContainer((Number)field);
        }
        else
        {
          throw new java.lang.IllegalArgumentException("Column " + column + " does not refer to a field");
        }
      }
      return colEntity;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting entity containing field for column " + column
                                    + " of object " + object,t);
    }
  }

  /**
   * Returns the value for the specified column in specified entity
   * @param IGTEntity
   * @param column
   */
  public Object getColumnValue(Object object, int column)
    throws GTClientException
  {
    try
    {
      assertGoodColumn(column);
      IGTEntity entity = (IGTEntity)object;
      Object value;
      Object field = _columns[column];
      if(field instanceof String)
      {
        value = entity.getFieldValue((String)field);
      }
      else
      {
        if(field instanceof Number)
        {
          value = entity.getFieldValue((Number)field);
        }
        else
        {
          throw new java.lang.IllegalArgumentException("Column " + column + " does not refer to a field");
        }
      }
      if(_transformers[column] != null)
      {
        value = _transformers[column].getTransformedValue(entity, column, _columns[column], value);
      }
/*System.out.println("columnEntity adapter returning value '" + value
+ "' (class=" + StaticUtils.getObjectClassName(value) + ")"
+ " for column " + column + " field=" + field
+ " in entity:" + entity);*/
      return value;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting column value for column " + column
                                    + " of object " + object,t);
    }
  }

  /*public String getColumnString(Object object, int column)
    throws GTClientException
  {
    try
    {
      IGTEntity entity = (IGTEntity)object;
      Object o = getColumnValue(entity, column);
      if(o == null)
        return "";

      if(o instanceof String)
        return (String)o;
      else
        return o.toString();
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting column string for column " + column
                                    + " of object " + object,t);
    }
  }*/

  /**
   * Return the field label for specified column
   * @param column
   */
  /*public String getColumnLabel(Object object, int column)
  {
    assertGoodColumn(column);
    IGTEntity entity = (IGTEntity)object;
    IGTFieldMetaInfo fmi = entity.getFieldMetaInfo((Number)_columns[column]);
    return fmi.getLabel();
  }*/

  public String getColumnLabel(int column)
    throws GTClientException
  {
    try
    {
      assertGoodColumn(column);
      Object field = _columns[column];
      if(field == null)
      {
        throw new java.lang.NullPointerException("_columns[" + column + "] is null");
      }
      IGTFieldMetaInfo fmi = null;
      if(field instanceof Number)
      {
        fmi = getManager().getSharedFieldMetaInfo(getEntityType(),(Number)field);
      }
      if(field instanceof String)
      {
        fmi = getManager().getSharedFieldMetaInfo(getEntityType(),(String)field);
      }
      if(fmi == null)
      {
        throw new GTClientException("Error getting field meta info for field:" + field);
      }
      return fmi.getLabel();
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting label for column " + column,t);
    }
  }


  /**
   * Throw an exception if column index out of bounds
   */
  private void assertGoodColumn(int column)
  {
    if( (column < 0) || (column >= _columns.length) )
      throw new java.lang.IllegalArgumentException("No such column:" + column);
  }
  
  public boolean isColumnLinkEnabled(Object entity, int column) throws GTClientException
  { //20050315AH
    if(_enabledLinks == null || _enabledLinks.length-1 < column)
    {
      return true;
    }
    else
    {
      return _enabledLinks[column];
    }
  }
  
  public void setColumnLinkEnabled(int column, boolean enabled)
  { //20050315AH
    if(_enabledLinks == null)
    {
      throw new NullPointerException("enabledLinks array not initialised");
    }
    if(column+1 > _enabledLinks.length)
    {
      throw new ArrayIndexOutOfBoundsException("Column " + column + " doesnt exist in enabled links array");
    }
    _enabledLinks[column] = enabled;
  }
  
  public void setColumnLinksForFieldEnabled(Object field, boolean enabled)
  { //20050315AH
    if(_columns == null)
    {
      throw new NullPointerException("columns is null");
    }
    for(int i=0; i < _columns.length; i++)
    {
      Object column = _columns[i];
      if(StaticUtils.objectsEqual(column, field))
      {
        setColumnLinkEnabled(i, enabled);
      }
    }
  }

}