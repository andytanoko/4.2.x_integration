/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SendBeAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-30     Andrew Hill         Created
 * 2003-01-15     Andrew Hill         gnUids
 */
package com.gridnode.gtas.client.web.be;

import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class SendBeAForm extends GTActionFormBase
{
  protected String[] _beUids;
  protected String  _toEnterpriseId; //obsoleted 20030115AH
  protected String _viaChannelUid; //obsoleted 20030115AH
  protected String[] _gnUids; //20030115AH

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _beUids = null;
    _gnUids = null; //20030115AH
  }

  public void setGnUids(String[] values)
  { //20030115AH
    _gnUids = values;
  }

  public String[] getGnUids()
  { //20030115AH
    return _gnUids;
  }

  public void setBeUids(String[] values)
  { _beUids = values; }

  public String[] getBeUids()
  { return _beUids; }

  public void setToEnterpriseId(String value)
  { _toEnterpriseId = value; }

  public String getToEnterpriseId()
  { return _toEnterpriseId; }

  public void setViaChannelUid(String value)
  { _viaChannelUid = value; }

  public String getViaChannelUid()
  { return _viaChannelUid; }
}