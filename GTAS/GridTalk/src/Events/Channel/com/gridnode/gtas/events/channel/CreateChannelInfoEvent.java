/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateChannelInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 05 2002    Goh Kan Mun             Created.
 * Jul 12 2002    Goh Kan Mun             Modified - Change of Class name from Add to Create.
 * Oct 04 2002    Ang Meng Hua            Removed RefID, IsDisable and PartnerCategory
 *                                        from event constructor
 *
 * Dec 22 2003    Jagadeesh               Modified - Added FlowControlProfile Embedded Entity.
 */
package com.gridnode.gtas.events.channel;

/**
 * This event class contains the data for creation of a CommInfo.
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

import java.util.Map;
import java.util.HashMap;

public class CreateChannelInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1730341192194820983L;

	/**
   * FieldId for Name given by the user.
   */
  public static final String NAME = "Name";

  /**
   * FieldId for Description given by the user.
   */
  public static final String DESCRIPTION = "Description";

  /**
   * FieldId for protocol type of Transport communication infomation.
   */
  public static final String TPT_PROTOCOL_TYPE = "Transport Protocol Type";

  /**
   * FieldId for Transport Communication Information.
   */
  public static final String TPT_COMMINFO = "Transport Communication";

  /**
   * Static value to represent transport protocol type.
   */
  public static final String JMS = ICommInfo.JMS;

  /**
   * FieldId for isPartner information.
   */
  public static final String IS_PARTNER = "IS Partner";

  /**
   * FieldId for isMaster information.
   */

//  public static final String IS_MASTER = "Is Master";

  /**
   * FieldId for PackagingProfile information.
   */

  public static final String PACKAGING_PROFILE = "Packaging Profile";

  /**
   * FieldId for SecurityProfile information.
   */

  public static final String SECURITY_PROFILE = "Security Profile";

  /**
   * FieldId for Partner Category.
   */

  public static final String PARTNER_CAT = "Partner Category";

  /**
   * FieldId for Is Disable.
   */

  public static final String IS_DISABLE  = "IS Disable";

  /**
   * FieldId for Is Disable.
   */

  public static final String FLOWCONTROL_PROFILE = "FlowControl Profile";



  public CreateChannelInfoEvent(
    String name,
    String description,
    String tptProtocolType,
    Long commInfoUId,
    Boolean isPartner,
    Long packagingInfoUId,
    Long securityInfoUId,
    Map flowControlProfile
    )
    throws EventException
  {
    setEventData(DESCRIPTION, description);
    checkSetString(NAME, name);
    //setEventData(REF_ID, referenceId);
    checkSetString(TPT_PROTOCOL_TYPE, tptProtocolType);
    checkSetLong(TPT_COMMINFO, commInfoUId);
    checkSetLong(PACKAGING_PROFILE,packagingInfoUId);
    checkSetLong(SECURITY_PROFILE,securityInfoUId);
    setEventData(IS_PARTNER,isPartner);
    setEventData(FLOWCONTROL_PROFILE,flowControlProfile);
  }

  public String getDescription()
  {
    return (String) getEventData(DESCRIPTION);
  }

  public String getName()
  {
    return (String) getEventData(NAME);
  }

//  public String getReferenceId()
//  {
//    return (String) getEventData(REF_ID);
//  }

  public Long getCommInfo()
  {
    return (Long) getEventData(TPT_COMMINFO);
  }

  public String getTptProtocolType()
  {
    return (String) getEventData(TPT_PROTOCOL_TYPE);
  }

/*  public Boolean isMasterChannel()
  {
    return (Boolean) getEventData(IS_MASTER);
  }
*/
  public Boolean isPartner()
  {
    return  (Boolean) getEventData(IS_PARTNER);
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


//  public String getPartnerCategory()
//  {
//    return (String) getEventData(PARTNER_CAT);
//  }
//
//  public Boolean isDisable()
//  {
//    return (Boolean) getEventData(IS_DISABLE);
//  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreateChannelInfoEvent";
  }


}