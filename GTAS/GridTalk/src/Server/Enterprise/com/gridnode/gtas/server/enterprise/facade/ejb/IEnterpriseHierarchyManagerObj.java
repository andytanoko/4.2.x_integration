/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEnterpriseHierarchyManagerObj.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 05 2002    Neo Sok Lay         Created
 * Aug 14 2002    Neo Sok Lay         Add methods to handle update of
 *                                    enterprise hierarchy on removal of
 *                                    hierarchy resources.
 * Sep 30 2002    Neo Sok Lay         Add methods to get Partners attached to
 *                                    BusinesEntity.
 * Jan 07 2003    Neo Sok Lay         Add method to handle on creation of this
 *                                    enterprise.
 * Jul 11 2003    Neo Sok Lay         Add methods:
 *                                    getBizEntitiesForChannel(),
 *                                    getPartnersForChannel(),
 *                                    getUsersForBizEntity()
 */
package com.gridnode.gtas.server.enterprise.facade.ejb;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.EJBObject;

import com.gridnode.pdip.app.coyprofile.model.CompanyProfile;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * LocalObject for EnterpriseHierarchyManagerBean.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I4
 * @since 2.0 I4
 */
public interface IEnterpriseHierarchyManagerObj
  extends        EJBObject
{

  // ******************** HIERARCHY (begin) ********************** //

  /**
   * Get the UIDs of the ChannelInfo(s) that are associated to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   *
   * @return A Collection of the UIDs of the ChannelInfo(s) associated with
   * the specified BusinessEntity.
   */
  public Collection getChannelsForBizEntity(Long beUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Set the ChannelInfo(s) that are associated to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   * @param channelUIDs The UIDs of the ChannelInfo(s).
   */
  public void setChannelsForBizEntity(Long beUID, Collection channelUIDs)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Check whether a ChannelInfo has been assigned to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity
   * @param channelUID The UID of the ChannelInfo entity.
   * @return <b>true</b> if the assignment exists, <b>false</b> otherwise.
   */
  public boolean isChannelAssignedToBe(Long beUID, Long channelUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the BusinessEntity that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @return The UID of the BusinessEntity that the Partner is associated to.
   */
  public Long getBizEntityForPartner(Long partnerUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Set the BusinessEntity that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @param beUID The UID of the BusinessEntity.
   */
  public void setBizEntityForPartner(Long partnerUID, Long beUID)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Set the ChannelInfo that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @param channelUID The UID of the ChannelInfo.
   */
  public void setChannelForPartner(Long partnerUID, Long channelUID)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Get the ChannelInfo that a Partner is associated to.
   *
   * @param partnerUID The UID of the Partner
   * @return The UID of the ChannelInfo.
   */
  public Long getChannelForPartner(Long partnerUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the BusinessEntity(s) that a User is associated to.
   *
   * @param userUID The UID of the UserAccount of the User.
   * @return A Collection of the UIDs of the BusinessEntity(s).
   */
  public Collection getBizEntitiesForUser(Long userUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Set the BusinessEntity(s) that a User is associated to.
   *
   * @param userUID The UID of the UserAccount of the User.
   * @param beUIDs The Collection of UIDs that the User is associated to.
   */
  public void setBizEntitiesForUser(Long userUID, Collection beUIDs)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the Partner(s) that are attached to a BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   * @return A Collection of the UIDs of the Partner(s).
   */
  public Collection getPartnersForBizEntity(Long beUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the Partner(s) that are attached to a BusinessEntity.
   *
   * @param enterpriseID The EnterpriseID of the BusinessEntity.
   * @param beID The ID of the BusinessEntity.
   * @return A Collection of the UIDs of the Partner(s).
   */
  public Collection getPartnersForBizEntity(String enterpriseID, String beID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the BusinessEntity(s) that are associated to a ChannelInfo.
   *
   * @param channelUID The UID of the ChannelInfo.
   *
   * @return A Collection of the UIDs of the BusinessEntity(s) associated with
   * the specified ChannelInfo.
   */
  public Collection getBizEntitiesForChannel(Long channelUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the Partner(s) that are associated to a ChannelInfo.
   *
   * @param channelUID The UID of the ChannelInfo.
   *
   * @return A Collection of the UIDs of the Partner(s) associated with
   * the specified ChannelInfo.
   */
  public Collection getPartnersForChannel(Long channelUID)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * Get the UIDs of the UserAccount(s) that are associated to.
   * BusinessEntity.
   *
   * @param beUID The UID of the BusinessEntity.
   * @return A Collection of the UIDs of the UserAccount(s).
   */
  public Collection getUsersForBizEntity(Long beUID)
    throws FindEntityException, SystemException, RemoteException;

  // **************** On removal **************************

  /**
   * Performs updates to the enterprise hierarchy on removal of a
   * BusinessEntity.
   *
   * @param beUID The UID of the removed BusinessEntity.
   * @throws UpdateEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public void onRemoveBizEntity(Long beUID)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Performs updates to the enterprise hierarchy on removal of a
   * ChannelInfo.
   *
   * @param channelUID The UID of the removed ChannelInfo.
   * @throws UpdateEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public void onRemoveChannel(Long channelUID)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Performs updates to the enterprise hierarchy on removal of a
   * Partner.
   *
   * @param partnerUID The UID of the removed Partner.
   * @throws UpdateEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public void onRemovePartner(Long partnerUID)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Performs updates to the enterprise hierarchy on removal of a
   * UserAccount.
   *
   * @param userUID The UID of the removed UserAccount.
   * @throws UpdateEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public void onRemoveUser(Long userUID)
    throws UpdateEntityException, SystemException, RemoteException;

  // *************************** On Creation *********************

  /**
   * Performs updates tot he enterprise hierarchy on creation of
   * Master channel for the the enterprise.
   *
   * @param channelUID UID of the master channel
   * @throws UpdateEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public void onCreateMasterChannel(Long channelUID)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * Performs updates tot he enterprise hierarchy on creation of
   * the enterprise.
   *
   * @param enterpriseID ID of this enterprise.
   * @param profile Profile of the enterprise.
   * @throws UpdateEntityException
   * @throws SystemException
   * @throws RemoteException
   */
  public void onCreateMyEnterprise(String enterpriseID, CompanyProfile profile)
    throws UpdateEntityException, SystemException, RemoteException;


  // ********************** HIERARCHY (end) ************************



}