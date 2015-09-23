/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GnCategory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 09 2002    Neo Sok Lay         Created
 * Sep 22 2002    Neo Sok Lay         Add NodeUsage field.
 */
package com.gridnode.gtas.server.gridnode.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This class defines the various GridNode categories.<p>
 * The data model:<p>
 * <PRE>
 * Code   - The Category code.
 * Name   - The Category name.
 * NodeUsage  - The usage of the GridNode of this Category, e.g. Live, Trial, Test
 * </PRE>
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class GnCategory
  extends    AbstractEntity
  implements  IGnCategory
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 1810377659406755334L;
	private String _code;
  private String _name;
  private String _nodeUsage;

  public GnCategory()
  {
  }

  // ****************** Methods from AbstractEntity ***************************

  public String getEntityDescr()
  {
    return getCode() + "-" + getName();
  }

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public Number getKeyId()
  {
    return CATEGORY_CODE;
  }


  // **************** Getters & Setters ***********************************

  /**
   * Get the CategoryCode.
   *
   * @return The CategoryCode of this Category object.
   *
   * @since 2.0 I5
   */
  public String getCode()
  {
    return _code;
  }

  /**
   * Get the CategoryName.
   *
   * @return The CategoryName of this Category object.
   *
   * @since 2.0 I5
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Get the NodeUsage.
   *
   * @return The NodeUsage of this Category object.
   *
   * @since 2.0 I5
   */
  public String getNodeUsage()
  {
    return _nodeUsage;
  }

  /**
   * Set the CategoryCode.
   *
   * @param catCode The CategoryCode to set.
   *
   * @since 2.0 I5
   */
  public void setCode(String catCode)
  {
    _code = catCode;
  }

  /**
   * Set the CategoryName.
   *
   * @param name The CategoryName to set.
   *
   * @since 2.0 I5
   */
  public void setName(String name)
  {
    _name = name;
  }

  /**
   * Set the NodeUsage.
   *
   * @param nodeUsage The nodeUsage to set.
   *
   * @since 2.0 I5
   */
  public void setNodeUsage(String nodeUsage)
  {
    _nodeUsage = nodeUsage;
  }
}