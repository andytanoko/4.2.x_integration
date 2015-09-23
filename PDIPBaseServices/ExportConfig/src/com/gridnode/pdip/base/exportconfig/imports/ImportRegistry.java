/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 03 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.imports;

import java.io.File;
import java.io.Serializable;

import com.gridnode.pdip.base.exportconfig.model.ImportEntityList;

public class ImportRegistry implements Serializable
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6093411581632579458L;
	private ImportEntityList _importList;
  private ImportEntityList _conflictList;
  private ImportEntityList _persistedList;
  private File _unzipDir;

  public ImportRegistry()
  {
    _importList = new ImportEntityList();
    _conflictList = new ImportEntityList();
    _persistedList = new ImportEntityList();
    _unzipDir = null;
  }

  public ImportRegistry(ImportEntityList importList, ImportEntityList conflictList,
                        File unzipDir)
  {
    _importList = importList;
    _conflictList = conflictList;
    _persistedList = new ImportEntityList();
    _unzipDir = unzipDir;
  }

  public ImportEntityList getImportList()
  {
    return _importList;
  }

  public ImportEntityList getConflictList()
  {
    return _conflictList;
  }

  public ImportEntityList getPersistedList()
  {
    return _persistedList;
  }

  public File getUnzipDir()
  {
    return _unzipDir;
  }

  public void setUnzipDir(File unzipDir)
  {
    _unzipDir = unzipDir;
  }

  /**
   * Update the conflict list with the entities that the user has choosen to
   * overwrite.
   *
   * @param overwriteList the ConfigEntitiesContainer containing entities
   *                      selected by the user to be overwritten.
   */
   /**@todo move out of this class */
//  public void updateConflictList(ConfigEntitiesContainer overwriteList)
//  {
//    Collection configEntityLists = overwriteList.getConfigEntityLists();
//    for (Iterator i = configEntityLists.iterator(); i.hasNext(); )
//    {
//      ConfigEntityList configEntityList = (ConfigEntityList)i.next();
//      String entityName = configEntityList.getEntityName();
//      Collection descs = configEntityList.getConfigEntityDescriptors();
//      for (Iterator j = descs.iterator(); j.hasNext(); )
//      {
//        ConfigEntityDescriptor desc = (ConfigEntityDescriptor)j.next();
//        Long oldUid = desc.getUid();
//        ImportEntity impEntity = _conflictList.getEntity(entityName, oldUid);
//        if (impEntity != null)
//        {
//          impEntity.setIsOverwrite(true);
//        }
//      }
//    }
//  }
}