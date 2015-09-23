/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DocumentType.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 08 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for DocumentType entity. A DocumentType is a
 * classification of a type of document.<P>
 *
 * The Model:<BR><PRE>
 *   UId       - UID for a DocumentType entity instance.
 *   Name      - Name of the DocumentType.
 *   Desc      - Description of the DocumentType.
 *   CanDelete - Whether the DocumentType can be deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class DocumentType
  extends    AbstractEntity
  implements IDocumentType
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1087373707628473984L;
	protected String  _docType;
  protected String  _desc;

  public DocumentType()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getName() + "/" + getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getName()
  {
    return _docType;
  }

  public String getDescription()
  {
    return _desc;
  }

  // *************** Setters for attributes *************************

  public void setName(String docType)
  {
    _docType = docType;
  }

  public void setDescription(String desc)
  {
    _desc = desc;
  }
}