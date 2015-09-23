/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RfcAForm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.bp;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.gridnode.gtas.client.web.strutsbase.GTActionFormBase;

public class RfcAForm extends GTActionFormBase 
{
  private String _name;
  private String _description;
  private String _connectionType;
  private String _host;
  private String _portNumber;
  private String _commandFileDir;
  private String _commandFile;
  private String _commandLine;
  private String _useCommandFile;

  public void doReset(ActionMapping mapping, HttpServletRequest request)
  {
    _useCommandFile = "false";
  }

  public String getName()
  { return _name; }

  public void setName(String name)
  { _name=name; }

  public String getDescription()
  { return _description; }

  public void setDescription(String description)
  { _description=description; }

  public String getConnectionType()
  { return _connectionType; }

  public void setConnectionType(String connectionType)
  { _connectionType=connectionType; }

  public String getHost()
  { return _host; }

  public void setHost(String host)
  { _host=host; }

  public String getPortNumber()
  { return _portNumber; }

  public void setPortNumber(String portNumber)
  { _portNumber=portNumber; }

  public String getCommandFileDir()
  { return _commandFileDir; }

  public void setCommandFileDir(String commandFileDir)
  { _commandFileDir=commandFileDir; }

  public String getCommandFile()
  { return _commandFile; }

  public void setCommandFile(String commandFile)
  { _commandFile=commandFile; }

  public String getCommandLine()
  { return _commandLine; }

  public void setCommandLine(String commandLine)
  { _commandLine=commandLine; }

  public String getUseCommandFile()
  { return _useCommandFile; }

  public void setUseCommandFile(String useCommandFile)
  { _useCommandFile=useCommandFile; }
}