/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EventNotifier.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 6, 2006    i00107              Created
 * Jan 05 2007    i00107              Send events based on configurations.
 * Feb 06 2007    i00107              Add in source/destination.
 * Feb 24 2007    i00107              Add event remark.
 * Mar 17 2007    i00107              Change to use Castor for conversion to XML.
 * Apr 19 2007    i00107              GNDB00028304: Event occur time not 'current' for
 *                                    Document deliveries.
 */

package com.gridnode.gridtalk.httpbc.common.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

import javax.naming.NamingException;

import com.gridnode.gridtalk.httpbc.common.exceptions.ILogErrorCodes;
import com.gridnode.gridtalk.httpbc.common.model.*;
import com.gridnode.util.ExceptionUtil;
import com.gridnode.util.alert.AlertUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.jms.JmsRetrySender;
import com.gridnode.util.jms.JmsSender;
import com.gridnode.util.jndi.JndiFinder;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;
import com.gridnode.util.xml.XMLBeanUtil;

/**
 * @author i00107
 * This class is responsible for notifying the document events that are 
 * interested for auditing.
 */
public class EventNotifier
{
  public static final String EVENT_NAME = "event.name";
  public static final String EVENT_TYPE = "event.type";
  public static final String EVENT_STATUS = "event.status";
  public static final String EVENT_ROOT = "event.root";
  public static final String EVENT_ERROR = "event.error";
  private static final String SOURCE_OR_DEST = "source.dest"; 
  private static final String EVENT_REMARK = "event.remark";
  private static final String EVENT_TIME = "event.time";
  
  private JmsSender _jmsSender;
  private static EventNotifier _self = null;
  private Logger _logger;
  
  private JmsRetrySender _retrySender;
  private Properties _sendProps;
  
  private EventNotifier()
  {
    init();
  }
  
  public static final synchronized EventNotifier getInstance()
  {
    if (_self == null)
    {
      _self = new EventNotifier();
    }
    return _self;
  }
  
  private void init()
  {
    _logger = LoggerManager.getInstance().getLogger(ILogTypes.TYPE_HTTPBC_COMMON, this.getClass().getSimpleName());
    _sendProps = ConfigurationStore.getInstance().getProperties(IConfigCats.EVENT_NOTFN);
    
    _jmsSender = new JmsSender();
    _jmsSender.setSendProperties(_sendProps);

    _retrySender = new JmsRetrySender();
  }
  
  /**
   * Called when a document is received successfully i.e. saved in db for processing later.
   * @param source The source that the document came from.
   * @param tDoc The saved document.
   */
  public void onDocumentReceived(String source, TransactionDoc tDoc)
  {
    Properties props = ConfigurationStore.getInstance().getProperties(IConfigCats.EVENT_NOTFN_DOC_RECEIVED);
    if (props.isEmpty())
    {
      _logger.debugMessage("onDocumentReceived", null, "No properties configured for "+IConfigCats.EVENT_NOTFN_DOC_RECEIVED);
      return;
    }
    
    props.setProperty(SOURCE_OR_DEST, source);
    send(props, tDoc, true);
  }
  
  /**
   * Called when a documetn is delivered successfully to the destination.
   * @param destination The destination.
   * @param tDoc The document delivered.
   * @param remark Remark for the event
   */
  public void onDocumentDelivered(String destination, TransactionDoc tDoc, String remark)
  {
    Properties props = ConfigurationStore.getInstance().getProperties(IConfigCats.EVENT_NOTFN_DOC_DELIVERED);
    if (props.isEmpty())
    {
      _logger.debugMessage("onDocumentDelivered", null, "No properties configured for "+IConfigCats.EVENT_NOTFN_DOC_DELIVERED);
      return;
    }
    
    props.setProperty(EVENT_TIME, String.valueOf(System.currentTimeMillis())); //NSL20070419
    props.setProperty(SOURCE_OR_DEST, destination);
    if (remark != null)
    {
      props.setProperty(EVENT_REMARK, remark);
    }
    send(props, tDoc, false);
  }
  
  /**
   * Called when the document cannot be delivered to the destination.
   * @param destination The destination.
   * @param tDoc The document to be delivered.
   * @param errMsg The error message.
   * @param remark remark for the event
   */
  public void onDocumentDeliveryFailed(String destination, TransactionDoc tDoc, String errMsg, String remark)
  {
    Properties props = ConfigurationStore.getInstance().getProperties(IConfigCats.EVENT_NOTFN_DOC_DELIVERY_FAILED);
    if (props.isEmpty())
    {
      _logger.debugMessage("onDocumentDeliveryFailed", null, "No properties configured for "+IConfigCats.EVENT_NOTFN_DOC_DELIVERY_FAILED);
      return;
    }
    
    props.setProperty(EVENT_TIME, String.valueOf(System.currentTimeMillis())); //NSL20070419
    props.setProperty(SOURCE_OR_DEST, destination);
    props.setProperty(EVENT_ERROR, errMsg);
    if (remark != null)
    {
      props.setProperty(EVENT_REMARK, remark);
    }
    send(props, tDoc, false);
  }

