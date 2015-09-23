/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IWorkflowConstants.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Oct 25 2002    Mahesh             Created.
 * Jan 30 2007    Neo Sok Lay             Rename Queues and Topics to Destination.
 */

package com.gridnode.pdip.app.workflow.util;

public class IWorkflowConstants {

  public static final String CONFIG_WORKFLOW = "workflow";
  public static final String CONFIG_WORKFLOW_JMS = "workflow.jms";
  public static final String CONFIG_WORKFLOW_SENDRESPONSEDOC_APP = "workflow.sendresponsedoc.app";
  public static final String CONFIG_WORKFLOW_SENDREQUESTDOC_APP = "workflow.sendrequestdoc.app";
  public static final String CONFIG_WORKFLOW_SENDSIGNAL_APP = "workflow.sendsignal.app";
  public static final String CONFIG_WORKFLOW_VALIDATEDOC_APP = "workflow.validatedoc.app";
  public static final String CONFIG_WORKFLOW_NOTIFY_RECEIVEDDOC_APP="workflow.notify.receiveddoc.app";
  public static final String CONFIG_WORKFLOW_NOTIFY_RECEIVEDSIGNAL_APP="workflow.notify.receivedsignal.app";

  public static final String WORKFLOW_PROCESS_DEST = "workflow.process.dest";
  public static final String WORKFLOW_ACTIVITY_DEST = "workflow.activity.dest";
  public static final String WORKFLOW_RESTRICTION_DEST = "workflow.restriction.dest";

  public static final String WORKFLOW_RECEIVEDOCUMENT_DEST = "workflow.receivedocument.dest";
  public static final String WORKFLOW_RECEIVESIGNAL_DEST = "workflow.receivesignal.dest";
  public static final String WORKFLOW_SENDSIGNAL_DEST = "workflow.sendsignal.dest";
  public static final String WORKFLOW_SENDREQDOCUMENT_DEST = "workflow.sendrequestdoc.dest";
  public static final String WORKFLOW_SENDRESDOCUMENT_DEST = "workflow.sendresponsedoc.dest";
  public static final String WORKFLOW_WORKLIST_DEST = "workflow.worklist.dest";

  public static final String WORKFLOW_XPDL_CACHE = "workflow.xpdl.cache";
  public static final String WORKFLOW_XPDL_CACHE_TIMEOUT = "workflow.xpdl.cache.timeout";
  
  public static final String WORKFLOW_XPDL_CLEANUP_INTERVAL = "workflow.xpdl.cleanup.interval";
  public static final String WORKFLOW_XPDL_CLEANUP_MAXPROCESSES = "workflow.xpdl.cleanup.maxprocesses";
  
  


  public static final String WORKFLOW_ENGINE_ID = "workflow.engineId";

  public static final String ENGINE_TYPE = "ENGINE_TYPE";
  public static final String XPDL_ENGINE = "XPDL";
  public static final String BPSS_ENGINE = "BPSS";

  public static final String RTPROCESS_UID = "rtProcessUId";
  public static final String RTACTIVITY_UID = "rtActivityUId";
  public static final String RTRESTRICTION_UID = "rtRestrictionUId";
  
  public static final String PROCESSDEF_KEY = "ProcessDefKey";
  
  public static final String WORKFLOW_DISPATCH_INTERVAL = "wf.dispatchInterval";
  public static final String WORKFLOW_DISPATCH_COUNT = "wf.dispatchCount";
  public static final String WORKFLOW_SUSPENDACTIVITY_KEY = "SUSPENDACTIVITY";

}