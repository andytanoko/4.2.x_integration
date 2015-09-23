/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2009 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ArchiveManagerBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 10, 2009   Ong Eu Soon         Created
 * Apr 12 2009    Tam Wei Xiang       #122 - Add method to create/update 
 *                                           scheduledArchiveTask
 */
package com.gridnode.gtas.server.dbarchive.facade.ejb;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.gridnode.gtas.server.dbarchive.helpers.ArchiveMetaInfoEntityHandler;
import com.gridnode.gtas.server.dbarchive.helpers.Logger;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.gtas.server.document.model.DocumentType;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;


public class ArchiveManagerBean implements SessionBean //Stateless Session Bean
{
	transient private SessionContext _sessionCtx = null;
	public void setSessionContext(SessionContext sessionCtx)
  {
    _sessionCtx = sessionCtx;
  }

  public void ejbCreate() throws CreateException
  {
  }

  public void ejbRemove()
  {
  }

  public void ejbActivate()
  {
  }

  public void ejbPassivate()
  {
  }
	
	/**
	 * Locate a collection of ArchiveMEtaInfo obj based on the given search criteria
	 * @param filter it contain a domain filter which take in list of archive UID.
	 * @return
	 * @throws FindEntityException
	 * @throws SystemException
	 */
	private Collection findArchiveEntityList(IDataFilter filter)
	 				throws FindEntityException, SystemException
	{
		
		Collection archiveMetaInfoList = null;
    try
    {
    	 archiveMetaInfoList = getArchiveMetaInfoEntityHandler().getEntityByFilterForReadOnly(filter);
    }
    catch (ApplicationException ex)
    {
      Logger.warn("[ArchiveManagerBean.findArchiveEntityList] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      Logger.warn("[ArchiveManagerBean.findArchiveEntityList] System Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger.warn("[ArchiveManagerBean.findArchiveEntityList] Error ", ex);
      throw new SystemException(
        "[ArchiveManagerBean.findArchiveEntityList] Error ",
        ex);
    }
		
		return archiveMetaInfoList;
		
	}

	/**
	 * Locate a list of Archive MetaInfo UID based on the given search criteria
	 * @param filter it contain a domain filter which take in list of archive UID.
	 * @return a list of Archive MetaInfo UID  or emptyList if we can't find the associate archive meta info
	 * @throws Exception
	 * @throws RemoteException
	 */
	public Collection getArchive(IDataFilter filter)
				 throws Exception, RemoteException
	{
	  Collection archiveList = new ArrayList();
		try
		{
		   archiveList = this.findArchiveEntityList(filter);

		}
		catch(FindEntityException ex)
		{
			Logger.warn("[ArchiveManagerBean.getArchive] BL Exception", ex);
			throw new FindEntityException(ex);
		}
		catch(SystemException ex)
		{
			Logger.warn("[ArchiveManagerBean.getArchive] System Exception", ex);
			throw ex;
		}
		catch(Throwable ex)
		{
			Logger.warn("[ArchiveManagerBean.getArchive] Error ", ex);
      throw new SystemException(
      		"ArchiveManagerBean.getArchive(uid) Error ",ex);
		}
		return archiveList;
	}
	/**
   * Locate a list of Archive MetaInfo UID  based on the given search criteria
   * @param filter it contain a given search criteria.
   * @return a list of Arive MetaInfo UID or emptyList if we can't find the associate archive meta info
   * @throws Exception
   * @throws RemoteException
   */
  public Collection getArchiveUIDs(IDataFilter filter)
         throws Exception, RemoteException
  {
    try
    {
       return getArchiveMetaInfoEntityHandler().getKeyByFilterForReadOnly(filter);
      
    }
    catch(FindEntityException ex)
    {
      Logger.warn("[ArchiveManagerBean.getArchiveList] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch(SystemException ex)
    {
      Logger.warn("[ArchiveManagerBean.getArchiveList] System Exception", ex);
      throw ex;
    }
    catch(Throwable ex)
    {
      Logger.warn("[ArchiveManagerBean.getArchiveList] Error ", ex);
      throw new SystemException(
          "ArchiveManagerBean.getArchiveList(uid) Error ",ex);
    }
  }
  public void deleteArchive(Long keyId) throws Exception, RemoteException{
    try
    {
      getArchiveMetaInfoEntityHandler().remove(keyId);
    }
    catch(FindEntityException ex)
    {
      Logger.warn("[ArchiveManagerBean.deleteArchive] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch(SystemException ex)
    {
      Logger.warn("[ArchiveManagerBean.deleteArchive] System Exception", ex);
      throw ex;
    }
    catch(Throwable ex)
    {
      Logger.warn("[ArchiveManagerBean.deleteArchive] Error ", ex);
      throw new SystemException(
          "ArchiveManagerBean.deleteArchive(uid) Error ",ex);
    }
  }
  public  ArchiveMetaInfo getArchive(Long uid) throws Exception, RemoteException{
    try
    {
      return getArchiveMetaInfoEntityHandler().findByUID(uid);
    }
    catch(FindEntityException ex)
    {
      Logger.warn("[ArchiveManagerBean.getArchive] BL Exception", ex);
      throw new FindEntityException(ex);
    }
    catch(SystemException ex)
    {
      Logger.warn("[ArchiveManagerBean.getArchive] System Exception", ex);
      throw ex;
    }
    catch(Throwable ex)
    {
      Logger.warn("[ArchiveManagerBean.getArchive] Error ", ex);
      throw new SystemException(
          "ArchiveManagerBean.getArchive(uid) Error ",ex);
    }
  }
  
  public Long createArchiveMetaInfo(ArchiveMetaInfo metaInfo)
     throws CreateEntityException,DuplicateEntityException, SystemException
  {
    
    return getArchiveMetaInfoEntityHandler().createArchiveMetaInfo(metaInfo);
  }
  
  /**
   * Update the archive task meta info entity
   * @param metaInfo
   */
  public void updateArchiveMetaInfo(ArchiveMetaInfo metaInfo) throws UpdateEntityException, SystemException
  {
    try
    {
      getArchiveMetaInfoEntityHandler().updateArchiveMetaInfo(metaInfo);
    }
    catch (EntityModifiedException ex)
    {
      Logger.warn("[ArchiveManagerBean.updateArchiveMetaInfo] BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable ex)
    {
      Logger.warn("[ArchiveManagerBean.updateArchiveMetaInfo] Error ", ex);
      throw new SystemException(
        "ArchiveManagerBean.updateArchiveMetaInfo Error ",
        ex);
    }

    Logger.log("[ArchiveManagerBean.updateArchiveMetaInfo] Exit");
  }
  
	//******************** EntityHandler *******************************
	private ArchiveMetaInfoEntityHandler getArchiveMetaInfoEntityHandler()
	{
		return ArchiveMetaInfoEntityHandler.getInstance();
	}
	
}
