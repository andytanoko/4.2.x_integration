package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpdateCertificateEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * March 29 2004    Guo Jianyu            Created
 * August 01 2008	Wong Yee Wah		  #38  Added: set swapDate and swapTime into event
 */


public class UpdateCertificateEvent extends EventSupport
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8394868476630271453L;
	public static final String CERTIFICATE_UID  = "Certificate UID";
	public static final String CERT_NAME_NEW = "New Name";
	public static final String RELATED_CERT_UID = "Related Certificate UID";
	public static final String SWAP_DATE = "Swapping Date";
	public static final String SWAP_TIME = "Swapping Time";

  public UpdateCertificateEvent(Long certUID,String newName, Long relatedCertUid, String swapDate, String swapTime) throws EventException
  {
    setEventData(CERTIFICATE_UID, certUID);
    checkSetString(CERT_NAME_NEW,newName);
    if (relatedCertUid == null)
      setEventData(RELATED_CERT_UID, null);
    else
      checkSetLong(RELATED_CERT_UID, relatedCertUid);
    setEventData(SWAP_DATE, swapDate);
    setEventData(SWAP_TIME, swapTime);
  }

  
  public Long getCertificateUID()
  {
    return (Long)getEventData(CERTIFICATE_UID);
  }

  public String getNewName()
  {
    return (String)getEventData(CERT_NAME_NEW);
  }

  public Long getRelatedCertUid()
  {
    return (Long)getEventData(RELATED_CERT_UID);
  }
  
  public String getSwapDate()
  {
    return (String)getEventData(SWAP_DATE);
  }
  
  public String getSwapTime()
  {
    return (String)getEventData(SWAP_TIME);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/UpdateCertificateEvent";
  }
}