/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDocumentManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10, 2009   Ong Eu Soon         Created
 * Apr 12 2009    Tam Wei Xiang       #122 - Add method to create/update 
 *                                           scheduledArchiveTask
 */

package com.gridnode.gtas.server.dbarchive.facade.ejb;


import javax.ejb.EJBObject;

import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import java.rmi.RemoteException;
import java.util.Collection;


public interface IArchiveManagerObj extends EJBObject
{
  public  Collection getArchive(IDataFilter filter) throws Exception, RemoteException;
  public Collection getArchiveUIDs(IDataFilter filter) throws Exception, RemoteException;
  public void deleteArchive(Long keyId) throws Exception, RemoteException;
  public  ArchiveMetaInfo getArchive(Long uid) throws Exception, RemoteException;
  public Long createArchiveMetaInfo(ArchiveMetaInfo metaInfo) throws CreateEntityException,DuplicateEntityException, SystemException, RemoteException;
  public void updateArchiveMetaInfo(ArchiveMetaInfo metaInfo) throws UpdateEntityException, SystemException, RemoteException;
}
