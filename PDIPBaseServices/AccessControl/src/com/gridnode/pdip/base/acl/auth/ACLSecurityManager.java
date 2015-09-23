/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ACLSecurityManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2002    Neo Sok Lay         Created
 */
package com.gridnode.pdip.base.acl.auth;

import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This SecurityManager allows checking of AccessPermission based on
 * feature, action, and optionally data type and criteria.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 2.0
 */
public class ACLSecurityManager extends SecurityManager
{
  /**
   * Checks the AccessPermission for accessing a certain Action under
   * a certain Feature.
   *
   * @param feature The name of the Feature
   * @param action The name of the Action.
   *
   * @since 2.0
   */
  public void checkActionAccess(
    String feature, String action)
  {
    if (feature == null)
      throw new NullPointerException("Feature can't be null!");
    if (action == null)
      throw new NullPointerException("Action can't be null!");

    checkPermission(new AccessPermission(feature, action));
  }

  /**
   * Checks the AccessPermission for accessing a certain certain type of data
   * during a certain performed Action of a Feature.
   *
   * @param feature The name of the Feature.
   * @param action The name of the Action.
   * @param dataType The type of the data i.e. The entity name.
   *
   * @since 2.0
   */
  public void checkDataAccess(
    String feature, String action, String dataType)
  {
    if (feature == null)
      throw new NullPointerException("Feature can't be null!");
    if (action == null)
      throw new NullPointerException("Action can't be null!");

    checkPermission(new AccessPermission(feature, action, dataType, null));
  }

  /**
   * Checks the AccessPermission for accessing a certain piece of
   * data of a certain type during a certain performed Action of a Feature.
   *
   * @param feature The name of the Feature.
   * @param action The name of the Action.
   * @param entity The piece of data to check access for.
   * @since 2.0
   */
  public void checkDataAccess(
    String feature, String action, IEntity entity)
  {
    if (feature == null)
      throw new NullPointerException("Feature can't be null!");
    if (action == null)
      throw new NullPointerException("Action can't be null!");

  }
}