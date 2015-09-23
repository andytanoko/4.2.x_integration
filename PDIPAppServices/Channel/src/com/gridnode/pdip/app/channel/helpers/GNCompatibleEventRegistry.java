/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNCompatibleEventRegistry.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * NOV 14 2002    Jagadeesh               Created
 * Nov 28 2002    Neo Sok Lay             Add Unzip set.
 * Jan 30 2003    Kan Mun                 Modified - Add methods and field for eventId that
 *                                                   will invoke file split.
 * Apr 03 2003    Kan Mun                 Modified - Add methods and field for split event Id.
 *                                        Modified - Add methods and field for split ack event Id.
 */

package com.gridnode.pdip.app.channel.helpers;

import com.gridnode.pdip.app.channel.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;

import java.util.*;

public class GNCompatibleEventRegistry
{

  private static final String CLASS_NAME = "GNCompatibleEventRegistry";
  private static final String SEPARATOR = ",";
  private static final String SEPARATOR2 = "|";

  private static GNCompatibleEventRegistry _self = null;
  private static Object lock = new Object();

  //  private HashSet _encryptSet = null;
  //  private HashSet _signSet = null;

  private HashSet _encryptNoneSet = null;
  private HashSet _encryptNoSign = null;
  private HashSet _relaySet = null;

  private HashSet _unzipSet = null;
  private HashSet _zipSet = null;
  private HashSet _splitSet = null;
  private HashSet _decryptExcludeFirsttowSet = null;
  private HashSet _decryptExcludeFirst = null;

  private HashSet _splitAckSet = null;

  private HashSet _splitAckEventSet = null;
  private HashSet _splitEventSet = null;

  private HashSet _encryptRealyEventSet = null;
  private HashSet _decryptRelayEventSet = null;

  private Map _splitEventAndAckMap = null;
  //FilePart Relay Events are the events the receiver can receive from GM or (other GT acting as relay).
  private HashSet _relayEventMap = null;

  private GNCompatibleEventRegistry()
  {
    ChannelLogger.debugLog(
      CLASS_NAME,
      "GNCompatibleEventRegistry()",
      "In GNCompatibleEventRegistry");
    //      _encryptSet = new HashSet();
    //      _signSet = new HashSet();
    _encryptNoneSet = new HashSet();
    _encryptNoSign = new HashSet();
    _zipSet = new HashSet();
    _unzipSet = new HashSet();
    _splitSet = new HashSet();
    _decryptExcludeFirsttowSet = new HashSet();
    _decryptExcludeFirst = new HashSet();
    _relaySet = new HashSet();
    _splitAckSet = new HashSet();
    _splitAckEventSet = new HashSet();
    _splitEventSet = new HashSet();
    _encryptRealyEventSet = new HashSet();
    _decryptRelayEventSet = new HashSet();
    _splitEventAndAckMap = new HashMap();
    _relayEventMap = new HashSet();

    Configuration config =
      ConfigurationManager.getInstance().getConfig(
        IChannelConfig.TPTIMPL_02000_CONFIG_NAME);
    Properties gneventProps = config.getProperties();
    boolean isInitlilize = initilizeEventList(gneventProps);
    if (isInitlilize)
      ChannelLogger.debugLog(
        CLASS_NAME,
        "GNCompatibleEventRegistry",
        "GNCompatibleEventRegistry Initilized Successfully");
    else
      ChannelLogger.debugLog(
        CLASS_NAME,
        "GNCompatibleEventRegistry",
        "GNCompatibleEventRegistry is Not Initilized...Please Check");
  }

  public static GNCompatibleEventRegistry getInstance()
  {
    if (_self == null)
    {
      synchronized (lock)
      {
        if (_self == null)
          _self = new GNCompatibleEventRegistry();
      }
    }
    return _self;
  }

  private boolean initilizeEventList(Properties gnProperties)
  {
    try
    {
      if (gnProperties != null)
      {
        ChannelLogger.infoLog(
          CLASS_NAME,
          "initilizeEventList()",
          "In gnProperties Begin");

        //        String encryptSet = gnProperties.getProperty(
        //        IChannelConfig.GNCOMPATIBLE_ENCRYPT_EVENTLIST);
        //        parseAndSet(_encryptSet,encryptSet);
        //
        //        String signSet = gnProperties.getProperty(
        //        IChannelConfig.GNCOMPATIBLE_SIGN_EVENTLIST);
        //        parseAndSet(_signSet,signSet);

        String relaySet =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_RELAY_CHANNEL);
        parseAndSet(_relaySet, relaySet);

        String encryptNoneSet =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_ENCRYPT_NONE);
        parseAndSet(_encryptNoneSet, encryptNoneSet);

        String encryptNoSign =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_ENCRYPT_NOSIGN);
        parseAndSet(_encryptNoSign, encryptNoSign);

