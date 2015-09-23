/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IWorkflowActivity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 10 2002    Koh Han Sing        Created
 * Apr 29 2003    Neo Sok Lay         Add RAISE_ALERT activity.
 */
package com.gridnode.gtas.model.partnerfunction;

/**
 * This interface defines the properties and FieldIds for accessing fields
 * in WorkflowActivity entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1
 * @since 2.0
 */
public interface IWorkflowActivity
{
  /**
   * Name of WorkflowActivity entity.
   */
  public static final String  ENTITY_NAME = "WorkflowActivity";

  /**
   * MappingRule WorkflowActivity
   */
  public static final Integer MAPPING_RULE = new Integer(0);

  /**
   * UserProcedure WorkflowActivity
   */
  public static final Integer USER_PROCEDURE = new Integer(1);

  /**
   * Exit to Import WorkflowActivity
   */
  public static final Integer EXIT_TO_IMPORT = new Integer(2);

  /**
   * Exit to Outbound WorkflowActivity
   */
  public static final Integer EXIT_TO_OUTBOUND = new Integer(3);

  /**
   * Exit to Export WorkflowActivity
   */
  public static final Integer EXIT_TO_EXPORT = new Integer(4);

  /**
   * Exit Workflow WorkflowActivity
   */
  public static final Integer EXIT_WORKFLOW = new Integer(5);

  /**
   * Exit to Port WorkflowActivity
   */
  public static final Integer EXIT_TO_PORT = new Integer(6);

  /**
   * Save to System Folder WorkflowActivity
   */
  public static final Integer SAVE_TO_FOLDER = new Integer(7);

  /**
   * Exit to Channel WorkflowActivity
   */
  public static final Integer EXIT_TO_CHANNEL = new Integer(8);

  /**
   * Raise Alert WorkflowActivity
   */
  public static final Integer RAISE_ALERT     = new Integer(9);

  /**
   * Exit to Import WorkflowActivity
   */
  public static final String EXIT_TO_IMPORT_DESC = "Exit to system folder Import";

  /**
   * Exit to Outbound WorkflowActivity
   */
  public static final String EXIT_TO_OUTBOUND_DESC = "Exit to system folder Outbound";

  /**
   * Exit to Export WorkflowActivity
   */
  public static final String EXIT_TO_EXPORT_DESC = "Exit to system folder Export";

  /**
   * Exit Workflow WorkflowActivity
   */
  public static final String EXIT_WORKFLOW_DESC = "Exit workflow";

  /**
   * Exit to Port WorkflowActivity
   */
  public static final String EXIT_TO_PORT_DESC = "Exit to port";

  /**
   * Save to System Folder WorkflowActivity
   */
  public static final String SAVE_TO_FOLDER_DESC = "Save documents to system folder";

  /**
   * Exit to Channel WorkflowActivity
   */
  public static final String EXIT_TO_CHANNEL_DESC = "Exit to channel";

  /**
   * Raise Alert WorkflowActivity description
   */
  public static final String RAISE_ALERT_DESC     = "Raise alert";

  /**
   * FieldId for the UID for a WorkflowActivity entity instance. A Number.
   */
  public static final Number UID = new Integer(0); //Integer

  /**
   * FieldId for Whether-the-PartnerFunction-can-be-deleted flag. A Boolean.
   */
  public static final Number CAN_DELETE  = new Integer(1); //Boolean

  /**
   * FieldId for the Type of the WorkflowActivity. A Integer.
   */
  public static final Number ACTIVITY_TYPE = new Integer(3); //Integer

  /**
   * FieldId for the Description of the WorkflowActivity. A String.
   */
  public static final Number DESCRIPTION = new Integer(4); //String(80)

  /**
   * FieldId for the ParamList of the WorkflowActivity. A Vector.
   */
  public static final Number PARAM_LIST = new Integer(5); //Vector

}