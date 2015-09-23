/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ResourceChangeDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2005   i00107             Created
 */

package com.gridnode.gtas.server.enterprise.helpers;

import com.gridnode.gtas.server.channel.helpers.MasterChannelCreator;
import com.gridnode.gtas.server.enterprise.model.IResourceTypes;
import com.gridnode.pdip.app.bizreg.model.BusinessEntity;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.app.partner.model.Partner;
import com.gridnode.pdip.app.user.model.UserAccount;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.log.FacadeLogger;

/**
 * Delegate for ResourceChangeListenerMDBean
 * 
 * @author i00107
 *
 */
public class ResourceChangeDelegate
{

  /**
   * Handling method for the event when a BusinessEntity has been deleted (physically).
   * @param entity The BusinessEntity deleted.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleBusinessEntityDeleted(IEntity entity)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleBusinessEntityDeleted";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      BusinessEntity be = (BusinessEntity)entity;
      ServiceLookupHelper.getEnterpriseHierarchyMgr().onRemoveBizEntity(
        (Long)be.getKey());
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handling method for the event when a BusinessEntity has been updated.
   * @param entity The BusinessEntity updated.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleBusinessEntityUpdated(IEntity entity)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleBusinessEntityUpdated";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      BusinessEntity be = (BusinessEntity)entity;

      logger.logMessage(methodName, params, "isPartner="+be.isPartner()+", state="+be.getState());
      if (!be.isPartner() && be.getState() != BusinessEntity.STATE_DELETED)
      {
        SyncResourceDelegate.synchronizeResource(IResourceTypes.BUSINESS_ENTITY, new Long(be.getUId()));
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handling method for the event when a BusinessEntity has been created.
   * @param entity The BusinessEntity created.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleBusinessEntityCreated(IEntity entity)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleBusinessEntityCreated";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);
      BusinessEntity be = (BusinessEntity)entity;

      if (!be.isPartner())
      {
        SyncResourceDelegate.synchronizeResource(IResourceTypes.BUSINESS_ENTITY, new Long(be.getUId()));
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handling method for the event when a BusinessEntity has been created.
   * @param entity The BusinessEntity created.
   * @throws UpdateEntityException
   * @throws SystemException
   */
//  protected void handleBusinessEntityCreated(IEntity entity)
//    throws UpdateEntityException, SystemException
//  {
//    FacadeLogger logger = Logger.getListenerFacadeLogger();
//    String methodName   = "handleBusinessEntityCreated";
//    Object[] params     = new Object[] {entity};
//
//    try
//    {
//      logger.logEntry(methodName, params);
//
//      BusinessEntity be = (BusinessEntity)entity;
//
//      if (isGtasBe(be)) // only associate if Gtas Be.
//      {
//        GridNode gn = ServiceLookupHelper.getGridNodeManager().findGridNodeByID(
//                        be.getEnterpriseId());
//
//        //ServiceLookupHelper.getEnterpriseHierarchyMgr().(be);
//      }
//    }
//    catch (Throwable ex)
//    {
//      logger.logUpdateError(methodName, params, ex);
//    }
//    finally
//    {
//      logger.logExit(methodName, params);
//    }
//  }
//
//  private boolean isGtasBe(BusinessEntity be)
//  {
//    return (be.getEnterpriseId() != null &&
//           !"".equals(be.getEnterpriseId().trim()));
//  }

