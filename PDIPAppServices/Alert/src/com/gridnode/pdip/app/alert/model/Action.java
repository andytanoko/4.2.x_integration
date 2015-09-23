/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Action.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 11 2002    Srinath	             Created
 * Feb 06 2003    Neo Sok Lay             Change MsgId to MsgUid.
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 */
package com.gridnode.pdip.app.alert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Alert entity. It contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a Alert entity instance.
 *   Name         - Name of the Alert.
 *   Description  - Description of the Alert.
 *   MsgUid       - UID of the MessageTemplate that this Action links to.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Srinath
 */

public class Action extends AbstractEntity implements IAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5269738667584783001L;
	
	protected String _name = null;
  protected String _description = null;
  protected Long _msgUid = null;

  public Action()
  {
  }

  public String getEntityDescr()
  {
    return new StringBuffer(_name).append('/').append(_description).toString();
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

  public String getName()
  {
    return _name;
  }

  public String getDescr()
  {
    return _description;
  }

  public Long getMsgUid()
  {
    return _msgUid;
  }

  // ******************** Setters for attributes ***************************

  public void setName(String name)
  {
    this._name = name;
  }

  public void setDescr(String descr)
  {
    this._description = descr;
  }

  public void setMsgUid(Long msgUid)
  {
    _msgUid = msgUid;
  }
}