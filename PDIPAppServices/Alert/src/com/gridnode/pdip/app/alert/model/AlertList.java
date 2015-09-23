/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertList.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 26 2002    Srinath	             Created
 * Feb 06 2003    Neo Sok Lay             Change UserId to UserUid.
 *                                        Change FromId to FromUid.
 */


package com.gridnode.pdip.app.alert.model;

import java.util.Date;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Alert entity. It contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a AlertList entity instance.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Srinath
 *
 *
 */

public class AlertList extends AbstractEntity implements IAlertList
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4517283179139105554L;
	
	protected Long _userUid  = null;
  protected Long _fromUid  = null;
  protected String _title   = null;
  protected String _message = null;
  protected boolean _readstatus = false;
  protected Date _date	= null;

  public AlertList()
  {
  }

  public String getEntityDescr()
  {
    return _uId + "/" + _userUid + "/" + _title;
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ******************** Getters for attributes ***************************

  public Long getUserUid()
  {
    return _userUid;
  }

  public Long getFromUid()
  {
    return _fromUid;
  }

  public String getTitle()
  {
    return _title;
  }

  public String getMessage()
  {
    return _message;
  }

  public boolean getReadStatus()
  {
    return _readstatus;
  }

  public Date getDate()
  {
    return _date;
  }

  // ******************** Setters for attributes ***************************
  public void setUserUid(Long userUid)
  {
    _userUid = userUid;
  }

  public void setFromUid(Long fromUid)
  {
    _fromUid = fromUid;
  }

  public void setTitle(String title)
  {
    _title = title;
  }

  public void setMessage(String message)
  {
    _message = message;
  }

  public void setReadStatus(boolean readStatus)
  {
    this._readstatus = readStatus;
  }

  public void setDate(Date date)
  {
    _date = date;
  }
}