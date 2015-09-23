/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FailedDeletionEntityReference.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-09     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


class FailedDeletionEntityReference extends SimpleEntityReference implements IGTFailedDeletionEntityReference
{
  private IGTMheReference _dependantEntities;
  private ResponseException _failureException;
  
  FailedDeletionEntityReference()
  {
    super();
  }

  public IGTMheReference getDependantEntities()
  {
    return _dependantEntities;
  }

  public void setDependantEntities(IGTMheReference reference)
  {
    _dependantEntities = reference;
  }

  public ResponseException getFailureException()
  {
    return _failureException;
  }

  public void setFailureException(ResponseException exception)
  {
    _failureException = exception;
  }

}
