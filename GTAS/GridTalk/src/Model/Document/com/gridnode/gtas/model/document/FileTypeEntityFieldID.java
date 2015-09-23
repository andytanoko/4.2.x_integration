/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 18 2002    Koh Han Sing        Created
 * Feb 06 2007    Chong SoonFui       Commented IFileType.CAN_DELETE
 */
package com.gridnode.gtas.model.document;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the FileType module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class FileTypeEntityFieldID
{
  private Hashtable _table;
  private static FileTypeEntityFieldID _self = null;

  private FileTypeEntityFieldID()
  {
    _table = new Hashtable();

    //FileType
    _table.put(IFileType.ENTITY_NAME,
      new Number[]
      {
//        IFileType.CAN_DELETE,
        IFileType.DESCRIPTION,
        IFileType.FILE_TYPE,
        IFileType.PARAMETERS,
        IFileType.PROGRAM_NAME,
        IFileType.PROGRAM_PATH,
        IFileType.UID,
        IFileType.WORKING_DIR
      });
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new FileTypeEntityFieldID();
    }
    return _self._table;
  }
}