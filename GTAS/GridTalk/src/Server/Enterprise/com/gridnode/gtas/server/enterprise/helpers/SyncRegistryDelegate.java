/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SyncRegistryDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 8  2003    Neo Sok Lay         Created
 * Oct 06 2003    Neo Sok Lay         Construct message service name base on
 *                                    pattern before comparison.
 * Oct 17 2005    Neo Sok Lay         Change IPublicRegistryManager to
 *                                    IPublicRegistryManagerObj.                                   
 */
package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.enterprise.exceptions.SynchronizationFailException;
import com.gridnode.gtas.server.enterprise.model.SearchedBusinessEntity;
import com.gridnode.gtas.server.enterprise.sync.handlers.SyncBizEntityDelegate;
import com.gridnode.gtas.server.enterprise.sync.models.SyncBusinessEntity;
import com.gridnode.pdip.app.bizreg.facade.ejb.IPublicRegistryManagerObj;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.bizreg.pub.IPublicRegistryManager;
import com.gridnode.pdip.app.bizreg.pub.model.*;
import com.gridnode.pdip.app.channel.model.ChannelInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This delegate class handles synchronization with a specified registry.
 * 
 * @author Neo Sok Lay
 * @since GT 2.2
 */
public class SyncRegistryDelegate implements ISyncRegistryKeys
{
  private String[] _connInfo = new String[IConnectionInfo.NUM_FIELDS];
  private transient IPublicRegistryManagerObj _pubMgr;

  /**
   * Constructs a SyncRegistryDelegate instance to synchronize with a registry specified
   * by <code>queryUrl</code>.
   * 
   * @param queryUrl Query Url of the registry to synchronize with
   * @throws Exception Error accessing the PublicRegistryManager.
   */
  public SyncRegistryDelegate(String queryUrl) throws Exception
  {
    _connInfo[IConnectionInfo.FIELD_QUERY_URL] = queryUrl;
    _connInfo[IConnectionInfo.FIELD_PUBLISH_URL] = "";
    _pubMgr = ServiceLookupHelper.getPublicRegistryMgr();    
  }

  /**
   * Synchronize the searched OrganizationInfo as BusinessEntity in this GridTalk.
   * 
   * @param searchedBe Temporary representation of the OrganizationInfo in a structure similar
   * to BusinessEntity (SearchedBusinessEntity).
   * @param orgInfo The OrganizationInfo searched from the registry.
   * @throws Exception Error during the synchronization.
   */
  public void synchronize(SearchedBusinessEntity searchedBe, OrganizationInfo orgInfo)
    throws SynchronizationFailException
  {
    try
    {
      Logger.debug("[SyncRegistryDelegate.synchronize] Start Synchronizing "+orgInfo.getKey());

      BusinessEntity addBe = new BusinessEntity();
      ActionHelper.copyEntityFields(searchedBe, SEARCHED_BE_FIELDS, addBe, BE_FIELDS);

      SyncBusinessEntity syncBe = new SyncBusinessEntity(
                                    addBe, (ChannelInfo[])searchedBe.getChannels().toArray(new ChannelInfo[0]));

      /**@todo check enterpriseid whether isActive partner. 
       * if active, add master channel to BE, set GT_PARTNER = true
       * otherwise, GT_PARTNER = false  
       */
      SyncBizEntityDelegate syncDelegate = new SyncBizEntityDelegate();
      syncDelegate.startSynchronize(syncBe, SyncBusinessEntity.MODE_CAN_DELETE | SyncBusinessEntity.MODE_IS_PARTNER);

      Long beUid = (Long)syncBe.getBusinessEntity().getKey();
      ArrayList channelUids = syncBe.getChannelUIDs();

      // Add registryobjectmappings
      // - for OrganizationInfo vs BusinessEntity
      orgInfo.setProprietaryObjectKey(String.valueOf(beUid));
      orgInfo.setProprietaryObjectType(BusinessEntity.ENTITY_NAME);
      addRegistryObject(orgInfo);
    
      // - for ServiceInfo 
      String msgServiceName = RegistryInfoConvertor.formatPattern(
                                IServiceIdentifierKeys.MSG_SERVICE_NAME_PATTERN, 
                                new String[]{syncBe.getBusinessEntity().getBusEntId()});
      List services = orgInfo.getServices();
      for (Iterator i = services.iterator(); i.hasNext();)
      {
        ServiceInfo serviceInfo = (ServiceInfo) i.next();
        // only process Messaging service
        if (msgServiceName.equals(serviceInfo.getName()))
          synchronize(serviceInfo, channelUids);
      }
      
      Logger.debug("[SyncRegistryDelegate.synchronize] Successfully Synchronized "+orgInfo.getKey());
    }
    catch (Exception e)
    {
      Logger.warn("[SyncRegistryDelegate.synchronize] " +
        "Fail to synchronize contents of BusinessEntity and related resources!", e);
      throw SynchronizationFailException.syncContentFailed();
    }
    
  }
  
  /**
   * Synchronize the searched ServiceInfo and its BindingInfo(s) as ChannelInfo(s) 
   * in this GridTalk. Only MessagingService type is synchronized at the moment.
   * 
   * @param serviceInfo The ServiceInfo to synchronize.
   * @param channelUids The proprietaryObjectKeys for the BindingInfo(s). The channelUids
   * are specified in natural order of the BindingInfo(s).
   * @throws Exception Error synchronizing the ServiceInfo and BindingInfo(s).
   */
  private void synchronize(ServiceInfo serviceInfo, ArrayList channelUids) throws Exception
  {
    serviceInfo.setProprietaryObjectKey(serviceInfo.getOrganizationKey());
    serviceInfo.setProprietaryObjectType(IServiceIdentifierKeys.MSG_SERVICE_PROPRIETARY_OBJ_TYPE);
    addRegistryObject(serviceInfo);

    // - for BindingInfo vs ChannelInfo
    Object[] bindings = serviceInfo.getBindings().toArray();
    Arrays.sort(bindings); // make sure match back the same binding
      
    BindingInfo bindingInfo;
    for (int j=0; j<bindings.length; j++)
    {
      bindingInfo = (BindingInfo)bindings[j];
        
      // match corresponding channelinfo
      bindingInfo.setProprietaryObjectKey(channelUids.get(j).toString());
      bindingInfo.setProprietaryObjectType(ChannelInfo.ENTITY_NAME);
      addRegistryObject(bindingInfo);
    }
  }
  
  /**
   * Adds registry object to mapping table.
   * 
   * @param regInfo The registry information object to add
   * @throws Exception Error adding the mapping.
   */
  private void addRegistryObject(IRegistryInfo regInfo) throws Exception
  {
    _pubMgr.addRegistryObjectMapping(regInfo, _connInfo, false);
  }
}
