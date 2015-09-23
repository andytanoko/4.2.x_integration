/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Taxonomy.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 18 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.bizreg.model;

/**
 * A Taxonomy represents a categorization or identification of
 * a Technical model.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class Taxonomy
{
  private String _key;
  private String _value;
  private String _name;
  
  /**
   * Constructs a Taxonomy 
   */
  public Taxonomy()
  {
  }

  /**
   * Gets the key of the classification scheme of the Taxonomy.
   * @return The Classification scheme key of the Taxonomy.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Gets the Name of the Taxonomy.
   * @return The Name of the Taxonomy.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Gets the value of the Taxonomy.
   * @return The Value of the Taxonomy.
   */
  public String getValue()
  {
    return _value;
  }

  /**
   * Sets the key of the classification scheme of the Taxonomy.
   * @param key The key to set.
   */
  public void setKey(String key)
  {
    _key = key;
  }

  /**
   * Sets the name of the taxonomy.
   * @param name the Name to set.
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Sets the value of the taxonomy.
   * @param value The value to set.
   */
  public void setValue(String value)
  {
    _value = value;
  }

}
