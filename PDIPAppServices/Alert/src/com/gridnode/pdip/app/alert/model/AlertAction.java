/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 14 2002    Srinath	             Created
 * Feb 06 2003    Neo Sok Lay             Change AlertId to AlertUid.
 *                                        Change ActionId to ActionUid.
 */


package com.gridnode.pdip.app.alert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for AlertAction entity. It contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a AlertAction entity instance.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Srinath
 *
 */

public class AlertAction extends AbstractEntity implements IAlertAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1908887563286894134L;
	protected Long _alertUid = null;
  protected Long _actionUid = null;

  public AlertAction()
  {
  }

  public String getEntityDescr()
  {
    return _uId + "/AlertAction";
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

  public Long getAlertUid()
  {
    return _alertUid;
  }

  public Long getActionUid()
  {
    return _actionUid;
  }

  // ******************** Setters for attributes ***************************

  public void setAlertUid(Long alertUid)
  {
    this._alertUid = alertUid;
  }

  public void setActionUid(Long actionUid)
  {
    this._actionUid = actionUid;
  }
}