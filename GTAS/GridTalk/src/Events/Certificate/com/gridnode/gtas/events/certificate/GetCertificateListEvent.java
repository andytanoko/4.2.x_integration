/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetCertificateListEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Sep 13 2002    Jagadeesh              Created
 */


package com.gridnode.gtas.events.certificate;

import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.GetEntityListEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;

/**
 * This event class contains the data for retrieval of a Certificate.
 *
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */


public class GetCertificateListEvent  extends GetEntityListEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9200877905070228759L;

	public GetCertificateListEvent()
  {
    super();
  }

  public GetCertificateListEvent(IDataFilter filter)
  {
    super(filter);
  }

  public GetCertificateListEvent(IDataFilter filter, int maxRows)
  {
    super(filter, maxRows);
  }

  public GetCertificateListEvent(IDataFilter filter, int maxRows, int startRow)
  {
    super(filter, maxRows, startRow);
  }

  public GetCertificateListEvent(String listID, int maxRows, int startRow)
         throws EventException
  {
    super(listID, maxRows, startRow);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/GetCertificateListEvent";
  }


}

