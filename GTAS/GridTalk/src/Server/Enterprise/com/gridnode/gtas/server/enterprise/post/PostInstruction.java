/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PostInstruction.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 09 2002    Neo Sok Lay         Created
 * Nov 06 2002    Neo Sok Lay         Moved from GridNode module.
 * Nov 25 2002    Neo Sok Lay         Add final target recipient of the message.
 * Jan 14 2003    Neo Sok Lay         Add File Id, RecipientChannel, EventSubId
 * Oct 01 2003    Neo Sok Lay         Add Header (as an alternative currently).
 */
package com.gridnode.gtas.server.enterprise.post;

import com.gridnode.pdip.app.channel.helpers.ChannelSendHeader;

import java.io.Serializable;
import java.io.File;

/**
 * A Post Instruction is a wrapper for a message to be posted. Each instruction
 * consists of the Event ID, TransID (if applicable), file payloads, and
 * data payloads.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.2
 * @since 2.0 I6
 */
public class PostInstruction
  implements Serializable
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4897764157537638595L;
	private String _eventID;
  private String _transID;
  private File[] _filePayload;
  private String[] _dataPayload;
  private String _senderNodeID;
  private String _recipientNodeID;
  private String _fileID;
  private Object _recipientChannel;
  private String _eventSubID;
  private ChannelSendHeader _header;
  
  public PostInstruction()
  {
  }

  // *************************** Getters & Setters ***************************

  public void setEventID(String eventID)
  {
    _eventID = eventID;
  }

  public String getEventID()
  {
    return _eventID;
  }

  public void setTransID(String transID)
  {
    _transID = transID;
  }

  public String getTransID()
  {
    return _transID;
  }

  public void setFilePayload(File[] filePayload)
  {
    _filePayload = filePayload;
  }

  public File[] getFilePayload()
  {
    return _filePayload;
  }

  public void setDataPayload(String[] dataPayload)
  {
    _dataPayload = dataPayload;
  }

  public String[] getDataPayload()
  {
    return _dataPayload;
  }

  public void setRecipientNodeID(String nodeID)
  {
    _recipientNodeID = nodeID;
  }

  public String getRecipientNodeID()
  {
    return _recipientNodeID;
  }

  public void setSenderNodeID(String senderNodeID)
  {
    _senderNodeID = senderNodeID;
  }

  public String getSenderNodeID()
  {
    return _senderNodeID;
  }

  public void setFileID(String fileID)
  {
    _fileID = fileID;
  }

  public String getFileID()
  {
    return _fileID;
  }

  public void setRecipientChannel(Object channel)
  {
    _recipientChannel = channel;
  }

  public Object getRecipientChannel()
  {
    return _recipientChannel;
  }

  public void setEventSubID(String eventSubID)
  {
    _eventSubID = eventSubID;
  }

  public String getEventSubID()
  {
    return _eventSubID;
  }

  public void setHeader(ChannelSendHeader header)
  {
    _header = header;
  }
  
  public ChannelSendHeader getHeader()
  {
    return _header;
  }
  
  public String toString()
  {
    StringBuffer buff = new StringBuffer();
    buff.append("[Event:").append(getEventID()).append(",trans:").append(getTransID());
    buff.append(",recipient:").append(getRecipientNodeID());
    return buff.toString();
  }
}