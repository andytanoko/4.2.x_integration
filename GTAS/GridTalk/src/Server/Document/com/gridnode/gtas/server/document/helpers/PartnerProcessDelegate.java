/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerProcessDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 11 2002    Koh Han Sing        Created
 * Aug 07 2003    Koh Han Sing        Add isLocalPending
 * Oct 21 2003    Guo Jianyu          Add addAlarm(), getChannelManager()
 *                                    Modified triggerPartnerFunction() to handle
 *                                    retries for import triggers
 * Nov 10 2005    Neo Sok Lay         Use ServiceLocator instead ServiceLookup
 * Apr 17 2006    Neo Sok Lay         To add retry alarm as disabled initially.    
 *                                    Use SourceFolder + GDocID as the SenderKey.                               
 */
package com.gridnode.gtas.server.document.helpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.document.model.TriggerInfo;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerHome;
import com.gridnode.gtas.server.partnerprocess.facade.ejb.IPartnerProcessManagerObj;
import com.gridnode.gtas.server.partnerprocess.model.Trigger;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerHome;
import com.gridnode.pdip.app.channel.facade.ejb.IChannelManagerObj;
import com.gridnode.pdip.app.channel.model.ChannelInfo;
import com.gridnode.pdip.app.workflow.client.WorkflowClient;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrHome;
import com.gridnode.pdip.base.time.facade.ejb.IiCalTimeMgrObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class provides services to the PartnerProcess module.
 *
 * @author Koh Han Sing
 *
 * @version 4.0
 * @since 2.0
 */
public class PartnerProcessDelegate
{
  /**
   * Obtain the EJBObject for the PartnerProcessManagerBean.
   *
   * @return The EJBObject to the PartnerProcessManagerBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  public static IPartnerProcessManagerObj getManager()
    throws ServiceLookupException
  {
    return (IPartnerProcessManagerObj)ServiceLocator.instance(
      ServiceLocator.CLIENT_CONTEXT).getObj(
      IPartnerProcessManagerHome.class.getName(),
      IPartnerProcessManagerHome.class,
      new Object[0]);
  }

  /**
   * This method obtains the EJBObject for the ChannelManagerBean
   *
   * @return ChannelManagerBean
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.3
   */
  public static IChannelManagerObj getChannelManager()
    throws ServiceLookupException
  {
    return (IChannelManagerObj)ServiceLocator.instance(
    ServiceLocator.CLIENT_CONTEXT).getObj(
    IChannelManagerHome.class.getName(),
    IChannelManagerHome.class,
    new Object[0]);
  }

