/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAlertTriggerManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-05-06     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;


public interface IGTAlertTriggerManager extends IGTManager
{
  /*
   * Utility method to return the type from a concatenated recipient string.
   * @param recipient
   * @return recipientType or null if null passed
   * @throws IllegalArgumentException is the recipient string is invalid
   */
  public String extractRecipientType(String recipient);
  
  /*
   * Utility method to return the value from a concatenated recipient string.
   * @param recipient
   * @return recipientValue or null if null passed
   * @throws IllegalArgumentException is the string is invalid
   */
  public String extractRecipientValue(String recipient);
  
  /*
   * Utility method to create a concatenated recipient String based on type and value components
   * @param recipientType
   * @param recipientValue
   * @return recipient
   */
  public String makeRecipient(String recipientType, String recipientValue);
}