        String unzipSet =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_UNZIP);
        parseAndSet(_unzipSet, unzipSet);

        String zipSet =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_ZIP);
        parseAndSet(_zipSet, zipSet);

        String splitSet =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_SPLIT);
        parseAndSet(_splitSet, splitSet);

        String decryptExcludeFirstTwoSet =
          gnProperties.getProperty(
            IChannelConfig.TPTIMPL_02000_DECRYPT_EXCLUDE_FIRSTTWO);
        parseAndSet(_decryptExcludeFirsttowSet, decryptExcludeFirstTwoSet);

        String decryptExcludeFirst =
          gnProperties.getProperty(
            IChannelConfig.TPTIMPL_02000_DECRYPT_EXCLUDE_FIRST);
        parseAndSet(_decryptExcludeFirst, decryptExcludeFirst);

        String splitAck =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_SPLIT_ACK);
        parseAndSet(_splitAckSet, splitAck);

        String splitAckEvent =
          gnProperties.getProperty(
            IChannelConfig.TPTIMPL_02000_SPLIT_ACK_EVENT);
        parseAndSet(_splitAckEventSet, splitAckEvent);

        String splitEvent =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_SPLIT_EVENT);
        parseAndSet(_splitEventSet, splitEvent);

        String encryptRelay =
          gnProperties.getProperty(
            IChannelConfig.TPTIMPL_02000_ENCRYPT_RELAYCHANNEL_EVENT);
        parseAndSet(_encryptRealyEventSet, encryptRelay);

        String decryptRelay =
          gnProperties.getProperty(
            IChannelConfig.TPTIMPL_02000_DECRYPT_RELAYCHANNEL_EVENT);
        parseAndSet(_decryptRelayEventSet, decryptRelay);

        String splitEventAndAck =
          gnProperties.getProperty(
            IChannelConfig.TPTIMPL_02000_SPLIT_EVENT_AND_ACK);
        parseAndSet(_splitEventAndAckMap, splitEventAndAck);

        String filePartEvent =
          gnProperties.getProperty(IChannelConfig.TPTIMPL_02000_RELAY_EVENT);

        parseAndSet(_relayEventMap, filePartEvent);

        return true;
      }
      else
      {
        ChannelLogger.infoLog(
          CLASS_NAME,
          "initilizeEventList()",
          "GNCompatible Properties is Null");
      }
    }
    catch (Exception ex)
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "initilizeEventList",
        "Cannot Initilize the EventList ",
        ex);
    }
    return false;
  }

  private void parseAndSet(HashSet eventSet, String eventList)
  {
    if (eventList != null)
    {
      StringTokenizer st = new StringTokenizer(eventList, SEPARATOR);
      while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        ChannelLogger.debugLog(
          CLASS_NAME,
          "parseAndSet()",
          "EventID Set--> " + token);
        eventSet.add(token);
      }
    }
    else
    {
      ChannelLogger.debugLog(CLASS_NAME, "parseAndSet()", "EventList is Null ");
    }
  }

  /*  public boolean isEncrypt(String eventId)
    {
     return _encryptSet.contains(eventId);
    }
  
    public boolean isSign(String eventId)
    {
     return _signSet.contains(eventId);
    }
  
  */

  public boolean isEncryptNone(String eventId)
  {
    return _encryptNoneSet.contains(eventId);
  }

  public boolean isEncryptNoSign(String eventId)
  {
    return _encryptNoSign.contains(eventId);
  }

  public boolean isZip(String eventId)
  {
    return _zipSet.contains(eventId);
  }

  public boolean isUnzip(String eventId)
  {
    return _unzipSet.contains(eventId);
  }

  public boolean isSplit(String eventId)
  {
    return _splitSet.contains(eventId);
  }

  public boolean isDecryptExcludeFirstTow(String eventId)
  {
    return _decryptExcludeFirsttowSet.contains(eventId);
  }

  public boolean isDecryptExcludeFirst(String eventId)
  {
    return _decryptExcludeFirst.contains(eventId);
  }

  public boolean isRelay(String eventId)
  {
    return _relaySet.contains(eventId);
  }

  public boolean isSplitAck(String eventId)
  {
    return _splitAckSet.contains(eventId);
  }

  public boolean isSplitAckEvent(String eventId)
  {
    return _splitAckEventSet.contains(eventId);
  }

  public boolean isSplitEvent(String eventId)
  {
    return _splitEventSet.contains(eventId);
  }

  public boolean isRealyChannelEncrypt(String eventId)
  {
    return _encryptRealyEventSet.contains(eventId);
  }

  public boolean isRelayChannelDecrypt(String eventId)
  {
    return _decryptRelayEventSet.contains(eventId);
  }

  private void parseAndSet(Map eventTable, String eventList)
  {
    if (eventList != null)
    {
      StringTokenizer st = new StringTokenizer(eventList, SEPARATOR);
      String value = null;
      String key = null;
      while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        ChannelLogger.infoLog(
          CLASS_NAME,
          "parseAndSet()",
          "Event and Ack Pair list--> " + token);
        int index = token.indexOf(SEPARATOR2);
        key = token.substring(0, index);
        value = token.substring(index + 1);
        eventTable.put(key, value);
      }
    }
    else
    {
      ChannelLogger.warnLog(
        CLASS_NAME,
        "parseAndSet",
        "[Event and Ack list is Null]");
    }
  }

  public String getSplitEventAckId(String eventId)
  {
    return (String) _splitEventAndAckMap.get(eventId);
  }

  public String getSplitEventId(String ackEventId)
  {
    if (_splitEventAndAckMap.containsValue(ackEventId))
    {
      synchronized (_splitEventAndAckMap)
      {
        Iterator iter = _splitEventAndAckMap.keySet().iterator();
        while (iter.hasNext())
        {
          Object o = iter.next();
          if (ackEventId.equals(_splitEventAndAckMap.get(o)))
            return (String) o;
        }
      }
    }
    return null;
  }

  public boolean isRelayEvent(String eventId)
  {
    return _relayEventMap.contains(eventId);
  }

}