/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 23 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
 
/**
 * A FileTypeBean provides persistency services for FileType.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class FileTypeBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8048335389556838508L;

	public String getEntityName()
  {
    return FileType.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity fileType)
    throws Exception
  {
    String fileTypeName = fileType.getFieldValue(FileType.FILE_TYPE).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, FileType.FILE_TYPE, filter.getEqualOperator(),
      fileTypeName, false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "FileType : "+fileTypeName+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}