/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DAOHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 5, 2004    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.dao.EntityDAOFactory;
import com.gridnode.pdip.framework.db.dao.IEntityDAO;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.exceptions.DuplicateEntityException;

/**
 * Helper class for DAO of entities in Channel module
 * 
 * @author Neo Sok Lay
 * @since GT 2.2.10
 */
public class DAOHelper
{
  private static final DAOHelper _self = new DAOHelper();

  private DAOHelper()
  {

  }

  /**
   * Get the singleton instance of DAOHelper
   * @return Singleton instance of DAOHelper
   */
  public static DAOHelper getInstance()
  {
    return _self;
  }

  /**
   * Check duplicate entity base on RefID and Name field of the entity.
   * 
   * @param entity The entity to check
   * @param refIdField The RefID field ID
   * @param nameField The Name field ID
   * @param checkKey Whether to include the Key in the checking. This should be <b>true<b> when
   * an update is requested -- making sure the specified entity is not falsely reported as a duplicate of
   * itself.
   * @param dao The DAO to use to query for existence
   * @throws Exception Error checking duplicate with the database
   */
  private void checkDuplicate(
    IEntity entity,
    Number refIdField,
    Number nameField,
    boolean checkKey,
    IEntityDAO dao)
    throws Exception
  {
    String refId = (String) entity.getFieldValue(refIdField);
    String name = (String) entity.getFieldValue(nameField);

    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(
      null,
      refIdField,
      filter.getEqualOperator(),
      refId,
      false);
    filter.addSingleFilter(
      filter.getAndConnector(),
      nameField,
      filter.getEqualOperator(),
      name,
      false);

    if (checkKey)
      filter.addSingleFilter(
        filter.getAndConnector(),
        entity.getKeyId(),
        filter.getNotEqualOperator(),
        entity.getKey(),
        false);

    if (dao.getEntityCount(filter) > 0)
      throw new DuplicateEntityException(
        entity.getEntityName()
          + " ["
          + entity.getEntityDescr()
          + "] already exists!");
  }

  /**
   * Check duplicate for ChannelInfo entity
   * 
   * @param channelInfo The ChannelInfo entity
   * @param checkKey Whether to include the Key in the checking
   * @throws Exception Error checking duplicate with the database
   */
  public void checkDuplicate(ChannelInfo channelInfo, boolean checkKey)
    throws Exception
  {
    checkDuplicate(
      channelInfo,
      ChannelInfo.REF_ID,
      ChannelInfo.NAME,
      checkKey,
      getChannelDAO());
  }

  /**
   * Check duplicate for SecurityInfo entity
   * 
   * @param secInfo The SecurityInfo entity
   * @param checkKey Whether to include the Key in the checking
   * @throws Exception Error checking duplicate with the database
   */
  public void checkDuplicate(SecurityInfo secInfo, boolean checkKey)
    throws Exception
  {
    checkDuplicate(
      secInfo,
      SecurityInfo.REF_ID,
      SecurityInfo.NAME,
      checkKey,
      getSecurityDAO());
  }

  /**
   * Check duplicate for PackagingInfo entity
   * 
   * @param packagingInfo The PackagingInfo entity
   * @param checkKey Whether to include the Key in the checking
   * @throws Exception Error checking duplicate with the database
   */
  public void checkDuplicate(PackagingInfo packagingInfo, boolean checkKey)
    throws Exception
  {
    checkDuplicate(
      packagingInfo,
      PackagingInfo.REF_ID,
      PackagingInfo.NAME,
      checkKey,
      getPackagingDAO());

  }

  /**
   * Check duplicate for CommInfo entity
   * 
   * @param commInfo The CommInfo entity
   * @param checkKey Whether to include the Key in the checking
   * @throws Exception Error checking duplicate with the database
   */
  public void checkDuplicate(CommInfo commInfo, boolean checkKey)
    throws Exception
  {
    checkDuplicate(
      commInfo,
      CommInfo.REF_ID,
      CommInfo.NAME,
      checkKey,
      getCommunicationDAO());
  }

  /**
   * Get the DAO for ChannelInfo
   * @return DAO instance for ChannelInfo
   */
  private IEntityDAO getChannelDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(ChannelInfo.ENTITY_NAME);
  }

  /**
   * Get the DAO for SecurityInfo
   * @return DAO instance for SecurityInfo
   */
  private IEntityDAO getSecurityDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(SecurityInfo.ENTITY_NAME);
  }

  /**
   * Get the DAO for CommInfo
   * @return DAO instance for CommInfo
   */
  private IEntityDAO getCommunicationDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(CommInfo.ENTITY_NAME);
  }

  /**
   * Get the DAO for PackagingInfo
   * @return DAO instance for PackagingInfo
   */
  private IEntityDAO getPackagingDAO()
  {
    return EntityDAOFactory.getInstance().getDAOFor(PackagingInfo.ENTITY_NAME);
  }
}
