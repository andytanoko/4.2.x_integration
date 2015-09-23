/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LoginActionMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-04-25     Andrew Hill         Created
 * 2003-07-01     Andrew Hill         Extend GTActionMapping
 * 2003-11-05     Andrew Hill         NoSecurity option
 * 2006-04-24     Neo Sok Lay         Add NoP2P and NoUDDI option
 */
package com.gridnode.gtas.client.web.login;

import com.gridnode.gtas.client.web.strutsbase.GTActionMapping;

public class LoginActionMapping extends GTActionMapping
{
  private boolean _promptPcp = true;
  private boolean _noSecurity = false;
  private boolean _noP2P = false;
  private boolean _noUDDI = false;
  
  /**
   * @return promptPcp or false if noSecurity
   */
  public boolean isPromptPcp()
  {
    return _noSecurity ? false : _promptPcp;
  }

  /**
   * Set to false to stop the security password screen coming up when the private cert password
   * is not known. Note that if noSecurity is true, then this value is ignored.
   * @param promptPcp set to false to prevent display of securityPassword screen
   */
  public void setPromptPcp(boolean promptPcp)
  {
    _promptPcp = promptPcp;
  }

	public boolean isNoSecurity()
	{
		return _noSecurity;
	}

	public void setNoSecurity(boolean b)
	{
		_noSecurity = b;
	}

	public boolean isNoP2P()
	{
		return _noP2P;
	}
	
	/**
	 * Set to true to hide all functions related to P2P.
	 * @param b
	 */
	public void setNoP2P(boolean b)
	{
		_noP2P = b;
	}
	
	public boolean isNoUDDI()
	{
		return _noUDDI;
	}
	
	/**
	 * Set to true to hide all functions related to UDDI.
	 * @param b
	 */
	public void setNoUDDI(boolean b)
	{
		_noUDDI = b;
	}
}