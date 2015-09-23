/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTRoleManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-07     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTRoleManager extends IGTManager
{
  //public IGTRoleEntity getRoleByRole(String role) throws GTClientException;
  public IGTRoleEntity getRoleByUID(long uid) throws GTClientException;
  public Collection getRolesForUser(IGTUserEntity user) throws GTClientException;
  public void addRoleToUser(long roleUid, long userUid) throws GTClientException;
  public void addRoleToUser(IGTRoleEntity role, IGTUserEntity user) throws GTClientException;
  public void removeRoleFromUser(long roleUid, long userUid) throws GTClientException;
  public void removeRoleFromUser(IGTRoleEntity roleUid, IGTUserEntity userUid) throws GTClientException;
}