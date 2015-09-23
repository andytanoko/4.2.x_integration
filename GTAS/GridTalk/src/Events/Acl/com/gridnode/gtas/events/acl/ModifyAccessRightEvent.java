/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ModifyAccessRightEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 29 2002    Neo Sok Lay             Created
 * Jun 21 2002    Neo Sok Lay             Allow Feature & Action for updating.
 * Feb 25 2004    Neo Sok Lay             Implements IGuardedEvent
 */

package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This event class contains the data for modifying access rights for a Role.
 *
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0
 */

public class ModifyAccessRightEvent extends EventSupport implements IGuardedEvent
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2344783808475213667L;
	private static final String GUARDED_FEATURE = "ROLE";
  private static final String GUARDED_ACTION = "ModifyAccessRight";

  /**
   * FieldId for UID.
   */
  public static final String ACCESS_RIGHT_UID = "AccessRight UID";

  /**
   * FieldId for Description.
   */
  public static final String UPD_DESCRIPTION = "Upd Description";

  /**
   * FieldId for Granted Data Type.
   */
  public static final String UPD_DATA_TYPE = "Upd Data Type";

  /**
   * FieldId for Grant Criteria.
   */
  public static final String UPD_CRITERIA = "Upd Criteria";

  /**
   * FieldID for Granted Feature
   */
  public static final String UPD_FEATURE  = "Upd Feature";

  /**
   * FieldID for Granted Action
   */
  public static final String UPD_ACTION   = "Upd Action";

  public ModifyAccessRightEvent(
    Long accessRightUID, String updDescription, String updDataType,
    IDataFilter updCriteria) throws EventException
  {
    checkSetLong(ACCESS_RIGHT_UID, accessRightUID);
    checkSetString(UPD_DESCRIPTION, updDescription);
    setEventData(UPD_DATA_TYPE, updDataType);
    setEventData(UPD_CRITERIA, updCriteria);
  }

  public ModifyAccessRightEvent(
    Long accessRightUID, String updDescription,
    String updFeature, String updAction, String updDataType,
    IDataFilter updCriteria) throws EventException
  {
    checkSetLong(ACCESS_RIGHT_UID, accessRightUID);
    checkSetString(UPD_DESCRIPTION, updDescription);
    checkSetString(UPD_FEATURE, updFeature);
    checkSetString(UPD_ACTION, updAction);
    setEventData(UPD_DATA_TYPE, updDataType);
    setEventData(UPD_CRITERIA, updCriteria);
  }

  public Long getAccessRightUID()
  {
    return (Long) getEventData(ACCESS_RIGHT_UID);
  }

  public String getUpdDescription()
  {
    return (String)getEventData(UPD_DESCRIPTION);
  }

  public String getUpdDataType()
  {
    return (String)getEventData(UPD_DATA_TYPE);
  }

  public IDataFilter getUpdCriteria()
  {
    return (IDataFilter)getEventData(UPD_CRITERIA);
  }

  public String getUpdFeature()
  {
    return (String)getEventData(UPD_FEATURE);
  }

  public String getUpdAction()
  {
    return (String)getEventData(UPD_ACTION);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/ModifyAccessRightEvent";
  }

  /**
   * @see com.gridnode.gtas.events.IGuardedEvent#getGuardedAction()
   */
  public String getGuardedAction()
  {
    return GUARDED_ACTION;
  }

  /**
   * @see com.gridnode.gtas.events.IGuardedEvent#getGuardedFeature()
   */
  public String getGuardedFeature()
  {
    return GUARDED_FEATURE;
  }

}