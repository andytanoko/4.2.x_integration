/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteBusinessEntityEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 30 2002    Neo Sok Lay         Created
 * Jul 14 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 * Feb 25 2004    Neo Sok Lay         Change GUARDED_FEATURE to "ENTERPRISE".
 */
package com.gridnode.gtas.events.bizreg;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.bizreg.IBusinessEntity;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting one or more BusinessEntitoes
 * based on UID.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2.10
 * @since 2.0 I4
 */
public class DeleteBusinessEntityEvent
  extends DeleteEntityListEvent
  implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -726696737525353294L;
	public static final String GUARDED_FEATURE = "ENTERPRISE";
  public static final String GUARDED_ACTION = "DeleteBusinessEntity";

  /**
   * Constructor for DeleteBusinessEntityEvent
   * 
   * @param beUID UID of the BusinessEntity entity to delete
   * @throws EventException Invalid UID specified.
   */
  public DeleteBusinessEntityEvent(Long beUID) throws EventException
  {
    super(new Long[] { beUID });
  }

  /**
   * Constructor for DeleteBusinessEntityEvent
   * 
   * @param beUIDs Collection of UIDs of the BusinessEntity entities to delete
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteBusinessEntityEvent(Collection beUIDs) throws EventException
  {
    super(beUIDs);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteBusinessEntityEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IBusinessEntity.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IBusinessEntity.UID;
  }

  // ************* From IGuardedEvent *************************

  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }
}