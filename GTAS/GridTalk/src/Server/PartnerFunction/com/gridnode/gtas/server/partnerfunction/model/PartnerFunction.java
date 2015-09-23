/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PartnerFunction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

import java.util.ArrayList;

/**
 * This is an object model for PartnerFunction entity.<P>
 *
 * The Model:<BR><PRE>
 *   UId                - UID for a PartnerFunction entity instance.
 *   PartnerFunctionId  - Id of the PartnerFunction.
 *   Description        - Description of the PartnerFunction.
 *   TriggerOn          - Type of trigger for the PartnerFunction
 *   WorkflowActivities - List of WorkflowActivity for the PartnerFunction
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
public class PartnerFunction
  extends    AbstractEntity
  implements IPartnerFunction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4399808429619560818L;
	protected String    _partnerFunctionId;
  protected String    _description;
  protected Integer   _triggerOn;
  protected ArrayList _workflowActivityUids;
  protected ArrayList _workflowActivities;

  public PartnerFunction()
  {
    _workflowActivities = new ArrayList();
    _workflowActivityUids = new ArrayList();
  }

  // **************** Methods from AbstractEntity *********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    String triggerDesc = "";
    Integer triggerOn = getTriggerOn();
    if (triggerOn.equals(PartnerFunction.TRIGGER_IMPORT))
      triggerDesc = "Import";
    else if (triggerOn.equals(PartnerFunction.TRIGGER_RECEIVE))
      triggerDesc = "Receive";
    else if (triggerOn.equals(PartnerFunction.TRIGGER_MANUAL_EXPORT))
      triggerDesc = "Manual Export";
    else if (triggerOn.equals(PartnerFunction.TRIGGER_MANUAL_SEND))
      triggerDesc = "Manual Send";

    return getPartnerFunctionId()+"/"+getDescription()+"/"+triggerDesc;
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***********************

  public String getPartnerFunctionId()
  {
    return _partnerFunctionId;
  }

  public String getDescription()
  {
    return _description;
  }

  public Integer getTriggerOn()
  {
    return _triggerOn;
  }

  public ArrayList getWorkflowActivityUids()
  {
    return _workflowActivityUids;
  }

  public ArrayList getWorkflowActivities()
  {
    return _workflowActivities;
  }

  // *************** Setters for attributes *************************

  public void setPartnerFunctionId(String partnerFunctionId)
  {
    _partnerFunctionId = partnerFunctionId;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setTriggerOn(Integer triggerOn)
  {
    _triggerOn = triggerOn;
  }

  public void addWorkflowActivityUid(Long workflowActivityUid)
  {
    _workflowActivityUids.add(workflowActivityUid);
  }

  public void clearWorkflowActivityUids()
  {
    _workflowActivityUids = new ArrayList();
  }

  public void addWorkflowActivity(WorkflowActivity workflowActivity)
  {
    _workflowActivities.add(workflowActivity);
  }

  public void clearWorkflowActivities()
  {
    _workflowActivities = new ArrayList();
  }
}