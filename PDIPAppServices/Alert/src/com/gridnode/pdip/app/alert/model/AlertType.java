/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertType.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 05 2002    Srinath	          Created
 */


package com.gridnode.pdip.app.alert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for AlertType entity. It contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a Alert Type entity instance.
 *   Name         - Name of the Alert type.
 *   Description  - Description of the Alert type.
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

public class AlertType extends AbstractEntity implements IAlertType
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4817473338843643812L;
	
	protected String _name = null;
  protected String _description = null;
  protected String _msgid = null;

  public AlertType()
  {
  }

  public String getEntityDescr()
  {
    return _uId + "/" + _name + "/" + _description;
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

  // ******************** Setters for attributes ***************************

  public void setName(String name)
  {
    this._name = name;
  }

  public void setDescr(String descr)
  {
    this._description = descr;
  }

}