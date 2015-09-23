/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CreateJMSCommInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh               Created
 * Oct 04 2002    Ang Meng Hua            Removed RefID, IsDisable and PartnerCategory
 *                                        from event constructor
 *                                        Added IsPartner in event constructor
 * Nov 03 2003    Guo Jianyu              Added PKG_INFO_EXTENSION and related methods
 */


package com.gridnode.gtas.events.channel;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.HashMap;

/**
 * This event class contains the data for creation of a CommInfo.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */


public class CreatePackagingInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8116495652993460675L;

	/**
   * FieldId for Name.
   */
  public static String NAME = "Name";

  /**
   * FieldId for Description.
   */
  public static final String DESCRIPTION = "Description";

  /**
   * FieldId for Envelope.
   */

  public static final String ENVELOPE = "Envelope";

  /**
   * FieldId for Zip.
   */

  public static final String ZIP = "Zip";

  /**
   * FieldId for ZipThreshold.
   */

  public static final String ZIP_THRESHOLD = "ZipThreshold";

  /**
   * FieldId for isPartner information.
   */
  public static final String IS_PARTNER = "IS Partner";

  /**
   * FieldId for packagingInfoExtension
   */
  public static final String PKG_INFO_EXTENSION = "Packaging Info Extension";

  public CreatePackagingInfoEvent(
    String name,
    String description,
    String envelope,
    //Boolean zip,
    //Integer zipThreshold,
    Boolean isPartner,
    HashMap pkgInfoExtension) throws EventException
  {
    setEventData(NAME, name);
    setEventData(DESCRIPTION, description);
    setEventData(ENVELOPE, envelope);
    //checkSetObject(ZIP, zip,Boolean.class);
    //checkSetObject(ZIP_THRESHOLD,zipThreshold,Integer.class);
    setEventData(IS_PARTNER,isPartner);
    //setEventData(REF_ID, referenceId);
    //setEventData(PARTNER_CAT,partnerCategory);
    //setEventData(IS_DISABLE,isDisable);

    setEventData(PKG_INFO_EXTENSION, pkgInfoExtension);
  }


  public String getName()
  {
    return (String)getEventData(NAME);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

/*  public boolean isZIP()
  {
    return ((Boolean)getEventData(ZIP)).booleanValue();
  }
*/
  public String getEnvelope()
  {
    return (String)getEventData(ENVELOPE);
  }

/*  public int getZipThreshold()
  {
    return ((Integer)getEventData(ZIP_THRESHOLD)).intValue();
  }
*/

//  public String getReferenceId()
//  {
//    return (String) getEventData(REF_ID);
//  }
//  public String getPartnerCategory()
//  {
//    return (String) getEventData(PARTNER_CAT);
//  }
//
//  public Boolean isDisable()
//  {
//    return (Boolean) getEventData(IS_DISABLE);
//  }

  public Boolean isPartner()
  {
    return  (Boolean) getEventData(IS_PARTNER);
  }

  public HashMap getPkgInfoExtension()
  {
    return (HashMap) getEventData(PKG_INFO_EXTENSION);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/CreatePackagingInfoEvent";
  }


}

