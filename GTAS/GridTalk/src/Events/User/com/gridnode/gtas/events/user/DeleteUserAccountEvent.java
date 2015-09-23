/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DeleteUserAccountEvent.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 20 2002    Neo Sok Lay         Created
 * Jun 03 2002    Neo Sok Lay         Event data check.
 * Jun 14 2002    Neo Sok Lay         Implement IGuardedEvent
 * Jul 15 2003    Neo Sok Lay         Extend from DeleteEntityListEvent
 */
package com.gridnode.gtas.events.user;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.gtas.model.user.IUserAccount;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.util.Collection;

/**
 * This Event class contains the data for deleting a User Account based on
 * UID.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2 I1
 * @since 2.0
 */
public class DeleteUserAccountEvent
  extends DeleteEntityListEvent
  implements IGuardedEvent
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -970739778365797558L;
	public static final String GUARDED_FEATURE = "USER.ADMIN";
  public static final String GUARDED_ACTION = "DeleteUserAccount";

  /**
   * Constructor for DeleteUserAccountEvent.
   * @param accountUID UID of the UserAccount entity to delete.
   * @throws EventException Invalid UID specified.
   */
  public DeleteUserAccountEvent(Long accountUID) throws EventException
  {
    super(new Long[] { accountUID });
  }

  /**
   * Constructor for DeleteUserAccountEvent.
   * @param uids Collection of UIDs of the UserAccount entities to delete.
   * @throws EventException Invalid UIDs specified.
   */
  public DeleteUserAccountEvent(Collection uids) throws EventException
  {
    super(uids);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/DeleteUserAccountEvent";
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getEntityType()
   */
  public String getEntityType()
  {
    return IUserAccount.ENTITY_NAME;
  }

  /**
   * @see com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent#getKeyId()
   */
  public Number getKeyId()
  {
    return IUserAccount.UID;
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