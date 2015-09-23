/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTRestoreArchiveEntity.java.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 22/05/2003     Andrew Hill         Created 
 */
package com.gridnode.gtas.client.ctrl;

/*
 * I am modelling this as a seperate entity for now to speed development. Later on when
 * DocumentArchive is a persistent entity all this stuff can be merged into it.
 */
public interface IGTRestoreDocument extends IGTEntity
{
  public static final Number ARCHIVE_FILE = new Integer(-10);  
}
