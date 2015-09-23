/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DivMsgList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-02     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.GTClientException;

public interface IDivMsgProcessor
{
  //The forward and back lists are kept quite seperate deliberately. While they function
  //in a similar manner they should not be confused with each other.
  //I was considering splitting this into two interfaces but decided against it for convienience.
  //If processing only one direction is required an empty method could be implemented.
  public Object processForwardDivMsg( Object context, Object divMsg ) throws GTClientException;
  public Object processBackDivMsg( Object context, Object divMsg ) throws GTClientException;
}