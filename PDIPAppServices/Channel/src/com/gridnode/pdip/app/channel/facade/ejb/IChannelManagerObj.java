/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelManagerObj.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Jun 21 2002    Goh Kan Mun             Modified - Use filter to retrieve data.
 * Jul 05 2002    Goh Kan Mun             Modified - Change the names of GetEntity methods.
 * Aug 06 2002    Neo Sok Lay             Add methods:
 *                                        - getChannelInfoUIDs(IDataFilter)
 *                                        - getCommInfoUIDs(IDataFilter)
 *
 * Aug 20 2002    Jagadeesh               Added Methods to perform operations.
 *                                        - send().
 * Oct 24 2002    Jagadeesh               Added - Channel Service Methods to throw ChannelException.
 * Dec 02 2002    Goh Kan Mun             Modified - Add in header for connect and
 *                                                   connectAndListen methods.
 */

package com.gridnode.pdip.app.channel.facade.ejb;

import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.helpers.IReceiveFeedbackHandler;
import com.gridnode.pdip.app.channel.helpers.IReceiveMessageHandler;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

import javax.ejb.EJBObject;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Collection;

/**
 * This interface defines the services provided by the ChannelManagerBean.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IChannelManagerObj extends EJBObject
{
  /**
   * To create a new <code>ChannelInfo</code> entity.
   */
  public Long createChannelInfo(ChannelInfo channelInfo)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To update changes to an existing <code>ChannelInfo</code> entity.
   */
  public void updateChannelInfo(ChannelInfo channelInfo)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * To remove an existing <code>ChannelInfo</code> entity.
   */
  public void deleteChannelInfo(Long channelInfoUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>ChannelInfo</code> entity with the specified uId.
   */
  public ChannelInfo getChannelInfo(Long uId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>ChannelInfo</code> entity with the specified filter.
   */
  public Collection getChannelInfo(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of <code>ChannelInfo</code> entities with the specified filter.
   */
  public Collection getChannelInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To create a new <code>CommInfo</code> entity.
   */
  public Long createCommInfo(CommInfo commInfo)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To update changes to an existing <code>CommInfo</code> entity.
   */
  public void updateCommInfo(CommInfo commInfo)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * To remove an existing <code>CommInfo</code> entity.
   */
  public void deleteCommInfo(Long commInfoUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>CommInfo</code> entity with the specified uId.
   */
  public CommInfo getCommInfo(Long uId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>CommInfo</code> entity with the specified filter.
   */
  public Collection getCommInfo(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of <code>CommInfo</code> entities with the specified filter.
   */
  public Collection getCommInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To Connect <code>ChannelInfo</code> with the assigned Handler.
   */

  public void connect(CommInfo info, String[] header)
    throws ChannelException, SystemException, RemoteException;

  /**
   * To ConnectAndListen <code>ChannelInfo</code> with the assigned Handler.
   */

  public void connectAndListen(CommInfo info, String[] header)
    throws ChannelException, SystemException, RemoteException;

  /**
   * To Send <code>ChannelInfo</code> with the assigned Handler.
   */

  public void send(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws ChannelException, SystemException, RemoteException;

  /**
   * To Disconnect <code>ChannelInfo</code> with the assigned Handler.
   */

  public void disconnect(CommInfo info)
    throws ChannelException, SystemException, RemoteException;

  /**
   * To ping <code>ChannelInfo</code> with the assigned Handler.
   */

  public void ping(CommInfo info)
    throws ChannelException, SystemException, RemoteException;

  /**
   * To get <code>IReceiveMessageHandler</code> .
   */

  public IReceiveMessageHandler getReceiveMessageHandler(String eventId)
    throws RemoteException;

  /**
   * To get <code>IReceiveFeedbackHandler</code> .
   */

  public IReceiveFeedbackHandler getReceiveFeedbackHandler(String eventId)
    throws RemoteException;

  /**
  * To create a new <code>PackagingInfo</code> entity.
  */
  public Long createPackagingInfo(PackagingInfo packagingInfo)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To update changes to an existing <code>PackagingInfo</code> entity.
   */
  public void updatePackagingInfo(PackagingInfo packagingInfo)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * To remove an existing <code>PackagingInfo</code> entity.
   */
  public void deletePackigingInfo(Long packageingInfoUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>PackagingInfo</code> entity with the specified uId.
   */
  public PackagingInfo getPackagingInfo(Long uId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>PackagingInfo</code> entity with the specified filter.
   */
  public Collection getPackagingInfo(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of <code>PackagingInfo</code> entities with the specified filter.
   */
  public Collection getPackagingInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
  * To create a new <code>SecurityInfo</code> entity.
  */
  public Long createSecurityInfo(SecurityInfo securityInfo)
    throws CreateEntityException, SystemException, RemoteException;

  /**
   * To update changes to an existing <code>SecurityInfo</code> entity.
   */
  public void updateSecurityInfo(SecurityInfo securityInfo)
    throws UpdateEntityException, SystemException, RemoteException;

  /**
   * To remove an existing <code>SecurityInfo</code> entity.
   */
  public void deleteSecurityInfo(Long securityInfoUId)
    throws DeleteEntityException, SystemException, RemoteException;

  /**
   * To retrieve a <code>SecurityInfo</code> entity with the specified uId.
   */
  public SecurityInfo getSecurityInfo(Long uId)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of <code>SecurityInfo</code> entity with the specified filter.
   */
  public Collection getSecurityInfo(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

  /**
   * To retrieve a collection of UIDs of <code>SecurityInfo</code> entities with the specified filter.
   */
  public Collection getSecurityInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException, RemoteException;

}