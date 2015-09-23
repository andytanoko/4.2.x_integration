/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 02 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.exports;

import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class will serialize a collection of entities into a XML file.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

import com.gridnode.pdip.base.exportconfig.model.ImportEntityList;

public class ExportRegistry implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4240522904352803868L;
	private ImportEntityList _exportList;
  private ImportEntityList _foreignList;

  public ExportRegistry(ImportEntityList exportList)
  {
    _exportList = exportList;
    _foreignList = new ImportEntityList();
  }

  public ImportEntityList getExportList()
  {
    return _exportList;
  }

  public ImportEntityList getForeignList()
  {
    return _foreignList;
  }

  public void updateExportListWithForeignEntities()
  {
    Collection foreignEntities = _foreignList.getEntities();
    for (Iterator i = foreignEntities.iterator(); i.hasNext(); )
    {
      ImportEntity foreignEntity = (ImportEntity)i.next();
      _exportList.addImportEntity(foreignEntity);
    }
  }
}