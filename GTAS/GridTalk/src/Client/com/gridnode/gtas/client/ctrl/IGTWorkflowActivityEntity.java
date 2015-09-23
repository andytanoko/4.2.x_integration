/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTWorkflowActivityEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-09-16     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         Added new wfa types
 * 2003-01-17     Andrew Hill         Use PARAM_LIST id defined in interface (wasnt there before!)
 * 2003-05-15     Andrew Hill         raiseAlert support
 * 2004-04-01     Daniel D'Cotta      Added support for SUSPEND_ACTIVITY
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.partnerfunction.IWorkflowActivity;


// These entities are embedded in a field of IGTPartnerFunctionEntity whose manager
// is thus responsible for them.
public interface IGTWorkflowActivityEntity extends IGTEntity
{
  //20030115AH
  //English string constants for descriptions:
  public static final String DESCRIPTION_EXIT_TO_IMPORT   = IWorkflowActivity.EXIT_TO_IMPORT_DESC;
  public static final String DESCRIPTION_EXIT_TO_OUTBOUND = IWorkflowActivity.EXIT_TO_OUTBOUND_DESC;
  public static final String DESCRIPTION_EXIT_TO_EXPORT   = IWorkflowActivity.EXIT_TO_EXPORT_DESC;
  public static final String DESCRIPTION_EXIT_WORKFLOW    = IWorkflowActivity.EXIT_WORKFLOW_DESC;
  public static final String DESCRIPTION_EXIT_TO_PORT     = IWorkflowActivity.EXIT_TO_PORT_DESC;
  public static final String DESCRIPTION_SAVE_TO_FOLDER   = IWorkflowActivity.SAVE_TO_FOLDER_DESC;
  public static final String DESCRIPTION_EXIT_TO_CHANNEL  = IWorkflowActivity.EXIT_TO_CHANNEL_DESC; //20030129AMH
  public static final String DESCRIPTION_RAISE_ALERT      = IWorkflowActivity.RAISE_ALERT_DESC; //20030515AH
  //...

  // Constants for TYPE
  public static final Integer TYPE_MAPPING_RULE       = IWorkflowActivity.MAPPING_RULE;
  public static final Integer TYPE_USER_PROCEDURE     = IWorkflowActivity.USER_PROCEDURE;
  public static final Integer TYPE_EXIT_TO_IMPORT     = IWorkflowActivity.EXIT_TO_IMPORT;
  public static final Integer TYPE_EXIT_TO_OUTBOUND   = IWorkflowActivity.EXIT_TO_OUTBOUND;
  public static final Integer TYPE_EXIT_TO_EXPORT     = IWorkflowActivity.EXIT_TO_EXPORT;
  public static final Integer TYPE_EXIT_TO_WORKFLOW   = IWorkflowActivity.EXIT_WORKFLOW;
  public static final Integer TYPE_EXIT_WORKFLOW      = TYPE_EXIT_TO_WORKFLOW; //synonym 20030115AH
  public static final Integer TYPE_EXIT_TO_PORT       = IWorkflowActivity.EXIT_TO_PORT;   //20030115AH
  public static final Integer TYPE_SAVE_TO_FOLDER     = IWorkflowActivity.SAVE_TO_FOLDER; //20030115AH
  public static final Integer TYPE_EXIT_TO_CHANNEL    = IWorkflowActivity.EXIT_TO_CHANNEL;   //20030129AMH
  public static final Integer TYPE_RAISE_ALERT        = IWorkflowActivity.RAISE_ALERT; //20030515AH

  // Field Ids
  public static final Number TYPE         = IWorkflowActivity.ACTIVITY_TYPE;
  public static final Number DESCRIPTION  = IWorkflowActivity.DESCRIPTION;
  //PARAM_LIST wasnt previously available in interface so we hardcoded it as we need it.
  //Of course the b-tier changed it from 4 to 5 later on causing all sorts of wierd errors...
  //Now its defined in the inteface :-)
  public static final Number PARAM_LIST   = IWorkflowActivity.PARAM_LIST; //20030117AH

  // Virtual Fields
  public static final Number MAPPING_RULE_UIDS        = new Integer(-10);
  public static final Number USER_PROCEDURE_UIDS      = new Integer(-20);
  public static final Number PORT_UIDS                = new Integer(-30);
  public static final Number ALERT_TYPE               = new Integer(-40); //20030515AH
  public static final Number USER_DEFINED_ALERT_UIDS  = new Integer(-50); //20030515AH
  public static final Number DISPATCH_INTERVAL        = new Integer(-60); // 20040401 DDJ
  public static final Number DISPATCH_COUNT           = new Integer(-70); // 20040401 DDJ
}