  /**
   * Handling method for the event when a UserAccount has been deleted (physically).
   * @param entity The UserAccount deleted.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleUserDeleted(IEntity entity)
      throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleUserDeleted";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      UserAccount acct = (UserAccount)entity;
      ServiceLookupHelper.getEnterpriseHierarchyMgr().onRemoveUser(
        (Long)acct.getKey());
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handling method for the event when a Partner has been updated. Re-direct to
   * EnterpriseHierarchyManager to onRemovePartner() if the Partner state is changed to DELETED. 
   * Otherwise, this method does nothing and returns.
   * 
   * @param entity The Partner updated
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handlePartnerUpdated(IEntity entity)
    throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handlePartnerUpdated";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);
      Partner partner = (Partner)entity;

      if (partner.getState() == Partner.STATE_DELETED)
      {
        ServiceLookupHelper.getEnterpriseHierarchyMgr().onRemovePartner(
        (Long)partner.getKey());
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }      
  }
  
  /**
   * Handling method for the event when a Partner has been deleted (physically).
   * @param entity The Partner deleted.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handlePartnerDeleted(IEntity entity)
      throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handlePartnerDeleted";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      Partner partner = (Partner)entity;
      ServiceLookupHelper.getEnterpriseHierarchyMgr().onRemovePartner(
        (Long)partner.getKey());
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handling method for the event when a ChannelInfo has been deleted.
   * @param entity The ChannelInfo deleted.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleChannelDeleted(IEntity entity)
      throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleChannelDeleted";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      ChannelInfo channel = (ChannelInfo)entity;
      ServiceLookupHelper.getEnterpriseHierarchyMgr().onRemoveChannel(
        (Long)channel.getKey());
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Handling method for the event when a ChannelInfo has been created.
   * @param entity The ChannelInfo created.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleChannelCreated(IEntity entity)
      throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleChannelCreated";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      ChannelInfo channel = (ChannelInfo)entity;

      if (channel.isMaster() && !channel.isPartner()) //own master channel
      {
        ServiceLookupHelper.getEnterpriseHierarchyMgr().onCreateMasterChannel(
          (Long)channel.getKey());
      }
      else if (channel.isMaster() && channel.isPartner()) //partner master channel
      {
        //ensure master cert is used
        SecurityInfo secInfo = channel.getSecurityProfile();
        Long myMasterCert = new Long(MasterChannelCreator.getMyMasterCert().getUId());
        if (!SecurityInfo.SIGNATURE_TYPE_DEFAULT.equals(secInfo.getSignatureType()) ||
            !SecurityInfo.DIGEST_ALGORITHM_MD5.equals(secInfo.getDigestAlgorithm()) ||
            !myMasterCert.equals(secInfo.getSignatureEncryptionCertificateID()))
        {
          secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_DEFAULT);
          secInfo.setDigestAlgorithm(SecurityInfo.DIGEST_ALGORITHM_MD5);
          secInfo.setSignatureEncryptionCertificateID(new Long(
            MasterChannelCreator.getMyMasterCert().getUId()));
          ServiceLookupHelper.getChannelManager().updateSecurityInfo(secInfo);  
        }
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }
  
  /**
   * Handling method for the event when a ChannelInfo has been updated.
   * @param entity The ChannelInfo updated.
   * @throws UpdateEntityException
   * @throws SystemException
   */
  public void handleChannelUpdated(IEntity entity)
  throws UpdateEntityException, SystemException
  {
    FacadeLogger logger = Logger.getListenerFacadeLogger();
    String methodName   = "handleChannelUpdated";
    Object[] params     = new Object[] {entity};

    try
    {
      logger.logEntry(methodName, params);

      ChannelInfo channel = (ChannelInfo)entity;

      if (channel.isMaster() && channel.isPartner()) //partner master channel
      {
        //ensure master cert is used
        SecurityInfo secInfo = channel.getSecurityProfile();
        secInfo = ServiceLookupHelper.getChannelManager().getSecurityInfo((Long)secInfo.getKey());
        Long myMasterCert = new Long(MasterChannelCreator.getMyMasterCert().getUId());
        if (!SecurityInfo.SIGNATURE_TYPE_DEFAULT.equals(secInfo.getSignatureType()) ||
            !SecurityInfo.DIGEST_ALGORITHM_MD5.equals(secInfo.getDigestAlgorithm()) ||
            !myMasterCert.equals(secInfo.getSignatureEncryptionCertificateID()))
        {
          secInfo.setSignatureType(SecurityInfo.SIGNATURE_TYPE_DEFAULT);
          secInfo.setDigestAlgorithm(SecurityInfo.DIGEST_ALGORITHM_MD5);
          secInfo.setSignatureEncryptionCertificateID(new Long(
            MasterChannelCreator.getMyMasterCert().getUId()));
          ServiceLookupHelper.getChannelManager().updateSecurityInfo(secInfo);  
        }
      }
    }
    catch (Throwable ex)
    {
      logger.logUpdateError(methodName, params, ex);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

  /**@todo handleCommInfoUpdated, handlePackagingProfileUpdated, handleSecurityProfileUpdated */

}
