/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EnterpriseCreatedNotification.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 07 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.notify;

/**
 * Notification message on the creation of the Enterprise (GridNode) for this
 * GTAS.<p>
 * Currently the following are contained in the notification:<p>
 * <pre>
 * EnterpriseId - Id of the Enterprise
 * Profile      - Profile object of the Enterprise.
 * </pre>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class EnterpriseCreatedNotification
  extends    AbstractNotification
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2284102938576927886L;
	private String _enterpriseId;
  private Object _profile;

  public EnterpriseCreatedNotification(
    String enterpriseId, Object profile)
  {
    _enterpriseId = enterpriseId;
    _profile = profile;
  }

  public String getEnterpriseId()
  {
    return _enterpriseId;
  }

  public Object getProfile()
  {
    return _profile;
  }

  public String getNotificationID()
  {
    return "EnterpriseCreated";
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer(getNotificationID());
    buff.append(" - EnterpriseId[").append(getEnterpriseId()).append("]");

    return buff.toString();
  }
}