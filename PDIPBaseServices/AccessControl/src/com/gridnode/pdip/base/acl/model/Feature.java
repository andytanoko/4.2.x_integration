/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Feature.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 20 2002    Goh Kan Mun             Created
 */


package com.gridnode.pdip.base.acl.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.List;

/**
 * This is an object model for Feature entity. A Feature contains information about
 * entities and permissable actions.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a Feature entity instance.
 *   Feature      - Name of the Feature.
 *   Description  - Description of the Feature.
 *   Actions      - List of available action that the feature allows.
 *   DataTypes    - List of data type that the feature allows.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 *
 */

public class Feature extends AbstractEntity implements IFeature
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4129694973837213548L;
	protected String _feature = null;
  protected String _description = null;
  protected List _actions = null;
  protected List _dataTypes = null;

  public Feature()
  {
  }

  public String getEntityDescr()
  {
    return _uId + "/" + _feature + "/" + _description;
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

  public String getFeature()
  {
    return _feature;
  }

  public String getDescr()
  {
    return _description;
  }

  public List getActions()
  {
    return _actions;
  }

  public List getDataTypes()
  {
    return _dataTypes;
  }

  // ******************** Setters for attributes ***************************

  public void setFeature(String feature)
  {
    this._feature = feature;
  }

  public void setDescr(String descr)
  {
    this._description = descr;
  }

  public void setActions(List actions)
  {
    this._actions = actions;
  }

  public void setDataTypes(List dataTypes)
  {
    this._dataTypes = dataTypes;
  }

}