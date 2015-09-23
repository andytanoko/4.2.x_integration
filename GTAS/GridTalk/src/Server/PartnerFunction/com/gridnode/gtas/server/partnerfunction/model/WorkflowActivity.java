/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WorkflowActivity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.Vector;

/**
 * This is an object model for WorkflowActivity entity.<P>
 *
 * The Model:<BR><PRE>
 *   UId          - UID for a WorkflowActivity entity instance.
 *   ActivityType - Type of the WorkflowActivity.
 *   Description  - Description of the WorkflowActivity.
 *   ParamList    - Vector containing the parameters for the WorkflowActivity
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
public class WorkflowActivity
  extends    AbstractEntity
  implements IWorkflowActivity
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8250935054348690539L;
	protected Integer   _activityType;
  protected String    _description;
  protected Vector    _paramList;

  public WorkflowActivity()
  {
    _paramList = new Vector();
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getActivityType()+"-"+getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public Integer getActivityType()
  {
    return _activityType;
  }

  public String getDescription()
  {
    return _description;
  }

  public Vector getParamList()
  {
    return _paramList;
  }

  // *************** Setters for attributes *************************

  public void setActivityType(Integer activityType)
  {
    _activityType = activityType;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void addParam(Object object)
  {
    _paramList.add(object);
  }

}