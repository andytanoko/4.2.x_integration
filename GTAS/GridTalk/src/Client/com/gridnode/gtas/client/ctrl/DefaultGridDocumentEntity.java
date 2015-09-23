/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultGridDocumentEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-22     Andrew Hill         Created
 * 2003-01-30     Andrew Hill         implement canEdit()
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

class DefaultGridDocumentEntity extends AbstractGTEntity implements IGTGridDocumentEntity
{
  public boolean canEdit() throws GTClientException
  { //20030130AH
    return(FOLDER_IMPORT.equals( getFieldValue(FOLDER) ) );
  }
}