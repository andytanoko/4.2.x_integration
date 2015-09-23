/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMetaInfoObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 02 2005    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.meta;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.framework.exceptions.SystemException;

/**
 * Remote interface for MetaInfoBean. This replaces IEntityMetaInfoHome for
 * retrieving entitymetainfo, and IFieldMetaInfoHome for retrieving fieldmetainfo.
 * 
 * @author i00107
 *
 */
public interface IMetaInfoObj extends EJBObject
{
	/**
	 * Find the entity meta info for the specified entity object
	 * @param objectName The unique object name of the entity object, the fully qualified class name
	 * @return The entity meta info found, together with the related field metainfos
	 * @throws SystemException If the EMI is not found for the specified objectName
	 * @throws RemoteException
	 */
	public EntityMetaInfo findByObjectName(String objectName) throws SystemException, RemoteException;
	
	/**
	 * Find the entity meta info the specified entity object
	 * @param entityName The name of the entity object, an arbituary name given to the entity object. Highly recommended to
	 * be unique among all entity types.
	 * @return The entity meta info found, together with the related field meta infos.
	 * @throws SystemException If the EMI is not found for the specified entityName
	 * @throws RemoteException
	 */
	public EntityMetaInfo findByEntityName(String entityName) throws SystemException, RemoteException;
	
	/**
	 * Get all the entity meta infos in the system
	 * @return Collection of EntityMetaInfo, together with their related field meta infos.
	 * @throws SystemException Error executing the retrieval.
	 * @throws RemoteException
	 */
	public Collection findAllEntityMetaInfo() throws SystemException, RemoteException;
	
	/**
	 * Find the FieldMetaInfo(s) for the specified label
	 * @param label The label to search for field metainfos
	 * @return Collection of FieldMetaInfo(s) having the specified label
	 * @throws SystemException Error executing the retrieval.
	 * @throws RemoteException
	 */
	public Collection findFieldMetaInfoByLabel(String label) throws SystemException, RemoteException;
	
}
