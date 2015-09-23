/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Alert.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 30 2002    Srinath	             Created
 * Feb 07 2003    Neo Sok Lay             Add BindActions field to cache
 *                                        the UIDs of the Actions that are
 *                                        bound to the Alert. Non persistent field.
 * Jul 18 2003    Neo Sok Lay             Change EntityDescr.
 * Mar 03 2006    Neo Sok Lay             Use generics 
 */
package com.gridnode.pdip.app.alert.model;

import java.util.Collection;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for Alert entity. It contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a Alert entity instance.
 *   Name         - Name of the Alert.
 *   Description  - Description of the Alert.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Srinath
 *
 */

public class Alert extends AbstractEntity implements IAlert
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4466065941983986791L;
	
	protected String _name = null;
  protected Long _alertTypeUid = null;
  protected Long _categoryUid = null;
  protected String _trigger = null;
  protected String _description = null;
  protected Collection<Long> _bindActions = null;

  public Alert()
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

  public Long getAlertType()
  {
    return _alertTypeUid;
  }

  public Long getCategory()
  {
    return _categoryUid;
  }

  public String getTrigger()
  {
    return _trigger;
  }

  public String getDescr()
  {
    return _description;
  }

  public Collection<Long> getBindActions()
  {
    return _bindActions;
  }

  // ******************** Setters for attributes ***************************

  public void setName(String name)
  {
    this._name = name;
  }

  public void setAlertType(Long typeUid)
  {
    this._alertTypeUid = typeUid;
  }

  public void setCategory(Long categoryUid)
  {
    this._categoryUid = categoryUid;
  }

  public void setTrigger(String trigger)
  {
    this._trigger = trigger;
  }

  public void setDescr(String descr)
  {
    this._description = descr;
  }

  public void setBindActions(Collection<Long> actionUids)
  {
    _bindActions = actionUids;
  }
}