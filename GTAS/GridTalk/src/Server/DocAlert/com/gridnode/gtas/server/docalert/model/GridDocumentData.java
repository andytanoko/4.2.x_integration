/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridDocumentData.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 26 2003    Neo Sok Lay         Created
 * May 14 2003    Neo Sok Lay         Return the actual udoc filename for
 *                                    U_DOC_FILENAME field.
 */
package com.gridnode.gtas.server.docalert.model;

import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import com.gridnode.pdip.framework.util.TimeUtil;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;


/**
 * This data provider is used to format GridDocument specific fields like
 * converting timestamp fields to local time, etc.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class GridDocumentData extends EntityDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -2620012527635689211L;

	public static final String NAME = GridDocument.ENTITY_NAME;

  private static final ArrayList _TIMESTAMP_FIELDS = new ArrayList();
  static
  {
    _TIMESTAMP_FIELDS.add(GridDocument.DT_CREATE);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_EXPORT);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_IMPORT);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_RECEIVE_END);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_RECEIVE_START);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_RECIPIENT_EXPORT);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_RECIPIENT_VIEW);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_SEND_END);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_SEND_START);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_TRANSACTION_COMPLETE);
    _TIMESTAMP_FIELDS.add(GridDocument.DT_VIEW);
  }

  public GridDocumentData(GridDocument gdoc)
  {
    super(gdoc);
  }

  /**
   * Override to convert the timestamps to local time.
   */
  protected Object getFieldValue(Number fieldId)
  {
    Object value = super.getFieldValue(fieldId);

    if (value != null)
    {
      if (_TIMESTAMP_FIELDS.contains(fieldId))
        value = convertTimestamp((Date)value);
      else if (GridDocument.U_DOC_FILENAME.equals(fieldId))
      {
        value = getActualFilename((String)value);
      }
    }
    return value;
  }

  /**
   * Get the actual filename from a filename string by stripping the ":" and
   * anything after ":".
   *
   * @param filename The filename string which may contain temporary filenames
   * (names after ":").
   * @return The actual filename.
   */
  protected String getActualFilename(String filename)
   {
    String actualFn = filename;

    int sepIdx = filename.indexOf(":");
    if (sepIdx > -1)
    {
      actualFn = filename.substring(0, sepIdx);
    }
    return actualFn;
  }

  /**
   * Convert the timestamp to local time.
   *
   * @param obj The timestamp.
   *
   * @return The timestamp converted to local time.
   *
   * @since 2.0 I7
   */
  private Date convertTimestamp(Date utcDate)
  {
    return TimeUtil.utcToLocalTimestamp(utcDate);
  }

  /**
   * Get the names of the fields available in the data provider.
   * @return List of field names (String)
   */
  public static final List getFieldNameList()
  {
    return getFieldNames(GridDocument.ENTITY_NAME);
  }
}