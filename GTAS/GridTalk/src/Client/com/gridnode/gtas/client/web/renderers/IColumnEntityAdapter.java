/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IColumnENtityAdapter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-16     Andrew Hill         Created
 * 2002-09-06     Andrew Hill         Made an extension of IColumnObjectAdapter
 * 2002-11-13     Andrew Hill         getColumnEntity()
 * 2005-03-15     Andrew Hill         isColumnLinkEnabled()
 */
package com.gridnode.gtas.client.web.renderers;

import com.gridnode.gtas.client.ctrl.IGTEntity;
import com.gridnode.gtas.client.ctrl.IGTFieldMetaInfo;
import com.gridnode.gtas.client.GTClientException;

public interface IColumnEntityAdapter extends IColumnObjectAdapter
{
  /**
   * Return the field meta info for the entity field that this column shows
   * @param entity The entity instance for this row
   * @param column The column index
   * @return fieldMetaInfo
   * @throws GTClientException wrapping any errors that occur
   */
  public IGTFieldMetaInfo getColumnMetaInfo(Object entity, int column) throws GTClientException;

  /**
   * Returns the entity containing the field for the column. (In the case of a nested field
   * this is not the same as the entity for that row)
   * @param entity The entity instance for this row
   * @param column The column index
   * @return entity The entity that contains the field to show
   * @throws GTClientException wrapping any errors that occur
   */
  public IGTEntity getColumnEntity(Object entity, int column) throws GTClientException;
  
  /**
   * Returns true if the column allows (where applicable) links. Note that this is a new feature
   * and is not necessarily supported by all renderers that utilise the IColumnEntityAdapter
   * interface. 
   * @param entity The entity instance for this row
   * @param column The column index
   * @return showLinks
   * @throws GTClientException wrapping any errors that occur
   */
  public boolean isColumnLinkEnabled(Object entity, int column) throws GTClientException; //20050315AH
}