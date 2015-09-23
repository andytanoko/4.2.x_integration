/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UserProcData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 26 Feb 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.userprocedure.model;

import com.gridnode.pdip.base.userprocedure.model.UserProcedure;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Data provider for UserProcedure fields.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I8
 * @since 2.0 I8
 */
public class UserProcData extends EntityDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1690368478771002129L;
	public static final String NAME = "UserProcedure";
  public static final String FIELD_PROC_DEF_FILENAME = "DEF_FILE_NAME";
  private static final List _selectableFieldIds = new ArrayList();
  static
  {
    _selectableFieldIds.add(UserProcedure.NAME);
    _selectableFieldIds.add(UserProcedure.DESCRIPTION);
    _selectableFieldIds.add(UserProcedure.PROC_TYPE);
  }

  public UserProcData(UserProcedure userProcedure)
  {
    super(userProcedure);
    if (userProcedure.getProcedureDefFile() != null)
      set(FIELD_PROC_DEF_FILENAME, userProcedure.getProcedureDefFile().getFileName());
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
    List list = new ArrayList();
    list.add(FIELD_PROC_DEF_FILENAME);
    list.addAll(getFieldNames(UserProcedure.ENTITY_NAME));

    return list;
  }

  /**
   * Overrides to limit the selectable fields in UserProcedure for substitution.
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
   * Overrides to limit the selectable fields in UserProcedure for substitution.
   */
  protected static boolean isSelectable(FieldMetaInfo fmi)
  {
    return _selectableFieldIds.contains(fmi.getFieldId());
  }
 }