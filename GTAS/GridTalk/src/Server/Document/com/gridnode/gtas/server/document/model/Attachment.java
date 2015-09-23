/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Attachment.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 22 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for the Attachment entity.
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a Attachment entity instance.
 *   CanDelete        - Whether the attachment can be deleted.
 *   PartnerId        - The partner ID of the partner this attachment is to be
 *                      send to.
 *   IsSendInitiated  - To indicate whether this attachment has been sent to the
 *                      partner.
 *   OriginalUid      - The uid of the orginal attachment entity at the sender
 *                      side.
 *   Filename         - The filename of the attachment in the attachment folder.
 *   OriginalFilename - The orginal filename of the attachment.
 *   IsOutgoing       - To indicate whether this attachment is a outgoing
 *                      attachment or incoming attachment.
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
public class Attachment
  extends    AbstractEntity
  implements IAttachment
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1929005975703035629L;
	protected String  _partnerId;
//  protected Boolean _isSendInitiated;
  protected Long    _originalUid;
  protected String  _filename;
  protected String  _originalFilename;
  protected Boolean _isOutgoing;

  public Attachment()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getOriginalUid() + "-" + getFilename() +  "-"
           + getOriginalFilename() + "-" + isOutgoing();
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

//  public Boolean isSendInitiated()
//  {
//    return _isSendInitiated;
//  }

  public Long getOriginalUid()
  {
    return _originalUid;
  }

  public String getFilename()
  {
    return _filename;
  }

  public String getOriginalFilename()
  {
    return _originalFilename;
  }

  public Boolean isOutgoing()
  {
    return _isOutgoing;
  }

  // *************** Setters for attributes *************************

  public void setPartnerId(String partnerId)
  {
    _partnerId = partnerId;
  }

//  public void setIsSendInitiated(Boolean isSendInitiated)
//  {
//    _isSendInitiated = isSendInitiated;
//  }

  public void setOriginalUid(Long originalUid)
  {
    _originalUid = originalUid;
  }

  public void setFilename(String filename)
  {
    _filename = filename;
  }

  public void setOriginalFilename(String originalFilename)
  {
    _originalFilename = originalFilename;
  }

  public void setOutgoing(Boolean isOutgoing)
  {
    _isOutgoing = isOutgoing;
  }

}