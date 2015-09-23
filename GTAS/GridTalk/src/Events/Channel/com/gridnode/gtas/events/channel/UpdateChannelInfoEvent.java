/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateChannelInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 08 2002    Goh Kan Mun             Created
 * Sep 16 2002    Jagadeesh               Added - isMaster/isPartner.
 * Sep 17 2002    Jagadeesh               Added - Packaging/Security Profiles ID's.
 * Oct 04 2002    Ang Meng Hua            Removed RefID, isPartner, PartnerCategoty
 *                                        and isDisable from event constructor
 */
package com.gridnode.gtas.events.channel;

/**
 * This event class contains the data for creation of a ChannelInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */
import com.gridnode.gtas.model.channel.ICommInfo;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.HashMap;

public class UpdateChannelInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -700055066251326038L;

	/**
   * FieldId for UId.
   */
  public static final String UID = "uId";

  /**
   * FieldId for Name.
   */
  public static final String NAME = "Name";

  /**
   * FieldId for Descrition.
   */
  public static final String DESCRIPTION = "Description";

  /**
   * FieldId for protocol type of transport communication information.
   */
  public static final String TPT_PROTOCOL_TYPE = "Transport Protocol Type";

  /**
   * FieldId for Transport Communication Information.
   */
  public static final String TPT_COMM_INFO = "Transport Communication Information";

  /**
   * Constant value for Transport protocol type.
   */
  public static final String JMS = ICommInfo.JMS;

  /**
   * FieldId for PackagingProfile information.
   */

  public static final String PACKAGING_PROFILE = "Packaging Profile";

  /**
   * FieldId for SecurityProfile information.
   */

  public static final String SECURITY_PROFILE = "Security Profile";

  /**
   * FieldId for FlowControlProfile information.
   */

  public static final String FLOWCONTROL_PROFILE = "FlowControl Profile";



  public UpdateChannelInfoEvent(
    Long uId,
    String name,
    String description,
    String tptProtocolType,
    Long tptCommInfoUId,
    Long packagingInfoUId,
    Long securityInfoUId,
    HashMap flowControlProfile)
    throws EventException
  {
    setEventData(UID, uId);
    setEventData(DESCRIPTION, description);
    checkSetString(NAME, name);
    checkSetString(TPT_PROTOCOL_TYPE, tptProtocolType);
    checkSetLong(TPT_COMM_INFO, tptCommInfoUId);
    checkSetLong(PACKAGING_PROFILE,packagingInfoUId);
    checkSetLong(SECURITY_PROFILE,securityInfoUId);
    checkSetObject(FLOWCONTROL_PROFILE,flowControlProfile,HashMap.class);
  }

  public Long getTptCommInfo()
  {
    return (Long) getEventData(TPT_COMM_INFO);
  }

  public String getProtocolType()
  {
    return (String) getEventData(TPT_PROTOCOL_TYPE);
  }

  public Long getUId()
  {
    return (Long) getEventData(UID);
  }

  public String getName()
  {
    return (String) getEventData(NAME);
  }

  public String getDescription()
  {
    return (String) getEventData(DESCRIPTION);
  }

  public Long getPackagingProfile()
  {
    return (Long) getEventData(PACKAGING_PROFILE);
  }

  public Long getSecurityProfile()
  {
    return (Long) getEventData(SECURITY_PROFILE);
  }

  public HashMap getFlowControlProfile()
  {
    return (HashMap)getEventData(FLOWCONTROL_PROFILE);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateChannelInfoEvent";
  }
}