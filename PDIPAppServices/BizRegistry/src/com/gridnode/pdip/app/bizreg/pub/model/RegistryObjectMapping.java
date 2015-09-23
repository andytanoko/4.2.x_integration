/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryObjectMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14, 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.pub.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.text.MessageFormat;

/**
 * This entity is used to maintain the mapping of published or
 * inquired registry objects to proprietary objects in the application.
 * <p>
 * The fields:<pre>
 * UID                   - UID of this entity instance.
 * RegistryObjectKey     - Key of the published/queried registry information object.
 * RegistryObjectType    - Type of the published/queried registry information object.
 * ProprietaryObjectKey  - Key of the proprietary object mapped to the registry information object. 
 * ProprietaryObjectType - Type of the proprietary object mapped to the registry information object.
 * RegistryPublishUrl    - Url to use for publishing the registry information object.
 * RegistryQueryUrl      - Url to use for querying the registry information object.
 * IsPublishedObject     - Indicates whether the registry information object is published from enterprise.
 * State                 - State of the registry information object.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryObjectMapping extends AbstractEntity
  implements IRegistryObjectMapping
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 301057897941287667L;

	private static final String DESCR_PATTERN = "{0}:Key={1}/{2}:Key={3}";

  private String _registryObjectKey;
  private String _registryObjectType;
  private String _registryQueryUrl;
  private String _registryPublishUrl;
  private String _proprietaryObjectType;
  private String _proprietaryObjectKey;
  private short _state = STATE_SYNCHRONIZED;
  private boolean _isPublishedObject;
  
  /**
   * Constructs a RegistryObjectMapping object with default values.
   */
  public RegistryObjectMapping()
  {
    super();
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getEntityName()
   */
  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getEntityDescr()
   */
  public String getEntityDescr()
  {
    return MessageFormat.format(
            DESCR_PATTERN, 
            new Object[]{
              getRegistryObjectType(),
              getRegistryObjectKey(),
              getProprietaryObjectType(),
              getProprietaryObjectKey(),
            });
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getKeyId()
   */
  public Number getKeyId()
  {
    return UID;
  }

  /**
   * Indicates whether the registry object is published from
   * within the enterprise.
   * 
   * @return <b>true</b> if registry object is published from
   * this enterprise, <b>false</b> otherwise.
   */
  public boolean isPublishedObject()
  {
    return _isPublishedObject;
  }

  /**
   * Get the key of the proprietary object that corresponds to the
   * registry object.
   * 
   * @return Unique identifier of the proprietary object.
   */
  public String getProprietaryObjectKey()
  {
    return _proprietaryObjectKey;
  }

  /**
   * Get the type of the proprietary object that corresponds to
   * the registry object.
   * 
   * @return Type name of the proprietary object.
   */
  public String getProprietaryObjectType()
  {
    return _proprietaryObjectType;
  }

  /**
   * Get the key of the registry object.
   * 
   * @return Universal unique identifier (UUID) of the registry object.
   */
  public String getRegistryObjectKey()
  {
    return _registryObjectKey;
  }

  /**
   * Get the type of the registry object
   * 
   * @return Type name of the registry object.
   */
  public String getRegistryObjectType()
  {
    return _registryObjectType;
  }

  /**
   * Get the Url for publishing the registry object.
   * 
   * @return If IsPublishedObject is <b>true</b>, 
   * returns the Url that published that object. Otherwise, returns
   * empty string or <b>null</b>.
   */
  public String getRegistryPublishUrl()
  {
    return _registryPublishUrl;
  }

  /**
   * Get the Url for querying the registry object.
   * 
   * @return The Url that can query for the registry object.
   */
  public String getRegistryQueryUrl()
  {
    return _registryQueryUrl;
  }

  /**
   * Get the state of the registry object.
   * 
   * @return State of the registry object.
   * @see #STATE_SYNCHRONIZED
   * @see #STATE_DELETED_FROM_REGISTRY
   * @see #STATE_PENDING_UPDATE
   * @see #STATE_PENDING_DELETE
   */
  public short getState()
  {
    return _state;
  }

  /**
   * Sets the IsPublishedObject flag.
   * 
   * @param isPublishedObject <b>true</b> to indicate that the
   * registry object is published from this enterprise, <b>false</b> otherwise.
   */
  public void setPublishedObject(boolean isPublishedObject)
  {
    _isPublishedObject = isPublishedObject;
  }

  /**
   * Sets the key to identify the proprietary object that corresponds
   * to the registry object.
   * 
   * @param key Key of proprietary object to set.
   */
  public void setProprietaryObjectKey(String key)
  {
    _proprietaryObjectKey = key;
  }

  /**
   * Sets the type of the proprietary object that corresponds to the
   * registry object.
   * 
   * @param type Type of proprietary object to set.
   */
  public void setProprietaryObjectType(String type)
  {
    _proprietaryObjectType = type;
  }

  /**
   * Sets the key of the registry object.
   * 
   * @param key The key of registry object to set.
   */
  public void setRegistryObjectKey(String key)
  {
    _registryObjectKey = key;
  }

  /**
   * Sets the type of the registry object.
   * 
   * @param type The type of registry object to set.
   */
  public void setRegistryObjectType(String type)
  {
    _registryObjectType = type;
  }

  /**
   * Sets the Url for publishing the registry object.
   * 
   * @param url The Url to set.
   */
  public void setRegistryPublishUrl(String url)
  {
    _registryPublishUrl = url;
  }

  /**
   * Sets the Url for querying the registry object.
   * 
   * @param url
   */
  public void setRegistryQueryUrl(String url)
  {
    _registryQueryUrl = url;
  }

  /**
   * Sets the state of the registry object.
   * 
   * @param state The state to set.
   */
  public void setState(short state)
  {
    _state = state;
  }

}
