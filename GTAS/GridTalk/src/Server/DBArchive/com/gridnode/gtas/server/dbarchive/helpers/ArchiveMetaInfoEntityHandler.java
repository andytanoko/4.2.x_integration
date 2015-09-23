/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: ArchiveMetaInfoEntityHandler.java
 * 
 **************************************************************************** 
 * Date           Author              Changes
 ****************************************************************************
 * ????           Ong Eu Soon         Created
 * Apr 12 2009    Tam Wei Xiang       #122
 *                                    1) Add method for update.
 *                                    2) Modified createArchiveMetaInfo to not including
 *                                    update logic in case the record is exist 
 * 
 */

package com.gridnode.gtas.server.dbarchive.helpers;

import java.util.Collection;

import javax.ejb.CreateException;

import com.gridnode.gtas.server.dbarchive.entities.ejb.IArchiveMetaInfoLocalHome;
import com.gridnode.gtas.server.dbarchive.entities.ejb.IArchiveMetaInfoLocalObj;
import com.gridnode.gtas.server.dbarchive.model.ArchiveMetaInfo;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.dao.EntityDAOImpl;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;


/**
 * 
 * 
 * Ong Eu Soon
 * 
 * @version 1.0
 * @since 1.0
 */
public class ArchiveMetaInfoEntityHandler extends LocalEntityHandler
{
  private ArchiveMetaInfoEntityHandler()
  {
    super(ArchiveMetaInfo.ENTITY_NAME);
  }

  /**
   * Get an instance of a ArchiveMetaInfoEntityHandler.
   */
  public static ArchiveMetaInfoEntityHandler getInstance()
  {
    ArchiveMetaInfoEntityHandler handler = null;

    if (EntityHandlerFactory
        .hasEntityHandlerFor(ArchiveMetaInfo.ENTITY_NAME, true))
    {
      handler = (ArchiveMetaInfoEntityHandler) EntityHandlerFactory
          .getHandlerFor(ArchiveMetaInfo.ENTITY_NAME, true);
    }
    else
    {
      handler = new ArchiveMetaInfoEntityHandler();
      EntityHandlerFactory
          .putEntityHandler(ArchiveMetaInfo.ENTITY_NAME, true, handler);
    }
    
    return handler;
  }

  /**
   * This create method will check whether the record we are going to insert is
   * existing in DB table or not. IF it doesn't, it will insert the doc info.
   * 
   * @param metaInfo
   *          The document metainfo that we will insert to DB
   * @param doc
   *          The DocInfo object
   */
  public Long createArchiveMetaInfo(ArchiveMetaInfo metaInfo) 
    throws CreateEntityException,DuplicateEntityException, SystemException
  {
    try
    {
      // 17052006
      // super.create(metaInfo); can be quite slow when there is many record
      // exist in DB

      EntityDAOImpl docMetaDAO = (EntityDAOImpl) super.getDAO();
      Long uniqueKey = docMetaDAO.createNewKey(false);
      metaInfo.setKey(uniqueKey);

      super.create(metaInfo, Boolean.TRUE);
      return (Long)metaInfo.getKey();
    }
    catch (CreateException ex)
    {
      Logger
          .warn(
                "[ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo] Exception ",
                ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (DuplicateEntityException ex)
    {
      Logger.warn("[ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo] BL Exception", ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      Logger
          .warn(
                "[ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo] Error ",
                ex);
      throw new SystemException(
                                "ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo Error ",
                                ex);
    }
  }

  public void updateArchiveMetaInfo(ArchiveMetaInfo metaInfo) throws Exception
  {

      try
      {
        ArchiveMetaInfo existingInfo = findByUID(metaInfo.getUId());
        metaInfo.setVersion(existingInfo.getVersion());
        metaInfo.setKey(existingInfo.getKey());
        metaInfo.setCanDelete(existingInfo.canDelete());
        super.update(metaInfo);
        return;
      }
      catch (EntityModifiedException ex1)
      {
        Logger
            .warn(
                  "[ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo] App Exception ",
                  ex1);
        throw new UpdateEntityException(ex1.getMessage());
      }
      catch (Throwable e)
      {
        Logger
            .warn(
                  "[ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo] Error ",
                  e);
        throw new SystemException(
                                  "ArchiveMetaInfoEntityHandler.createDocumentSchedulerMetaInfo Error ",
                                  e);
      }
  }
  
  /**
   * 19012006: The uniqueness of ArchiveMetaInfo is archiveID This
   * checkDuplicate method is used by createDocumentSchedulerMetaInfo
   * 
   * @param jobID
   * @param archiveID
   * @throws Exception
   */
  public void checkDuplicate(String archiveID) throws Exception
  {
    
    Logger.debug("ArchiveMetaInfoEntityHandler: Archive ID - " +archiveID);
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ArchiveMetaInfo.ARCHIVE_ID, filter
        .getEqualOperator(), archiveID, false);
   
    Logger.debug("ArchiveMetaInfoEntityHandler: Filter - " +filter.toString());
     if(super.getEntityCount(filter) > 0) { throw new DuplicateEntityException(
                                                                                 "[ArchiveMetaInfoEntityHandler.checkDuplicate] Entry with Archive ID : "
                                                                                     + archiveID
                                                                                     + " has already exist in table archive_meta_info"); }
    
  }

  /**
   * Locate the document meta info obj based on its UID (PK for table
   * archive_meta_info)
   * 
   * @param UID
   *          PK for table archive_meta_info
   * @return document meta info obj
   * @throws Exception
   */
  public ArchiveMetaInfo findByUID(Long UID) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ArchiveMetaInfo.UID, filter
        .getEqualOperator(), UID, false);

    Collection result = super.getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty()) { return null; }
    return (ArchiveMetaInfo) (result.iterator()).next();
  }

  /**
   * Find the ArchiveMetaInfo based on archiveID which is a unique key
   * for table archive_meta_info.
   * 
   * @param archiveID
   *          Archive ID
   * @return the ArchiveMetaInfo object
   */
  public ArchiveMetaInfo findByArchiveID(String archiveID) throws Exception
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, ArchiveMetaInfo.ARCHIVE_ID, filter
        .getEqualOperator(), archiveID, false);
    Collection result = super.getEntityByFilterForReadOnly(filter);
    if (result == null || result.isEmpty()) { return null; }
    return (ArchiveMetaInfo) (result.iterator()).next();
  }

  /**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   * 
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IArchiveMetaInfoLocalObj.class;
  }

  @Override
  protected Class getHomeInterfaceClass() throws Exception
  {
    return IArchiveMetaInfoLocalHome.class;
  }
}
