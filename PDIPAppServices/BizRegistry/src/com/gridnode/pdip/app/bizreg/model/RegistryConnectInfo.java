/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RegistryConnectInfo.java
 * Moved from GTAS/GridTalk/BizRegistry
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 24 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.bizreg.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This entity models the connection information for registry connection.
 * <p>
 * The model contains:<p>
 * <PRE>
 * UID             - UId of the RegistryConnectInfo entity instance.
 * Name            - Name of the RegistryConnectInfo.
 * QueryUrl        - Query Url to the target registry.
 * PublishUrl      - Publish Url to the target registry
 * PublishUser     - Username for authentication at the target registry for publishing
 * PublishPassword - Password for authentication at the target registry for publishing.
 * CanDelete       - Indicates whether the RegistryConnectInfo entity instance can
 *                   be delete from persistent storage.
 * Version         - Version number of the RegistryConnectInfo entity instance.
 * </PRE>
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class RegistryConnectInfo
  extends AbstractEntity
  implements IRegistryConnectInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3631851689795003376L;
	private String _name = "";
  private String _queryUrl;
  private String _publishUrl;
  private String _publishUser;
  private String _publishPassword;
  
  /**
   * Constructs a RegistryConnectInfo with no details. 
   */
  public RegistryConnectInfo()
  {
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
    return getName() + "/" + getQueryUrl() + "/" + getPublishUrl();
  }

  /**
   * @see com.gridnode.pdip.framework.db.entity.IEntity#getKeyId()
   */
  public Number getKeyId()
  {
    return UID;
  }

  /**
   * Gets the name of the RegistryConnectInfo.
   *  
   * @return The Name given to this RegistryConnectInfo instance.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Gets the password for publishing to the registry.
   * 
   * @return The PublishPassword.
   */
  public String getPublishPassword()
  {
    return _publishPassword;
  }

  /**
   * Gets the Url for publishing to the registry.
   * 
   * @return The PublishUrl.
   */
  public String getPublishUrl()
  {
    return _publishUrl;
  }

  /**
   * Gets the User for publishing to the registry.
   * 
   * @return The PublishUser.
   */
  public String getPublishUser()
  {
    return _publishUser;
  }

  /**
   * Gets the Url for querying from the registry.
   * 
   * @return The QueryUrl.
   */
  public String getQueryUrl()
  {
    return _queryUrl;
  }

  /**
   * Sets the Name for this RegistryConnectionInfo instance.
   * 
   * @param name The name to set.
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Sets the PublishPassword.
   * 
   * @param password The password to set.
   */
  public void setPublishPassword(String password)
  {
    _publishPassword = password;
  }

  /**
   * Sets the PublishUrl.
   * 
   * @param url The url to set.
   */
  public void setPublishUrl(String url)
  {
    _publishUrl = url;
  }

  /**
   * Sets the PublishUser.
   * 
   * @param user The user to set.
   */
  public void setPublishUser(String user)
  {
    _publishUser = user;
  }

  /**
   * Sets the QueryUrl.
   * 
   * @param url The url to set.
   */
  public void setQueryUrl(String url)
  {
    _queryUrl = url;
  }

  
  /**
   * @see java.lang.Object#hashCode()
   */
//  public int hashCode()
//  {
//    return getName().hashCode();
//  }

}
