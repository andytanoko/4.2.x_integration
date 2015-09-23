/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveRNExceptionProc.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 25 2003    Neo Sok Lay             Created
 */
package com.infineon.userproc;

/**
 * This User Procedure is written for the Infineon Project.
 * <p>
 * Example:<p>
 * When Infineon GTAS receives an RN Exception, the system will send an
 * email notification, attaching the sent RN document, to the Infineon
 * users.
 * <p>
 * This user procedure is used to perform customized tasks on receival of
 * documents whose document types are configured in the "receive-rnexcept.properties" file.
 * <p>
 * Currently, there is only a sendEmailNotification() task.
 * <p>
 */
public class ReceiveRNExceptionProc extends AbstractReceiveRNSignalProc
{
  private static final String CLASS_NAME  = "ReceiveRNExceptionProc";

  private static final String CONFIG_FILE = "receive-rnexcept.properties";

  protected String getClassName()
  {
    return CLASS_NAME;
  }

  protected String getConfigFileName()
  {
    return CONFIG_FILE;
  }
}