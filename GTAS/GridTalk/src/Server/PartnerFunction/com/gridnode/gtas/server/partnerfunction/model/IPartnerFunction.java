/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerFunction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 06 2002    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.model;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in PartnerFunction entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public interface IPartnerFunction
{
  /**
   * Name of PartnerFunction entity.
   */
  public static final String  ENTITY_NAME = "PartnerFunction";

  /**
   * Trigger on Import
   */
  public static final Integer TRIGGER_IMPORT = new Integer(0);

  /**
   * Trigger on Receive
   */
  public static final Integer TRIGGER_RECEIVE = new Integer(1);

  /**
   * Trigger on Manual Send
   */
  public static final Integer TRIGGER_MANUAL_SEND = new Integer(2);

  /**
   * Trigger on Manual Export
   */
  public static final Integer TRIGGER_MANUAL_EXPORT = new Integer(3);

  /**
   * FieldId for the UID for a PartnerFunction entity instance. A Number.
   */
  public static final Number UID         = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-PartnerFunction-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for Version. A double.
   */
  public static final Number VERSION       = new Integer(2); //Double

  /**
   * FieldId for the Id of the PartnerFunction. A String.
   */
  public static final Number PARTNER_FUNCTION_ID = new Integer(3); //String(4)

  /**
   * FieldId for the Description of the PartnerFunction. A String.
   */
  public static final Number DESCRIPTION = new Integer(4); //String(80)

  /**
   * FieldId for the Trigger of the PartnerFunction. A Integer.
   */
  public static final Number TRIGGER_ON = new Integer(5); //Integer

  /**
   * FieldId for the WorkflowActivityUids of the PartnerFunction.
   * A ArrayList of WorkflowActivity uids.
   */
  public static final Number WORKFLOW_ACTIVITY_UIDS = new Integer(6); //ArrayList

  /**
   * FieldId for the WorkflowActivities of the PartnerFunction.
   * A ArrayList of WorkflowActivity.
   */
  public static final Number WORKFLOW_ACTIVITIES = new Integer(7); //ArrayList

}