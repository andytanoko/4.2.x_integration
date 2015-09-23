/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionAudit.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    Ooi Hui Linn        Created
 */
package com.gridnode.pdip.base.session.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;
import java.util.Date;
import java.util.HashMap;

/**
 * This is an object model for SessionAudit entity. A SessionAudit contains the
 * session information.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a SessionAudit entity instance.
 *   SessionId      - Session Id to uniquely identify the session.
 *                    Uses key-gen (with latest value stored in DB) for uniqueness
 *                    across multiple servers. Format: SeqNo_RandomNoup to length of 30.
 *   SessionName    - User Name which this session belongs to.
 *                    Available for authenticated Session only.
 *   State          - State of the Session.
 *   SessionData    - Data stored in the session. This is serialised binary data.
 *                    A byte[].
 *   OpenTime       - Time when session is opened.
 *   LastActiveTime - Time of the last activity.
 *                    (This refers only to activity involving the session, such
 *                    as gettting or updating session. This time must be updated
 *                    continously to prevent the session from expiring.
 *  DestroyTime     - Time this session is closed / expired.
 *
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */

 public class SessionAudit
  extends    AbstractEntity
  implements ISessionAudit
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5952687186571166216L;
	protected String  _sessionId;
  protected String  _sessionName;
  protected short   _state;
//  protected byte[]  _sessionData = null;
  protected HashMap  _sessionData = null;
  protected Date    _openTime;
  protected Date    _lastActiveTime;
  protected Date    _destroyTime;

  public SessionAudit()
  {
  }

  // ***************** Methods from AbstractEntity *************************

  public Number getKeyId()
  {
    return UID;
  }

  public String getEntityDescr()
  {
    return getSessionId() + "-" + getState();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  // ******************** Getters for attributes ***************************

  public String  getSessionId()
  {
    return _sessionId;
  }

  public String  getSessionName()
  {
    return _sessionName;
  }

  public short   getState()
  {
    return _state;
  }

  public HashMap  getSessionData()
  {
    return _sessionData;
  }

  public Date    getOpenTime()
  {
    return _openTime;
  }

  public Date    getLastActiveTime()
  {
    return _lastActiveTime;
  }

  public Date    getDestroyTime()
  {
    return _destroyTime;
  }


  // ******************** Setters for attributes ***************************
  public void setSessionId(String sessionId)
  {
    _sessionId = sessionId;
  }

  public void setSessionName(String sessionName)
  {
    _sessionName = sessionName;
  }

  public void setState(short  state)
  {
    _state = state;
  }

  public void setSessionData(HashMap sessionData)
  {
    _sessionData = sessionData;
  }

  public void setOpenTime(Date openTime)
  {
    _openTime = openTime;
  }

  public void setLastActiveTime(Date lastActiveTime)
  {
    _lastActiveTime = lastActiveTime;
  }

  public void setDestroyTime(Date destroyTime)
  {
    _destroyTime = destroyTime;
  }


}
