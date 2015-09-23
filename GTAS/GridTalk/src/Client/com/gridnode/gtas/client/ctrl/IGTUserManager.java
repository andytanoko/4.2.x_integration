/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTUserManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-11-26     Andrew Hill         getUserBusinessEntities()
 * 2005-03-18     Andrew Hill         updatePasswordOnly()
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTUserManager extends IGTManager
{
  public IGTUserEntity getUserByID(String userID) throws GTClientException;
  public IGTUserEntity getUserByUID(long uid) throws GTClientException;
  public void update(IGTEntity entity, IUserAdminOptions options) throws GTClientException;

  public Collection getUserBusinessEntities(long userUid) throws GTClientException;
  public Collection getUserBusinessEntities(Long userUid) throws GTClientException;
  
  /**
   * Fires a ChangePassword event to the bTier based on the old and new passwords in
   * the entity. This is for use by the user account screen
   * @param entity
   * @param oldPassword old password for security reasons
   * @throws GTClientException
   */
  public void updatePasswordOnly(IGTUserEntity entity, String oldPassword) throws GTClientException; //20050318AH
}