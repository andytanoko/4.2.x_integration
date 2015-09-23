/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 25 Feb 2003    Neo Sok Lay         Created
 * 09 May 2003    Neo Sok Lay         Add more fields in fieldname list.
 * 24 Jan 2006    Neo Sok Lay         Add IS_REQUEST_MSG in fieldname list
 */

package com.gridnode.gtas.server.rnif.model;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Data provider for Process fields.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I8
 */
public class ProcessData extends EntityDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8093462138717162724L;
	public static final String NAME = "Process";
  private static final List _selectableFieldIds = new ArrayList();
  static
  {
    _selectableFieldIds.add(RNProfile.BUS_ACTIVITY_IDENTIFIER);
    _selectableFieldIds.add(RNProfile.DELIVERY_MESSAGE_TRACKING_ID);
    _selectableFieldIds.add(RNProfile.PROCESS_DEF_NAME);
    _selectableFieldIds.add(RNProfile.PROCESS_INSTANCE_ID);
    _selectableFieldIds.add(RNProfile.PROCESS_ORIGINATOR_ID);
    _selectableFieldIds.add(RNProfile.SENDER_GLOBAL_BUS_IDENTIFIER);
    _selectableFieldIds.add(RNProfile.RECEIVER_GLOBAL_BUS_IDENTIFIER);
    _selectableFieldIds.add(RNProfile.IS_REQUEST_MSG);
  }

  public ProcessData(RNProfile profile)
  {
    super(profile);
  }

  public String getName()
  {
    return NAME;
  }

  /**
   * Get the names of fields available in this data provider.
   * @return List of field names (String).
   */
  public static final List getFieldNameList()
  {
    return getFieldNames(RNProfile.ENTITY_NAME);
  }

  /**
   * Overrides to limit the selectable fields in RNProfile for substitution.
   */
  protected static List getFieldNames(String entityName)
  {
    ArrayList fieldNames = new ArrayList();

    EntityMetaInfo emi = MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    if (emi != null)
    {
      FieldMetaInfo[] fmis = emi.getFieldMetaInfo();

      for (int i=0; i<fmis.length; i++)
      {
        if (isSelectable(fmis[i]))
          fieldNames.add(fmis[i].getFieldName());
      }
    }
    return fieldNames;
  }

  /**
   * Overrides to limit the selectable fields in RNProfile for substitution.
   */
  protected static boolean isSelectable(FieldMetaInfo fmi)
  {
    return _selectableFieldIds.contains(fmi.getFieldId());
  }
}