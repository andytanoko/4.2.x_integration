/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: UpcCommInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 17 2003    Neo Sok Lay         Created
 * Nov 14 2003    Neo Sok Lay         Add send options: sendAll, skipGdoc, sendUdocOnly.
 */
package com.gridnode.gtas.server.userprocedure.model;

import com.gridnode.gtas.server.userprocedure.helpers.BaseUserProcedureUtil;
import com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.StringTokenizer;


/**
 * Transport Communication Profile for User Procedure Call.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class UpcCommInfo extends AbstractCommInfo implements IUpcCommInfo
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 3931917804825456217L;
	private static final MessageFormat _format = new MessageFormat(URL_FORMAT_PATTERN);
  private static final MessageFormat _cmdFormat = new MessageFormat(COMMAND_FORMAT_PATTERN);
  private final Hashtable _commandUpTable = new Hashtable();
  private boolean _sendUdocOnly = true;
  private boolean _skipGdoc = true;
  private boolean _sendAll = false;
  
  /**
   * Constructs an empty UpcCommInfo profile.
   */
  public UpcCommInfo()
  {
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.ICommInfo#getProtocolType()
   */
  public String getProtocolType()
  {
    return UPC;
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#getProtocolVersion()
   */
  public String getProtocolVersion()
  {
    return UPC_VERSION;
  }

  /**
   * @see com.gridnode.gtas.server.userprocedure.model.IUPCCommInfo#getUserProcedure()
   */
  public String getSendUserProcedure()
  {
    return (String)_commandUpTable.get(COMMAND_SEND);
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseAndSetURL(java.lang.String)
   */
  protected void parseAndSetURL(String url)
  {
    if (url.startsWith(PROTOCOL))
    {
      String commandStr = url.substring(PROTOCOL.length());
      
      // tokenize commandStr into individual command-userprocedure specifiers
      StringTokenizer st = new StringTokenizer(commandStr, UP_COMMAND_SEPARATOR);
      while (st.hasMoreTokens())
      {
        String token = st.nextToken();
        int commandUpSepIndex = token.indexOf(COMMAND_UP_SEPARATOR); 
        if (commandUpSepIndex > -1)
        {
          int userProcStartPos = commandUpSepIndex + COMMAND_UP_SEPARATOR.length();
          String command = token.substring(0, commandUpSepIndex);
          String userProc = token.substring(userProcStartPos);

          int optionStartPos = userProc.indexOf(UP_OPTION_SEPARATOR);
          if (optionStartPos > -1)
          {
            userProc = userProc.substring(0, optionStartPos);
            parseAndSetOptions(userProc.substring(optionStartPos + UP_OPTION_SEPARATOR.length()));    
          }
          else
          {
            parseAndSetOptions("");
          }
          _commandUpTable.put(command, userProc);    
        }
      }
    }
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.ICommInfo#getURLPattern()
   */
  public String getURLPattern()
  {
    return URL_PATTERN;
  }

  /**
   * @see com.gridnode.pdip.base.transport.comminfo.AbstractCommInfo#parseAndGetURL()
   */
  protected String parseAndGetURL()
  {
    String sendCmd = _cmdFormat.format(getCommandParams(COMMAND_SEND));
    
    String[] params = new String[]{
      sendCmd,
    };
    return _format.format(params);
  }

  /**
   * Get the parameters for formatting the Command portion of the URL.
   * 
   * @param command The command involved.
   * @return Dynamic Parameters for formatting. 
   */
  private String[] getCommandParams(String command)
  {
    return new String[]{
             command,
             (String)_commandUpTable.get(command),
            };
  }
  
  /**
   * @see com.gridnode.pdip.base.transport.comminfo.ICommInfo#parseURL(java.lang.String)
   */
  public void parseURL(String url) throws MalformedURLException
  {
    if (url.startsWith(PROTOCOL))
    {
      try
      {
        String commandStr = url.substring(PROTOCOL.length());
      
        // tokenize commandStr into individual command-userprocedure specifiers
        StringTokenizer st = new StringTokenizer(commandStr, UP_COMMAND_SEPARATOR);
        while (st.hasMoreTokens())
        {
          String token = st.nextToken();
          int commandUpSepIndex = token.indexOf(COMMAND_UP_SEPARATOR); 
          if (commandUpSepIndex > -1)
          {
            String command = token.substring(0, commandUpSepIndex);
            if (!isValidCommand(command))
            {
              throw new Exception("Invalid command specified - "+command);
            }

            int userProcStartPos = commandUpSepIndex + COMMAND_UP_SEPARATOR.length();
            String userProc = token.substring(userProcStartPos);
            int optionStartPos = userProc.indexOf(UP_OPTION_SEPARATOR);
            if (optionStartPos > -1)
            {
              userProc = userProc.substring(0, optionStartPos);
              parseOptions(userProc.substring(optionStartPos + UP_OPTION_SEPARATOR.length()));    
            }
            
            if (!isValidUserProcedure(userProc))
            {
              throw new Exception("Invalid User Procedure name specified - "+userProc);
            }
          }
          else
            throw new Exception("Expected a command and User Procedure name");
        }
      }
      catch (Exception e)
      {
        throw new MalformedURLException("Invalid UPC URL due to: "+ e.getMessage());
      }
    }
    else
      throw new MalformedURLException("UPC URL must start with "+PROTOCOL);
  }

  /**
   * Parse the options string
   * 
   * @param options The options string
   * @throws Exception Invalid option encountered 
   */
  protected void parseOptions(String options)
    throws Exception
  {
    StringTokenizer st = new StringTokenizer(options, OPTIONS_SEPARATOR);
    while (st.hasMoreTokens())
    {
      String option = st.nextToken();
      if (!OPTION_SKIP_GDOC.equalsIgnoreCase(option) &&
          !OPTION_UDOC_ONLY.equalsIgnoreCase(option) &&
          !OPTION_SEND_ALL.equalsIgnoreCase(option))
      {
        throw new Exception("Invalid Option specified - "+option);   
      }
    }
  }
  
  /**
   * Parse and set options
   * 
   * @param options The options string.
   */
  protected void parseAndSetOptions(String options)
  {
    StringTokenizer st = new StringTokenizer(options, OPTIONS_SEPARATOR);
    while (st.hasMoreTokens())
    {
      String option = st.nextToken();
      if (OPTION_SEND_ALL.equalsIgnoreCase(option))
      {
        _sendAll = true;
      }
      else if (OPTION_SKIP_GDOC.equalsIgnoreCase(option))
      {
        _skipGdoc = true;
      }
      else if (OPTION_UDOC_ONLY.equalsIgnoreCase(option))
      {
        _sendUdocOnly = true;
      }
    } 
  }
  
  /**
   * Checks if the specified command is supported.
   * 
   * @param command The command to check.
   * @return <b>true</b> if the command is currently supported, <b>false</b>
   * otherwise.
   * @see COMMAND_SEND
   */
  private boolean isValidCommand(String command)
  {
    boolean valid = false;
    valid = COMMAND_SEND.equals(command);
     
    return valid;
  }
  
  /**
   * Checks if the specified user procedure is a valid user procedure name.
   * 
   * @param userProcedure The name of the user procedure.
   * @return <b>true</b> if a matching user procedure can be found in the database,
   * <b>false</b> otherwise.
   * @throws Exception Error querying the database.
   */
  private boolean isValidUserProcedure(String userProcedure) throws Exception
  {
    boolean valid = false;
    valid = BaseUserProcedureUtil.isValidUserProcedure(userProcedure);
    return valid;
  }
  
  /**
   * @see com.gridnode.gtas.server.userprocedure.model.IUpcCommInfo#isSendUdocOnly()
   */
  public boolean isSendUdocOnly()
  {
    return _sendUdocOnly;
  }

  /**
   * @see com.gridnode.gtas.server.userprocedure.model.IUpcCommInfo#isSkipSendGdoc()
   */
  public boolean isSkipSendGdoc()
  {
    return _skipGdoc;
  }

  /**
   * @see com.gridnode.gtas.server.userprocedure.model.IUpcCommInfo#isSendAll()
   */
  public boolean isSendAll()
  {
    return _sendAll;
  }

}