  /**
   * This method will search and trigger the PartnerFunction base on the
   * TriggerInfo passed in.
   *
   * @param gdoc The GridDocument to be process by the Partnerfunction.
   * @param triggerInfo The TriggerInfo containing information on which
   *                    PartnerFunction to trigger.
   *
   * @since 2.0
   */
  public static void triggerPartnerFunction(GridDocument gdoc, TriggerInfo triggerInfo)
    throws Exception
  {
    String docType = triggerInfo.getDocType();
    String partnerType = triggerInfo.getPartnerType();
    String partnerGroup = triggerInfo.getPartnerGroup();
    String partnerId = triggerInfo.getPartnerId();
    Integer triggerType = triggerInfo.getTriggerType();
    Collection triggers = getManager().findTriggers(docType,
                                                    partnerType,
                                                    partnerGroup,
                                                    partnerId,
                                                    triggerType);

    Trigger trigger = (Trigger)triggers.iterator().next();
    String partnerFunctionId = trigger.getPartnerFunctionId();
    String processDefId = trigger.getProcessId();
    Boolean isRequest = trigger.isRequest();
    Boolean isLocalPending = trigger.isLocalPending();
    Long channelUID = trigger.getChannelUID();

    Logger.debug("[PartnerProcessDelegate.triggerPartnerFunction] trigger found is = "+trigger);
    Logger.debug("[PartnerProcessDelegate.triggerPartnerFunction] partnerFunctionId = "+partnerFunctionId);

    gdoc.setPartnerFunction(partnerFunctionId);
    gdoc.setLocalPending(isLocalPending);

    if (trigger.getTriggerType().equals(trigger.TRIGGER_IMPORT))
    {
      //set the channel in gDoc
      if (channelUID != null && !"-99999999".equals(channelUID.toString()))
      {
        gdoc.setRecipientChannelUid(channelUID);
        IChannelManagerObj channelManager = getChannelManager();
        ChannelInfo channelInfo = channelManager.getChannelInfo(channelUID);
        if (channelInfo != null)
        {
          gdoc.setRecipientChannelName(channelInfo.getName());
          gdoc.setRecipientChannelProtocol(channelInfo.getTptProtocolType());
        }
        else
        {
          throw new Exception("Error finding channel based on channelUID = " + channelUID);
        }
      }
      else
        channelUID = null;
      // else use the default channel of the partner. It will be set later on.

      //set the alarm if numOfRetries and retryInterval is not 0
      Integer numOfRetries = trigger.getNumOfRetries();
      Integer retryInterval = trigger.getRetryInterval();
      if ((numOfRetries != null) && (retryInterval != null) &&
        (numOfRetries.intValue() > 0) && (retryInterval.intValue() > 0) )
      {
        Long docUid = new Long(gdoc.getUId());
        Logger.debug("[PartnerProcessDelegate.triggerPartnerFunction] adding Alarm, "
          + "numRetries=" + numOfRetries + ",retryInterval=" + retryInterval +
          ",gdoc UID = " + docUid.longValue());
        //addAlarm(numOfRetries, retryInterval, docUid, channelUID);
        //NSL20060417 Delegate to helper class
        DocumentResendTimerHelper.addTimer(numOfRetries, retryInterval, gdoc.getFolder(), gdoc.getGdocId(), channelUID);
      }
    }

    if ((processDefId != null) && (!processDefId.equals("")))
    {
      Logger.debug("[PartnerProcessDelegate.triggerPartnerFunction] set ProcessDefId/isRequest ="+ processDefId + "/" + isRequest);
      gdoc.setProcessDefId(processDefId);
      gdoc.setIsRequest(isRequest);
      Logger.debug("[PartnerProcessDelegate.triggerPartnerFunction] processDefId =" + processDefId + " isRequest =" + isRequest);
    }
    double version = PartnerFunctionDelegate.getPartnerFunctionVersion(
                       partnerFunctionId);

    ArrayList ingdocs = new ArrayList();
    ingdocs.add(gdoc);
    HashMap gdocshm = new HashMap();
    gdocshm.put("main.gdocs", ingdocs);

    WorkflowClient wfClient = new WorkflowClient();
    String id = "http://XPDL/"+partnerFunctionId+"/"+version+"/XpdlProcess/"+partnerFunctionId;
    Logger.log("[PartnerProcessDelegate.triggerPartnerFunction] Creating Process with ID : "+id);
    wfClient.createRtProcess(id, null, gdocshm);
  }

  /*NSL20060417 Not required anymore
  private static void addAlarm(Integer numOfRetries, Integer retryInterval, Long docUid,
  {
    try
    {
      iCalAlarm alarm = new iCalAlarm();
      alarm.setCategory("DocumentResend");
      alarm.setSenderKey(docUid.toString());
      alarm.setReceiverKey(channelUID == null ? null : channelUID.toString());
      alarm.setRepeat(new Integer(numOfRetries.intValue() - 1));
      alarm.setDelayPeriod(new Long(retryInterval.longValue()));
      alarm.setStartDt(new Date(new Date().getTime() + retryInterval.longValue() * 1000));
      
      IiCalTimeMgrObj mgrObject = (IiCalTimeMgrObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(
                                      IiCalTimeMgrHome.class.getName(),
                                      IiCalTimeMgrHome.class,
                                      new Object[0]);
      
      mgrObject.addAlarm(alarm);
    }
    catch (Throwable t)
    {
      Logger.err("Error setting up alarm with numOfRetries=" + numOfRetries.intValue() +
        ", retryInterval=" + retryInterval.intValue() + ", docUid=" + docUid);
    }
  }
  */
}
