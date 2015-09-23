/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentTypeBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.entities.ejb;

import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * A DocumentTypeBean provides persistency services for document type.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class DocumentTypeBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3183693159802650340L;

	public String getEntityName()
  {
    return DocumentType.ENTITY_NAME;
  }

  protected void checkDuplicate(IEntity documentType)
    throws Exception
  {
    String docType = documentType.getFieldValue(DocumentType.DOC_TYPE).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, DocumentType.DOC_TYPE, filter.getEqualOperator(),
      docType, false);

    if (getDAO().findByFilter(filter).size() > 0)
    {
      String msg = "DocumentType : "+docType+" already exist";
      throw new DuplicateEntityException(msg);
    }
  }

}