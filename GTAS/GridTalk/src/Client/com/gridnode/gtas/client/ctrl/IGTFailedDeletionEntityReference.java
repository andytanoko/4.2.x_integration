/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTFailedDeletionEntityReference.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-07     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


public interface IGTFailedDeletionEntityReference extends IGTEntityReference
{
  public ResponseException getFailureException();
  public IGTMheReference getDependantEntities();
}
