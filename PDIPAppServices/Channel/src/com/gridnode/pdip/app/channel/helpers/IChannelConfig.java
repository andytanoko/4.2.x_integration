/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IChannelConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 16 2002    Jagadeesh               Created.
 * Nov 28 2002    Neo Sok Lay             Add TPTIMPL_02000_UNZIP property.
 * Jan 24 2003    Jagadeesh               Added: RNIF Packaging Handler.
 * Jan 30 2003    Goh Kan Mun             Add TPTIMPL_02000_SPLIT_ACK property.
 * Apr 02 2003    Goh Kan Mun             Add TPTIMPL_02000_SPLIT_ACK_EVENT property.
 *                                        Add TPTIMPL_02000_SPLIT_EVENT property.
 * Jan 29 2007    Neo Sok Lay             Rename CHANNEL_TOPIC* to CHANNEL_DESTINATION
 *                                        Rename APPSERVER_TOPIC_CONN* to APPSERVER_CONN*                                       
 */

package com.gridnode.pdip.app.channel.helpers;

public interface IChannelConfig
{

  public static final String CONFIG_NAME = "channel";

  public static final String RECEIVEEVENT_CONFIG_NAME =
    "channel.receive.eventlist";

  public static final String FEEDBACKEVENT_CONFIG_NAME =
    "channel.feedback.eventlist";

  public static final String TPTIMPL_VERSION = "02000";

  public static final String TPTIMPL_02000_CONFIG_NAME =
    "tptimpl.02000.eventlist";

  public static final String FLOWCONTROL_CONFIG_NAME =
    "channel.flowcontrol.eventlist";

  public static final String DEFAULT_CHANNEL_HANDLER = "default.channelhandler";

  public static final String HTTP_CHANNEL_HANDLER = "http.channelhandler";

  public static final String TPTIMPL_02000_RELAY_CHANNEL =
    "tptimpl.02000.relaychannel";

  public static final String TPTIMPL_02000_ENCRYPT_NONE =
    "tptimpl.02000.encryptnone";

  public static final String TPTIMPL_02000_ENCRYPT_NOSIGN =
    "tptimpl.02000.encryptnosign";

  public static final String TPTIMPL_02000_ZIP = "tptimpl.02000.zip";

  public static final String TPTIMPL_02000_UNZIP = "tptimpl.02000.unzip";

  public static final String TPTIMPL_02000_SPLIT = "tptimpl.02000.split";

  public static final String TPTIMPL_02000_DECRYPT_EXCLUDE_FIRSTTWO =
    "tptimpl.02000.excludefirsttwo";

  public static final String TPTIMPL_02000_DECRYPT_EXCLUDE_FIRST =
    "tptimpl.02000.excludefirst";

  public static final String CHANNEL_DESTINATION_APP_TO_SEND =
    "appserver.channel.tolistener_send";

  public static final String APPSERVER_CONNECTION_FACTORY =
    "appserver.jndi.connection_factory";

  public static final String RNIF_GTASPACKAGING_HANDLER =
    "rnif.gtas.packaginghandler";
  /** Keys for GTAS Receive Message Handler */

  public static final String DEFAULT_CHANNEL_RECEIVE_HANDLER =
    "default.channel.receive.handler";

  public static final String BACKWARDCOMPATIBLE_CHANNEL_RECEIVE_HANDLER =
    "backwardcompatible.channel.receive.handler";

  public static final String NONGTAS_RN_CHANNEL_RECEIVE_HANDLER =
    "nongtas.rn.channel.receive.handler";

  public static final String NONGTAS_RN_PACKAGING_HANDLER =
    "nongtas.rn.packaging.handler";

  public static final String TPTIMPL_02000_SPLIT_ACK = "tptimpl.02000.splitAck";

  public static final String TPTIMPL_02000_SPLIT_ACK_EVENT =
    "tptimpl.02000.split.ack.event";

  public static final String TPTIMPL_02000_SPLIT_EVENT =
    "tptimpl.02000.split.event";

  public static final String TPTIMPL_02000_ENCRYPT_RELAYCHANNEL_EVENT =
    "tptimpl.02000.encrypt.relaychannel";

  public static final String TPTIMPL_02000_DECRYPT_RELAYCHANNEL_EVENT =
    "tptimpl.02000.decrypt.relaychannel";

  public static final String TPTIMPL_02000_SPLIT_EVENT_AND_ACK =
    "tptimpl.02000.splitEventAndAck";

  public static final String TPTIMPL_02000_RELAY_EVENT =
    "tptimpl.02000.relayevent";

  public static final String FLOWCONTROL_DISABLE_SPLIT =
    "flowcontrol.disable.split.event";

  public static final String FLOWCONTROL_DISABLE_ZIP =
    "flowcontrol.disable.zip.event";

}