/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChannelInfo.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jun 06 2002    Goh Kan Mun             Created
 * Jun 14 2002    Goh Kan Mun             Modified - Add methods to create/delete/update/find
 *                                        CommInfo entity. Check if CommInfo exist in
 *                                        create/update ChannelInfo entity.
 * Jul 03 2002    Goh Kan Mun             Modified - Change in ChannelInfo and CommInfo.
 *                                                   Change the names of the GetEntity methods.
 * Jul 11 2002    Goh Kan Mun             Modified - check for null value for
 *                                                   unique non-null field. (Limitation of DB)
 *                                                 - throws correct FindEntityException for
 *                                                    GetEntity by filter methods.
 *                                                 - Check for null value of the filter.
 * Jul 13 2002    Goh Kan Mun             Modified - check for null value for protocolDetail.
 * Aug 06 2002    Neo Sok Lay             Add method: getChannelInfoUIDs(IDataFilter)
 *
 * OCT 24 2002    Jagadeesh               Added : All Service Methods to throw ChannelException.
 * Dec 02 2002    Goh Kan Mun             Modified - Add in header for connect and
 *                                                   connectAndListen methods.
 *
 * Nov 05 2003		Jagadeesh							  Modified - Changed service methods to use Service
 * 																				delegates.
 * Apr 19 2004    Guo Jianyu              Modified send() to add AS2 headers into Message
 * Nov 21 2005    Neo Sok Lay             Remove use of getEntityByXXXForReadOnly().
 */

package com.gridnode.pdip.app.channel.facade.ejb;

import com.gridnode.pdip.app.channel.IMessageContext;
import com.gridnode.pdip.app.channel.MessageContext;
import com.gridnode.pdip.app.channel.exceptions.ChannelException;
import com.gridnode.pdip.app.channel.handler.ChannelHandlerFactory;
import com.gridnode.pdip.app.channel.handler.IChannelHandler;
import com.gridnode.pdip.app.channel.helpers.*;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.channel.model.CommInfo;
import com.gridnode.pdip.app.channel.model.ISecurityInfo;
import com.gridnode.pdip.app.channel.model.PackagingInfo;
import com.gridnode.pdip.app.channel.model.SecurityInfo;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.EntityModifiedException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;
import com.gridnode.pdip.framework.messaging.Message;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import java.io.File;
import java.util.Collection;

/**
  * The bean manages the ChannelInfo and CommInfo data.
  *
  * @author Goh Kan Mun
  *
  * @version 2.0
  * @since 2.0
  */

