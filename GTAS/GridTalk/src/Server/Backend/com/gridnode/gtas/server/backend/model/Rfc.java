/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RFC.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.backend.model;

import com.gridnode.pdip.framework.db.entity.AbstractEntity;

/**
 * This is an object model for RFC entity. A RFC keeps tracks of a
 * remote function call settings.<P>
 *
 * The Model:<BR><PRE>
 *   UId            - UID for a RFC entity instance.
 *   Name           - Name of the RFC.
 *   Description    - Description of the RFC.
 *   Host           - The host to run the remote function.
 *   ConnectionType - Connection type to the remote function.
 *   PortNumber     - Listening port number of the remote function.
 *   UseCommandFile - To indicate whether to use command file.
 *   CommandFileDir - Directory path to the remote function command file.
 *   CommandFile    - Name of the remote function command file.
 *   CommandLine    - The parameters to be passed to the command file.
 * </PRE>
 * <P>
 * Getters and setters are provided for each attribute.<BR>
 * NOTE that all getters and setters are required for JDO
 * marshalling/unmarshalling.
 *
 * @author Koh Han Sing
 *
 * @version 2.0 I7
 * @since 2.0 I7
 */
public class Rfc
  extends    AbstractEntity
  implements IRfc
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5796014419888680187L;
	protected String      _name;
  protected String      _description;
  protected String      _host;
  protected String      _connectionType = TCPIP;
  protected Integer     _portNumber;
  protected Boolean     _useCommandFile;
  protected String      _commandFileDir;
  protected String      _commandFile;
  protected String      _commandLine;

  public Rfc()
  {
  }

  // ************* Methods from AbstractEntity ***********************

  public String getEntityName()
  {
    return ENTITY_NAME;
  }

  public String getEntityDescr()
  {
    return getName() + "/" + getDescription();
  }

  public Number getKeyId()
  {
    return UID;
  }

  // ***************** Getters for attributes ***************************

  public String getName()
  {
    return _name;
  }

  public String getHost()
  {
    return _host;
  }

  public String getDescription()
  {
    return _description;
  }

  public String getConnectionType()
  {
    return _connectionType;
  }

  public Integer getPortNumber()
  {
    return _portNumber;
  }

  public Boolean getUseCommandFile()
  {
    return _useCommandFile;
  }

  public String getCommandFileDir()
  {
    return _commandFileDir;
  }

  public String getCommandFile()
  {
    return _commandFile;
  }

  public String getCommandLine()
  {
    return _commandLine;
  }

  // **************** Setters for attributes ***************************

  public void setName(String name)
  {
    _name = name;
  }

  public void setHost(String host)
  {
    _host = host;
  }

  public void setDescription(String description)
  {
    _description = description;
  }

  public void setPortNumber(Integer portNumber)
  {
    _portNumber = portNumber;
  }

  public void setConnectionType(String connectionType)
  {
    _connectionType = connectionType;
  }

  public void setUseCommandFile(Boolean useCommandFile)
  {
    _useCommandFile = useCommandFile;
  }

  public void setCommandFileDir(String commandFileDir)
  {
    _commandFileDir = commandFileDir;
  }

  public void setCommandFile(String commandFile)
  {
    _commandFile = commandFile;
  }

  public void setCommandLine(String commandLine)
  {
    _commandLine = commandLine;
  }

}