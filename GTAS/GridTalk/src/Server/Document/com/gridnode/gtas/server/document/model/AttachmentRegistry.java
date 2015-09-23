/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AttachmentRegistry.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 06 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;

/**
 * This is an object model for the AttachmentRegistry entity.
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a Attachment entity instance.
 *   CanDelete        - Whether the attachment can be deleted.
 *   PartnerId        - The partner ID of the partner this attachment is to be
 *                      send to.
 *   AttachmentUid    - To indicate whether this attachment has been sent to the
 *                      partner.
 *   DateProcessed    - Date processed.
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
public class AttachmentRegistry
  extends    AbstractEntity
  implements IAttachmentRegistry
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7015179210453260238L;
	protected String  _partnerId;
  protected Long    _attachmentUid;
  protected Date    _dateProcessed;

  public AttachmentRegistry()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getPartnerId() + "-" + getAttachmentUid() + "-" + getDateProcessed();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getPartnerId()
  {
    return _partnerId;
  }

  public Long getAttachmentUid()
  {
    return _attachmentUid;
  }

  public Date getDateProcessed()
  {
    return _dateProcessed;
  }

  // *************** Setters for attributes *************************

  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

  public void setAttachmentUid(Long attachmentUid)
  {
    _attachmentUid = attachmentUid;
  }

  public void setDateProcessed(Date dateProcessed)
  {
    _dateProcessed = dateProcessed;
  }

}