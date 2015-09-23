/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSearchRegistryQueryManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-09-15     Daniel D'Cotta      Created
 * 2003-10-10     Neo Sok Lay         Add newSearchedChannelInfo() method.
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTSearchRegistryQueryManager extends IGTManager
{
  public IGTSearchRegistryCriteriaEntity  newSearchRegistryCriteria() throws GTClientException;
  public IGTSearchRegistryQueryEntity     newSearchRegistryQuery()    throws GTClientException;
  public IGTPublishBusinessEntityEntity   newPublishBusinessEntity()  throws GTClientException;
  public IGTSearchedBusinessEntityEntity  newSearchedBusinessEntity() throws GTClientException;
  public IGTSearchedChannelInfoEntity     newSearchedChannelInfo()    throws GTClientException;

  public void publishBusinessEntity(Collection businessEntitiyUids, String registryConnectInfoName) throws GTClientException;
  public void configureBizEntity(Long searchId, Collection uuids) throws GTClientException;
  public Collection getMessagingStandards() throws GTClientException;
}