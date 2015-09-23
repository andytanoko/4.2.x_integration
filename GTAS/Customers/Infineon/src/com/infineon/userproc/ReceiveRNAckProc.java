/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveRNAckProc.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 04 2003    Jagadeesh/Neo Sok Lay   Created
 */
package com.infineon.userproc;

/**
 * This User Procedure is written for the Infineon Project.
 * <p>
 * Example:<p>
 * When Infineon GTAS receives an RN Acknowledgement, the system will send an
 * email notification, attaching the sent RN document, to the Infineon
 * users.
 * <p>
 * This user procedure is used to perform customized tasks on receival of
 * documents whose document types are configured in the "receive-rnack.properties" file.
 * <p>
 * Currently, there is only a sendEmailNotification() task.
 * <p>
 */
public class ReceiveRNAckProc extends AbstractReceiveRNSignalProc
{
  private static final String CLASS_NAME  = "ReceiveRNAckProc";

  private static final String CONFIG_FILE = "receive-rnack.properties";

  protected String getClassName()
  {
    return CLASS_NAME;
  }

  protected String getConfigFileName()
  {
    return CONFIG_FILE;
  }
}