/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertCategory.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Dec 05 2002    Srinath	             Created
 */


package com.gridnode.pdip.app.alert.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for AlertCategory entity. It contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a AlertCategory entity instance.
 *   Name         - Name of the AlertCategory.
 *   Description  - Description of the AlertCategory.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Srinath
 *
 */

public class AlertCategory extends AbstractEntity implements IAlertCategory
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 216003744189208429L;
	
	protected String _name = null;
  protected String _description = null;
  protected String _code = null;

  public AlertCategory()
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

  public String getCode()
  {
    return _code;
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

  public void setCode(String code)
  {
    _code = code;
  }
}