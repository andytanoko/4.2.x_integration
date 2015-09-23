/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AddAccessRightEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 28 2002    Neo Sok Lay             Addd
 * Feb 25 2004    Neo Sok Lay             Implements IGuardedEvent
 */

package com.gridnode.gtas.events.acl;

import com.gridnode.gtas.events.IGuardedEvent;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventSupport;
import com.gridnode.pdip.framework.rpf.event.EventException;

/**
 * This event class contains the data for adding access rights to a Role.
 *
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1.19
 * @since 2.0
 */

public class AddAccessRightEvent extends EventSupport implements IGuardedEvent
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3699111692323304440L;
	private static final String GUARDED_FEATURE = "ROLE";
  private static final String GUARDED_ACTION = "AddAccessRight";
  
  /**
   * FieldId for RoleUID.
   */
  public static final String ROLE_UID = "Role UID";

  /**
   * FieldId for Feature.
   */
  public static final String FEATURE = "Feature";

  /**
   * FieldId for Description.
   */
  public static final String DESCRIPTION = "Description";

  /**
   * FieldId for Granted Actions.
   */
  public static final String GRANTED_ACTION = "Granted Action";

  /**
   * FieldId for Granted Data Type.
   */
  public static final String GRANTED_DATA_TYPE = "Granted Data Type";

  /**
   * FieldId for Grant Criteria.
   */
  public static final String GRANT_CRITERIA = "Grant Criteria";

  public AddAccessRightEvent(
    String description, Long roleUId, String feature,
    String grantedAction) throws EventException
  {
    checkSetString(DESCRIPTION, description);
    checkSetLong(ROLE_UID, roleUId);
    checkSetString(FEATURE, feature);
    checkSetString(GRANTED_ACTION, grantedAction);
  }

  public AddAccessRightEvent(
    String description, Long roleUId, String feature,
    String grantedAction, String grantedDataType, IDataFilter grantCriteria)
    throws EventException
  {
    checkSetString(DESCRIPTION, description);
    checkSetLong(ROLE_UID, roleUId);
    checkSetString(FEATURE, feature);
    checkSetString(GRANTED_ACTION, grantedAction);
    setEventData(GRANTED_DATA_TYPE, grantedDataType);
    setEventData(GRANT_CRITERIA, grantCriteria);
  }

  public String getDescription()
  {
    return (String)getEventData(DESCRIPTION);
  }

  public Long getRoleUID()
  {
    return (Long)getEventData(ROLE_UID);
  }

  public String getFeature()
  {
    return (String)getEventData(FEATURE);
  }

  public String getGrantedAction()
  {
    return (String)getEventData(GRANTED_ACTION);
  }

  public String getGrantedDataType()
  {
    return (String)getEventData(GRANTED_DATA_TYPE);
  }

  public IDataFilter getGrantCriteria()
  {
    return (IDataFilter)getEventData(GRANT_CRITERIA);
  }

  public String getEventName()
  {
    return "java:comp/env/param/event/AddAccessRightEvent";
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