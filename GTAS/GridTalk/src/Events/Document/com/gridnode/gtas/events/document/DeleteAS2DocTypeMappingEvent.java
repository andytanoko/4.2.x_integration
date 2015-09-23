/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-08-28    Wong Yee Wah         Created
 */
package com.gridnode.gtas.events.document;

import java.util.Collection;

import com.gridnode.gtas.model.document.IAS2DocTypeMapping;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

public class DeleteAS2DocTypeMappingEvent extends DeleteEntityListEvent
{
  private static final long serialVersionUID = 2672295211478927458L;
  
  
  /**
   * Constructor for DeleteAS2DocTypeMappingEvent.
   * @param uids Collection of UIDs of AS2DocTypeMapping entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteAS2DocTypeMappingEvent(Collection uids) throws EventException
  { 
    super(uids);
  }

  /**
   * Constructor for DeleteAS2DocTypeMappingEvent.
   * @param AS2DocTypeMapping UID of AS2DocTypeMapping entity to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteAS2DocTypeMappingEvent(Long typeMappingUID) throws EventException
  {
    super(new Long[] { typeMappingUID });
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteAS2DocTypeMappingEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IAS2DocTypeMapping.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IAS2DocTypeMapping.UID;
  }
}
