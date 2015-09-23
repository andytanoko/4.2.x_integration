/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Property.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 9, 2006    i00107              Created
 * Mar 14, 2007   i00118			  Added "updateDeleteScheduler" query for
 * 									  housekeep report
 * Mar 21, 2007   i00118			  Modified query name from "updateDeleteScheduler"
 *                                    to "updateProperty"
 */

package com.gridnode.util.config;

import java.io.Serializable;

/**
 * @author i00107
 * Persistent object for a configuration property
 * @hibernate.class table="\"config_props\""
 * @hibernate.query name = "getProperty"
 *    query = "from Property as p where p.category = :category and p.key = :key"
 * @hibernate.query name = "getPropertyList"
 *    query = "from Property as p where p.category = :category"
 * @hibernate.query name = "updateProperty"
 *   query = "UPDATE Property set value=:value where category=:category and key=:key"
 */
public class Property implements Serializable
{
  /**
   * Serial Version UID 
   */
  private static final long serialVersionUID = -3748295250951685539L;
  private String _category;
  private String _key;
  private String _value;
  private Integer _uid;
  
  
  public Property()
  {
    
  }

  /**
   * @return Returns the category.
   * @hibernate.property  column = "\"category\"" not-null = "true"
   */
  public String getCategory()
  {
    return _category;
  }

  /**
   * @param category The category to set.
   */
  public void setCategory(String category)
  {
    _category = category;
  }

  /**
   * @return Returns the key.
   * @hibernate.property column = "\"property_key\"" not-null = "true"
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * @param key The key to set.
   */
  public void setKey(String key)
  {
    _key = key;
  }

  /**
   * @return Returns the value.
   * @hibernate.property column = "\"value\""
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * @param value The value to set.
   */
  public void setValue(String value)
  {
    _value = value;
  }

  /**
   * @return Returns the uid.
   * @hibernate.id column = "\"uid\"" generator-class = "sequence"
   * @hibernate.generator-param name = "sequence" value = "config_props_uid_seq"
   */
  public Integer getUid()
  {
    return _uid;
  }

  /**
   * @param uid The uid to set.
   */
  public void setUid(Integer uid)
  {
    _uid = uid;
  }
  
  
}
