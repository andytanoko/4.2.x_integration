/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractConnectionSetupDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 01 2002    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.setup;

import com.gridnode.gtas.server.connection.helpers.IConnectionConfig;

/** 
 * Abstract class for Connection related delegates.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I6
 * @since 2.0 I6
 */
public abstract class AbstractConnectionSetupDelegate
  implements IConnectionSetupDelegate,
             IConnectionConfig
{
  protected ConnectionSetupContext _ctx;
  protected DelegateHelper    _helper;

  protected String            _eventID;

  protected Object            _lock = new Object();
  protected boolean           _resultsReturned = false;

  protected boolean           _success = false;
  protected String            _failureReason = "Fail to receive response from the network";

  public AbstractConnectionSetupDelegate(ConnectionSetupContext ctx)
  {
    _ctx = ctx;
    _helper = new DelegateHelper();
  }

}