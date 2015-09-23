/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RemoveCertificateEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Apr 28 2003    Qingsong                Created
 * Jul 14 2003    Neo Sok Lay             Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.certificate;

import com.gridnode.gtas.model.certificate.ICertificate;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

public class RemoveCertificateEvent extends DeleteEntityListEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7655504222191542867L;

	/**
   * Constructor for RemoveCertificateEvent.
   * @param uids Collection of UIDs of the Certificate entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public RemoveCertificateEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  /**
   * Constructor for RemoveCertificateEvent.
   * @param certUID UID of the Certificate entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public RemoveCertificateEvent(Long certUID) throws EventException
  {
    super(new Long[] { certUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/RemoveCertificateEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return ICertificate.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return ICertificate.UID;
  }
}