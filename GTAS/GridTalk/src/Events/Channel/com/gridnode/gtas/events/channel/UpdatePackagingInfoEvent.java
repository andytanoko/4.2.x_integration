/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateJMSCommInfoEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Aug 27 2002    Jagadeesh             Created
 * Oct 04 2002    Ang Meng Hua            Removed RefID, isPartner, PartnerCategoty
 *                                        and isDisable from event constructor
 * Nov 03 2003    Guo Jianyu              Added PKG_INFO_EXTENSION and related methods
 * Dec 22 2003   Jagadeesh              Modified: Commented isZip,ZipThreshold,
 *                                                is now available in FlowControl Profile.
 */


package com.gridnode.gtas.events.channel;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.EventSupport;

import java.util.HashMap;

/**
 * This event class contains the data for Updating  of a PackagingInfo.
 *
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */


public class UpdatePackagingInfoEvent extends EventSupport
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -194514383598252372L;


	/**
   * FieldId for UId.
   */
  public static final String UID = "uId";


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
   * FieldId for packagingInfoExtension
   */
  public static final String PKG_INFO_EXTENSION = "Packaging Info Extension";

  public UpdatePackagingInfoEvent(
    Long   uId,
    String name,
    String description,
    String envelope,
    //Boolean zip,
    //Integer zipThreshold
    //Integer zipThreshold,
    HashMap pkgInfoExtension)
    throws EventException
  {
    setEventData(UID, uId);
    setEventData(NAME, name);
    setEventData(DESCRIPTION, description);
    setEventData(ENVELOPE, envelope);
//    checkSetObject(ZIP, zip,Boolean.class);
//    checkSetObject(ZIP_THRESHOLD,zipThreshold,Integer.class);
    checkSetObject(PKG_INFO_EXTENSION, pkgInfoExtension, HashMap.class);
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

  public Long getUId()
  {
    return (Long) getEventData(UID);
  }

  public HashMap getPkgInfoExtension()
  {
    return (HashMap) getEventData(PKG_INFO_EXTENSION);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdatePackagingInfoEvent";
  }
}

