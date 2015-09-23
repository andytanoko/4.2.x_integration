/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISessionKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-04-24     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web;

/**
 * String keys for various objects that may get put into the session context.
 */
public interface ISessionKeys
{
  /**
   * Key for the IGTSession object stored in session context and used to communicate with GridTalk
   */
  public static final String GTAS_SESSION = "com.gridnode.gtas.client.ctrl.IGTSession";
}