public class ChannelManagerBean implements SessionBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 7825749357906306578L;
	transient private SessionContext _sessionContext = null;
  private static final String CLASS_NAME = "ChannelManagerBean";

  public ChannelManagerBean()
  {
  }

  /* *****************************SessionBean Interfaces ***************************** */

  public void ejbCreate() //throws javax.ejb.EJBException, java.rmi.RemoteException
  {
  }

  public void ejbActivate() //throws javax.ejb.EJBException, java.rmi.RemoteException
  {
  }

  public void ejbPassivate() //throws javax.ejb.EJBException, java.rmi.RemoteException
  {
  }

  public void ejbRemove() //throws javax.ejb.EJBException, java.rmi.RemoteException
  {
  }

  public void setSessionContext(SessionContext sessionContext) //throws javax.ejb.EJBException, java.rmi.RemoteException
  {
    _sessionContext = sessionContext;
  }

  /* *****************************Service provide by this class ***************************** */

  // *************************** ChannelInfo ****************************************

  /**
   * To create a new <code>ChannelInfo</code> entity.
   *
   * @param           channelInfo     The new <code>ChannelInfo</code> entity.
   *
   * @exception       Thrown when the create operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Long createChannelInfo(ChannelInfo channelInfo)
    throws CreateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "createChannelInfo", "Enter");
      if (!checkCommInfoExist(new Long(channelInfo.getTptCommInfo().getUId())))
        throw new CreateEntityException("CommInfo does not exist!");
      if (!checkPackagingInfoExist(new Long(channelInfo
        .getPackagingProfile()
        .getUId())))
        throw new CreateEntityException("PackagingInfo dose not exist");
      if (!checkSecurityInfoExist(new Long(channelInfo
        .getSecurityProfile()
        .getUId())))
        throw new CreateEntityException("SecurityInfo dose not exist");
      return new Long(
        ((ChannelInfo) ChannelInfoEntityHandler
          .getInstance()
          .createEntity(channelInfo))
          .getUId());
    }
    catch (CreateException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "createChannelInfo",
        "BL Exception ",
        ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "createChannelInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.createChannelInfo(ChannelInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "createChannelInfo", "Exit ");
    }
  }

  /**
   * To update changes to an existing <code>ChannelInfo</code> entity.
   *
   * @param           channelInfo    The modified <code>ChannelInfo</code> entity.
   *
   * @exception       Thrown when the update operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void updateChannelInfo(ChannelInfo channelInfo)
    throws UpdateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "updateChannelInfo", "Enter");
      if (!checkCommInfoExist(new Long(channelInfo.getTptCommInfo().getUId())))
        throw new UpdateEntityException("CommInfo does not exist!");
      if (!checkPackagingInfoExist(new Long(channelInfo
        .getPackagingProfile()
        .getUId())))
        throw new CreateEntityException("PackagingInfo dose not exist");
      if (!checkSecurityInfoExist(new Long(channelInfo
        .getSecurityProfile()
        .getUId())))
        throw new CreateEntityException("SecurityInfo dose not exist");
      if (channelInfo.getName() == null)
        throw new Exception("Name cannot be null!");
      if (channelInfo.getTptProtocolType() == null)
        throw new Exception("Protocol type cannot be null!");
      ChannelInfoEntityHandler.getInstance().update(channelInfo);
    }
    catch (EntityModifiedException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "updateChannelInfo",
        "BL Exception",
        ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "updateChannelInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.updateChannelInfo(ChannelInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "updateChannelInfo", "Exit ");
    }
  }

  /**
   * To remove an existing <code>ChannelInfo</code> entity.
   *
   * @param           channelInfoUId   the uId of the <code>ChannelInfo</code> entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteChannelInfo(Long channelInfoUId)
    throws DeleteEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "deleteChannelInfo",
        "channelInfoUId: " + channelInfoUId);

      ChannelInfoEntityHandler.getInstance().remove(channelInfoUId);
    }
    catch (RemoveException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "deleteChannelInfo",
        "BL Exception",
        ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "deleteChannelInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.deleteChannelInfo(channelInfoUId) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "deleteChannelInfo", "Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>ChannelInfo</code> entity with the specified filter.
   *
   * @param           filter   the filter used to retrieve the <code>ChannelInfo</code> entity.
   *
   * @return          A collection of <code>ChannelInfo<code> entities.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getChannelInfo(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoByFilter",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));
      return ChannelInfoEntityHandler
        .getInstance()
        //.getEntityByFilterForReadOnly(
        .getEntityByFilter(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByFilter",
        "BL Exception",
        ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByFilter",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByFilter",
        "Error ",
        ex);
      throw new SystemException(
        "ChannelManagerBean.getChannelInfoByFilter(refId) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getChannelInfoByRefId", "Exit ");
    }
  }

  /**
   * To retrieve a collection of UIDs of <code>ChannelInfo</code> entities with the specified filter.
   */
  public Collection getChannelInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoUIDs",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));
      return ChannelInfoEntityHandler.getInstance().getKeyByFilterForReadOnly(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoUIDs",
        "BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoUIDs",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getChannelInfoUIDs", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getChannelInfoUIDs(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getChannelInfoUIDs", "Exit ");
    }
  }

  /**
   * To retrieve a <code>ChannelInfo</code> entity with the specified uId.
   *
   * @param           uId   the uId of the <code>ChannelInfo</code> entity.
   *
   * @return          The <code>ChannelInfo<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public ChannelInfo getChannelInfo(Long uId)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "getChannelInfoByUId", "uId: " + uId);

      return (ChannelInfo) ChannelInfoEntityHandler
        .getInstance()
        //.getEntityByKeyForReadOnly(uId);
        .getEntityByKey(uId);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByUId",
        "BL Exception",
        ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByUId",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getChannelInfoByUId", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getChannelInfoByUId(type) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getChannelInfoByUId", "Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>ChannelInfo</code> entity with the <code>CommInfo</code> uId.
   *
   * @param           uId   the uId of the <code>ChannelInfo</code> entity.
   *
   * @return          A collection of <code>ChannelInfo<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  private Collection getChannelInfoByCommInfoUId(Long commInfoUId)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoByCommInfoUId",
        "commInfoUId: " + commInfoUId);
      return ChannelInfoEntityHandler.getInstance().findByCommInfo(commInfoUId);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByCommInfoUId",
        "BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByCommInfoUId",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByCommInfoUId",
        "Error ",
        ex);
      throw new SystemException(
        "ChannelManagerBean.getChannelInfoByCommInfoUId(type) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoByCommInfoUId",
        "Exit ");
    }
  }

  // *************************** CommInfo + ChannelInfo ************************************
  /**
   * To check if the specified <code>CommInfo</code> enity exist in the db.
   *
   * @param           commInfoUId   the uId of <code>CommInfo</code> entity.
   *
   * @return          true if the <code>CommInfo</code> entity exist, else false.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   *
   */
  private boolean checkCommInfoExist(Long commInfoUId) throws SystemException
  {
    try
    {
      return (getCommInfo(commInfoUId) != null);
    }
    catch (FindEntityException ex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "checkCommInfoExist",
        "CommInfo with uId " + commInfoUId + "does not exist!",
        ex);
      return false;
    }
  }

  private boolean checkPackagingInfoExist(Long packagingInfoUId)
    throws SystemException
  {
    try
    {
      return (getPackagingInfo(packagingInfoUId) != null);
    }
    catch (FindEntityException ex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "checkPackagingInfoExist",
        "PackagingInfo with UID " + packagingInfoUId + "dose Not Exist",
        ex);
      return false;
    }

  }

  private boolean checkSecurityInfoExist(Long securityInfoUId)
    throws SystemException
  {
    try
    {
      return (getSecurityInfo(securityInfoUId) != null);
    }
    catch (FindEntityException ex)
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "checkSecurityInfoExist",
        "SecurityInfo with UID " + securityInfoUId + "dose Not Exist",
        ex);
      return false;
    }
  }

  /**
   * To check if there is any dependence <code>ChannelInfo</code> entity
   * to the specified uid of the <code>CommInfo</code> enity.
   *
   * @param           commInfoUId   the <code>CommInfo</code> entity.
   *
   * @return          true if the dependency exist, else false.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   *
   */
  private boolean checkDepChannelInfo(Long commInfoUId) throws Exception
  {
    ChannelLogger.debugLog(
      CLASS_NAME,
      "checkDepChannelInfo",
      "commInfoUId: " + commInfoUId);
    Collection c = getChannelInfoByCommInfoUId(commInfoUId);
    return (c != null && c.size() != 0);
  }

  // *************************** CommInfo ****************************************

  /**
   * To create a new <code>CommInfo</code> entity.
   *
   * @param           info     The new <code>CommInfo</code> entity.
   *
   * @exception       Thrown when the create operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Long createCommInfo(CommInfo commInfo)
    throws CreateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "createCommInfo", "Enter");
      if (commInfo.getURL() == null)
        throw new Exception("ProtocolDetail field cannot be null.");
      return new Long(
        ((CommInfo) CommInfoEntityHandler.getInstance().createEntity(commInfo))
          .getUId());
    }
    catch (CreateException ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "createCommInfo", "BL Exception ", ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "createCommInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.createCommInfo(CommInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "createCommInfo", "Exit ");
    }
  }

  /**
   * To update changes to an existing <code>CommInfo</code> entity.
   *
   * @param           CommInfo    The modified <code>CommInfo</code> entity.
   *
   * @exception       Thrown when the update operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void updateCommInfo(CommInfo commInfo)
    throws UpdateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "updateCommInfo", "Enter");
      if (commInfo.getName() == null)
        throw new Exception("Field name cannot be null!");
      if (commInfo.getProtocolType() == null)
        throw new Exception("Field protocol type cannot be null!");
      if (commInfo.getTptImplVersion() == null)
        throw new Exception("Field Transport Implementation Version cannot be null!");
      if (commInfo.getURL() == null)
        throw new Exception("ProtocolDetail field cannot be null.");
      CommInfoEntityHandler.getInstance().update(commInfo);
    }
    catch (EntityModifiedException ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "updateCommInfo", "BL Exception", ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "updateCommInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.updateCommInfo(CommInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "updateCommInfo", "Exit ");
    }
  }

  /**
   * To remove an existing <code>CommInfo</code> entity.
   *
   * @param           info   the uId of the <code>CommInfo</code> entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteCommInfo(Long commInfoUId)
    throws DeleteEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "deleteCommInfo",
        "commInfoUId: " + commInfoUId);
      if (checkDepChannelInfo(commInfoUId))
        throw new DeleteEntityException(
          "Existing dependency on CommInfo " + commInfoUId);
      CommInfoEntityHandler.getInstance().remove(commInfoUId);
    }
    catch (RemoveException ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "deleteCommInfo", "BL Exception", ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "deleteCommInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.deleteCommInfo(commInfoUId) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "deleteCommInfo", "Exit ");
    }
  }

  /**
   * To retrieve a <code>CommInfo</code> entity with the specified uId.
   *
   * @param           uId   the uId of the <code>CommInfo</code> entity.
   *
   * @return          The <code>CommInfo<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public CommInfo getCommInfo(Long uId)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "getCommInfoByUId", "uId: " + uId);

      return (CommInfo) CommInfoEntityHandler
        .getInstance()
        .getEntityByKey(
        //.getEntityByKeyForReadOnly(
        uId);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getCommInfoByUId",
        "BL Exception",
        ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getCommInfoByUId",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getCommInfoByUId", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getCommInfoByUId(uid) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getCommInfoByUId", "Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>CommInfo</code> entity with the specified filter.
   *
   * @param           filter   the filter use to retrieve the <code>CommInfo</code> entities.
   *
   * @return          a collection of <code>CommInfo<code> entities.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getCommInfo(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getCommInfoByFilter",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));

      //return CommInfoEntityHandler.getInstance().getEntityByFilterForReadOnly(
      //  filter);
      return CommInfoEntityHandler.getInstance().getEntityByFilter(filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getCommInfoByFilter",
        "BL Exception",
        ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getCommInfoByFilter",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getCommInfoByFilter", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getCommInfoByFilter(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getCommInfoByFilter", "Exit ");
    }
  }

  /**
   * To retrieve a collection of UIDs of <code>CommInfo</code> entities with the specified filter.
   */
  public Collection getCommInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getCommInfoUIDs",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));
      return CommInfoEntityHandler.getInstance().getKeyByFilterForReadOnly(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getCommInfoUIDs", "BL Exception", ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getCommInfoUIDs",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getCommInfoUIDs", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getCommInfoUIDs(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getCommInfoUIDs", "Exit ");
    }
  }

  public void send(
    ChannelInfo info,
    String[] dataToSend,
    File[] file,
    String[] header)
    throws ChannelException, SystemException
  {
    Message outBoundMessage = null;
    MessageContext messageContext = null;
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "send()", "[Send Message Begin]");
      outBoundMessage = new Message();
      messageContext = new MessageContext();
      outBoundMessage.setData(dataToSend);
      outBoundMessage.setPayLoad(file);
      outBoundMessage.setCommonHeaders(
        ChannelSendHeader.getCommonHeaders(header));
      outBoundMessage.setMessageHeaders(
        ChannelSendHeader.getAS2Headers(header));
      //Since BL Header needs transformation-before it's placed as common header.
      //Lets transform this header here and place it in

      //outBoundMessage.setAttribute(IMessage.KEY_BL_HEADER,header);
      //Set outbound message.
      messageContext.setAttribute(
        IMessageContext.OUTBOUND_MESSAGE,
        outBoundMessage);
      //Set ChannelInfo.
      messageContext.setAttribute(IMessageContext.CHANNEL_INFO, info);
      //Dispatch this messageContext to Messaging Middleware
      MessageDispatcher.dispatchMessageToSendListener(messageContext);
      ChannelLogger.debugLog(CLASS_NAME, "send()", "[Send Message End]");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "send",
        "Could Not Send the Message" + ex.getMessage());
      throw new ChannelException(
        "Could Not Send the Message. Please try again..",
        ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "send",
        "Could Not Send the Message",
        th);
      throw new SystemException(
        "Could Not Send the Message. Please try again..",
        th);
    }

  }

  public void connect(CommInfo info, String[] header)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "connect", "[In Connect Begin]");
      TransportServiceDelegate.connect(info, header);
      ChannelLogger.infoLog(CLASS_NAME, "connect", "[In Connect End]");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "connect",
        "[Could Not Connect]" + ex.getMessage());
      throw new ChannelException(
        "[Cannot establish Connection. Please try again..]",
        ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(CLASS_NAME, "connect", "Could Not Connect ", th);
      throw new SystemException(
        "Cannot establish Connection. Please try again..",
        th);
    }

  }

  public void connectAndListen(CommInfo info, String[] header)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "connectAndListen",
        "[In connectAndListen Begin]");
      TransportServiceDelegate.connectAndListen(info, header);
      ChannelLogger.infoLog(
        CLASS_NAME,
        "connectAndListen()",
        "[After connectAndListen End]");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "connectAndListen()",
        "Could Not ConnectAndListen " + ex.getMessage());
      throw new ChannelException("Could Not ConnectAndListen ", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "connectAndListen()",
        "Could Not ConnectAndListen ",
        th);
      throw new SystemException("Could Not ConnectAndListen ", th);
    }

  }

  public void disconnect(CommInfo info)
    throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "disconnect()",
        "[In disconnect Begin]");
      TransportServiceDelegate.disconnect(info);
      ChannelLogger.infoLog(
        CLASS_NAME,
        "disconnect()",
        "[After disconnect End]");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "disconnect()",
        "Could Not disconnect " + ex.getMessage());
      throw new ChannelException("Could Not disconnect ", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "disconnect()",
        "Could Not disconnect ",
        th);
      throw new SystemException("Could Not disconnect ", th);
    }

  }

  public void ping(CommInfo info) throws ChannelException, SystemException
  {
    try
    {
      ChannelLogger.infoLog(CLASS_NAME, "ping()", "[In ping Begin]");
      TransportServiceDelegate.ping(info);
      ChannelLogger.infoLog(CLASS_NAME, "ping()", "[After ping End]");
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "ping()",
        "Could Not ping " + ex.getMessage());
      throw new ChannelException("Could Not ping ", ex);
    }
    catch (Throwable th)
    {
      ChannelLogger.warnLog(CLASS_NAME, "ping()", "Could Not ping ", th);
      throw new SystemException("Could Not ping ", th);
    }

  }

  public IReceiveMessageHandler getReceiveMessageHandler(String eventId)
  {
    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getReceiveMessageHandler()",
        "In getReceiveMessageHandler");
      IChannelHandler channelFactory =
        ChannelHandlerFactory.getChannelHandler();
      return channelFactory.getReceiveMessageHandler(eventId);
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getReceiveMessageHandler()",
        "Could Not get ReceiveMessageHandler " + ex.getMessage());
      ex.printStackTrace();
    }
    return null;
  }

  public IReceiveFeedbackHandler getReceiveFeedbackHandler(String eventId)
  {

    try
    {
      ChannelLogger.infoLog(
        CLASS_NAME,
        "getReceiveFeedbackHandler()",
        "In getReceiveFeedbackHandler");
      IChannelHandler channelFactory =
        ChannelHandlerFactory.getChannelHandler();
      return channelFactory.getReceiveFeedbackHandler(eventId);

    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getReceiveFeedbackHandler",
        "Could Not get ReceiveFeedBackMessageHandler " + ex.getMessage());
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * To create a new <code>PackagingInfo</code> entity.
   *
   * @param           info     The new <code>PackagingInfo</code> entity.
   *
   * @exception       Thrown when the create operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Long createPackagingInfo(PackagingInfo packageingInfo)
    throws CreateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "createPackagingInfo", "Enter");
      //      if (commInfo.getProtocolDetail() == null)
      //        throw new Exception("ProtocolDetail field cannot be null.");
      return new Long(
        ((PackagingInfo) PackagingInfoEntityHandler
          .getInstance()
          .createEntity(packageingInfo))
          .getUId());
    }
    catch (CreateException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "createPackagingInfo",
        "BL Exception ",
        ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "createPackagingInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.createPackagingInfo(PackagingInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "createPackagingInfo", "Exit ");
    }
  }

  /**
   * To remove an existing <code>PackagingInfo</code> entity.
   *
   * @param           info   the uId of the <code>PackagingInfo</code> entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deletePackigingInfo(Long packagingInfoUId)
    throws DeleteEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "deletePackagingInfo",
        "packagingInfoUId: " + packagingInfoUId);
      if (checkDepChannelToPackagingInfo(packagingInfoUId))
        throw new DeleteEntityException(
          "Existing dependency on CommInfo " + packagingInfoUId);
      PackagingInfoEntityHandler.getInstance().remove(packagingInfoUId);
    }
    catch (RemoveException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "deletePackagingInfo",
        "BL Exception",
        ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "deletePackagingInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.deletePackagingInfo(packagingInfoUId) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "deletePackagingInfo", "Exit ");
    }
  }

  /**
   * PackagingInfo (or rather PackagingProfile) is associated with Channel (Relationship
   * being 1 -to- 1) hence, its mandatory to check for  dependent Channel to this PackagingProfile.
   *
   * If true, A Delete Operation shld be deemed none.
   *
   * @param packagingInfoUID - PackagingInfo UID
   * @return boolean value of existance.
   * @throws Exception - thrown when in exception condition.
   */

  private boolean checkDepChannelToPackagingInfo(Long packagingInfoUID)
    throws Exception
  {
    //Check for Depended Channelinfo uncomment this
    ChannelLogger.debugLog(
      CLASS_NAME,
      "checkDepChannelToPackagingInfo",
      "packagingInfoUID: " + packagingInfoUID);
    Collection c = getChannelInfoByPackagingInfoUID(packagingInfoUID);
    return (c != null && c.size() != 0);

  }

  /**
   * To retrieve a <code>PackagingInfo</code> entity with the specified uId.
   *
   * @param           uId   the uId of the <code>CommInfo</code> entity.
   *
   * @return          The <code>PackagingInfo<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public PackagingInfo getPackagingInfo(Long uId)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "getPackigingInfo", "uId: " + uId);

      return (PackagingInfo) PackagingInfoEntityHandler
        .getInstance()
        //.getEntityByKeyForReadOnly(uId);
        .getEntityByKey(uId);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getPackigingInfo",
        "BL Exception",
        ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getPackigingInfo",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getPackigingInfo", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getPackigingInfo(uid) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getPackigingInfo", "Exit ");
    }
  }

  /**
   * To retrieve a collection of <code>PackagingInfo</code> entity with the specified filter.
   *
   * @param           filter   the filter use to retrieve the <code>PackagingInfo</code> entities.
   *
   * @return          a collection of <code>PackagingInfo<code> entities.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getPackagingInfo(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getPackigingInfo",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));

      return PackagingInfoEntityHandler
        .getInstance()
        //.getEntityByFilterForReadOnly(
        .getEntityByFilter(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getPackigingInfo",
        "BL Exception",
        ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getPackigingInfo",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getPackigingInfo", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getPackigingInfo(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getPackigingInfo", "Exit ");
    }
  }

  /**
   * To retrieve a collection of UIDs of <code>CommInfo</code> entities with the specified filter.
   */
  public Collection getPackagingInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getPackagingInfoUIDs",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));
      return PackagingInfoEntityHandler
        .getInstance()
        .getKeyByFilterForReadOnly(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getPackagingInfoUIDs",
        "BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getPackagingInfoUIDs",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getPackagingInfoUIDs", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getPackagingInfoUIDs(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getPackagingInfoUIDs", "Exit ");
    }
  }

  public void updatePackagingInfo(PackagingInfo packagingInfo)
    throws UpdateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "updatePackagingInfo", "Enter");
      if (packagingInfo.getName() == null)
        throw new Exception("Field name cannot be null!");
      //      if (packagingInfo.getEnvelope() == null)
      //        throw new Exception("Field protocol type cannot be null!");
      PackagingInfoEntityHandler.getInstance().update(packagingInfo);
    }
    catch (EntityModifiedException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "updatePackagingInfo",
        "BL Exception",
        ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "updatePackagingInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.updatePackagingInfo(updatePackagingInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "updatePackagingInfo", "Exit ");
    }
  }

  /**
  * To create a new <code>PackagingInfo</code> entity.
  *
  * @param           info     The new <code>PackagingInfo</code> entity.
  *
  * @exception       Thrown when the create operation fails.
  *
  * @since 2.0
  * @version 2.0
  */
  public Long createSecurityInfo(SecurityInfo securityInfo)
    throws CreateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "createSecurityInfo", "Enter");
      //      if (commInfo.getProtocolDetail() == null)
      //        throw new Exception("ProtocolDetail field cannot be null.");
      return new Long(
        ((SecurityInfo) SecurityInfoEntityHandler
          .getInstance()
          .createEntity(securityInfo))
          .getUId());
    }
    catch (CreateException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "createSecurityInfo",
        "BL Exception ",
        ex);
      throw new CreateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "createSecurityInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.createSecurityInfo(SecurityInfo) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "createSecurityInfo", "Exit ");
    }
  }

  /**
   * To remove an existing <code>PackagingInfo</code> entity.
   *
   * @param           info   the uId of the <code>PackagingInfo</code> entity to be deleted.
   *
   * @exception       Thrown when the delete operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public void deleteSecurityInfo(Long securityInfoUId)
    throws DeleteEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "deleteSecurityInfo",
        "securityInfoUId: " + securityInfoUId);
      if (checkDepChannelToSecurityInfo(securityInfoUId))
        throw new DeleteEntityException(
          "Existing dependency on CommInfo " + securityInfoUId);
      SecurityInfoEntityHandler.getInstance().remove(securityInfoUId);
    }
    catch (RemoveException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "deletePackagingInfo",
        "BL Exception",
        ex);
      throw new DeleteEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "deletePackagingInfo", "Error ", t);
      throw new SystemException(
        "ChannelManagerBean.deletePackagingInfo(packagingInfoUId) Error ",
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "deletePackagingInfo", "Exit ");
    }
  }

  /**
   *
    * SecurityInfo (or rather SecurityProfile) is associated with Channel (Relationship
    * being 1 -to- 1) hence, its mandatory to check for  dependent Channel to this SecurityProfile.
    *
    * If true, A Delete Operation shld be deemed none.
    *
    * @param securityInfoUID - SecurityInfo UID
    * @return boolean value of existance.
    * @throws Exception - thrown when in exception condition.
    */

  private boolean checkDepChannelToSecurityInfo(Long securityInfoUID)
    throws Exception
  {
    //Check for Depended Channelinfo uncomment this
    ChannelLogger.debugLog(
      CLASS_NAME,
      "checkDepChannelToSecurityInfo",
      "SecurityInfoUID: " + securityInfoUID);
    Collection c = getChannelInfoBySecurityInfoUID(securityInfoUID);
    return (c != null && c.size() != 0);

  }

  /**
   * Return's a Collection of ChannelInfo, associated with this PackagingProfile.
   * @param packagingInfoUID - PackagingInfo UID.
   * @return - Collection of ChannelInfo.
   * @throws FindEntityException
   * @throws SystemException
   */

  private Collection getChannelInfoByPackagingInfoUID(Long packagingInfoUID)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoByPackagingInfoUID",
        "packagingInfoUID: " + packagingInfoUID);
      return ChannelInfoEntityHandler.getInstance().findByPackagingInfo(
        packagingInfoUID);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByPackagingInfoUID",
        "BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByPackagingInfoUID",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoByPackagingInfoUID",
        "Error ",
        ex);
      throw new SystemException(
        "ChannelManagerBean.getChannelInfoByPackagingInfoUID(type) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoByPackagingInfoUId",
        "Exit ");
    }
  }

  /**
   * Return's A collection of all dependend ChannelInfo to this SecurityInfo.
   * @param securityInfoUID - SecurityInfoUID
   * @return - Collection of ChannelInfo
   * @throws FindEntityException
   * @throws SystemException
   */

  private Collection getChannelInfoBySecurityInfoUID(Long securityInfoUID)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoBySecurityInfoUID",
        "securityInfoUID: " + securityInfoUID);
      return ChannelInfoEntityHandler.getInstance().findBySecurityInfo(
        securityInfoUID);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoBySecurityInfoUID",
        "BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoBySecurityInfoUID",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getChannelInfoBySecurityInfoUID",
        "Error ",
        ex);
      throw new SystemException(
        "ChannelManagerBean.getChannelInfoBySecurityInfoUID(type) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getChannelInfoBySecurityInfoUId",
        "Exit ");
    }
  }

  /**
   * To retrieve a <code>PackagingInfo</code> entity with the specified uId.
   *
   * @param           uId   the uId of the <code>CommInfo</code> entity.
   *
   * @return          The <code>PackagingInfo<code> entity.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public SecurityInfo getSecurityInfo(Long uId)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "getSecurityInfo", "uId: " + uId);

      return (SecurityInfo) SecurityInfoEntityHandler
        .getInstance()
        //.getEntityByKeyForReadOnly(uId);
        .getEntityByKey(uId);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getSecurityInfo", "BL Exception", ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getSecurityInfo",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getSecurityInfo", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getSecurityInfo(uid) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getSecurityInfo", "Exit ");
    }

  }

  /**
   * To retrieve a collection of <code>PackagingInfo</code> entity with the specified filter.
   *
   * @param           filter   the filter use to retrieve the <code>PackagingInfo</code> entities.
   *
   * @return          a collection of <code>PackagingInfo<code> entities.
   *
   * @exception       Thrown when the retrieval operation fails.
   *
   * @since 2.0
   * @version 2.0
   */
  public Collection getSecurityInfo(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getSecurityInfo",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));

      return SecurityInfoEntityHandler
        .getInstance()
        //.getEntityByFilterForReadOnly(
        .getEntityByFilter(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getSecurityInfo", "BL Exception", ex);
      //      throw new FindEntityException(ex.getMessage());
      throw new FindEntityException(ex);
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getSecurityInfo",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getSecurityInfo", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getSecurityInfo(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getSecurityInfo", "Exit ");
    }
  }

  /**
   * To retrieve a collection of UIDs of <code>CommInfo</code> entities with the specified filter.
   */
  public Collection getSecurityInfoUIDs(IDataFilter filter)
    throws FindEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(
        CLASS_NAME,
        "getSecurityInfoUIDs",
        "filter: " + ((filter == null) ? null : filter.getFilterExpr()));
      return SecurityInfoEntityHandler.getInstance().getKeyByFilterForReadOnly(
        filter);
    }
    catch (ApplicationException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getSecurityInfoUIDs",
        "BL Exception",
        ex);
      throw new FindEntityException(ex.getMessage());
    }
    catch (SystemException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "getSecurityInfoUIDs",
        "System Exception",
        ex);
      throw ex;
    }
    catch (Throwable ex)
    {
      ChannelLogger.warnLog(CLASS_NAME, "getSecurityInfoUIDs", "Error ", ex);
      throw new SystemException(
        "ChannelManagerBean.getSecurityInfoUIDs(filter) Error ",
        ex);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "getSecurityInfoUIDs", "Exit ");
    }
  }

  public void updateSecurityInfo(SecurityInfo securityInfo)
    throws UpdateEntityException, SystemException
  {
    try
    {
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "Enter: uid="+securityInfo.getUId());
      if (securityInfo.getName() == null)
        throw new Exception("Field name cannot be null!");
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "b4 signatureType="+securityInfo.getSignatureType());
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "b4 signatureEncryptionCertificateID="+securityInfo.getSignatureEncryptionCertificateID());
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "b4 digestAlgorithm="+securityInfo.getDigestAlgorithm());
      
      //TWX 28 Nov 2008, clear those encrypt/ sign algo if its type is "None"
       if(ISecurityInfo.ENCRYPTION_TYPE_NONE.equals(securityInfo.getEncryptionType()))
       {
         securityInfo.setEncryptionAlgorithm(null);
         securityInfo.setEncryptionLevel(0);
         securityInfo.setEncryptionCertificateID(null);
       }

       if(ISecurityInfo.SIGNATURE_TYPE_NONE.equals(securityInfo.getSignatureType()))
       {
         securityInfo.setDigestAlgorithm(null);
         securityInfo.setSignatureEncryptionCertificateID(null);
       }

       if(ISecurityInfo.COMPRESSION_TYPE_NONE.equals(securityInfo.getCompressionType()))
       {
         securityInfo.setCompressionLevel(0);
         securityInfo.setCompressionMethod(null);
       } 
      
      SecurityInfoEntityHandler.getInstance().update(securityInfo);
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "after signatureType="+securityInfo.getSignatureType());
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "after signatureEncryptionCertificateID="+securityInfo.getSignatureEncryptionCertificateID());
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "after digestAlgorithm="+securityInfo.getDigestAlgorithm());
      
    }
    catch (EntityModifiedException ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "updateSecurityInfo",
        "BL Exception",
        ex);
      throw new UpdateEntityException(ex.getMessage());
    }
    catch (Throwable t)
    {
      ChannelLogger.warnLog(CLASS_NAME, "updateSecurityInfo", "Error ", t);
      throw new SystemException(
        "Error while updating SecurityInfo: "+t.getMessage(),
        t);
    }
    finally
    {
      ChannelLogger.debugLog(CLASS_NAME, "updateSecurityInfo", "Exit ");
    }
  }

}