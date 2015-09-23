/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingData.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 26 Feb 2003    Neo Sok Lay         Created
 * 09 Mar 2004    Neo Sok Lay         Include Mapping file subpath in MAPPING_FILE_NAME
 *                                    data field.
 */

package com.gridnode.gtas.server.mapper.model;

import com.gridnode.pdip.app.mapper.model.MappingRule;

import com.gridnode.pdip.app.alert.providers.EntityDetails;

import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Data provider for Mapping fields.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2.10
 * @since 2.0 I8
 */
public class MappingData extends EntityDetails
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2222535707212464088L;
	public static final String NAME = "Mapping";
  public static final String FIELD_MAPPING_FILENAME = "MAPPING_FILE_NAME";
  private static final List _selectableFieldIds = new ArrayList();
  static
  {
    _selectableFieldIds.add(MappingRule.NAME);
    _selectableFieldIds.add(MappingRule.DESCRIPTION);
    _selectableFieldIds.add(MappingRule.KEEP_ORIGINAL);
    _selectableFieldIds.add(MappingRule.PARAM_NAME);
    _selectableFieldIds.add(MappingRule.TRANSFORM_REF_DOC);
    _selectableFieldIds.add(MappingRule.TYPE);
    _selectableFieldIds.add(MappingRule.XPATH);
  }


  public MappingData(MappingRule mappingRule)
  {
    super(mappingRule);
    if (mappingRule.getMappingFile() != null)
    {
      String filename = mappingRule.getMappingFile().getFilename();
      String subPath = mappingRule.getMappingFile().getSubPath();
      if (subPath != null)
        filename = subPath.concat(filename);
      set(FIELD_MAPPING_FILENAME, filename);
    }
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
    list.add(FIELD_MAPPING_FILENAME);
    list.addAll(getFieldNames(MappingRule.ENTITY_NAME));

    return list;
  }

  /**
   * Overrides to limit the selectable fields in MappingRule for substitution.
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
   * Overrides to limit the selectable fields in MappingRule for substitution.
   */
  protected static boolean isSelectable(FieldMetaInfo fmi)
  {
    return _selectableFieldIds.contains(fmi.getFieldId());
  }

 }