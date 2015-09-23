/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractInfoCollator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.extraction.collator;

import com.gridnode.gtas.audit.extraction.exception.AuditInfoCollatorException;
import com.gridnode.gtas.audit.common.model.AuditTrailData;
import com.gridnode.pdip.framework.notification.AbstractNotification;

/**
 * This abstract class is  the superclass for EventInfoCollator, DocInfoCollator, 
 * and ProcessInfoCollator which decouples the client from directly interact with the 
 * concrete collator.
 * 
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public abstract class AbstractInfoCollator
{
  /**
   * Collate the necessary information that emitted from the GT and package the info into
   * the format expected in the Online Tracking Component.
   * @param notify The notification which emitted from GT component.
   * @return the AuditTrailData instance. 
   * @throws AuditInfoCollatorException while we have encountered error during the collating info process.
   */
  public abstract AuditTrailData gatherInfo(AbstractNotification notify) throws AuditInfoCollatorException;
}
