/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: Activity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 07 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Date;

/**
 * This is an object model for the Activity entity. A Activity describe what
 * is done to a user document. Example import, executing mapping rule, etc
 *
 * The Model:<BR><PRE>
 *   UId              - UID for a Activity entity instance.
 *   ActivityType     - Indicate which type this activity belongs to.
 *   Description      - Description of the activity.
 *   DateTime         - Date timestamp of when this activity occured.
 *   CanDelete        - Whether the activity can be deleted.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class Activity
  extends    AbstractEntity
  implements IActivity
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -1744526204808546986L;
	protected String  _activityType;
  protected String  _description;
  protected Date    _dateTime;

  public Activity()
  {
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getActivityType() + "-" + getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getActivityType()
  {
    return _activityType;
  }

  public String getDescription()
  {
    return _description;
  }

  public Date getDateTime()
  {
    return _dateTime;
  }

  // *************** Setters for attributes *************************

  public void setActivityType(String activityType)
  {
    _activityType = activityType;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setDateTime(Date dateTime)
  {
    _dateTime = dateTime;
  }

}