  private void send(Properties eventProps, TransactionDoc tDoc, boolean attachDoc)
  {
    EventDoc[] eventDocs = attachDoc ? createEventDocs(tDoc.getDocContent(), tDoc.getAttContent()) : null;
    
    EventInfo eventInfo = new EventInfo();
    eventInfo.setBeID(tDoc.getBizEntId());
    eventInfo.setEventName(MessageFormat.format(eventProps.getProperty(EVENT_NAME), eventProps.getProperty(SOURCE_OR_DEST, "")));
    //eventInfo.setEventOccurredTime(new Date(tDoc.getTimestamp()));
    eventInfo.setEventOccurredTime(getEventOccurredTime(eventProps.getProperty(EVENT_TIME, null), tDoc.getTimestamp())); //NSL20070419
    eventInfo.setEventType(eventProps.getProperty(EVENT_TYPE));
    eventInfo.setStatus(eventProps.getProperty(EVENT_STATUS));
    eventInfo.setTracingID(tDoc.getTracingId());
    eventInfo.setErrorReason(eventProps.getProperty(EVENT_ERROR, null));
    eventInfo.setMessageID(tDoc.getMessageID());
    eventInfo.setEventRemark(eventProps.getProperty(EVENT_REMARK, null));
    
    EventTrail trail = new EventTrail();
    trail.setBizDocuments(eventDocs);
    trail.setEventInfo(eventInfo);
    
    //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
    //String trailXml = XMLBeanUtil.beanToXml(trail, eventProps.getProperty(EVENT_ROOT), df);
    //NSL20070317 Change to use Castor
    String trailXml = XMLBeanUtil.objToXml(trail, eventProps.getProperty(EVENT_ROOT));
    send(trailXml);
  }
  
  private Date getEventOccurredTime(String eventTime, long tDocTs)
  {
    if (eventTime == null)
    {
      return new Date(tDocTs);
    }
    try
    {
      long ts = Long.parseLong(eventTime);
      return new Date(ts);
    }
    catch (NumberFormatException ex)
    {
      _logger.logWarn("getEventOccurredTime", new Object[]{eventTime, tDocTs}, "Unable to convert to Date", ex);
      return new Date(tDocTs);
    }
  }
  
  private EventDoc[] createEventDocs(FileContent doc, FileContent[] atts)
  {
    EventDoc edDoc = createEventDoc(doc);
    
    if (atts == null || atts.length==0)
    {
      return new EventDoc[]{edDoc};
    }
    
    EventDoc[] eventDocs = new EventDoc[atts.length+1];
    eventDocs[0] = edDoc;
    for (int i=1; i<eventDocs.length; i++)
    {
      eventDocs[i] = createEventDoc(atts[i-1]);
    }
    return eventDocs;
  }
  
  private EventDoc createEventDoc(FileContent fc)
  {
    EventDoc eventDoc = new EventDoc();
    eventDoc.setDoc(fc.getContent());
    eventDoc.setFilename(fc.getFilename());
    eventDoc.setRequiredPack(true);
    return eventDoc;
  }
    
  private void send(Serializable obj)
  {
    try
    {
      if(_retrySender.isSendViaDefMode())
      {
        _jmsSender.send(obj);
      }
      else
      {
        JndiFinder finder = new JndiFinder(null, LoggerManager.getOneTimeInstance());
        finder.setJndiSuffix("_HC");
        _retrySender.setJNDIFinder(finder);
        _retrySender.retrySendWithPersist(obj, null, IConstantValue.FAILED_JMS_CAT, _sendProps, true);
      }
    }
    catch (Exception ex)
    {
      _logger.logError(ILogErrorCodes.EVENT_NOTIFIER_SEND, "send", null, "JMS Error. Unable to send event notification: "+ex.getMessage(), ex);
      AlertUtil.getInstance().sendAlert(IAlertKeys.UNEXPECTED_SYSTEM_ERROR,
                                        ILogTypes.TYPE_HTTPBC_COMMON, 
                                        "EventNotifier",
                                        "send",
                                        "Error sending event notification",
                                        ExceptionUtil.getStackStraceStr(ex));
    }
  }
}
