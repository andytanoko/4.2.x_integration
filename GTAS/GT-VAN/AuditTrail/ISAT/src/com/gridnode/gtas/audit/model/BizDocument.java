/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BizDocument.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 * Dec 27, 2006    Tam Wei Xiang       Change the content type from String 
 *                                     to byte[]
 * May 24, 2007    Tam Wei Xiang       Add in isRequiredUnzip flag. This is
 *                                     to handle the performance tuning for the
 *                                     GTVAN.
 */
package com.gridnode.gtas.audit.model;

import com.gridnode.util.db.AbstractPersistable;

/**
 * The instance of this class has a direct mapping on one of the record in the BizDocument table
 * @author Tam Wei Xiang
 * @since GT 4.0
 * 
 * @hibernate.class table = "`isat_business_document`"
 * @hibernate.query
 *   query = "FROM BizDocument bt WHERE bt.UID = :uid"
 *   name = "getBusinessDocumentByUID"
 */
public class BizDocument extends AbstractPersistable implements IAuditTrailEntity
{
  private byte[] _content;
  private Boolean _isRequiredUnpack;
  private String _groupName;
  private Boolean _isRequiredUnzip = true;
  
  public BizDocument()
  {
  }
  
  public BizDocument(String groupName, byte[] file, Boolean isRequiredUnpack, Boolean isRequiredUnzip)
  {
    setContent(file);
    setRequiredUnpack(isRequiredUnpack);
    setGroupName(groupName);
    setRequiredUnzip(isRequiredUnzip);
  }
  
  /**
   * @hibernate.property column = "`group_name`"
   */
  public String getGroupName()
  {
    return _groupName;
  }

  public void setGroupName(String name)
  {
    _groupName = name;
  }
  
  /**
   * @hibernate.property name = "content" column = "`files`" type = "binary"
   * @return
   */
  public byte[] getContent()
  {
    return _content;
  }

  public void setContent(byte[] _file)
  {
    this._content = _file;
  }
  
  /**
   * @hibernate.property name = "RequiredUnpack" column = "`is_required_unpack`" type = "boolean"
   * @return
   */
  public Boolean isRequiredUnpack()
  {
    return _isRequiredUnpack;
  }

  public void setRequiredUnpack(Boolean requiredUnpack)
  {
    _isRequiredUnpack = requiredUnpack;
  }
  
  /**
   * @hibernate.property name="RequiredUnzip" column = "`is_required_unzip`"
   * @return
   */
  public Boolean isRequiredUnzip()
  {
    return _isRequiredUnzip;
  }

  public void setRequiredUnzip(Boolean requiredUnzip)
  {
    _isRequiredUnzip = requiredUnzip;
  }

  public String toString()
  {
    return "BizDocument[UID "+(getUID()== null ? "" : getUID())+" file: "+ (getContent() == null? "" : "content size "+getContent().length+"")+
           " isRequiredUnpack: "+isRequiredUnpack()+"groupName: "+getGroupName()+" isRequiredUnzip: "+isRequiredUnzip()+"]";
  }
}
