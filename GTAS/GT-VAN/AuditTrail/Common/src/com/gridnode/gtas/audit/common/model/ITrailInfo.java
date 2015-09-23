/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITrailInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 17, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.common.model;

import java.io.Serializable;

/**
 * Interface that represent the tracking information emitted from GT (not 
 * included the document(s)v). 
 * 
 * Known implemented class: EventInfo, DocInfo, ProcessInfo.
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface ITrailInfo extends Serializable
{
  public String toString();

}
