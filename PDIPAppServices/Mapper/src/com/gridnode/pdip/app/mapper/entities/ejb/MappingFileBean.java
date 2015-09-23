/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.entities.ejb;

import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A MappingFileBean provides persistency services for MappingFile.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class MappingFileBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 6232746401877568470L;

	public String getEntityName()
  {
    return MappingFile.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity mappingFile)
    throws Exception
  {
    String mappingFileName = mappingFile.getFieldValue(MappingFile.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MappingFile.NAME, filter.getEqualOperator(),
      mappingFileName, false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "MappingFile : "+mappingFileName+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}